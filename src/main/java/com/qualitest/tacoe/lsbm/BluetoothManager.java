package com.qualitest.tacoe.lsbm;

import java.io.IOException;
import java.nio.file.Path;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class BluetoothManager {
    
    /**
     * Configuration Object.
     */
    private LSBMConfiguration lsbmConfig;

    /**
     * SSHClient object.
     */
    private SSHClient sshClient;

    /**
     * default constructor. expects configuration file by name
     * 'bluetooth-manager-config.toml' at project root.
     */
    public BluetoothManager()
          throws ConfigurationFileMissingException, IOException {
        this.lsbmConfig = Configuration.fromTOML(
            "./lsbm.config.toml").to(LSBMConfiguration.class);
        this.sshClient = new SSHClient(this.lsbmConfig);
    }

    /**
     * getter for config.
     *
     * @return    LSBMConfiguration   configuraton object\
     */
    public LSBMConfiguration getConfig() {
        return this.lsbmConfig;
    }

    /**
     * contructor.
     *
     * @param   config   Configuration object
     */
    public BluetoothManager(final Configuration config) {
        this.lsbmConfig = config.to(LSBMConfiguration.class);
        this.sshClient = new SSHClient(this.lsbmConfig);
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
    public SSHClient sshClient() {
        return this.sshClient;
    }

    /**
     * sshClient setter. for DI.
     *
     * @param   sshClientObject   SSHClient Object
     */
    public void setSshClient(final SSHClient sshClientObject) {
        this.sshClient = sshClientObject;
    }

    /**
     * getter for default bluetooth controller name.
     *
     * @return  String  name of Default bluetooth controller.
     */
    public final String name() throws SSHKeyFileMissingException, IOException {
        CommandResponse cmdResponse = this.sshClient().exec(
            "bluetoothctl show");
        String name = cmdResponse.getResponse()
            .split("Name: ")[1].split("\n")[0];
        return name;
    }

    /**
     * get discoverable state of defacult controller.
     *
     * @return  Boolean   true if discoverable, false otherwise.
     */
    public Boolean discoverable()
          throws SSHKeyFileMissingException, IOException {
        CommandResponse cmdResponse = this.sshClient().exec(
            "bluetoothctl show");
        String discoverable = cmdResponse.getResponse()
            .split("Discoverable: ")[1].split("\n")[0];

        return discoverable.equalsIgnoreCase("yes");
    }

    /**
     * get pairable state of default controller.
     *
     * @return  Boolean   true id device is pairable, false otherwise.
     */
    public Boolean pairable()
        throws SSHKeyFileMissingException, IOException {
        CommandResponse cmdResponse = this.sshClient().exec(
            "bluetoothctl show");
        String discoverable = cmdResponse.getResponse()
            .split("Pairable: ")[1].split("\n")[0];

        return discoverable.equalsIgnoreCase("yes");
    }

    /**
     * get a list of paired devices.
     *
     * @param   DeviceStatus  Status of devices to filter known device list.
     *    DeviceStatus can be ANY/PAIRED/BONDED/TRUSTED/CONNECTED.
     * @return  List<Device>  list of devices.
     */
    public List<Device> devices(final DeviceStatus deviceStatus)
        throws SSHKeyFileMissingException, IOException {
        String command = "bluetoothctl devices";
        if (deviceStatus != DeviceStatus.ANY) {
            command += " " + StringUtils.capitalize(deviceStatus.toString()
                .toLowerCase());
        }
        CommandResponse cmdResponse = this.sshClient().exec(command);

        List<Device> devices = new ArrayList<Device>();
        for (String line : cmdResponse.getResponse().split("\n")) {
            String name = line.split(" ")[2];
            String mac = line.split(" ")[1];
            Device device = new Device(name, mac);
            devices.add(device);
        }

        return devices;
    }

    /**
     * get a list of all known deviecs in all states.
     *
     * @return  List<Device>  list of devices.
     */
    public List<Device> devices() throws SSHKeyFileMissingException, IOException {
        return this.devices(DeviceStatus.ANY);
    }

}
