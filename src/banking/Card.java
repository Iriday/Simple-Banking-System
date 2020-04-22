package banking;

import java.sql.*;

public class Card {
    private final String cardNumber;
    private final String pin;

    private Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    // factory method
    public static Card generateUniqueCard(String dbUrl) {
        StringBuilder builderCardNum;

        try (Connection cn = DriverManager.getConnection(dbUrl); Statement st = cn.createStatement()) {
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

                try (ResultSet rs = st.executeQuery("""
                        SELECT
                            number
                        FROM 
                            card
                        WHERE 
                            number = $number
                        ;""".replace("$number", builderCardNum.toString()))) {

                    if (!rs.next()) { //if number is unique return
                        return new Card(builderCardNum.toString(), Utils.randNDigitNum(4));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }
}
