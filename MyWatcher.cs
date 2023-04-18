using System;
using System.Collections.Generic;
using System.Linq;
using Windows.Devices.Bluetooth.Advertisement;

namespace BluetoothLEReceiver
{
    public class MyWatcher
    {
        #region Private Members

        /// <summary>
        /// The Bluetooth watcher that will be used throughout the class
        /// </summary>
        private readonly BluetoothLEAdvertisementWatcher watcher;


        /// <summary>
        /// List of discovered devices with the address being used as keys 
        /// </summary>
        private readonly Dictionary<ulong, BluetoothLEDevice> discoveredDevices = new Dictionary<ulong, BluetoothLEDevice>();

        /// <summary>
        /// Thread lock object for class
        /// </summary>
        private readonly object threadLock = new object();

        #endregion

        #region Public Properties

        /// <summary>
        /// Indicates if this watcher is listening for advertisements 
        /// </summary>
        public bool Listening => watcher.Status == BluetoothLEAdvertisementWatcherStatus.Started;

        /// <summary>
        /// Public list of discovered devices 
        /// </summary>
        public IReadOnlyCollection<BluetoothLEDevice> Devices
        {

            // when Devices is accessed, remove all timed out devices from the collection 
            get
            {
               CleanupTimeouts();

                // For thread safety and so the collection doesn't continue to be added to 
                lock (threadLock)
                {
                    return discoveredDevices.Values.ToList().AsReadOnly();
                }
            }
        }

        /// <summary>
        /// The timeout in seconds that the device is removed from the <see cref="discoveredDevices"/>
        /// list if it is not re-advertised within this time
        /// </summary>
        public int TimeOut { get; set; } = 3;

        #endregion

        #region Public Events

        // Events which will fire based on various actions, to be used for filtering console output 
        
        public event Action StoppedListening = () => { };

        public event Action StartedListening = () => { };

        public event Action<BluetoothLEDevice> NewDeviceDiscovered = (device) => { };

        public event Action<BluetoothLEDevice> DeviceDiscovered = (device) => { };

        public event Action<BluetoothLEDevice> DeviceNameChanged = (device) => { };

        public event Action<BluetoothLEDevice> DeviceTimedOut = (device) => { };

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public MyWatcher()
        {
            watcher = new BluetoothLEAdvertisementWatcher
            {
                ScanningMode = BluetoothLEScanningMode.Active
            };
                
            watcher.Received += OnAdvertisementRecieved;
            watcher.Stopped += OnAdvertisementWatcherStopped;
        }

        #endregion

        #region Private Methods

        // Unused for now 
        private void OnAdvertisementWatcherStopped(BluetoothLEAdvertisementWatcher sender, BluetoothLEAdvertisementWatcherStoppedEventArgs args)
        {
            //throw new NotImplementedException();
        }

        /// <summary>
        /// Listens for BLE advertisements 
        /// </summary>
        /// <param name="sender">The BLE watcher</param>
        /// <param name="args">The arguments</param>
        private void OnAdvertisementRecieved(BluetoothLEAdvertisementWatcher sender, BluetoothLEAdvertisementReceivedEventArgs args)
        {
            // Remove devices from list if cleanup criteria met each time an advertisement is recieved 
            CleanupTimeouts();

            BluetoothLEDevice device = null;

            var newDiscovery = !discoveredDevices.ContainsKey(args.BluetoothAddress);

            // Name changed?
            var nameChanged =
                // If it already exists 
                !newDiscovery &&
                // And is not a blank name 
                !string.IsNullOrEmpty(args.Advertisement.LocalName) &&
                // And the name is different 
                discoveredDevices[args.BluetoothAddress].Name != args.Advertisement.LocalName;

            lock (threadLock)
            {

                // Name of the device 
                var name = args.Advertisement.LocalName;

                // If new name is blank and we already have a device, dont override 
                if (string.IsNullOrEmpty(name) && !newDiscovery)
                {
                    name = discoveredDevices[args.BluetoothAddress].Name;
                }

                device = new BluetoothLEDevice
                (
                    broadcastTime : args.Timestamp,
                    address : args.BluetoothAddress,
                    name : name,
                    signalStrength : args.RawSignalStrengthInDBm
                );

                // Add/update the device in list
                discoveredDevices[args.BluetoothAddress] = device;

            }

            DeviceDiscovered(device);

            if (nameChanged)
            {
                DeviceNameChanged(device);
            }

            if (newDiscovery)
            {
                NewDeviceDiscovered(device);
            }
        }

        /// <summary>
        /// Remove devices from list of discovered devices based on time since last advertisement
        /// </summary>
        private void CleanupTimeouts()
        {
            lock (threadLock)
            {
                // The date and time that if less than, means the device has "timed out" for the purposes of the program 
                var threshold = DateTime.UtcNow - TimeSpan.FromSeconds(TimeOut);

                // Any devices that have not sent a new broadcast within threshold will be removed from the list of discovered devices 
                discoveredDevices.Where(f => f.Value.BroadcastTime < threshold).ToList().ForEach(device =>
                {
                    discoveredDevices.Remove(device.Key);
                    DeviceTimedOut(device.Value);
                });
            }
        }

        #endregion

        #region Public Methods

        /// <summary>
        /// Starts checking for BLE advertisements 
        /// </summary>
        public void Run()
        {
            lock (threadLock)
            {

                // Exit method if already checking for advertisements
                if (Listening)
                {
                    return;
                }

                watcher.Start();
            }
            StartedListening();
        }

        /// <summary>
        /// Stop checking for BLE advertisements
        /// </summary>
        public void StopListening()
        { 
            lock (threadLock)
            {

                // Exit method if already stopped 
                if (!Listening)
                {
                    return;
                }

                watcher.Stop();

                // Clear previous list of devices once stopped 
                discoveredDevices.Clear();
            }
            StoppedListening();
        }

        #endregion
    }
}
