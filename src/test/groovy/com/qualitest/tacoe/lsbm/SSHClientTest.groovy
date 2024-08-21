package com.qualitest.tacoe.lsbm

import spock.lang.Specification

import java.time.Duration;

class SSHClientTest extends Specification {

    def toml_content = """
[lsbm]
host = "192.168.1.235"
port = 22
username = "ramandeep"
authType = "ssh-key"
sshKey = "./id_rsa"
password = "\$KaaYoT3"
sshKeyPassphrase = ""
            """

    def "exec() execute command and returns shell response"() {
        setup:
            def file = new File("./lsbm.config.toml")
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

            def sshClient = new SSHClient(Configuration.fromTOML(
            "./lsbm.config.toml")); 

        when:
            def cmdResponse = sshClient.exec("bluetoothctl show", Duration.ofSeconds(10));
        /**
            String response = "";
            String command = "bluetoothctl show";
            Long timeoutInSeconds = 5;

            SshClient client = SshClient.setUpDefaultClient();
            client.start();
            
            try (ClientSession session = client.connect("ramandeep", "192.168.1.235", 22)
              .verify(timeoutInSeconds, TimeUnit.SECONDS).getSession()) {
                //session.addPasswordIdentity("\\\$KaaYoT3");
                String[] files = new String[1];
                files[0] = "./id_ed25519";
                FileKeyPairProvider provider = new FileKeyPairProvider(java.nio.file.Paths.get("id_rsa"));
                provider.setPasswordFinder(FilePasswordProvider.of(""));
                session.setKeyIdentityProvider(provider);
                session.auth().verify(timeoutInSeconds, TimeUnit.SECONDS);
                
                try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream(); 
                    ByteArrayOutputStream errorStream = new ByteArrayOutputStream(); 
                    ClientChannel channel = session.createChannel(Channel.CHANNEL_EXEC, command)) {
                    channel.setOut(responseStream);
                    channel.setErr(errorStream);
                    try {
                        channel.open().verify(timeoutInSeconds, TimeUnit.SECONDS);
                        try (OutputStream pipedIn = channel.getInvertedIn()) {
                            pipedIn.write(command.getBytes());
                            pipedIn.flush();
                        }
                    
                        channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 
                        TimeUnit.SECONDS.toMillis(timeoutInSeconds));
                        System.out.println(new String(responseStream.toByteArray()));
                        System.out.println(new String(errorStream.toByteArray()));
                    } finally {
                        channel.close(false);
                    }
                }
            } finally {
                client.stop();
            }
        */ 
           
        then:
            println cmdResponse.getResponse();
            println cmdResponse.getError();
    }
}
