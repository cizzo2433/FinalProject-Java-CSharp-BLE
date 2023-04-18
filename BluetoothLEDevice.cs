using System;

namespace BluetoothLEReceiver
{
    /// <summary>
    /// Contains information about a BLE device discovered by a BLE advertisement watcher
    /// </summary>
    public class BluetoothLEDevice
    {
        /// <summary>
        /// The time of the broadcast advertisement 
        /// </summary>
        public DateTimeOffset BroadcastTime { get; }

        /// <summary>
        /// The address of the device
        /// </summary>
        public ulong Address { get; }

        /// <summary>
        /// The name of the device 
        /// </summary>
        public string Name { get; }

        /// <summary>
        /// The signal strength in dB
        /// </summary>
        public short SignalStrength { get; }

        /// <summary>
        /// Default constructor 
        /// </summary>
        /// <param name="broadcastTime"></param>
        /// <param name="address"></param>
        /// <param name="name"></param>
        /// <param name="signalStrength"></param>
        public BluetoothLEDevice(DateTimeOffset broadcastTime, ulong address, string name, short signalStrength)
        {
            BroadcastTime = broadcastTime;
            Address = address;
            Name = name;
            SignalStrength = signalStrength;
        }

        /// <summary>
        /// Formatted ToString
        /// </summary>
        /// <returns>A string with relevant values</returns>
        public override string ToString()
        {
            return $"{ (string.IsNullOrEmpty(Name) ? "{No Name}" : Name) } {Address} ({SignalStrength})";
        }
    }
}
