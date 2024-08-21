package com.qualitest.tacoe.lsbm;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.moandjiezana.toml.Toml;

public class Configuration {

    /**
     * toml configuration data.
     */
    private Toml toml;

    /**
     * default contructor.
     *
     * @param  toml   TOML Object for configuration file
     */
    public Configuration(final Toml toml) {
        this.toml = toml;
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

        return new Configuration(new Toml().read(
              new File(stringPathToConfigToml)));
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
        return new Configuration(new Toml().read(pathToConfigToml.toFile()));
    }

    public <T> T to(Class<T> targetClass) {
        return this.toml.to(targetClass);
    }
}
