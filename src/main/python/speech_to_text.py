import speech_recognition as sr


# Simple script for live speech to text conversion using device microphone
def main():
    r = sr.Recognizer()

    with sr.Microphone() as source:
        r.adjust_for_ambient_noise(source)

        audio = r.listen(source)

        try:
            # This will be read as InputStream on the Java side
            print(r.recognize_google(audio))

        except Exception:

            print("Timed out")


if __name__ == "__main__":
    main()
