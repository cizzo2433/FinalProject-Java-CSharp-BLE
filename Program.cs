using System;
using Windows.Devices.Enumeration;

namespace BluetoothLEReceiver 
{
    class Program
    {
        public static void Main(string[] args)
        {
            var watcher = new MyWatcher();

            watcher.StartedListening += () =>
            {
                Console.ForegroundColor = ConsoleColor.DarkYellow;
                Console.WriteLine("Started listening");
            };

            watcher.StoppedListening += () =>
            {
                Console.ForegroundColor = ConsoleColor.Gray;
                Console.WriteLine("Stopped listening");
            };

            watcher.NewDeviceDiscovered += (device) =>
            {
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine($"New device: {device}");
            };

            watcher.DeviceNameChanged += (device) =>
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine($"Device name changed: {device}");
            };

            watcher.DeviceTimedOut += (device) =>
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"Device timeout: {device}");
            };

            watcher.Run();

            while (true)
            {
                // Pause when enter is pressed and print all devices 
                Console.ReadLine();

                var devices = watcher.Devices;

                Console.ForegroundColor = ConsoleColor.White;
                Console.WriteLine($"{devices.Count} devices.....");

                foreach ( var device in devices )
                {
                    Console.WriteLine(device);
                }

                watcher.StopListening();

                if (!watcher.Listening)
                {
                    Console.ReadLine();
                    watcher.Run();
                }
            }
        }

        /// <summary>
        /// Copy of the main method for use in exporting as a class library function that can be called from Java
        /// </summary>
        public static void StartProgram()
        {

            // The watcher holds the functionality for picking up BLE advertisements and their payload data 
            var watcher = new MyWatcher();

            watcher.StartedListening += () =>
            {
                Console.ForegroundColor = ConsoleColor.DarkYellow;
                Console.WriteLine("Started listening");
            };

            watcher.StoppedListening += () =>
            {
                Console.ForegroundColor = ConsoleColor.Gray;
                Console.WriteLine("Stopped listening");
            };

            watcher.NewDeviceDiscovered += (device) =>
            {
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine($"New device: {device}");
            };

            watcher.DeviceNameChanged += (device) =>
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine(device);
            };

            watcher.DeviceTimedOut += (device) =>
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"Device timeout: {device}");
            };

            watcher.Run();

            while (true)
            {
                // Pause when enter is pressed and print all devices 
                Console.ReadLine();

                var devices = watcher.Devices;

                Console.ForegroundColor = ConsoleColor.White;
                Console.WriteLine($"{devices.Count} devices.....");

                foreach (var device in devices)
                {
                    Console.WriteLine(device);
                }

                watcher.StopListening();

                if (!watcher.Listening)
                {

                    // Start checking again when enter is pressed
                    Console.ReadLine();
                    watcher.Run();
                }
            }
        }

        /// <summary>
        /// Checks specifically for the BlueCharm bluetooth signal.
        /// </summary>
        /// <returns>True if the signal is detected, false otherwise</returns>
        public static bool CheckForBlueCharm()
        {
            var watcher = new MyWatcher();
            bool signal = false;


            watcher.Run();
          

            while (!signal)
            {
               
                var devices = watcher.Devices;

                foreach (var device in devices)
                {
                    if (device.Name == "MyBlueCharm")
                    {
                        signal = true;
                        Console.WriteLine("Signal Found");
                    }
                }
            }
            return signal;
        }
    }
}
