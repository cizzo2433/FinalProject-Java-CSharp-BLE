# FinalProject-Java-CSharp-BLE

Ok so there might be a few things needed to set this up on your own computer. #1 get a free API key for OpenAI from here https://platform.openai.com/account/api-keys.
In the ActivityWheel class that will go into the final field at the top. 

private static final OpenAiService SERVICE
            = new OpenAiService("API KEY HERE");
            
Then make sure you have Python 3 installed on your computer. Here's that link https://www.python.org/downloads/

Also go to File, Project Structure, Modules, and then click the + sign (the one further to the left) and click Python. This will add a Python interpreter facet
to the module since I wrote a small Python script for speech to text. If it doesn't give you the option to add Python, go to File, Settings, Plugins and install
the Python Community Edition Plugin. The last thing you may need to do is go to the directory where the python.exe is installed (will usually be a folder called Scripts)
and open the command prompt. From there, set the current directory as that Scripts folder (cd /pathtofolder/), and then run these two commands.

pip install PyAudio

pip install SpeechRecognition

Should confirm the installation and then (hopefully) the project will work. Definitley hit me up if you need help troubleshooting. 

