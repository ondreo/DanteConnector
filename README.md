# DanteConnector

DanteConnector is a Java-based project designed to facilitate the discovery and basic management of Dante devices and channels on a network through the mDNS protocol.

## Context

Dante is a closed standard. The official way to control devices — for example, connecting or disconnecting two devices or adjusting their parameters — is through Dante Controller. Unfortunately, there is no publicly available API for controlling these parameters.

As part of my Master's thesis, I aimed to create a method for adjusting basic settings without needing the Dante Controller software, enabling control of these devices without a standard laptop or PC — for example, using an Android tablet.

## Disclaimer

The software was primarily developed by observing Dante Controller (version 4.13.3.2) packets in Wireshark. It relies only partially on the official Dante standard documentation. While it works with my devices, it may not be compatible with other devices. Testing was conducted exclusively on Windows devices using Dante Virtual Soundcard (version 4.4.1.3) and Dante Via (version 1.3.2.1).

## Features

The DanteConnector application 
- **mDNS Service Discovery**: Discover Dante devices and transmitting channels on the network.
- **Connection of two Dante channels**: Establish a connection between two Dante audio channels.
- **Disconnection of two Dante channels**: Disconnect an existing connection between two Dante audio channels.
- **Changing sampling rate of a Dante device**: Modify the sampling rate of a Dante device.
- **Changing bit depth of a Dante device**: Adjust the bit depth of a Dante device.

## Requirements

- Java 21
- Maven

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/ondreo/DanteConnector.git
    cd DanteConnector
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Usage

### Dante discovery

1. Create an instance of `MdnsServiceDiscovery` and start the service discovery:
    ```java
    MdnsServiceDiscovery discovery = new MdnsServiceDiscovery();
   ```

2. Invoke the `createMdnsServiceDiscovery` method to start the service discovery.
   ```java
   discovery.createMdnsServiceDiscovery("your_ip_address");
   ```

### Controlling devices

1. Create an instance of `DanteConnectorService`
    ```java
    DanteConnectorService danteConnectorService = new DanteConnectorServiceImpl();
   ```

2. Invoke one of the methods below, according to your needs:
   - `sendUdpPacketToConnectTwoChannels`
   - `sendUdpPacketToDisconnectTwoChannels`
   - `changeDeviceSamplingRate`
   - `changeDeviceBitDepth`

Example usage of each functionality is implemented in the `DanteConnector` class.


## License

This program is provided 'as is,' without any express or implied warranties. The author makes no guarantees regarding compatibility with the original Dante software, nor assumes any responsibility for legal compliance or any consequences of its use. This program was created using reverse engineering methods, and any functional similarities may result from similar technical approaches. All trademarks and trade names referenced in this program are the property of their respective owners. Users assume all risks associated with using this program, and the author is not liable for any direct, indirect, incidental, special, or consequential damages arising from its use.
