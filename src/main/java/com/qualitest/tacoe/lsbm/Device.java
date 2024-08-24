package com.qualitest.tacoe.lsbm;

public class Device {
   
    /**
     * variable to hold Device name.
     */
    private String name;

    /**
     * variable to hold Deviec MAC address.
     */
    private String macAddress;

    public Device (String dname, String mac) {
        this.name = dname;
        this.macAddress = mac;
    }

    /**
     * getter for Device name.
     *
     * @return  String  device name.
     */
    public String name() {
        return this.name;
    }

    /**
     * getter for Device MAC address.
     *
     * @return  String  device MAC address.
     */
    public String macAddress() {
        return this.macAddress;
    }
}
