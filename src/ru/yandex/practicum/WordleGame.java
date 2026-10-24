package ru.yandex.practicum;

import ru.yandex.practicum.exeptions.WordNotFoundInDictionaryException;

import java.util.LinkedHashMap;
import java.util.Map;

public class WordleGame {
    private final String answer;
    private int steps;
    private final WordleDictionary dictionary;

    private final Map<String, String> history = new LinkedHashMap<>();
    private String lastHint = "";

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = 0;
    }

    public String makeMove(String input) {
        String word = input.trim().toLowerCase().replace('ё', 'е');

        // 1. Обработка подсказки (пустой ввод)
        if (word.isEmpty()) {
            String hint = dictionary.getHint(history);
            if (hint == null) {
                return "Подсказок больше нет!";
            }

            steps++; // Увеличиваем шаг
            lastHint = WordleDictionary.checkWord(hint, answer);
            history.put(hint, lastHint);
            return "Подсказка: " + hint + "\n" + lastHint;
        }

        // 2. Валидация слова
        if (word.length() != 5) {
            throw new IllegalArgumentException("Слово должно состоять из 5 букв");
        }
        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionaryException("Такого слова нет в словаре");
        }

        // 3. Игровой ход
        steps++; // Увеличиваем шаг
        lastHint = WordleDictionary.checkWord(word, answer);
        history.put(word, lastHint);

        return lastHint;
    }

    public boolean isGameOver() {
        return steps >= 6 || isWin();
    }

    public boolean isWin() {
        return "+++++".equals(lastHint);
    }

    public int getAttemptsLeft() {
        return 6 - steps;
    }

    public String getAnswer() {
        return answer;
    }
}
