/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sudhanshu
 */
import py4j.GatewayServer;

import javax.lang.model.element.Modifier;
import javax.swing.JFrame;
import java.io.IOException;


public class CodeGenerator {
    static CodeEditorFrame codeEditorFrame;
    static GatewayServer gatewayServer;

    public static void main(String[] args) {
        gatewayServer = new GatewayServer(new CodeGenerator());
        gatewayServer.start();
        System.out.println("JVM started");
    }

    public static void startCodeEditor() {
        codeEditorFrame = CodeEditorFrame.main(new String[] {});
    }

    public static void finishCodeEditor() {
        codeEditorFrame.dispose();
    }

    public void shutdownJVM() {
        System.out.println("Shutting down JVM");
        new Thread(new Runnable() {
            public void run() {
                gatewayServer.shutdown();
            }
        }).start();
    }

    public int processText(String s) throws Exception {
       return CommandRedirect.redirectCommand(s);
    }
    public int setClassName(String cName)throws Exception{
        Actions.className=cName;
        return Actions.defineClass();
    }
    public int setClassModifier(String modifierName)throws Exception{
        if(modifierName.equals("public")){
            Actions.modifier=Modifier.PUBLIC;
        }
        return Actions.defineClass();
    }
    public int printMsg(String s)throws Exception{
        codeEditorFrame.customOutputStream.clearEditor();
        return Actions.printMessage(s);
    }

}
