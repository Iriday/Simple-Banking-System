package banking;

import java.sql.*;

public class Model implements ModelInterface {
    private final String dbUrl;
    private String cardNumber = null;

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
    public Card createAccount() {
        Card newCard = Card.generateUniqueCard(dbUrl);
        insertCardInDb(newCard, dbUrl);
        return newCard;
    }

    @Override
    public boolean logIntoAccount(String cardNumber, String pin) {
        if (!Utils.isCardCorrect(cardNumber) || !Utils.isPinCorrect(pin)) return false;

        if (isCardInDb(cardNumber, pin, dbUrl)) {
            this.cardNumber = cardNumber;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long getAccountBalance() {
        throwExcIfNotLoggedIn();
        return getBalance(cardNumber, dbUrl);
    }

    @Override
    public void addIncome(long income) {
        throwExcIfNotLoggedIn();
        addMoney(cardNumber, income, dbUrl);
    }

    private String recCardCheckErrMessage = null;

    @Override
    public String receiverCardCheckBeforeTransfer(String receiverCardNumber) {
        throwExcIfNotLoggedIn();
        recCardCheckErrMessage = receiverCardCheckBeforeTransfer(cardNumber, receiverCardNumber, dbUrl);
        return recCardCheckErrMessage;
    }

    @Override
    public void doTransfer(String receiverCardNumber, long money) throws NotEnoughMoneyException {
        throwExcIfNotLoggedIn();
        if (recCardCheckErrMessage == null) throw new IllegalStateException("receiverCardCheckBeforeTransfer is not called before doTransfer");
        if (!recCardCheckErrMessage.isEmpty()) throw new IllegalStateException("Transfer is not possible because receiverCardCheckBeforeTransfer returned an error");
        if (getBalance(cardNumber, dbUrl) < money) throw new NotEnoughMoneyException();

        recCardCheckErrMessage = null;
        doTransfer(cardNumber, receiverCardNumber, money, dbUrl);
    }

    @Override
    public void closeAccount() {
        throwExcIfNotLoggedIn();
        deleteCardFromDb(cardNumber, dbUrl);
        logOutOfAccount();
    }

    @Override
    public void logOutOfAccount() {
        cardNumber = null;
    }

    private void throwExcIfNotLoggedIn() {
        if (cardNumber == null) throw new IllegalStateException("User not logged in");
    }

    private static void insertCardInDb(Card card, String dbUrl) {
        String insertCard = """
                INSERT INTO card(
                    id,
                    number,
                    pin)
                VALUES(
                    ?,
                    ?,
                    ?
                );""";
        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(insertCard)) {
            ps.setLong(1, card.getId());
            ps.setString(2, card.getNumber());
            ps.setString(3, card.getPin());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static long getBalance(String cardNumber, String dbUrl) {
        try (Connection cn = DriverManager.getConnection(dbUrl);
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT
                        balance
                     FROM
                        card
                     WHERE
                        number = '$num'
                     ;"""
                     .replace("$num", cardNumber))) {
            return rs.getLong("balance");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void doTransfer(String senderCardNumber, String receiverCardNumber, long money, String dbUrl) {
        takeMoney(senderCardNumber, money, dbUrl);
        addMoney(receiverCardNumber, money, dbUrl);
    }

    private static void addMoney(String cardNumber, long money, String dbUrl) {
        String addToBalance = """
                UPDATE
                    card
                SET
                    balance = balance + ?
                WHERE
                    number = ?
                ;""";
        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(addToBalance)) {
            ps.setLong(1, money);
            ps.setString(2, cardNumber);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void takeMoney(String cardNumber, long money, String dbUrl) {
        String subtractFromBalance = """
                UPDATE 
                    card
                SET 
                    balance = balance - ?
                WHERE
                    number = ?
                ;""";
        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(subtractFromBalance)) {
            ps.setLong(1, money);
            ps.setString(2, cardNumber);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteCardFromDb(String cardNumber, String dbUrl) {
        String sqlDelCard = """
                DELETE
                FROM
                    card
                WHERE
                    number = ?
                ;""";
        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(sqlDelCard)) {
            ps.setString(1, cardNumber);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isCardNumberInDb(String cardNumber, String dbUrl) {
        String isNumberInDb = """
                SELECT
                    number
                FROM
                    card
                WHERE
                    number = ?
                """;
        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(isNumberInDb)) {
            ps.setString(1, cardNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isCardInDb(String cardNumber, String cardPin, String dbUrl) {
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
                     .replace("$pin", cardPin))
        ) {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String receiverCardCheckBeforeTransfer(String senderCardNumber, String receiverCardNumber, String dbUrl) {
        if (senderCardNumber.equals(receiverCardNumber)) return "You can't transfer money to the same account!";
        if (!Utils.isCardCorrect(receiverCardNumber)) return "Probably you made mistake in card number. Please try again!";
        if (!isCardNumberInDb(receiverCardNumber, dbUrl)) return "Such a card does not exist";
        return "";
    }
}
