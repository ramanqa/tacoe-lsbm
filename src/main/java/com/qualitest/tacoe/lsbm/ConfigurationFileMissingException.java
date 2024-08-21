package com.qualitest.tacoe.lsbm;

import java.nio.file.Path;

public class ConfigurationFileMissingException extends Exception {

    /**
     * constructor.
     *
     * @param   tomlFilePath   expected file path of TOML config file.
     */
    public ConfigurationFileMissingException(final Path tomlFilePath) {
        super("Configuration TOML file not found in path: "
            + tomlFilePath.toString());
    }
}
