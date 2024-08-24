package com.qualitest.tacoe.lsbm

import spock.lang.Specification
import java.nio.file.Paths

class ConfigurationTest extends Specification {

    def toml_content = """
[lsbm]
  host = "HOST_IP"
  port = 22
  username = "USER_ID"
  authType = "AUTH_TYPE"
  sshKey = "./SSH_KEY_FILEPATH"
  password = "PASSWORD"
            """

    def "lsbm().host() throws ConfigurationFileMissingException when config TOML does not exist"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false

        when:
            def lsbmConfig = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
        
        then:
            thrown ConfigurationFileMissingException

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "lsbm().host() returns hostname from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
       
        then:
            config.lsbm().host() == "HOST_IP"

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "lsbm().port() returns port from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
        
        then:
            config.lsbm().port() == 22

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "lsbm().username() returns username from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
        
        then:
            config.lsbm().username() == "USER_ID"

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "lsbm().authType() returns auth type from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
        
        then:
            config.lsbm().authType() == "AUTH_TYPE"

        cleanup:
            file.exists() ? file.delete() : false
    }

    def "lsbm().sshKey() returns path to ssh key file from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content
            def sshKeyFile = new File("./SSH_KEY_FILEPATH")
            sshKeyFile.exists() ? sshKeyFile.delete() : false
            sshKeyFile.createNewFile()
            sshKeyFile.write ""

        when:
            def config = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
        
        then:
            config.lsbm().sshKey() == Paths.get('./SSH_KEY_FILEPATH')

        cleanup:
            file.exists() ? file.delete() : false
            sshKeyFile.exists() ? sshKeyFile.delete() : false
    }

    def "lsbm().sshKey() throws SSHKeyFileMissingException when sshKey file does not exist"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content
            def sshKeyFile = new File("./SSH_KEY_FILEPATH")
            sshKeyFile.exists() ? sshKeyFile.delete() : false

        when:
            def config = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
            config.lsbm().sshKey()
        
        then:
            thrown SSHKeyFileMissingException

        cleanup:
            file.exists() ? file.delete() : false
            sshKeyFile.exists() ? sshKeyFile.delete() : false
    }

    def "lsbm().password() returns password from TOML when config TOML file exists"() {
        setup:
            def file = new File("./bluetooth-manager-config.toml");
            file.exists() ? file.delete() : false
            file.createNewFile()
            file.write toml_content

        when:
            def config = Configuration
                .fromTOML("./bluetooth-manager-config.toml")
                .to(LSBMConfiguration.class);
        
        then:
            config.lsbm().password() == "PASSWORD"

        cleanup:
            file.exists() ? file.delete() : false
    }
}
