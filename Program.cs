using System;
using System.Device.Location;
using Windows.Devices.Enumeration;
using Windows.Devices.Geolocation;

namespace BluetoothLEReceiver 
{
    class Program
    {
       

        /// <summary>
        /// Method for testing, will show the status of finding current location, or if location services are disables 
        /// </summary>
        public static void ShowStatusUpdates()
        {
            GeoCoordinateWatcher watcher = new GeoCoordinateWatcher();
            watcher.Start();

            watcher.StatusChanged += new EventHandler<GeoPositionStatusChangedEventArgs>(watcher_StatusChanged);

            Console.WriteLine("Enter any key to quit.");
            Console.ReadLine();
        }

        /// <summary>
        /// Prints to console based on current GeoPositionStatus enum field
        /// </summary>
        /// <param name="sender"></param> the watcher sending the event 
        /// <param name="e"></param> an event representing a change in status of the watcher 
        public static void watcher_StatusChanged(object sender, GeoPositionStatusChangedEventArgs e)
        {
            switch (e.Status)
            {
                case GeoPositionStatus.Initializing:
                    Console.WriteLine("Searching for location");
                    break;

                case GeoPositionStatus.Ready:
                    Console.WriteLine("Have location");
                    break;

                case GeoPositionStatus.NoData:
                    Console.WriteLine("No data");
                    break;

                case GeoPositionStatus.Disabled:
                    Console.WriteLine("Disabled");
                    break;

            }
        }

        /// <summary>
        /// Accesses system location services to determine current latitude and longitude coordinates
        /// </summary>
        /// <returns></returns> a double array with latitude and longitude
        public static double[] GetLocationProperty()
        {
            GeoCoordinateWatcher watcher = new GeoCoordinateWatcher();

            // Do not suppress prompt, and wait 1000 milliseconds to start.
            watcher.TryStart(false, TimeSpan.FromMilliseconds(1000));

            GeoCoordinate coord = watcher.Position.Location;
            Console.WriteLine("Searching");

            while (coord.IsUnknown)
            {
                coord = watcher.Position.Location;
            }
            Console.WriteLine("Location Found");

            double[] location = {coord.Latitude, coord.Longitude};
            return location;

        }
    

        /// <summary>
        /// Main watcher method for use in exporting as a class library function that can be called from Java
        /// </summary>
        public static void StartWatcher()
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
            watcher.StopListening();
            return signal;
        }
    }
}
