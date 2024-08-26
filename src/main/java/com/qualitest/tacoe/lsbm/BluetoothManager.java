package com.qualitest.tacoe.lsbm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.time.Duration;

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
     * download file from remote machine.
     *
     * @param   sourceFilePath    file path of file to download.
     * @param   destinationPath   location where to download the file.
     * @param   timeout   max wait timeout to wait for download to complete.
     */
    public void download(String sourceFilePath, String destinationPath,
          Duration timeout) throws SSHKeyFileMissingException, IOException {
        this.sshClient().download(sourceFilePath, destinationPath, timeout); 
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
     * switch discoverable on/off.
     *
     * @param   doSwitch  Switch.ON/Switch.OFF.
     */
    public void discoverable(final Switch doSwitch)
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl discoverable "
            + doSwitch.toString().toLowerCase()); 
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
     * switch pairable on/off.
     *
     * @param   doSwitch  Switch.ON/Switch.OFF.
     */
    public void pairable(final Switch doSwitch)
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl pairable "
            + doSwitch.toString().toLowerCase()); 
    }

    /**
     * switch power on/off.
     *
     * @param   doSwitch  Switch.ON/Switch.OFF.
     */
    public void power(final Switch doSwitch)
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl power "
            + doSwitch.toString().toLowerCase()); 
    }

    /**
     * switch scan on/off.
     *
     * @param   doSwitch  Switch.ON/Switch.OFF.
     */
    public void scan(final Switch doSwitch)
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl scan "
            + doSwitch.toString().toLowerCase()); 
    }

    /**
     * switch agent on/off.
     *
     * @param   doSwitch  Switch.ON/Switch.OFF.
     */
    public void agent(final Switch doSwitch)
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl agent "
            + doSwitch.toString().toLowerCase()); 
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
            try{
                String name = line.split(" ")[2];
                String mac = line.split(" ")[1];
                Device device = new Device(name, mac);
                devices.add(device);
            } catch (ArrayIndexOutOfBoundsException e) {
                //ignore exception
            }
        }

        return devices;
    }

    /**
     * get a list of all known deviecs in all states.
     *
     * @return  List<Device>  list of devices.
     */
    public List<Device> devices() throws SSHKeyFileMissingException,
           IOException {
        return this.devices(DeviceStatus.ANY);
    }

    /**
     * rest controller scan list by power of and on the controller.
     *
     * @throws  SSHKeyFileMissingException  thrown when ssh key file provided
     *    in configuration does not exist.
     * @throws  IOException   thrown when config file is not readable or
     *    accessible.
     * @throws  InterruptedException  thrown when wait time is interrupted.
     */
    public void resetController() 
        throws SSHKeyFileMissingException, IOException, InterruptedException {
        this.sshClient().exec("bluetoothctl power off");
        Thread.sleep(3000);
        this.sshClient().exec("bluetoothctl power on");
    }

    public void enableAgent()
        throws SSHKeyFileMissingException, IOException {
        this.agent(Switch.ON);
    }

    /**
     * return device from scanned devices list for matching MAC.
     *
     * @param   deviceMAC   device MAC address to serach in scanned devices.
     */
    public Device getDeviceByMac(final String deviceMAC)
          throws SSHKeyFileMissingException, IOException,
              NoSuchDeviceFoundInScanListException {
        for (Device device:this.devices()) {
            if (device.mac().equals(deviceMAC)) {
                return device;
            }
        }
        throw new NoSuchDeviceFoundInScanListException(
            Map.of("mac", deviceMAC)); 
    }

    /**
     * return device from scanned devices list for matching Deviec Name. 
     *
     * @param   name   device name to serach in scanned devices.
     */
    public Device getDeviceByName(final String name)
          throws SSHKeyFileMissingException, IOException,
              NoSuchDeviceFoundInScanListException {
        for (Device device:this.devices()) {
            if (device.name().equals(name)) {
                return device;
            }
        }
        throw new NoSuchDeviceFoundInScanListException(
            Map.of("name", name)); 
    }

    /**
     * trust device.
     * @param   Device    device object from scanned devices list.
     */
    public void trustDevice(final Device device)
          throws SSHKeyFileMissingException, IOException, 
            UnexpectedCommandResponseException {
        CommandResponse response = this.sshClient().exec("bluetoothctl trust "
            + device.mac());
        if (response.getResponse().endsWith("Changing " + device.mac()
              + " trust succeeded")) {
            throw new UnexpectedCommandResponseException(
                "Device: " + device + " trust action failed.\n"
                + response.getResponse() + "\n----\n" + response.getError());
        }
    }

    /**
     * untrust device.
     * @param   device    device object from scanned devices list.
     */
    public void untrustDevice(final Device device) 
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl untrust " + device.mac());
    }

    /**
     * pair device.
     * 
     * @param   device    device object from scanned devices list.
     * @param   timeout   timeout to wait for pair action to complete.
     */
    public void pairDevice(final Device device, Duration timeout)
          throws SSHKeyFileMissingException, IOException,
            UnexpectedCommandResponseException {
        CommandResponse response = this.sshClient().exec("bluetoothctl pair "
            + device.mac(), timeout);
        if (response.getResponse().endsWith("Pairing successful")) {
            throw new UnexpectedCommandResponseException(
                "Device: " + device + " pair action failed.\n"
                + response.getResponse() + "\n----\n" + response.getError());
        }
    }

    /**
     * unpair device.
     * @param   device    device object from scanned devices list.
     */
    public void unpairDevice(final Device device)
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl cancel-pairing" + device.mac());
    }

    /**
     * connect device.
     * @param   device    device object from scanned devices list.
     */
    public void connectDevice(final Device device)
          throws SSHKeyFileMissingException, IOException,
             UnexpectedCommandResponseException {
        CommandResponse response = this.sshClient().exec("bluetoothctl connect "
            + device.mac());
        if (response.getResponse().endsWith("Connection successful")) {
            throw new UnexpectedCommandResponseException(
                "Device: " + device + " connect action failed.\n"
                + response.getResponse() + "\n----\n" + response.getError());
        }
    }

    /**
     * disconnect device.
     * @param   device    device object from scanned devices list.
     */
    public void disconnectDevice(final Device device)
          throws SSHKeyFileMissingException, IOException {
        this.sshClient().exec("bluetoothctl disconnect " + device.mac());
    }

    /**
     * remove device.
     * @param   device    device object from scanned devices list.
     */
    public void removeDevice(final Device device)
          throws SSHKeyFileMissingException, IOException,
             UnexpectedCommandResponseException {
        CommandResponse response = this.sshClient().exec("bluetoothctl remove "
            + device.mac());
        if (response.getResponse().endsWith("Device has been removed")) {
            throw new UnexpectedCommandResponseException(
                "Device: " + device + " remove action failed.\n"
                + response.getResponse() + "\n----\n" + response.getError());
        }
    }

    public Path recordAudio(Duration durationOfRecording)
          throws SSHKeyFileMissingException, IOException {
        String filename = "audio-record." + System.currentTimeMillis() + ".wav";
        this.sshClient().exec("arecord -t wav -d "
            + durationOfRecording.getSeconds() + " "
            + filename, durationOfRecording.plusSeconds(2));
        this.sshClient().download(filename, ".", Duration.ofSeconds(5));
        return Paths.get("./" + filename);
    }


        // reset controller
        // enable agnet
        // enable pairable
        // enable discoverable
        // scan devices
        // check device discovered  
        // trust device
        // pair device
        
}
