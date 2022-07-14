package com.company;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static int bulls;
    private static int cows;
    private static String secretCode;

    public static void main(String[] args) {
        System.out.println("Input the length of the secret code:");
        String lengthStr = scanner.next();
        String regex = "\\d+";

        if (!lengthStr.matches(regex)) {
            System.out.printf("Error: \"%s\" isn't a valid number.",lengthStr);
            System.exit(0);
        }

        int length = Integer.parseInt(lengthStr);

        System.out.println("Input the number of possible symbols in the code:");
        int range = scanner.nextInt();

        if (range < length || length <= 0) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", length, range);
            System.exit(0);
        }

        if (range > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        }

        try {
            secretCode = generateSecretCode(length, range);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        String text = range <= 10 ? "0-" + (range - 1) : "0-9, a-" + (char) ('a' + range - 11);
        System.out.printf("The secret is prepared: %s (%s).%n", secretCode.replaceAll(".", "*"), text);
        System.out.println("Okay, let's start a game!");

        for (int i = 1;; i++) {
            System.out.printf("Turn %d:%n", i);
            String answer = scanner.next();
            countBullsCows(answer);
            System.out.println(getResult());

            if (bulls == secretCode.length()) {
                break;
            }
        }

        System.out.println("Congratulations! You guessed the secret code.");
    }

    private static String generateSecretCode(int length, int range) {
        List<Character> symbols = getCharactersFromRange(range);
        StringBuilder code = new StringBuilder();

        if (length <= 36) {
            for (int i = 0; i < length; i++) {
                code.append(getRandomCharacterFromList(symbols));
            }
            return code.toString();
        } else {
            throw new IllegalArgumentException(String.format("Error: can't generate a secret number with a " +
                    "length of %d because there aren't enough unique digits.%n", length));
        }
    }

    private static Character getRandomCharacterFromList(List<Character> list) {
        Random random = new Random();
        Character result = list.get(random.nextInt(list.size()));
        list.remove(result);
        return result;
    }

    private static List<Character> getCharactersFromRange(int range) {
        List<Character> result = new ArrayList<>();
        for (int i = 0; i < range; i++) {
            if (i < 10) {
                result.add((char) ('0' + i));
            } else {
                result.add((char) ('a' + i - 10));
            }
        }
        return result;
    }

    private static void countBullsCows(String answer) {
        bulls = 0;
        cows = 0;
        int cowCount = 0;
        for (int i = 0; i < answer.length(); i++) {
            for (int j = 0; j < secretCode.length(); j++) {
                if (i == j && answer.charAt(i) == secretCode.charAt(j)) {
                    bulls++;
                }
                if (answer.charAt(i) == secretCode.charAt(j)) {
                    cowCount++;
                }
            }
        }
        cows = cowCount - bulls;
    }

    private static String getResult() {
        String bullStr = bulls == 0 ? "" : String.format("%d bull(s)", bulls);
        String cowStr = cows == 0 ? "" : String.format("%d cow(s)", cows);
        String result = bulls == 0 && cows == 0 ? "None" : String.join(" and ", bullStr, cowStr);
        return String.format("Grade: %s", result);
    }

}
