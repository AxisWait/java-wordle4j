package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exeptions.DictionaryLoadException;
import ru.yandex.practicum.exeptions.WordNotFoundInDictionaryException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private static WordleDictionary realDictionary;
    private WordleGame game;

    @BeforeAll
    static void loadDictionaryOnce() throws DictionaryLoadException {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        realDictionary = loader.loadDictionary("words_ru.txt");
    }

    @BeforeEach
    void setUp() {
        game = new WordleGame(realDictionary);
    }

    // ==========================================
    // 1. ТЕСТЫ АЛГОРИТМА (Анализ слова)
    // ==========================================

    @Test
    void testAnalyzeWord_AllCorrect() {
        assertEquals("+++++", WordleDictionary.checkWord("герой", "герой"));
    }

    @Test
    void testAnalyzeWord_MixedMatches() {
        assertEquals("+^+^-", WordleDictionary.checkWord("горец", "герой"));
    }

    @Test
    void testAnalyzeWord_Duplicates() {
        String result = WordleDictionary.checkWord("озони", "озеро");
        assertEquals("++^--", result);
    }

    // ==========================================
    // 2. ТЕСТЫ ВАЛИДАЦИИ И ОШИБОК ВВОДА
    // ==========================================

    @Test
    void testValidation_WordTooShort() {
        try {
            game.makeMove("кот");
            // Если код дошел сюда, значит исключение НЕ было брошено. Это провал теста.
            fail("Должно было выброситься исключение IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Ожидаемое поведение: исключение поймано.
        }
        // Проверяем, что попытка не потратилась
        assertEquals(6, game.getAttemptsLeft());
    }

    @Test
    void testValidation_WordTooLong() {
        try {
            game.makeMove("велосипед");
            fail("Должно было выброситься исключение IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Исключение поймано успешно
        }
        assertEquals(6, game.getAttemptsLeft());
    }

    @Test
    void testValidation_WordNotInDictionary() {
        try {
            game.makeMove("ааааа"); // Такого слова точно нет
            fail("Должно было выброситься исключение WordNotFoundInDictionaryException");
        } catch (WordNotFoundInDictionaryException e) {
            // Исключение поймано успешно
        }
        assertEquals(6, game.getAttemptsLeft());
    }

    // ==========================================
    // 3. ТЕСТЫ ИГРОВОГО ЦИКЛА И ПОДСКАЗОК
    // ==========================================

    @Test
    void testHint_ReturnsValidWord() {
        String result = game.makeMove("");

        assertTrue(result.startsWith("Подсказка: "));

        String hintWord = result.split("\n")[0].replace("Подсказка: ", "");

        assertTrue(realDictionary.contains(hintWord));
        assertEquals(5, game.getAttemptsLeft());
    }

    @Test
    void testHint_AvoidsPreviousAttempts() {
        game.makeMove("пилот");

        String result = game.makeMove("");
        String hintWord = result.split("\n")[0].replace("Подсказка: ", "");

        assertNotEquals("пилот", hintWord);
    }

    @Test
    void testGameLoop_LosesAfterSixAttempts() {
        List<String> validWords = List.of("герой", "пилот", "ветер", "город", "доска", "пенал");

        for (int i = 0; i < 6; i++) {
            try {
                game.makeMove(validWords.get(i));
            } catch (Exception e) {
                // Игнорируем
            }
        }

        assertTrue(game.isGameOver(), "Игра должна завершиться после 6 попыток");
        assertEquals(0, game.getAttemptsLeft());
    }

    @Test
    void testGameLoop_WinsOnCorrectWord() {
        WordleDictionary testDict = new WordleDictionary(List.of("герой"));
        WordleGame testGame = new WordleGame(testDict);

        String result = testGame.makeMove("герой");

        assertEquals("+++++", result);
        assertTrue(testGame.isWin());
        assertTrue(testGame.isGameOver());
    }

    // ==========================================
    // 4. ТЕСТ ЗАГРУЗЧИКА (Обработка ошибки файла)
    // ==========================================

    @Test
    void testLoadDictionary_FileNotFound() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();

        try {
            loader.loadDictionary("non_existent_file.txt");
            fail("Должно было выброситься исключение DictionaryLoadException");
        } catch (DictionaryLoadException e) {
            // Успешно поймали исключение о том, что файл не найден
        }
    }
}