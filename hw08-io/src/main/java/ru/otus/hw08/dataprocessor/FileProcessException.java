package ru.otus.hw08.dataprocessor;

public class FileProcessException extends RuntimeException {

    public FileProcessException(Exception ex) {
        super(ex);
    }

    public FileProcessException(String msg) {
        super(msg);
    }
}
