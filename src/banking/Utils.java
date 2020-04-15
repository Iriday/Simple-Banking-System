package banking;

import java.util.Random;

public class Utils {
    private static final Random rand = new Random();

    public static String randNDigitNum(int numOfDigits) {
        var builder = new StringBuilder(numOfDigits);
        for (int i = 0; i < numOfDigits; i++) {
            builder.append(rand.nextInt(10));
        }
        return builder.toString();
    }
}
