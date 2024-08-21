package com.qualitest.tacoe.lsbm;

import java.io.IOException;
import java.nio.file.Path;

public class BluetoothManager {

    /**
     * Configuration Object.
     */
    private Configuration configuration;

    /**
     * default constructor. expects configuration file by name
     * 'bluetooth-manager-config.toml' at project root.
     */
    public BluetoothManager()
          throws ConfigurationFileMissingException, IOException {
        this.configuration = Configuration.fromTOML(
            "./lsbm.config.toml");
    }

    /**
     * contructor.
     *
     * @param   configuration   Configuration object
     */
    public BluetoothManager(final Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * contructor to init BluetoothManager using TOML config file path.
     *
     * @param   tomlFilePath    PATH to config TOML file.
     * @return  BluetoothManager Object  
     */
    public static BluetoothManager fromTOML(final Path tomlFilePath)
          throws ConfigurationFileMissingException, IOException {
        return new BluetoothManager(Configuration.fromTOML(tomlFilePath));
    }

    /**
     * get SSH Client.
     *
     * @return    SSHClient object.
     */
    private SSHClient sshClient() {
        return new SSHClient(this.configuration);
    }

    /*
    public final String name() {
        // run SSH command
    }
    */
}
