package com.qualitest.tacoe.lsbm

import spock.lang.Specification
import static org.mockito.Mockito.*;
import java.time.Duration;

class BluetoothManagerTest extends Specification {

    def tomlFile = new File("./lsbm.config.toml")

    def setup() {
        def toml_content = """
            [lsbm]
            host = "192.168.1.235"
            port = 22
            username = "ramandeep"
            authType = "ssh-key"
            sshKey = "./id_rsa"
            password = "PASSWORD"
            sshKeyPassphrase = ""
                """
        tomlFile.exists() ? tomlFile.delete() : false
        tomlFile.createNewFile()
        tomlFile.write toml_content
    }

    def cleanup() {
        tomlFile.exists() ? tomlFile.delete() : false
    }

    def "name() returns default controler name"() {
        setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""
                  Controller D8:3A:DD:01:F6:90 (public)
                  Name: ControllerName
                  Alias: ControllerAlias
                  Class: 0x006c0000
                  Powered: yes
                  Discoverable: no
                  DiscoverableTimeout: 0x000000b4
                  Pairable: yes
                  UUID: A/V Remote Control        (0000110e-0000-1000-8000-00805f9b34fb) 
                """)
            when(mockedSSHClient.exec("bluetoothctl show"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            assert bluetoothManager.name().equals("ControllerName")
    }

    def "discoverable() returns default controller discoberable status"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""
                  Controller D8:3A:DD:01:F6:90 (public)
                  Name: ControllerName
                  Alias: ControllerAlias
                  Class: 0x006c0000
                  Powered: yes
                  Discoverable: no
                  DiscoverableTimeout: 0x000000b4
                  Pairable: yes
                  UUID: A/V Remote Control        (0000110e-0000-1000-8000-00805f9b34fb) 
                """)
            when(mockedSSHClient.exec("bluetoothctl show"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            assert !bluetoothManager.discoverable()
    }

    def "pairable() returns default controller pairable status"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""
                  Controller D8:3A:DD:01:F6:90 (public)
                  Name: ControllerName
                  Alias: ControllerAlias
                  Class: 0x006c0000
                  Powered: yes
                  Discoverable: no
                  DiscoverableTimeout: 0x000000b4
                  Pairable: yes
                  UUID: A/V Remote Control        (0000110e-0000-1000-8000-00805f9b34fb) 
                """)
            when(mockedSSHClient.exec("bluetoothctl show"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            assert bluetoothManager.pairable()
    }

    def "devices(DeviceStatus.PAIRED) returns list of devices in paired status"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""Device 64:A2:F9:90:69:A7 OnePlus6
Device 40:4E:36:49:35:5E Pixel2""")
            when(mockedSSHClient.exec("bluetoothctl devices Paired"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            List<Device> devices = bluetoothManager.devices(
                DeviceStatus.PAIRED);
            assert devices.size() == 2;
            assert devices.get(0).name() == "OnePlus6";
            assert devices.get(1).name() == "Pixel2";
    }

    def "devices(DeviceStatus.BONDED) returns list of devices in bonded status"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""Device 64:A2:F9:90:69:A7 OnePlus6
Device 40:4E:36:49:35:5E Pixel2""")
            when(mockedSSHClient.exec("bluetoothctl devices Bonded"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            List<Device> devices = bluetoothManager.devices(
                DeviceStatus.BONDED);
            assert devices.size() == 2;
            assert devices.get(0).name() == "OnePlus6";
            assert devices.get(1).name() == "Pixel2";
    }

    def "devices(DeviceStatus.TRUSTED) returns list of devices in trusted status"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""Device 64:A2:F9:90:69:A7 OnePlus6
Device 40:4E:36:49:35:5E Pixel2""")
            when(mockedSSHClient.exec("bluetoothctl devices Trusted"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            List<Device> devices = bluetoothManager.devices(
                DeviceStatus.TRUSTED);
            assert devices.size() == 2;
            assert devices.get(0).name() == "OnePlus6";
            assert devices.get(1).name() == "Pixel2";
    }

    def "devices(DeviceStatus.CONNECTED) returns list of devices in connected status"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""Device 64:A2:F9:90:69:A7 OnePlus6
Device 40:4E:36:49:35:5E Pixel2""")
            when(mockedSSHClient.exec("bluetoothctl devices Connected"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            List<Device> devices = bluetoothManager.devices(
                DeviceStatus.CONNECTED);
            assert devices.size() == 2;
            assert devices.get(0).name() == "OnePlus6";
            assert devices.get(1).name() == "Pixel2";
    }

    def "devices() returns list of devices"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("""Device 64:A2:F9:90:69:A7 OnePlus6
Device 40:4E:36:49:35:5E Pixel2""")
            when(mockedSSHClient.exec("bluetoothctl devices"))
                .thenReturn(mockedCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            List<Device> devices = bluetoothManager.devices();
            assert devices.size() == 2;
            assert devices.get(0).name() == "OnePlus6";
            assert devices.get(1).name() == "Pixel2";
    }

    def "scanForDevices(Duration timeout) makes devices available in scan list"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("")
            when(mockedSSHClient.exec("bluetoothctl scan on",
              Duration.ofSeconds(10)))
                .thenReturn(mockedCommandResponse)
            CommandResponse mockedDevicesCommandResponse = mock(CommandResponse.class)
            when(mockedDevicesCommandResponse.getResponse()).thenReturn(
                """Device 64:A2:F9:90:69:A7 OnePlus6
Device 40:4E:36:49:35:5E Pixel2""")
            when(mockedSSHClient.exec("bluetoothctl devices"))
                .thenReturn(mockedDevicesCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);
        
        then:
            bluetoothManager.scan(Switch.ON)
            Thread.sleep(10000);
            def response = bluetoothManager.devices()
            assert response.get(0).name() == "OnePlus6"
            assert response.get(1).name() == "Pixel2"
    }

    def "resetController() clears deviecs list"() {
         setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)
            CommandResponse mockedCommandResponse = mock(CommandResponse.class)
            when(mockedCommandResponse.getResponse()).thenReturn("")
            when(mockedSSHClient.exec("bluetoothctl power off"))
                .thenReturn(mockedCommandResponse)
            when(mockedSSHClient.exec("bluetoothctl power on"))
                .thenReturn(mockedCommandResponse)
            CommandResponse mockedDevicesCommandResponse = mock(CommandResponse.class)
            when(mockedDevicesCommandResponse.getResponse()).thenReturn("");
            when(mockedSSHClient.exec("bluetoothctl devices"))
                .thenReturn(mockedDevicesCommandResponse)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))
            bluetoothManager.setSshClient(mockedSSHClient);

            bluetoothManager.resetController();
        
        then:
            def response = bluetoothManager.devices()
            assert response.size() == 0
    }

    def "pair device"() {
        setup:
            SSHClient mockedSSHClient = mock(SSHClient.class)

        when:
            def bluetoothManager = new BluetoothManager(Configuration.fromTOML(
                "./lsbm.config.toml"))

            bluetoothManager.resetController()
            bluetoothManager.pairable(Switch.ON)
            bluetoothManager.discoverable(Switch.ON)
            bluetoothManager.agent(Switch.ON)
            bluetoothManager.scan(Switch.ON)
            Thread.sleep(20000)
            def response = bluetoothManager.devices()
            def onePlus = bluetoothManager.getDeviceByName("OnePlus6")
            bluetoothManager.trustDevice(onePlus);
            bluetoothManager.pairDevice(onePlus, Duration.ofSeconds(30));
            bluetoothManager.connectDevice(onePlus);
            Thread.sleep(5);
            def path = bluetoothManager.recordAudio(Duration.ofSeconds(20));
            println path.toString();

        then:
            println "done"
            //println(response);
            //println(bluetoothManager.getDeviceByName("OnePlus6").mac());
            bluetoothManager.removeDevice(onePlus);
          
    }
}
