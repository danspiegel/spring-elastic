package br.com.elastic.example.elasticapp.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidEnvironmentException extends RuntimeException {

    public static final String INVALID_ENVIROMENT_MESSAGE = "Invalid enviroment.";

    public InvalidEnvironmentException(String message) {
        super(message);
        log.error(message, this);
    }

}
