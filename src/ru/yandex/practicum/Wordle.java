package ru.yandex.practicum;

import ru.yandex.practicum.exeptions.DictionaryLoadException;
import ru.yandex.practicum.exeptions.WordNotFoundInDictionaryException;

import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter("game.log")) {
            startGame(log);
        } catch (Exception e) {
            System.err.println("Критическая ошибка запуска программы: " + e.getMessage());
        }
    }

    private static void startGame(PrintWriter log) {
        WordleDictionary dictionary;

        // 1. Загрузка словаря
        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            dictionary = loader.loadDictionary("words_ru.txt");
            log.println("Словарь успешно загружен.");
        } catch (DictionaryLoadException e) {
            // Ошибка загрузки — пишем в лог и сообщаем пользователю
            log.println("Ошибка загрузки словаря: " + e.getMessage());
            System.out.println("Не удалось загрузить словарь. Проверьте файл words_ru.txt.");
            return;
        }

        // 2. Создание игры
        WordleGame game = new WordleGame(dictionary);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в Wordle!");
        System.out.println("Загадано слово из 5 букв. У вас 6 попыток.");
        System.out.println("Введите слово или нажмите Enter для подсказки.\n");

        // 3. Игровой цикл
        while (!game.isGameOver()) {
            System.out.println("Осталось попыток: " + game.getAttemptsLeft());
            System.out.print("> ");

            String input = scanner.nextLine();

            try {
                // Делаем ход или получаем подсказку
                String result = game.makeMove(input);
                System.out.println(result);

            } catch (IllegalArgumentException e) {
                // Ошибка ввода (не 5 букв, пустая строка в dictionary)
                System.out.println("Ошибка: " + e.getMessage());
            } catch (WordNotFoundInDictionaryException e) {
                // Слова нет в словаре
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                // Непредвиденная игровая ошибка — пишем в лог
                log.println("Непредвиденная ошибка в игре: " + e.getMessage());
                System.out.println("Произошла непредвиденная ошибка. Подробности в логе.");
            }
        }

        // 4. Итог игры
        System.out.println("\n==================================");
        if (game.isWin()) {
            System.out.println("Поздравляем! Вы угадали слово!");
        } else {
            System.out.println("Игра окончена. Вы не угадали.");
        }
        System.out.println("Загаданное слово: " + game.getAnswer());
        System.out.println("==================================");

        log.println("Игра завершена. Результат: " + (game.isWin() ? "Победа" : "Поражение"));
    }

}
