package com.qualitest.tacoe.lsbm;

import java.io.ByteArrayOutputStream;

public class CommandResponse {

    /**
     * variable to hold response.
     */ 
    private String response;
    
    /**
     * variable to hold error.
     */ 
    private String error;

    /**
     * constructor.
     *
     * @param   responseStream    IO Response stream from command execution.
     * @param   errorStream     Error Stream from command execuction.
     */
    public CommandResponse(final ByteArrayOutputStream responseStream,
          final ByteArrayOutputStream errorStream) {
        this.response = new String(responseStream.toByteArray());
        this.error = new String(errorStream.toByteArray());
    }

    /**
     * getter for response.
     *
     * @return  String  response
     */
    public String getResponse() {
        return this.response;
    }

    /**
     * getter for error.
     *
     * @return  String  error.
     */
    public String getError() {
        return this.error;
    }
}
