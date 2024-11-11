package pl.ondreo.service.mdns_discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MdnsServiceDiscovery {

    public void createMdnsServiceDiscovery(String ipAddress) {
        try {
            JmDNS jmdns = JmDNS.create(InetAddress.getByName(ipAddress));

            jmdns.addServiceListener("_netaudio-cmc._udp.local.", new MdnsDanteDeviceServiceDiscovery());
            jmdns.addServiceListener("_netaudio-chan._udp.local.", new MdnsDanteTxChannelServiceDiscovery());
        } catch (UnknownHostException e) {
            log.error("An UnknownHostException occurred: {}", e.getMessage());
        } catch (IOException e) {
            log.error("An IOException occurred: {}", e.getMessage());
        }
    }
}
