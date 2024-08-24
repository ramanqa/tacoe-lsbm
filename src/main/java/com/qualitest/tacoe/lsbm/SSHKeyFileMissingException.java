package com.qualitest.tacoe.lsbm;

import java.nio.file.Path;

public class SSHKeyFileMissingException extends Exception {

    /**
     * constructor.
     *
     * @param   sshKeyFilePath   expected file path of sshKey file.
     */
    public SSHKeyFileMissingException(final Path sshKeyFilePath) {
        super("SSH-Key file not found in path: "
            + sshKeyFilePath.toString());
    }
}
