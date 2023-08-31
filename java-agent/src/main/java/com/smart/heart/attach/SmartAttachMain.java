package com.smart.heart.attach;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * @author Arnold.zhao
 * @version SmartAttachMain.java, v 0.1 2022-05-01 22:00 Arnold.zhao Exp $$
 */
public class SmartAttachMain {

    public static void main(String[] args) {
        VirtualMachine vm = null;
        try {
            vm = VirtualMachine.attach(args[0]);
            System.out.println("SmartAttachMain:args>" + args[0]);
            vm.loadAgent("W:\\JAVA\\arnoldworkspace\\smart-heart\\java-agent\\target\\java-agent-1.0-SNAPSHOT.jar");
        } catch (AttachNotSupportedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AgentLoadException e) {
            e.printStackTrace();
        } catch (AgentInitializationException e) {
            e.printStackTrace();
        } finally {
            try {
                vm.detach();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
