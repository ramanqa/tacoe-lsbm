package com.qualitest.tacoe.lsbm

import spock.lang.Specification

import java.time.Duration;

class SSHClientTest extends Specification {

    def setup() {
        def output = '''total 20
        drwxr-xr-x 3 1100 1100 4096 Aug  7 16:52 .
        drwxr-xr-x 8 1100 1100 4096 Aug  1 17:53 ..
        drwxr-xr-x 3 1100 1100 4096 Aug  7 16:49 examples\n\n
            '''
    }

    def cleanup() {
    }

    def toml_content = """
[lsbm]
host = "localhost"
port = 2223
username = "ramandeep"
authType = "password"
sshKey = "./id_rsa"
password = "PASSWORD"
sshKeyPassphrase = ""
            """
/*
    def "exec() execute command and returns shell response"() {
        setup:
            def file = new File("./lsbm.config.toml")
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

            def sshClient = new SSHClient(Configuration.fromTOML(
              "./lsbm.config.toml")) 

        when:
            def cmdResponse = sshClient.exec("ls -l", Duration.ofSeconds(10))
           
        then:
            println cmdResponse.getResponse()
            println cmdResponse.getError()
    }
*/
}
