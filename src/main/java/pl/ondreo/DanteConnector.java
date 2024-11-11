package pl.ondreo;

import lombok.extern.slf4j.Slf4j;
import pl.ondreo.enums.BitDepth;
import pl.ondreo.enums.SamplingRate;
import pl.ondreo.service.mdns_discovery.MdnsServiceDiscovery;
import pl.ondreo.service.connector.DanteConnectorService;
import pl.ondreo.service.connector.DanteConnectorServiceImpl;

@Slf4j
public class DanteConnector {

    public static void main(String[] args) throws InterruptedException {
        DanteConnectorService danteConnectorService = new DanteConnectorServiceImpl();
        MdnsServiceDiscovery mdnsServiceDiscovery = new MdnsServiceDiscovery();

        String mainPcIpAddress = "192.168.1.101";
        String secondPcIpAddress = "192.168.1.105";

        String mainPcDeviceName = "main-pc-name";
        String secondPcDeviceName = "second-pc-name";

        testConnectionOfTwoChannels(danteConnectorService,
                                    secondPcIpAddress,
                                    mainPcDeviceName,
                                    mainPcIpAddress,
                                    secondPcDeviceName);

        Thread.sleep(5000);

        testDisconnectionOfTwoChannels(danteConnectorService,
                                       secondPcIpAddress,
                                       mainPcIpAddress);

        Thread.sleep(5000);

        testChangeDeviceSamplingRate(danteConnectorService, secondPcIpAddress, mainPcIpAddress);

        Thread.sleep(5000);

        testChangeDeviceBitDepth(danteConnectorService, secondPcIpAddress, mainPcIpAddress);

        testMdnsDiscovery(mdnsServiceDiscovery, mainPcIpAddress);
    }

    private static void testConnectionOfTwoChannels(DanteConnectorService danteConnectorService,
                                                    String secondPcIpAddress,
                                                    String mainPcDeviceName,
                                                    String mainPcIpAddress,
                                                    String secondPcDeviceName) {

        danteConnectorService.sendUdpPacketToConnectTwoChannels(secondPcIpAddress,
                                                                mainPcDeviceName,
                                                                "01",
                                                                1);

        danteConnectorService.sendUdpPacketToConnectTwoChannels(secondPcIpAddress,
                                                                mainPcDeviceName,
                                                                "02",
                                                                2);

        danteConnectorService.sendUdpPacketToConnectTwoChannels(mainPcIpAddress,
                                                                secondPcDeviceName,
                                                                "01",
                                                                2);

        danteConnectorService.sendUdpPacketToConnectTwoChannels(mainPcIpAddress,
                                                                secondPcDeviceName,
                                                                "02",
                                                                1);
    }

    private static void testDisconnectionOfTwoChannels(DanteConnectorService danteConnectorService,
                                                       String secondPcIpAddress,
                                                       String mainPcIpAddress) {
        danteConnectorService.sendUdpPacketToDisconnectTwoChannels(secondPcIpAddress, 1);

        danteConnectorService.sendUdpPacketToDisconnectTwoChannels(secondPcIpAddress, 2);

        danteConnectorService.sendUdpPacketToDisconnectTwoChannels(mainPcIpAddress, 2);

        danteConnectorService.sendUdpPacketToDisconnectTwoChannels(mainPcIpAddress, 1);
    }

    private static void testChangeDeviceSamplingRate(DanteConnectorService danteConnectorService,
                                                     String secondPcIpAddress,
                                                     String mainPcIpAddress) {
        danteConnectorService.changeDeviceSamplingRate(secondPcIpAddress, SamplingRate.K44_1.getValue());
        danteConnectorService.changeDeviceSamplingRate(secondPcIpAddress, SamplingRate.K48.getValue());
        danteConnectorService.changeDeviceSamplingRate(secondPcIpAddress, SamplingRate.K88_2.getValue());

        danteConnectorService.changeDeviceSamplingRate(mainPcIpAddress, SamplingRate.K96.getValue());
        danteConnectorService.changeDeviceSamplingRate(mainPcIpAddress, SamplingRate.K176_4.getValue());
        danteConnectorService.changeDeviceSamplingRate(mainPcIpAddress, SamplingRate.K192.getValue());
    }

    private static void testChangeDeviceBitDepth(DanteConnectorService danteConnectorService,
                                                 String secondPcIpAddress,
                                                 String mainPcIpAddress) {
        danteConnectorService.changeDeviceBitDepth(secondPcIpAddress, BitDepth.B16.getValue());
        danteConnectorService.changeDeviceBitDepth(secondPcIpAddress, BitDepth.B24.getValue());
        danteConnectorService.changeDeviceBitDepth(secondPcIpAddress, BitDepth.B32.getValue());

        danteConnectorService.changeDeviceBitDepth(mainPcIpAddress, BitDepth.B16.getValue());
        danteConnectorService.changeDeviceBitDepth(mainPcIpAddress, BitDepth.B24.getValue());
        danteConnectorService.changeDeviceBitDepth(mainPcIpAddress, BitDepth.B32.getValue());
    }

    private static void testMdnsDiscovery(MdnsServiceDiscovery mdnsServiceDiscovery, String mainPcIpAddress) {
        mdnsServiceDiscovery.createMdnsServiceDiscovery(mainPcIpAddress);
    }
}
