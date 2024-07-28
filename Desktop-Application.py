import speech_recognition as sr
import pyttsx3
import webbrowser
import datetime
import wikipediaapi
import pywhatkit
import os 

def speech_to_text():
    # Initialize the recognizer
    recognizer = sr.Recognizer()

    # Use the default microphone as the audio source
    with sr.Microphone() as source:
        recognizer.adjust_for_ambient_noise(source)
        text_to_speech("iam listining....")
        audio = recognizer.listen(source)

        try:
            text = recognizer.recognize_google(audio)
            return text.lower()
        except sr.UnknownValueError:
            text_to_speech("Sorry, could not understand audio.")
            return 0
        except sr.RequestError as e:
            text_to_speech("Could not request results from Google Speech Recognition service; {0}".format(e))
            return 0


def text_to_speech(text):
    # Initialize the TTS engine
    engine = pyttsx3.init()

    # Set properties (optional)
    # You can set properties like the rate (speed) and volume
    engine.setProperty('rate', 150)  # Speed of speech
    engine.setProperty('volume', 1.0)  # Volume level (0.0 to 1.0)

    # Convert the text to speech
    engine.say(text)

    # Wait for the speech to finish
    engine.runAndWait()


def remove(command):
    element = ['what is','who is','open','.com','please','play','can','you','website','for me','search','for', ' ']
    for x in element:
        if x in command:
            command = command.replace(x, '')

    return command


def data_time(command):
    if 'time' in command:
        current_time = datetime.datetime.now().time()
        time = current_time.strftime("%#I %#M %p")
        text_to_speech("current time is"+time)

    if 'date' in command:
        current_date = datetime.date.today()
        date= current_date.strftime("%A, %#d  %B %Y")
        text_to_speech(date)


def get_wikipedia_summary(query, lang='english'):
    
    query = remove(query)
    wiki_wiki = wikipediaapi.Wikipedia(lang)
    page_py = wiki_wiki.page(query)

    if not page_py.exists():
        text_to_speech(f"Page '{query}' does not exist on Wikipedia.")
        pywhatkit.search(query)

    text = page_py.summary[:500]
    text_to_speech(text)

def check_sysApps(command):
    applications = ['notepad.exe', 'calc.exe', 'explorer.exe', 'control.exe', 'taskmgr.exe', 'cmd.exe', 
              'powershell.exe', 'regedit.exe', 'devmgmt.msc', 'services.msc', 'compmgmt.msc', 
              'diskmgmt.msc', 'mspaint.exe', 'wordpad.exe', 'msconfig.exe', 'notepad++.exe', 
              'chrome.exe', 'iexplore.exe', 'outlook.exe', 'winword.exe', 'excel.exe', 'powerpnt.exe',
                'code.exe',  'skype.exe' ,  'zoom.exe', 'teams.exe']

    app_names=['notepad', 'calculator', 'fileexplorer', 'controlpanel', 'taskmanager', 'commandpromptcmd',
            'powershell', 'registryeditor', 'devicemanager', 'services', 'computermanagement', 
            'diskmanagement', 'mspaint', 'wordpad', 'systemconfiguration', 'notepad++', 'chrome',
            'internetexplorer', 'microsoftoutlook(msoutkook)', 'word', 'excel', 
            'powerpoint', 'visualstudiocodevscode', 'skype', 'zoom', 'teams']
    command = remove(command)
    print(command)
    for x in range(len(app_names)):
        
        if command in app_names[x] or app_names[x] in command:
            if command =='':
                text_to_speech("please tell the application name to be open")
                return True
            print("validated",app_names[x],' ',applications[x])
            open_application(applications[x])
            return True
        
    return False


def open_application(application_path):
    try:
        # Using os.system to open the application
        text_to_speech("Opening Your Application")
        os.system('start '+application_path)

    except FileNotFoundError:
        text_to_speech(f"Error: Application not found at {application_path}")
    except Exception as e:
        text_to_speech(f"An unexpected error occurred: {e}")
        

            
def open_website(command):
    command = remove(command)
    url = 'www.'+command+'.com'
    url= url.replace(' ', '')
    print(url)
    webbrowser.open(url)

def play(command):
    try:
        pywhatkit.playonyt(command)
        text_to_speech("Playing..."+command)

    except:
        text_to_speech("Network Error Occurred try again...")


if __name__ == '__main__':
    a=0
    while True:
        if a==0:
            text_to_speech("hello boss welcome! back.." )
            a+=1
        command = speech_to_text()
        if command==0:  
            continue  

        elif 'exit' in command or 'close' in command:
            text_to_speech("Good bye, see you again")
            break

        elif 'date' in command or 'time' in command:
           data_time(command)

        elif 'who is' in command or 'what is' in command:
            get_wikipedia_summary(command)

        elif 'open' in command:
            if not (check_sysApps(command)):
                open_website(command)

        elif 'play' in command:
            command = remove(command)
            play(command)


        elif 'search' in command:
            command = remove(command)
            pywhatkit.search(command)
        
        else:
            text_to_speech(' sorry i dont understand you commands')


