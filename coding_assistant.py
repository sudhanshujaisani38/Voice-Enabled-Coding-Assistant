import speech_recognition as sr
import os
import threading
import time
from return_codes import *
from py4j.java_gateway import JavaGateway


def start_jvm():
    os.system('javac -cp javapoet-1.11.1.jar:py4j0.10.8.1.jar: CodeGenerator.java')
    os.system('java -cp javapoet-1.11.1.jar:py4j0.10.8.1.jar: CodeGenerator')

terminating_words=["dan","dhan","done","done done","done done done","ton","tonne","donne","Dan","Dhan","Done","Done Done","Done Done Done","Ton","Tonne","Donne","OK","Okay","okay","dun dun"]
t1 = threading.Thread(target=start_jvm, args=())
t1.start() 
time.sleep(2)
gateway = JavaGateway()
r = sr.Recognizer()
r2=sr.Recognizer()
r3=sr.Recognizer()
gateway.entry_point.startCodeEditor()


def getClassName():
    class_name=""
    with sr.Microphone() as src:
        while class_name=="":
            try:
                print("Please specify the name of the class..")
                # aud=r2.listen(src)
                # class_name=r.recognize_google(aud)
                class_name=input()
                return gateway.entry_point.setClassName(class_name)
            except sr.UnknownValueError:
                print("Sorry, could not understand the classname")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getClassModifier():
    modifier=""
    with sr.Microphone() as src:
        while modifier=="":
            try:
                print("please specify the modifier")
                # aud=r2.listen(src)
                # modifier=r2.recognize_google(aud)
                modifier=input()
                return gateway.entry_point.setClassModifier(modifier)
            except sr.UnknownValueError:
                print("Sorry, could not understand the modifier name")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getPrintMessage():
    msg=""
    with sr.Microphone() as src:
        while msg=="":
            try:
                print("Please specify the message to be printed..")
                # aud=r3.listen(src)
                # msg=r3.recognize_google(aud)
                msg=input()
                return gateway.entry_point.printMsg(msg)
            except sr.UnknownValueError:
                print("Sorry, could not understand the message")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getMethodName():
    methodName=""
    with sr.Microphone() as src:
        while methodName=="":
            try:
                print("Please specify the method name..")
                # aud=r3.listen(src)
                # methodName=r3.recognize_google(aud)
                methodName=input()
                return gateway.entry_point.setMethodName(methodName)
            except sr.UnknownValueError:
                print("Sorry, could not understand the methodName")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))                

def getMethodReturnType():
    returnType=""
    with sr.Microphone() as src:
        while returnType=="":
            try:
                print("Please specify the return type..")
                # aud=r3.listen(src)
                # methodName=r3.recognize_google(aud)
                returnType=input()
                return gateway.entry_point.setMethodReturnType(returnType)
            except sr.UnknownValueError:
                print("Sorry, could not understand the return type")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getWantMoreParameters():
    ans=""
    with sr.Microphone() as src:
        while ans=="":
            try:
                print("Do you want to add more parameters?")
                # aud=r3.listen(src)
                # methodName=r3.recognize_google(aud)
                ans=input()
                return gateway.entry_point.setWantMoreParameters(ans)
            except sr.UnknownValueError:
                print("Sorry, could not understand the response")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getMethodParameterType():
    parameterType=""
    with sr.Microphone() as src:
        while parameterType=="":
            try:
                print("Please specify the parameter type..")
                # aud=r3.listen(src)
                # methodName=r3.recognize_google(aud)
                parameterType=input()
                return gateway.entry_point.setMethodParameterType(parameterType)
            except sr.UnknownValueError:
                print("Sorry, could not understand the return type")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getMethodParameterName():
    parameterName=""
    with sr.Microphone() as src:
        while parameterName=="":
            try:
                print("Please specify the parameter name..")
                # aud=r3.listen(src)
                # methodName=r3.recognize_google(aud)
                parameterName=input()
                return gateway.entry_point.setMethodParameterName(parameterName)
            except sr.UnknownValueError:
                print("Sorry, could not understand the parameter name")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getContextName():
    contextName=""
    with sr.Microphone() as src:
        while contextName=="":
            try:
                print("Please specify the context..")
                # aud=r3.listen(src)
                # methodName=r3.recognize_google(aud)
                contextName=input()
                return gateway.entry_point.changeContext(contextName)
            except sr.UnknownValueError:
                print("Sorry, could not understand the parameter name")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

#--------------------------------------MAIN-----------------------------------------------------                
with sr.Microphone() as source:
    r.adjust_for_ambient_noise(source)
    text = ""
    try:
        while True :
            try:
                print("Listening...")
                #audio = r.listen(source)
                #text = r.recognize_google(audio)
                text=input()
                if text in terminating_words:
                    break
                code=gateway.entry_point.processText(text)
                while(code!=0):
                    print(code)
                    if(code==REQUEST_CLASS_NAME):
                        code=getClassName()
                    if(code==REQUEST_CLASS_MODIFIER):
                        code=getClassModifier()
                    if(code==REQUEST_MSG_TO_PRINT):
                        code=getPrintMessage()
                    if(code==REQUEST_METHOD_NAME):
                        code=getMethodName()
                    if(code==REQUEST_METHOD_RETURN_TYPE):
                        code=getMethodReturnType()
                    if(code==REQUEST_WANT_MORE_PARAMETERS):
                        code=getWantMoreParameters()
                    if(code==REQUEST_METHOD_PARAMETER_NAME):
                        code=getMethodParameterName()
                    if(code==REQUEST_METHOD_PARAMETER_TYPE):
                        code=getMethodParameterType()
                    if(code==REQUEST_CONTEXT_NAME):
                        code=getContextName()
            except sr.UnknownValueError:
                print("Sorry, could not understand the audio")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))
        gateway.entry_point.finishCodeEditor()        
        gateway.entry_point.shutdownJVM()

    except Exception as e:
        print(e)
    print("Exitting..")
