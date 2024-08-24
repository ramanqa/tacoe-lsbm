package com.qualitest.tacoe.lsbm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class LSBM {

    /**
     * default ssh port contant.
     */
    private static final int DEFAULT_SSH_PORT = 22;

    /**
     * constant for default commandWaitTimeoutInSeconds.
     */
    private static final int DEFAULT_COMMAND_WAIT_TIMEOUT_IN_SECONDS = 5;

    /**
     * configuration host variable.
     */
    private String host;

    /**
     * configuration port variable.
     */
    private Integer port;

    /**
     * configuration username variable.
     */
    private String username;

    /**
     * configuration authType variable.
     */
    private String authType;

    /**
     * configuration password variable.
     */
    private String password;

    /**
     * configuration sshKey variable.
     */
    private String sshKey;

    /**
     * timeout for remote commands.
     */
    private Integer commandWaitTimeoutInSeconds;

    /**
     * configuration sshKeyPassphrase variable.
     */
    private String sshKeyPassphrase;

    /**
     * host getter.
     *
     * @return    String    host value
     */
    public String host() {
        return this.host;
    }

    /**
     * port getter.
     *
     * @return    Integer    port value, defaults to 22
     */
    public Integer port() {
        if (this.port == null) {
            return DEFAULT_SSH_PORT;
        }
        return this.port;
    }

    /**
     * username getter.
     *
     * @return  String  username value.
     */
    public String username() {
        return this.username;
    }

    /**
     * authType getter.
     *
     * @return  String  authType value. defualts to 'password'.
     */
    public String authType() {
        if (this.authType == null) {
            return "password";
        }
        return this.authType;
    }

    /**
     * password getter.
     *
     * @return  String  password value.
     */
    public String password() {
        return this.password;
    }

    /**
     * sshKey Path getter.
     *
     * @return  Path  sshKey PATH value.
     */
    public Path sshKey() throws SSHKeyFileMissingException {
        if (!Files.exists(Paths.get(this.sshKey))) {
            throw new SSHKeyFileMissingException(Paths.get(this.sshKey));
        }
        return Paths.get(this.sshKey);
    }

    /**
     * sshKeyPassphrase getter.
     *
     * @return  String  sshKeyPassphrase value.
     */
    public String sshKeyPassphrase() {
        return this.sshKeyPassphrase;
    }

    /**
     * getter for commandWaitTimeoutInSeconds.
     *
     * @return  Duration  commandWaitTimeoutInSeconds.
     */
    public Duration commandWaitTimeout() {
        if (this.commandWaitTimeoutInSeconds == null) {
            return Duration.ofSeconds(DEFAULT_COMMAND_WAIT_TIMEOUT_IN_SECONDS);
        }
        return Duration.ofSeconds(this.commandWaitTimeoutInSeconds);
    }
}
