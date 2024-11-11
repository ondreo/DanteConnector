package pl.ondreo.service.connector;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import pl.ondreo.util.ConverterUtil;

@Slf4j
public class DanteConnectorServiceImpl implements DanteConnectorService {

    int packetId = 0x0000;

    @Override
    public void sendUdpPacketToConnectTwoChannels(String destinationIpAddress,
                                                  String asciiTransmitterDeviceName,
                                                  String asciiTransmitterChannelName,
                                                  int receiverChannelNumber) {

        String transmitterDeviceName = ConverterUtil.convertAsciiStringtoHexString(asciiTransmitterDeviceName);
        String transmitterChannelName = ConverterUtil.convertAsciiStringtoHexString(asciiTransmitterChannelName);

        int destinationPortNumber = 4440;

        // length of the UDP packet body expressed in bytes
        int dantePacketLength =  (76 + transmitterChannelName.length() + transmitterDeviceName.length()) / 2;

        // 3410 - command to connect or disconnect two audio channels
        int danteCommand = 0x3410;

        String hexStringTemplate = "280900{dantePacketLength}{packetId}{danteCommand}00000000000000000800020100{receiverChannelNumber}0003002400270000000000000000{transmitterChannelName}00{transmitterDeviceName}00";

        // Creating a hexadecimal string template with named placeholders
        String hexString = getHexUdpMessageStringForConnectionAndDisconnection(transmitterDeviceName, transmitterChannelName, receiverChannelNumber,
                                                                               dantePacketLength, danteCommand,
                                                                               hexStringTemplate);

        // increment packet id with each request
        incrementPacketId();

        logConnectionParameters(asciiTransmitterDeviceName, asciiTransmitterChannelName, receiverChannelNumber);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    @Override
    public void sendUdpPacketToDisconnectTwoChannels(String destinationIpAddress, int receiverChannelNumber) {

        int destinationPortNumber = 4440;

       // length of the UDP packet body expressed in bytes
        int dantePacketLength =  72 / 2;

        // 3410 - command to connect or disconnect two audio channels
        int danteCommand = 0x3410;

        String hexStringTemplate = "280900{dantePacketLength}{packetId}{danteCommand}00000000000000000800020100{receiverChannelNumber}0003000000000000000000000000";

        // Creating a hexadecimal string template with named placeholders
        String hexString = getHexUdpMessageStringForConnectionAndDisconnection(null, null, receiverChannelNumber,
                                                                               dantePacketLength, danteCommand,
                                                                               hexStringTemplate);

        // increment packet id with each request
        incrementPacketId();

        logDisconnectionParameters(receiverChannelNumber);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    @Override
    public void changeDeviceSamplingRate(String destinationIpAddress, int samplingRate) {
        int destinationPortNumber = 38700;

        // length of the UDP packet body expressed in bytes
        int dantePacketLength =  80 / 2;

        // 0e38 - command to change the sampling rate and bit depth of the audio
        int danteCommand = 0x0e38;

        String hexStringTemplate = "ffff00{dantePacketLength}{packetId}{danteCommand}00e04cc5a6810000417564696e617465073400810000006400000001000{samplingRate}";

        // Creating a hexadecimal string template with named placeholders
        String hexString = getHexUdpMessageStringForChangeSamplingRate(samplingRate,
                                                                       dantePacketLength, danteCommand,
                                                                       hexStringTemplate);

        // increment packet id with each request
        incrementPacketId();

        logChangeDeviceSamplingRateParameters(samplingRate);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    @Override
    public void changeDeviceBitDepth(String destinationIpAddress, int bitDepth) {
        int destinationPortNumber = 38700;

        // length of the UDP packet body expressed in bytes
        int dantePacketLength =  80 / 2;

        // 0e38 - command to change the sampling rate and bit depth of the audio
        int danteCommand = 0x0e38;

        String hexStringTemplate = "ffff00{dantePacketLength}{packetId}{danteCommand}00e04cc5a6810000417564696e617465073400830000006400000001000000{bitDepth}";

        // Creating a hexadecimal string template with named placeholders
        String hexString = getHexUdpMessageStringForChangeBitDepth(bitDepth,
                                                                   dantePacketLength, danteCommand,
                                                                   hexStringTemplate);

        // increment packet id with each request
        incrementPacketId();

        logChangeDeviceBitDepthParameters(bitDepth);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    /**
     * Method that increments the packet id by 1.
     * <br>
     * The packet id must fit within four hexadecimal digits, so if it exceeds this range, we reset it.
     * <br>
     * The packet id is only needed for temporary identification to ensure packets don't get "lost",
     * so after handling a packet, another packet can take the same identifier.
     */
    private void incrementPacketId() {
        ++packetId;

        if (packetId > 0xffff) {
            packetId = 0;
        }
    }

    /**
     * Method that creates a hexadecimal string, sent as the content of a UDP packet based on the given parameters.
     * It is used for connection or disconnection packets.
     */
    private String getHexUdpMessageStringForConnectionAndDisconnection(String transmitterDeviceName, String transmitterChannelName, int receiverChannelNumber,
                                                                       int dantePacketLength, int danteCommand,
                                                                       String hexStringTemplate) {

        // Creating a map of parameters along with their formats
        Map<String, String> values = new HashMap<>();
        values.put("dantePacketLength", String.format("%02X", dantePacketLength));
        values.put("packetId", String.format("%04X", packetId));
        values.put("danteCommand", String.format("%04X", danteCommand));
        values.put("receiverChannelNumber", String.format("%02X", receiverChannelNumber));
        values.put("transmitterChannelName", transmitterChannelName);
        values.put("transmitterDeviceName", transmitterDeviceName);

        // Replacing placeholders in the template with their actual values
        return formatTemplateString(hexStringTemplate, values);
    }

    /**
     * Method that creates a hexadecimal string, sent as the content of a UDP packet based on the given parameters.
     * It is used for changing sampling rate packets.
     */
    private String getHexUdpMessageStringForChangeSamplingRate(int samplingRate,
                                                               int dantePacketLength, int danteCommand,
                                                               String hexStringTemplate) {

        // Creating a map of parameters along with their formats
        Map<String, String> values = new HashMap<>();
        values.put("dantePacketLength", String.format("%02X", dantePacketLength));
        values.put("packetId", String.format("%04X", packetId));
        values.put("danteCommand", String.format("%04X", danteCommand));
        values.put("samplingRate", String.format("%05X", samplingRate));

        // Replacing placeholders in the template with their actual values
        return formatTemplateString(hexStringTemplate, values);
    }

    /**
     * Method that creates a hexadecimal string, sent as the content of a UDP packet based on the given parameters.
     * It is used for changing bit depth packets.
     */
    private String getHexUdpMessageStringForChangeBitDepth(int bitDepth,
                                                           int dantePacketLength, int danteCommand,
                                                           String hexStringTemplate) {

        // Creating a map of parameters along with their formats
        Map<String, String> values = new HashMap<>();
        values.put("dantePacketLength", String.format("%02X", dantePacketLength));
        values.put("packetId", String.format("%04X", packetId));
        values.put("danteCommand", String.format("%04X", danteCommand));
        values.put("bitDepth", String.format("%02X", bitDepth));

        // Replacing placeholders in the template with their actual values
        return formatTemplateString(hexStringTemplate, values);
    }

    /**
     * Formats a template string by replacing defined keys with corresponding values from the map.
     *
     * <p>
     * In the template, each key-value pair in the map is used to replace a placeholder
     * in the form of `{key}` with the value associated with that key in the map.
     * If the key `{key}` appears in the template, it will be replaced with the corresponding
     * value from the map.
     * </p>
     *
     * @param template the template string containing placeholders in the format `{key}`
     * @param values the map containing keys and their corresponding values to insert into the template
     * @return the formatted string with all keys replaced by their corresponding values
     */
    private String formatTemplateString(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }

    private static void logConnectionParameters(String asciiTransmitterDeviceName, String asciiTransmitterChannelName, int receiverChannelNumber) {
        log.info("Sending request to connect two audio channels");
        log.debug("Transmitter device name: {}", asciiTransmitterDeviceName);
        log.debug("Transmitter channel name: {}", asciiTransmitterChannelName);
        log.debug("Receiver channel number: {}", receiverChannelNumber);
    }

    private static void logDisconnectionParameters(int receiverChannelNumber) {
        log.info("Sending request to disconnect two audio channels");
        log.debug("Receiver channel number: {}", receiverChannelNumber);
    }

    private static void logChangeDeviceSamplingRateParameters(int samplingRate) {
        log.info("Sending request to change device sampling rate");
        log.debug("New device sampling rate: {}", samplingRate);
    }

    private static void logChangeDeviceBitDepthParameters(int bitDepth) {
        log.info("Sending request to change device bit depth");
        log.debug("New device bit depth: {}", bitDepth);
    }

    /**
     * Method that sends a UDP packet to the given IP address, to the given port, with the given string in hexadecimal format
     *
     * @param destinationIpAddressString target IP address of the request
     * @param destinationPort target port of the request
     * @param hexString content of the UDP packet in hexadecimal string format
     */
    private void sendUdpMessage(String destinationIpAddressString,
                                int destinationPort,
                                String hexString) {
        try {
            InetAddress destinationIpAddress = InetAddress.getByName(destinationIpAddressString);

            // Convert hexadecimal string to byte array
            byte[] bufferData = ConverterUtil.convertHexStringToByteArray(hexString);

            // Create UDP packet
            DatagramPacket packet = new DatagramPacket(bufferData, bufferData.length, destinationIpAddress, destinationPort);

            // Create UDP socket to send the packet
            try (DatagramSocket socket = new DatagramSocket()) {
                log.debug("Sending hexadecimal string: {}", hexString);

                // Send the packet
                socket.send(packet);

                log.info("Packet sent successfully.");
            }

        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage(), e);
        }
    }
}
