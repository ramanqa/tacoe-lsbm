package com.qualitest.tacoe.lsbm;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

public class Configuration {

    /**
     * toml configuration data.
     */
    private TomlParseResult toml;

    /**
     * default contructor.
     *
     * @param  tomlParseResult   TOML Object for configuration file
     */
    public Configuration(final TomlParseResult tomlParseResult) {
        this.toml = tomlParseResult;
    }

    /**
     * Configuration class constructor.
     *
     * @param  stringPathToConfigToml    String path to TOML file
     * @return Configuration object
     */
    public static final Configuration fromTOML(
        final String stringPathToConfigToml)
          throws ConfigurationFileMissingException, IOException {
        final Path source = Paths.get(stringPathToConfigToml);
        if (Files.notExists(source)) {
            throw new ConfigurationFileMissingException(source);
        }
        return new Configuration(Toml.parse(source));
    }

    /**
     * Configuration class constructor.
     *
     * @param  stringPathToConfigToml    String path to TOML file
     * @return Configuration object
     */
    public static final Configuration fromTOML(
        final Path pathToConfigToml)
          throws ConfigurationFileMissingException, IOException {
        if (Files.notExists(pathToConfigToml)) {
            throw new ConfigurationFileMissingException(pathToConfigToml);
        }
        return new Configuration(Toml.parse(pathToConfigToml));
    }

    /**
     * get value for configuration key form toml.
     *
     * @param   configKey   key for configuration in TOML
     * @return  String  value for configuration
     */
    private String getString(final String configKey)
          throws ConfigurationNotFoundException {
        if (!this.toml.contains(configKey)) {
            throw new ConfigurationNotFoundException(configKey);
        }
        return this.toml.getString(configKey);
    }

    /**
     * get value for configuration key from toml.
     *
     * @param   configKey   key for configuration in TOML
     * @param   defaultValue    Default value to return when configKey
     *    value is not available
     * @return  String  value for configuration
     */
    private String getString(final String configKey, String defaultValue)
          throws ConfigurationNotFoundException {
        if (!this.toml.contains(configKey)) {
            return defaultValue;
        }
        return this.toml.getString(configKey);
    }

    /**
     * get value for configuration key form toml.
     *
     * @param   configKey   key for configuration in TOML
     * @return  Integer  value for configuration
     */
    private Integer getInteger(final String configKey)
          throws ConfigurationNotFoundException {
        if (!this.toml.contains(configKey)) {
            throw new ConfigurationNotFoundException(configKey);
        }
        return Integer.parseInt(this.toml.get(configKey).toString());
    }

    /**
     * get value for configuration key from toml.
     *
     * @param   configKey   key for configuration in TOML
     * @param   defaultValue    Default value to return when configKey
     *    value is not available
     * @return  Integer  value for configuration
     */
    private Integer getInteger(final String configKey, Integer defaultValue)
          throws ConfigurationNotFoundException {
        if (!this.toml.contains(configKey)) {
            return defaultValue;
        }
        return Integer.parseInt(this.toml.get(configKey).toString());
    }

    /**
     * get host from configuration.
     *
     * @return  String  value of host config.
     */
    public final String host() throws ConfigurationNotFoundException {
        return this.getString("lsbm.host");
    }

    /**
     * get port from configuration.
     *
     * @return  Integer  value of port config.
     */
    public final Integer port() throws ConfigurationNotFoundException {
        return this.getInteger("lsbm.port", 22);
    }

    /**
     * get username from configuration.
     *
     * @return  String  value of username config.
     */
    public final String username() throws ConfigurationNotFoundException {
        return this.getString("lsbm.username");
    }

    /**
     * get auth_type from configuration.
     *
     * @return  String  value of auth_type config.
     */
    public final String authType() throws ConfigurationNotFoundException {
        return this.getString("lsbm.auth_type");
    }

    /**
     * get ssh_key file path. throws exception if the path does not exist.
     *
     * @return  Path  ssh key file path if path exists.
     */
    public final Path sshKey()
          throws ConfigurationNotFoundException, FileNotFoundException {
        Path path = Paths.get(this.getString("lsbm.ssh_key"));
        if (Files.notExists(path)) {
            throw new FileNotFoundException("SSH Key file not found at path: "
                + path.toString());
        }
        return path;
    }

    /**
     * get password from configuration.
     *
     * @return  String  value of password config.
     */
    public final String password() throws ConfigurationNotFoundException {
        return this.getString("lsbm.password");
    }
}
