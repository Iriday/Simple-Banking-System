package banking;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Utils {
    public static final Random rand = new Random();
    public static final Pattern patternDigits = Pattern.compile("\\d+");

    public static String randNDigitNum(int numOfDigits) {
        var builder = new StringBuilder(numOfDigits);
        for (int i = 0; i < numOfDigits; i++) {
            builder.append(rand.nextInt(10));
        }
        return builder.toString();
    }

    public static int applyLuhnAlgorithm(String number) {
        if (!patternDigits.matcher(number).matches()) {
            throw new IllegalArgumentException("Incorrect number (String)");
        }
        AtomicInteger index = new AtomicInteger(1);
        return Stream.of(number.split(""))
                .mapToInt(Integer::parseInt)
                .limit(number.length() - 1)                                         // 1 drop last digit
                .map(digit -> index.getAndIncrement() % 2 == 1 ? digit * 2 : digit) // 2 if digit index+1 is odd multiply digit by 2
                .map(digit -> digit > 9 ? digit - 9 : digit)                        // 3 subtract 9 to numbers over 9
                .reduce(Integer::sum)                                               // 4 add all numbers
                .orElseThrow()
                + Integer.parseInt(Character.toString(number.charAt(number.length() - 1))); // include dropped last digit
    }  // if (result % 10 == 0) (card)number is valid

    public static boolean isCardCorrect(String number) {
        return number.length() == 16 && patternDigits.matcher(number).matches()
                && applyLuhnAlgorithm(number) % 10 == 0;
    }

    public static boolean isPinCorrect(String pin) {
        return pin.length() == 4 && patternDigits.matcher(pin).matches();
    }
}
