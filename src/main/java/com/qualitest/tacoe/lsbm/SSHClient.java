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
import java.util.concurrent.TimeUnit;
import java.util.EnumSet;

public class SSHClient {

    /**
     * configuration from TOML.
     */
    private Configuration configuration;

    /**
     * Constructor.
     * 
     * @param   configuration   TOML configuration for SSH Connection.
     */
    public SSHClient(final Configuration configuration) {
        this.configuration = configuration;
    }
    
    /**
     * execue shell command on ssh and get response.
     *
     * @param   command   command to execute
     * @return    String SSH command response
     */
    //public String exec(final String command)
    //      throws ConfigurationNotFoundException, FileNotFoundException, 
    //         JSchException, InterruptedException {
    //}

}
