package com.qualitest.tacoe.lsbm;

public class Device {
   
    /**
     * variable to hold Device name.
     */
    private String name;

    /**
     * variable to hold Deviec MAC address.
     */
    private String mac;

    public Device (String dname, String macAdd) {
        this.name = dname;
        this.mac = macAdd;
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
    public String mac() {
        return this.mac;
    }

    public String toString() {
        return "[Device|" + this.mac() + "|" + this.name() + "]";
    }
}
