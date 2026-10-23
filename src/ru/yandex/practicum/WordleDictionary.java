package ru.yandex.practicum;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WordleDictionary {

    private final List<String> words;
    private final Random random = new Random();

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public static String checkWord(String guess, String target) {
        char[] result = new char[5];
        boolean[] targetUsed = new boolean[5];
        boolean[] guessUsed = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                result[i] = '+';
                targetUsed[i] = true; // буква израсходована из target, нельзя использовать повторно
                guessUsed[i] = true; // обработанная буква +
            }
        }

        for (int i = 0; i < 5; i++) {
            if (guessUsed[i]) continue; // была обработана +, пропуск
            for (int j = 0; j < 5; j++) {
                if (!targetUsed[j] && guess.charAt(i) == target.charAt(j)) {
                    result[i] = '^';
                    targetUsed[j] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] == 0) result[i] = '-'; // где лежит символ 0, неявный каст
        }

        return new String(result);
    }

    public String getHint(Map<String, String> history) {
        if (history.isEmpty()) {
            return getRandomWord();
        }

        for (String word : words) {

            if (history.containsKey(word)) continue;

            boolean isCandidate = true;

            for (Map.Entry<String, String> entry : history.entrySet()) {
                String attempt = entry.getKey();
                String expectedHint = entry.getValue();

                String actualHint = checkWord(attempt, word);
                if (!actualHint.equals(expectedHint)) {
                    isCandidate = false;
                    break;
                }
            }

            if (isCandidate) {
                return word;
            }
        }
        return null;
    }

}
