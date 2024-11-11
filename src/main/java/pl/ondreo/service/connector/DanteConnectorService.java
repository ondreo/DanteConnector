package pl.ondreo.service.connector;

public interface DanteConnectorService {

    /**
     * Metoda wysyłająca żądanie połączenia dwóch kanałów audio na podstawie docelowego adresu IP urządzenia odbierającego,
     * nazwy urządzenia nadającego, nazwy kanału nadającego, numeru kanału odbierającego.
     *
     * @param destinationIpAddress docelowy adres IP urządzenia odbierającego
     * @param asciiTransmitterDeviceName nazwa urządzenia nadającego
     * @param asciiTransmitterChannelName nazwa kanału nadającego
     * @param receiverChannelNumber numer kanału odbierającego
     */
    void sendUdpPacketToConnectTwoChannels(String destinationIpAddress,
                                           String asciiTransmitterDeviceName,
                                           String asciiTransmitterChannelName,
                                           int receiverChannelNumber);

    /**
     * Metoda wysyłająca żądanie rozłączenia dwóch kanałów audio na podstawie docelowego adresu IP urządzenia odbierającego i numeru kanału odbierającego.
     *
     * @param destinationIpAddress docelowy adres IP urządzenia odbierającego
     * @param receiverChannelNumber numer kanału odbierającego
     */
    void sendUdpPacketToDisconnectTwoChannels(String destinationIpAddress,
                                              int receiverChannelNumber);

    /**
     * Metoda wysyłająca żądanie zmiany częstotliwości próbkowania urządzenia na podstawie docelowego adresu IP urządzenia odbierającego
     * oraz nowej częstotliwości próbkowania urządzenia.
     *
     * @param destinationIpAddress docelowy adres IP urządzenia odbierającego
     * @param samplingRate nowa częstotliwość próbkowania urządzenia
     */
    void changeDeviceSamplingRate(String destinationIpAddress,
                                  int samplingRate);

    /**
     * Metoda wysyłająca żądanie zmiany częstotliwości głębi bitowej dźwięku urządzenia na podstawie docelowego
     * adresu IP urządzenia odbierającego oraz nowej częstotliwości próbkowania urządzenia.
     *
     * @param destinationIpAddress docelowy adres IP urządzenia odbierającego
     * @param bitDepth nowa głębia bitowa dźwięku urządzenia
     */
    void changeDeviceBitDepth(String destinationIpAddress,
                              int bitDepth);
}
