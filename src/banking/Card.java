package banking;

import java.sql.*;

public class Card {
    private final String number;
    private final String pin;
    private final long id;

    private Card(String number, String pin, long id) {
        this.number = number;
        this.pin = pin;
        this.id = id;
    }

    public static Card generateUniqueCard(String dbUrl) {
        return new Card(generateUniqueNumber(dbUrl), generateRandPin(), getMaxIdFromDb(dbUrl) + 1);
    }

    public static String generateUniqueNumber(String dbUrl) {
        StringBuilder builderCardNum;
        String selectNumber = """
                SELECT
                    number
                FROM 
                    card
                WHERE 
                    number = ?
                ;""";

        try (Connection cn = DriverManager.getConnection(dbUrl); PreparedStatement ps = cn.prepareStatement(selectNumber)) {
            while (true) {
                builderCardNum = new StringBuilder();
                builderCardNum.append("400000"); // Issuer Identification Number (IIN)
                builderCardNum.append(Utils.randNDigitNum(9)); // account identifier | customer account number

                int result = Utils.applyLuhnAlgorithm(builderCardNum.toString() + 0);
                for (int i = 0; i < 10; i++) {  // find/add check digit | checksum
                    if ((result + i) % 10 == 0) {
                        builderCardNum.append(i);
                        break;
                    }
                }

                ps.setString(1, builderCardNum.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) { //if number is unique return
                        return builderCardNum.toString();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateRandPin() {
        return Utils.randNDigitNum(4);
    }

    public static long getMaxIdFromDb(String dbUrl) { // 0 if empty
        try (Connection cn = DriverManager.getConnection(dbUrl);
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("""
                     SELECT 
                        MAX(id) as id
                     FROM
                        card
                     ;""")) {
            return rs.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public long getId() {
        return id;
    }
}
