package com.qualitest.tacoe.lsbm;

public class UnexpectedCommandResponseException extends Exception {

    /**
     * constructor.
     *
     * @param   message   reason for exception.
     */
    public UnexpectedCommandResponseException(final String message) {
        super(message);
    }
}
