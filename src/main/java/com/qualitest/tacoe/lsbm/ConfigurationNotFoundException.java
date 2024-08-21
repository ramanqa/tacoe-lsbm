package com.qualitest.tacoe.lsbm;

public class ConfigurationNotFoundException extends Exception {

    /**
     * constructor.
     *
     * @param   configKey   String key for configuration in TOML file.
     */
    public ConfigurationNotFoundException(final String configKey) {
        super("Configuration not found in TOML file for key: " + configKey);
    }

}
