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

        // długość ciała pakietu UDP wyrażona w ilości bajtów
        int dantePacketLength =  (76 + transmitterChannelName.length() + transmitterDeviceName.length()) / 2;

        // 3410 - komenda na połączenie lub rozłączenie dwóch kanałów audio
        int danteCommand = 0x3410;

        String hexStringTemplate = "280900{dantePacketLength}{packetId}{danteCommand}00000000000000000800020100{receiverChannelNumber}0003002400270000000000000000{transmitterChannelName}00{transmitterDeviceName}00";

        // Utworzenie szablonu szesnastkowego ciągu znaków z nazwanymi symbolami zastępczymi
        String hexString = getHexUdpMessageStringForConnectionAndDisconnection(transmitterDeviceName, transmitterChannelName, receiverChannelNumber,
                                                                               dantePacketLength, danteCommand,
                                                                               hexStringTemplate);

        // inkrementacja id pakietu przy każdym żądaniu
        incrementPacketId();

        logConnectionParameters(asciiTransmitterDeviceName, asciiTransmitterChannelName, receiverChannelNumber);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    @Override
    public void sendUdpPacketToDisconnectTwoChannels(String destinationIpAddress, int receiverChannelNumber) {

        int destinationPortNumber = 4440;

        // długość ciała pakietu UDP wyrażona w ilości bajtów
        int dantePacketLength =  72 / 2;

        // 3410 - komenda na połączenie lub rozłączenie dwóch kanałów audio
        int danteCommand = 0x3410;

        String hexStringTemplate = "280900{dantePacketLength}{packetId}{danteCommand}00000000000000000800020100{receiverChannelNumber}0003000000000000000000000000";

        // Utworzenie szablonu szesnastkowego ciągu znaków z nazwanymi symbolami zastępczymi
        String hexString = getHexUdpMessageStringForConnectionAndDisconnection(null, null, receiverChannelNumber,
                                                                               dantePacketLength, danteCommand,
                                                                               hexStringTemplate);

        // inkrementacja id pakietu przy każdym żądaniu
        incrementPacketId();

        logDisconnectionParameters(receiverChannelNumber);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    @Override
    public void changeDeviceSamplingRate(String destinationIpAddress, int samplingRate) {
        int destinationPortNumber = 38700;

        // długość ciała pakietu UDP wyrażona w ilości bajtów
        int dantePacketLength =  80 / 2;

        // 0e38 - komenda na zmianę częstotliwości próbkowania dźwięku i głębi bitowej dźwięku
        int danteCommand = 0x0e38;

        String hexStringTemplate = "ffff00{dantePacketLength}{packetId}{danteCommand}00e04cc5a6810000417564696e617465073400810000006400000001000{samplingRate}";

        // Utworzenie szablonu szesnastkowego ciągu znaków z nazwanymi symbolami zastępczymi
        String hexString = getHexUdpMessageStringForChangeSamplingRate(samplingRate,
                                                                       dantePacketLength, danteCommand,
                                                                       hexStringTemplate);

        // inkrementacja id pakietu przy każdym żądaniu
        incrementPacketId();

        logChangeDeviceSamplingRateParameters(samplingRate);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    @Override
    public void changeDeviceBitDepth(String destinationIpAddress, int bitDepth) {
        int destinationPortNumber = 38700;

        // długość ciała pakietu UDP wyrażona w ilości bajtów
        int dantePacketLength =  80 / 2;

        // 0e38 - komenda na zmianę częstotliwości próbkowania dźwięku i głębi bitowej dźwięku
        int danteCommand = 0x0e38;

        String hexStringTemplate = "ffff00{dantePacketLength}{packetId}{danteCommand}00e04cc5a6810000417564696e617465073400830000006400000001000000{bitDepth}";

        // Utworzenie szablonu szesnastkowego ciągu znaków z nazwanymi symbolami zastępczymi
        String hexString = getHexUdpMessageStringForChangeBitDepth(bitDepth,
                                                                   dantePacketLength, danteCommand,
                                                                   hexStringTemplate);

        // inkrementacja id pakietu przy każdym żądaniu
        incrementPacketId();

        logChangeDeviceBitDepthParameters(bitDepth);

        sendUdpMessage(destinationIpAddress,
                       destinationPortNumber,
                       hexString);
    }

    /**
     * Metoda zwiększająca o 1 id pakietu.
     * <br>
     * Id pakietu musi się zmieścić w czterech cyfrach szesnastkowych, więc jeśli przekroczy ten zakres, to go resetujemy.
     * <br>
     * Id pakietu potrzebne jest jedynie do tymczasowej identyfikacji, żeby się pakiety "nie zgubiły",
     * więc po zakończeniu obsługi pakietu inny pakiet może przyjąć ten sam identyfikator.
     */
    private void incrementPacketId() {
        ++packetId;

        if (packetId > 0xffff) {
            packetId = 0;
        }
    }

    /**
     * Metoda tworząca szesnastkowy ciąg znaków, wysyłany jako treść pakietu UDP na podstawie podanych parametrów.
     */
    private String getHexUdpMessageStringForConnectionAndDisconnection(String transmitterDeviceName, String transmitterChannelName, int receiverChannelNumber,
                                                                       int dantePacketLength, int danteCommand,
                                                                       String hexStringTemplate) {

        // Utworzenie mapy parametrów wraz z ich formatami
        Map<String, String> values = new HashMap<>();
        values.put("dantePacketLength", String.format("%02X", dantePacketLength));
        values.put("packetId", String.format("%04X", packetId));
        values.put("danteCommand", String.format("%04X", danteCommand));
        values.put("receiverChannelNumber", String.format("%02X", receiverChannelNumber));
        values.put("transmitterChannelName", transmitterChannelName);
        values.put("transmitterDeviceName", transmitterDeviceName);

        // Zamiana symboli zastępczych z szablonu na ich rzeczywiste wartości
        return formatTemplateString(hexStringTemplate, values);
    }

    /**
     * Metoda tworząca szesnastkowy ciąg znaków, wysyłany jako treść pakietu UDP na podstawie podanych parametrów.
     */
    private String getHexUdpMessageStringForChangeSamplingRate(int samplingRate,
                                                               int dantePacketLength, int danteCommand,
                                                               String hexStringTemplate) {

        // Utworzenie mapy parametrów wraz z ich formatami
        Map<String, String> values = new HashMap<>();
        values.put("dantePacketLength", String.format("%02X", dantePacketLength));
        values.put("packetId", String.format("%04X", packetId));
        values.put("danteCommand", String.format("%04X", danteCommand));
        values.put("samplingRate", String.format("%05X", samplingRate));

        // Zamiana symboli zastępczych z szablonu na ich rzeczywiste wartości
        return formatTemplateString(hexStringTemplate, values);
    }

    /**
     * Metoda tworząca szesnastkowy ciąg znaków, wysyłany jako treść pakietu UDP na podstawie podanych parametrów.
     */
    private String getHexUdpMessageStringForChangeBitDepth(int bitDepth,
                                                           int dantePacketLength, int danteCommand,
                                                           String hexStringTemplate) {

        // Utworzenie mapy parametrów wraz z ich formatami
        Map<String, String> values = new HashMap<>();
        values.put("dantePacketLength", String.format("%02X", dantePacketLength));
        values.put("packetId", String.format("%04X", packetId));
        values.put("danteCommand", String.format("%04X", danteCommand));
        values.put("bitDepth", String.format("%02X", bitDepth));

        // Zamiana symboli zastępczych z szablonu na ich rzeczywiste wartości
        return formatTemplateString(hexStringTemplate, values);
    }

    /**
     * Formatuje ciąg szablonu, zastępując zdefiniowane w nim klucze odpowiednimi wartościami
     * z mapy.
     *
     * <p>
     * W szablonie każda para klucz-wartość w mapie jest używana do zastąpienia miejsca
     * wstawienia o postaci `{key}` wartością powiązaną z danym kluczem w mapie.
     * Jeśli klucz `{key}` występuje w szablonie, zostanie on zastąpiony odpowiednią
     * wartością z mapy.
     * </p>
     *
     * @param template ciąg szablonu zawierający miejsca wstawienia w formacie `{key}`
     * @param values   mapa zawierająca klucze i odpowiadające im wartości do wstawienia w szablonie
     * @return sformatowany ciąg, w którym wszystkie klucze zostały zastąpione odpowiednimi wartościami
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
        log.info("Wysyłanie żądania połączenia dwóch kanałów audio");
        log.debug("Nazwa urządzenia nadającego: {}", asciiTransmitterDeviceName);
        log.debug("Nazwa kanału nadającego: {}", asciiTransmitterChannelName);
        log.debug("Numer kanału odbierającego: {}", receiverChannelNumber);
    }

    private static void logDisconnectionParameters(int receiverChannelNumber) {
        log.info("Wysyłanie żądania rozłączenia dwóch kanałów audio");
        log.debug("Numer kanału odbierającego: {}", receiverChannelNumber);
    }

    private static void logChangeDeviceSamplingRateParameters(int samplingRate) {
        log.info("Wysyłanie żądania zmiany częstotliwości próbkowania urządzenia");
        log.debug("Nowa częstotliwość próbkowania urządzenia: {}", samplingRate);
    }

    private static void logChangeDeviceBitDepthParameters(int bitDepth) {
        log.info("Wysyłanie żądania zmiany głębi bitowej dźwięku urządzenia");
        log.debug("Nowa głębia bitowa dźwięku urządzenia: {}", bitDepth);
    }

    /**
     * Metoda wysyłająca pakiet UDP na podany adres IP, na podany port, z danym ciągiem znaków w postaci szesnastkowej
     * @param destinationIpAddressString docelowy adres IP żądania
     * @param destinationPort port docelowy żądania
     * @param hexString treść pakietu UDP w postaci szesnastkowego ciągu znaków
     */
    private void sendUdpMessage(String destinationIpAddressString,
                                int destinationPort,
                                String hexString) {
        try {
            InetAddress destinationIpAddress = InetAddress.getByName(destinationIpAddressString);

            // Konwersja ciągu znaków szesnastkowych na tablicę bajtów
            byte[] bufferData = ConverterUtil.convertHexStringToByteArray(hexString);

            // Utworzenie pakietu UDP
            DatagramPacket packet = new DatagramPacket(bufferData, bufferData.length, destinationIpAddress, destinationPort);

            // Utworzenie gniazda UDP na wysłanie pakietu
            try (DatagramSocket socket = new DatagramSocket()) {
                log.debug("Wysyłany ciąg znaków w postaci szesnastkowej: {}", hexString);

                // Wysłanie pakietu
                socket.send(packet);

                log.info("Pakiet został wysłany pomyślnie.");
            }

        } catch (Exception e) {
            log.error("Wystąpił błąd: {}", e.getMessage(), e);
        }
    }
}
