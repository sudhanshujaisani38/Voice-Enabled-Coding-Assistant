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
import javax.lang.model.element.Modifier;

public class Actions {
    static String className;
    static Modifier modifier;
    static TypeSpec typeSpec;

    public static int defineClass() throws IOException {
        if (className == null) {
            return 101;
        }
        if (modifier == null) {
            return 102;
        }
        TypeSpec.Builder tBuilder = TypeSpec.classBuilder(className);
        tBuilder.addModifiers(modifier);

        ArrayTypeName stringArray = ArrayTypeName.of(String.class);
        ParameterSpec.Builder pBuilder = ParameterSpec.builder(stringArray, "args");

        MethodSpec.Builder mBuilder = MethodSpec.methodBuilder("main");
        mBuilder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        mBuilder.addParameter(pBuilder.build());
        mBuilder.returns(TypeName.VOID);

        tBuilder.addMethod(mBuilder.build());
        typeSpec = tBuilder.build();
        JavaFile.Builder javaFileBuilder = JavaFile.builder("", typeSpec);
        JavaFile javaFile = javaFileBuilder.build();
        System.out.println("Defining class");
        javaFile.writeTo(CodeEditorFrame.printStream);
        return 0;
    }

    public static int printMessage(String msg) throws Exception {
        TypeSpec.Builder tBuilder = TypeSpec.classBuilder(className);
        tBuilder.addModifiers(modifier);

        ArrayTypeName stringArray = ArrayTypeName.of(String.class);
        ParameterSpec.Builder pBuilder = ParameterSpec.builder(stringArray, "args");

        MethodSpec.Builder mBuilder = MethodSpec.methodBuilder("main");
        mBuilder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        mBuilder.addParameter(pBuilder.build());
        mBuilder.addStatement("System.out.println(\" " + msg + "\")");
        mBuilder.returns(TypeName.VOID);

        tBuilder.addMethod(mBuilder.build());
        typeSpec = tBuilder.build();
        JavaFile.Builder javaFileBuilder = JavaFile.builder("", typeSpec);
        JavaFile javaFile = javaFileBuilder.build();
        System.out.println("Defining class");
        javaFile.writeTo(CodeEditorFrame.printStream);
        return 0;
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
        return 0;
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
        return 0;
    }

}