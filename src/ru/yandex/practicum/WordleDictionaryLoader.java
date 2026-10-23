package ru.yandex.practicum;

import ru.yandex.practicum.exeptions.DictionaryLoadException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private static final int WORDS_SIZE = 5;

    public  WordleDictionary loadDictionary(String filename) throws DictionaryLoadException {
        List<String> words = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String normalizedWord = line.trim().toLowerCase().replace("ё", "е");

                if (normalizedWord.length() == WORDS_SIZE) {
                    words.add(normalizedWord);
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadException("Не удалось прочитать файл словаря: " + filename, e);
        }
        if (words.isEmpty()) {
            throw new DictionaryLoadException("Словарь пуст или не содержит слов из 5 букв.", null);
        }

        return new WordleDictionary(words);
    }
}
