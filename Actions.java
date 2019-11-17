import java.io.File;
import java.io.FileOutputStream;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.*;
import java.io.*;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;

public class Actions {
    //related to class
    static String className;
    static Modifier modifier;
    static TypeSpec.Builder typeSpecBuilder;

    //related to method
    static String methodName;
    static TypeName methodReturnType;
    static TypeName methodParaType;
    static String methodParameterName;
    static Boolean wantMoreParameters;
    static MethodSpec.Builder methodSpecBuilder;
    static boolean definingParameters;
    static HashMap<String, ArrayList<ParameterSpec.Builder>>parameterList=new HashMap<>();

    //current context
    static MethodSpec.Builder currentMBuilder;

    //to print msg
    static String printMsg;

    static HashMap<String,MethodSpec.Builder> methodList=new HashMap<>();
    static HashMap<String,FieldSpec.Builder> fieldList=new HashMap<>();

    public static int defineClass() throws Exception {
        if (className == null) {
            return ReturnCodes.REQUEST_CLASS_NAME;
        }
        if (modifier == null) {
            return ReturnCodes.REQUEST_CLASS_MODIFIER;
        }
        System.out.println("Defining class");
        refreshTypeSpec();
        return ReturnCodes.TASK_SUCCESSFUL;
    }

    public static int printMessage() throws Exception {
        if(printMsg==null){
            return ReturnCodes.REQUEST_MSG_TO_PRINT;
        }
        currentMBuilder.addStatement("System.out.println(\" " + printMsg + "\")");
        refreshTypeSpec();
        printMsg=null;
        return ReturnCodes.TASK_SUCCESSFUL;
    }

    public static int changeContext(){
       return ReturnCodes.REQUEST_CONTEXT_NAME;
    }

    public static int saveFile() throws Exception {
        String code = CodeGenerator.codeEditorFrame.getCode();
        File file = new File(className+".java");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = code.getBytes(Charset.defaultCharset());
        fos.write(bytes);
        fos.flush();
        fos.close();
        System.out.println(" File Saved!");
        return ReturnCodes.TASK_SUCCESSFUL;
    }



    public static int addMethodToClass() throws Exception{
        
        if(methodName==null){
            return ReturnCodes.REQUEST_METHOD_NAME;
        }
        if(methodReturnType==null){
            return ReturnCodes.REQUEST_METHOD_RETURN_TYPE;
        }
        if(wantMoreParameters==null){
            return ReturnCodes.REQUEST_WANT_MORE_PARAMETERS;
        }
        methodSpecBuilder=MethodSpec.methodBuilder(methodName);
        methodSpecBuilder.returns(methodReturnType);
        
        while(wantMoreParameters){
            int ret=addParameterToMethodSpec();
            if(ret==ReturnCodes.TASK_SUCCESSFUL){
                return ReturnCodes.REQUEST_WANT_MORE_PARAMETERS;
            }else{
                return ret;
            }
        }
        ArrayList<ParameterSpec.Builder>paraList=parameterList.get(methodName);
        if(paraList!=null){
            for(ParameterSpec.Builder pBuilder:paraList){
                methodSpecBuilder.addParameter(pBuilder.build());
            }
        }
        methodList.put(methodName, methodSpecBuilder);
        refreshTypeSpec();
        methodSpecBuilder=null;
        methodName=null;
        methodReturnType=null;
        wantMoreParameters=null;
        return ReturnCodes.TASK_SUCCESSFUL;
    }


    public static int addParameterToMethodSpec()throws Exception{
        if(methodParaType==null){
            return ReturnCodes.REQUEST_METHOD_PARAMETER_TYPE;
        }
        if(methodParameterName==null){
            return ReturnCodes.REQUEST_METHOD_PARAMETER_NAME;
        }
        ParameterSpec.Builder parameterSpecBuilder=ParameterSpec.builder(methodParaType,methodParameterName);
        ArrayList<ParameterSpec.Builder> temp=parameterList.get(methodName);
        if(temp==null){
            temp=new ArrayList<ParameterSpec.Builder>();
        }
        temp.add(parameterSpecBuilder);
        parameterList.put(methodName, temp);
        
        if(temp!=null){
            for(ParameterSpec.Builder pBuilder:temp){
                methodSpecBuilder.addParameter(pBuilder.build());
            }
        }
        methodList.put(methodName, methodSpecBuilder);
        refreshTypeSpec();
        methodParaType=null;
        methodParameterName=null;
        return ReturnCodes.TASK_SUCCESSFUL;
    }

    public static void refreshTypeSpec() throws Exception{
        CodeEditorFrame.customOutputStream.clearEditor();

        typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addModifiers(modifier);

        for(MethodSpec.Builder m:methodList.values()){
            typeSpecBuilder.addMethod(m.build());
        }
        for(FieldSpec.Builder f:fieldList.values()){
            typeSpecBuilder.addField(f.build());
        }
        JavaFile.Builder javaFileBuilder = JavaFile.builder("", typeSpecBuilder.build());
        JavaFile javaFile = javaFileBuilder.build();
        javaFile.writeTo(CodeEditorFrame.printStream);
    }

    public static int addMainMethod()throws Exception{
        ArrayTypeName stringArray = ArrayTypeName.of(String.class);
        ParameterSpec.Builder pBuilder = ParameterSpec.builder(stringArray, "args");

        MethodSpec.Builder mBuilder = MethodSpec.methodBuilder("main");
        mBuilder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        mBuilder.addParameter(pBuilder.build());
        mBuilder.returns(TypeName.VOID);
        methodList.put("main", mBuilder);
        currentMBuilder=mBuilder;
        refreshTypeSpec();
        return ReturnCodes.TASK_SUCCESSFUL;
    }

    public static int executeFile() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Compiling...");
        Process p1 = runtime.exec("javac "+className+".java");
        BufferedReader br = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
        System.out.println("Here is the compilation logs of your voice generated program:\n");
        String s = null;
        while ((s = br.readLine()) != null) {
            System.out.println(s);
        }
        System.out.println("Running...");
        Process p2=runtime.exec("java "+className);
        br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        System.out.println("Here is the standard output of the your voice generated program:\n");
        s = null;
        while ((s = br.readLine()) != null) {
            System.out.println(s);
        }
        return ReturnCodes.TASK_SUCCESSFUL;
    }

}