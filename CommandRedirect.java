import java.util.*;

public class CommandRedirect{
    static List cmdDefineClass=Arrays.asList("define a class","define class","define necklace","define applause","define oculus","define across","define iglas","define a glass");
    static List cmdSave=Arrays.asList("save","save save","shape","Save","safe","Safe","sev","Sev","sabe","seb","zev","saif");
    static List cmdRun=Arrays.asList("run","Run","ranch","kiran","ran","rang");
    static List cmdPrint=Arrays.asList("print message","print message print message","print message print message print message","print","print print","print statement","print statement print statement","print statement print statement print statement","print log");

    public static int redirectCommand(String s)throws Exception{
        if (cmdDefineClass.contains(s)) {
            return Actions.defineClass();
        } else if(cmdSave.contains(s)){
            return Actions.saveFile();
        }else if(cmdRun.contains(s)){
            return Actions.executeFile();
        }else if(cmdPrint.contains(s)){
            return 103;
        }else {
            System.out.println("\"" + s + "\" is not a valid command");
            return 0;
        }
    }
}