package ru.yandex.practicum.exeptions;

public class DictionaryLoadException extends Exception {
    public DictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
