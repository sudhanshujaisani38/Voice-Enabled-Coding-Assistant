import java.util.*;

public class CommandRedirect{
    static List cmdDefineClass=Arrays.asList("define a class","define class","define necklace","define applause","define oculus","define across","define iglas","define a glass","define akash","define atlas");
    static List cmdSave=Arrays.asList("save","save save","shape","Save","safe","Safe","sev","Sev","sabe","seb","zev","saif");
    static List cmdRun=Arrays.asList("run","Run","ranch","kiran","ran","rang");
    static List cmdPrint=Arrays.asList("print message","print message print message","print message print message print message","print","print print","print statement","print statement print statement","print statement print statement print statement","print log");
    static List cmdAddMethod=Arrays.asList("add a method","Add a method","add method","add function","atom method");
    static List cmdAddMain=Arrays.asList("add main method","add main","Add Main","Add main","ad mean","admin");
    static List cmdChangeContext=Arrays.asList("change context","context change","change the context","Change the context","Change context");

    public static int redirectCommand(String s)throws Exception{
        if (cmdDefineClass.contains(s)) {
            return Actions.defineClass();
        } 
        else if(cmdSave.contains(s)){
            return Actions.saveFile();
        }
        else if(cmdRun.contains(s)){
            return Actions.executeFile();
        }
        else if(cmdPrint.contains(s)){
            return Actions.printMessage();
        }
        else if(cmdAddMethod.contains(s)){
            return Actions.addMethodToClass();
        }
        else if(cmdAddMain.contains(s)){
            return Actions.addMainMethod();
        }
        else if(cmdChangeContext.contains(s)){
            return Actions.changeContext();
        }
        else {
            System.out.println("\"" + s + "\" is not a valid command");
            return 0;
        }
    }
}