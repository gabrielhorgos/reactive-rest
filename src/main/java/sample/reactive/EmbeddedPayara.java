package sample.reactive;

import fish.payara.micro.BootstrapException;
import fish.payara.micro.PayaraMicro;

import java.io.File;

public class EmbeddedPayara {

    public static void main(String[] args) {
        File warFile = new File("target", "registration-1.0-SNAPSHOT.war");

        // "--contextroot=registration"
        PayaraMicro instance = PayaraMicro.getInstance();
        instance.setHttpPort(8080);
        instance.addDeploymentFile(warFile);

        try {
            instance.bootStrap();
        } catch (BootstrapException e) {
            e.printStackTrace();
        }
    }
}

