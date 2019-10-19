import speech_recognition as sr
import os
import threading
import time
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
                aud=r2.listen(src)
                class_name=r.recognize_google(aud)
                code=gateway.entry_point.setClassName(class_name)
                if(code==102):
                    modifier=""
                    while modifier=="":
                        try:
                            print("please specify the modifier")
                            aud=r2.listen(src)
                            modifier=r2.recognize_google(aud)
                            code=gateway.entry_point.setClassModifier(modifier)
                        except sr.UnknownValueError:
                            print("Sorry, could not understand the modifier name")
                        except sr.RequestError as e:
                            print("Please Check Your Internet Connection, {0}".format(e))
            except sr.UnknownValueError:
                print("Sorry, could not understand the classname")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

def getPrintMessage():
    msg=""
    with sr.Microphone() as src:
        while msg=="":
            try:
                print("Please specify the message to be printed..")
                aud=r3.listen(src)
                msg=r3.recognize_google(aud)
                code=gateway.entry_point.printMsg(msg)
            except sr.UnknownValueError:
                print("Sorry, could not understand the message")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))

with sr.Microphone() as source:
    r.adjust_for_ambient_noise(source)
    text = ""
    try:
        while True :
            try:
                print("Listening...")
                audio = r.listen(source)
                text = r.recognize_google(audio)
                if text in terminating_words:
                    break
                code=gateway.entry_point.processText(text)
                print(code)
                if (code==101):
                    getClassName()
                if(code==103):
                    getPrintMessage()
            except sr.UnknownValueError:
                print("Sorry, could not understand the audio")
            except sr.RequestError as e:
                print("Please Check Your Internet Connection, {0}".format(e))
        gateway.entry_point.finishCodeEditor()        
        gateway.entry_point.shutdownJVM()

    except Exception as e:
        print(e)
    print("Exitting..")
