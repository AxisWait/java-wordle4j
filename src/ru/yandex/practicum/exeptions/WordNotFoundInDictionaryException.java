package ru.yandex.practicum.exeptions;

public class WordNotFoundInDictionaryException extends RuntimeException {
    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}
