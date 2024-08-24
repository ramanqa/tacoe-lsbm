package com.qualitest.tacoe.lsbm;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.common.config.keys.FilePasswordProvider;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.EnumSet;
import java.time.Duration;

public class SSHClient {

    /**
     * configuration from TOML.
     */
    private LSBMConfiguration config;

    /**
     * Constructor.
     *
     * @param   configuration   TOML configuration for SSH Connection.
     */
    public SSHClient(final Configuration configuration) {
        this.config = configuration.to(LSBMConfiguration.class);
    }

    /**
     * alternate contructor.
     *
     * @param   lsbmConfiguration   LSBMConfiguration object.
     */
    public SSHClient(final LSBMConfiguration lsbmConfiguration) {
        this.config = lsbmConfiguration;
    }

    /**
     * getter for config.
     *
     * @return    LSBMConfiguration   configuraton object\
     */
    public LSBMConfiguration getConfig() {
        return this.config;
    }

    /**
     * execue shell command on ssh and get response.
     *
     * @param   command   command to execute
     * @param   timeout   Duration timeout to wait for command response.
     * @return  CommandResponse   SSH command response
     */
    public CommandResponse exec(final String command, final Duration timeout)
            throws SSHKeyFileMissingException, IOException {
        CommandResponse cmdResponse;
        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        try (ClientSession session = client.connect(
              this.getConfig().lsbm().username(),
              this.getConfig().lsbm().host(),
              this.getConfig().lsbm().port())
          .verify(timeout.getSeconds(), TimeUnit.SECONDS).getSession()) {
            if (this.getConfig().lsbm().password() != null) {
                session.addPasswordIdentity(this.getConfig().lsbm().password());
            }
            if (this.getConfig().lsbm().authType().equals("ssh-key")) {
                FileKeyPairProvider provider = new FileKeyPairProvider(
                    this.getConfig().lsbm().sshKey());
                provider.setPasswordFinder(FilePasswordProvider.of(
                    this.getConfig().lsbm().sshKeyPassphrase()));
                session.setKeyIdentityProvider(provider);
            }
            session.auth().verify(timeout.getSeconds(), TimeUnit.SECONDS);
            try (ByteArrayOutputStream responseStream =
              new ByteArrayOutputStream();
                ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
                ClientChannel channel = session.createChannel(
                    Channel.CHANNEL_EXEC, command)) {
                channel.setOut(responseStream);
                channel.setErr(errorStream);
                try {
                    channel.open().verify(timeout.getSeconds(),
                        TimeUnit.SECONDS);
                    try (OutputStream pipedIn = channel.getInvertedIn()) {
                        pipedIn.write(command.getBytes());
                        pipedIn.flush();
                    }
                    channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
                    TimeUnit.SECONDS.toMillis(timeout.getSeconds()));
                    cmdResponse = new CommandResponse(responseStream,
                        errorStream);
                } finally {
                    channel.close(false);
                }
            }
        } finally {
            client.stop();
        }
        return cmdResponse;
    }

    /**
     * execue shell command on ssh and get response.
     *
     * @param   command   command to execute
     * @return  CommandResponse   SSH command response
     */
    public CommandResponse exec(final String command)
          throws SSHKeyFileMissingException, IOException {
        return this.exec(command, this.getConfig().lsbm().commandWaitTimeout());
    }
}
