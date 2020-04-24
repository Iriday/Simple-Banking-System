package banking;

import java.sql.*;

public class Model implements ModelInterface {
    private final String dbUrl;
    private String cardNumber = null;
    private String cardPin = null;

    public Model(String[] args) {
        if (args.length == 2 && args[0].equals("-fileName")) {
            dbUrl = "jdbc:sqlite:" + args[1];
            initDB(dbUrl);
        } else {
            throw new IllegalArgumentException("Incorrect arg, or args length");
        }
    }

    private void initDB(String dbUrl) { // create db and table "card" if not created
        try (Connection cn = DriverManager.getConnection(dbUrl); Statement st = cn.createStatement()) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS card(
                        id INTEGER not null,
                        number TEXT not null,
                        pin TEXT not null, 
                        balance INTEGER DEFAULT 0 not null
                    );""");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createAccount() {
        Card newCard = Card.generateUniqueCard(dbUrl);
        String number = newCard.getNumber();
        String pin = newCard.getPin();
        String id = newCard.getId() + "";
        try (Connection cn = DriverManager.getConnection(dbUrl); Statement st = cn.createStatement()) {
            st.executeUpdate("""
                    INSERT INTO card(
                        id,
                        number,
                        pin)
                    VALUES(
                        $id,
                        '$num',
                        '$pin'
                    );"""
                    .replace("$id", id)
                    .replace("$num", number)
                    .replace("$pin", pin));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Your card have been created\n" + "Your card number:\n" + number + "\nYour card PIN:\n" + pin;
    }

    @Override
    public boolean logIntoAccount(String cardNumber, String pin) {
        if (Utils.applyLuhnAlgorithm(cardNumber) % 10 != 0) return false;

        try (Connection cn = DriverManager.getConnection(dbUrl);
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT
                         number,
                         pin
                     FROM
                         card
                     WHERE
                         number = '$num'
                         AND pin = '$pin'
                     ;"""
                     .replace("$num", cardNumber)
                     .replace("$pin", pin))
        ) {
            if (rs.next()) {
                this.cardNumber = cardNumber;
                this.cardPin = pin;
                return true; // loggedIn
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getAccountBalance() {
        if (cardNumber == null || cardPin == null) throw new IllegalStateException("User not logged in");
        try (Connection cn = DriverManager.getConnection(dbUrl);
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT
                        balance
                     FROM
                        card
                     WHERE
                        number = '$num'
                        AND pin = '$pin'
                     ;"""
                     .replace("$num", cardNumber)
                     .replace("$pin", cardPin))) {
            return rs.getLong("balance");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIncome(long income) {
        if (cardNumber == null) throw new IllegalStateException("User not logged in");
        String addIncome = """
                UPDATE
                    card
                SET
                    balance = balance + ?
                WHERE
                    number = ?
                ;""";
        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(addIncome)) {
            ps.setLong(1, income);
            ps.setString(2, cardNumber);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeAccount() {
        if (cardNumber == null) throw new IllegalStateException("User not logged in");
        String delAcc = """
                DELETE
                FROM
                    card
                WHERE
                    number = ?
                ;""";
        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(delAcc)) {
            ps.setString(1, cardNumber);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logOutOfAccount();
    }

    @Override
    public void logOutOfAccount() {
        cardNumber = null;
        cardPin = null;
    }
}
