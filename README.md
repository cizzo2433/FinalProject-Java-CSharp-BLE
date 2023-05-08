# FinalProject-Java-CSharp-BLE

Make sure you have Python 3 installed, you may need to install PyAudio and SpeechRecognition via the command prompt by navigating to the Scripts directory 
in your Python folder and running:

pip install PyAudio 

pip install SpeechRecognition

Also make sure that your Python directory is added to the Path in your system enviornmental variables. This can be accessed on Windows by going to 
Advanced System Settings, Enviornmental Variables, clicking on the Path variable under System Variables, and adding the path to yoru Python folder.
My path looks like C:\Users\izzoc\AppData\Local\Programs\Python\Python311, yours is likely in a similar location. 

Also, in IntelliJ press Ctrl twice to bring up the run anything window and copy in this command, replacing the quotation marks and everything contained within them
with the paths to the Javonet.jar in your project and to your local Maven repository. The will install the JAR as a dependencey 
since it isn't located on the Maven Central remote repo.

mvn install:install-file -Dfile="full path to jar file in project" -DgroupId=com.javonet -DartifactId=javonet 
-Dversion=1.5.155 -Dpackaging=jar -DlocalRepositoryPath="path to maven repo on your computer"

Finally, in the resources folder of the project, create a file called application.properties and write one line with the format:

openAIKey=""

Replacing the quoatation marks with your free API key from OpenAI, which can be obtained at https://platform.openai.com/account/api-keys
This will prevent the API key from being deactived because the project is shared on GitHub. 
