import speech_recognition as sr


def main():
    r = sr.Recognizer()

    with sr.Microphone() as source:
        r.adjust_for_ambient_noise(source)

        audio = r.listen(source)

        try:
            print(r.recognize_google(audio))

        except Exception as e:

            print("Timed out")


if __name__ == "__main__":
    main()
