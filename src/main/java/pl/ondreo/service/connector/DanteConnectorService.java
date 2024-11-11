package pl.ondreo.service.connector;

public interface DanteConnectorService {

    /**
     * Method that sends a request to connect two audio channels based on the target IP address of the receiving device,
     * the name of the transmitting device, the name of the transmitting channel, and the number of the receiving channel.
     *
     * @param destinationIpAddress target IP address of the receiving device
     * @param asciiTransmitterDeviceName name of the transmitting device
     * @param asciiTransmitterChannelName name of the transmitting channel
     * @param receiverChannelNumber number of the receiving channel
     */
    void sendUdpPacketToConnectTwoChannels(String destinationIpAddress,
                                           String asciiTransmitterDeviceName,
                                           String asciiTransmitterChannelName,
                                           int receiverChannelNumber);

    /**
     * Method that sends a request to disconnect two audio channels based on the target IP address of the receiving device and the number of the receiving channel.
     *
     * @param destinationIpAddress target IP address of the receiving device
     * @param receiverChannelNumber number of the receiving channel
     */
    void sendUdpPacketToDisconnectTwoChannels(String destinationIpAddress,
                                              int receiverChannelNumber);

    /**
     * Method that sends a request to change the sampling rate of a device based on the target IP address of the receiving device
     * and the new sampling rate of the device.
     *
     * @param destinationIpAddress target IP address of the receiving device
     * @param samplingRate new sampling rate of the device
     */
    void changeDeviceSamplingRate(String destinationIpAddress,
                                  int samplingRate);

    /**
     * Method that sends a request to change the bit depth of the device based on the target IP address of the receiving device
     * and the new bit depth of the device.
     *
     * @param destinationIpAddress target IP address of the receiving device
     * @param bitDepth new bit depth of the device
     */
    void changeDeviceBitDepth(String destinationIpAddress,
                              int bitDepth);
}
