package com.qualitest.tacoe.lsbm

import spock.lang.Specification

class ConfigurationTest extends Specification {

    def toml_content = """
[lsbm]
host = "HOST_IP"
port = 22
username = "USER_ID"
auth_type = "AUTH_TYPE"
ssh_key = "./SSH_KEY_FILEPATH"
password = "PASSWORD"
            """

    def "host() throws ConfigurationFileMissingException when config TOML does not exist"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            thrown ConfigurationFileMissingException

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "host() returns hostname from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            config.host() == "HOST_IP"

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "host() throws ConfiguratioNotFoundException when config key is not available in TOML file"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write ""

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
            config.host()

        then:
            thrown ConfigurationNotFoundException

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "port() returns port from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            config.port() == 22

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "port() returns default port '22' when TOML file does not have port key"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write 

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            config.port() == 22

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "username() returns username from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            config.username() == "USER_ID"

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "authType() returns auth type from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            config.authType() == "AUTH_TYPE"

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "sshKey() returns path to ssh key file from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content
            def testSSHKeyfile = new File("./SSH_KEY_FILEPATH");
            testSSHKeyfile.exists() ? testSSHKeyfile.delete() : false
            testSSHKeyfile.createNewFile()
            testSSHKeyfile.write ""

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            config.sshKey() == testSSHKeyfile.toPath()

        cleanup:
            file.exists() ? file.delete() : false
            testSSHKeyfile.exists() ? file.delete() : false
    }

    def "password() returns password from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration.fromTOML("./bluetooth-manager-config.toml")
        
        then:
            config.password() == "PASSWORD"

        cleanup:
            file.exists() ? file.delete() : false
    }
}
