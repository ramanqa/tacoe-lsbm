package com.qualitest.tacoe.lsbm;

import java.io.IOException;
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
     * @param  tomlObj   TOML Object for configuration file
     */
    public Configuration(final Toml tomlObj) {
        this.toml = tomlObj;
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
     * @param  pathToConfigToml    Path Object to TOML file
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

    /**
     * instantiate TOML object to LSBM Configuration class.
     *
     * @param   targetClass   Class File for instantiation.
     * @param   <T>   class mapper
     * @return  <T>   instantiated class
     */
    public <T> T to(final Class<T> targetClass) {
        return this.toml.to(targetClass);
    }
}
