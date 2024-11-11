package pl.ondreo.service.mdns_discovery;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Class handling the discovery of Dante devices on the network using the mDNS protocol.
 */
@Slf4j
public class MdnsDanteDeviceServiceDiscovery implements ServiceListener {
    @Override
    public void serviceAdded(ServiceEvent event) {
        log.debug("Service added: {}", event.getInfo());
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        log.debug("Service removed: {}", event.getInfo());
        log.info("Dante device disappeared from the network: '{}'", event.getName());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        log.debug("Service resolved: {}", event.getInfo());

        ServiceInfo serviceInfo = event.getInfo();
        String[] addresses = serviceInfo.getHostAddresses();
        if (addresses.length > 0) {
            String ipAddress = addresses[0];
            log.info("Found Dante device: '{}' with IP address: {}", event.getName(), ipAddress);
        } else {
            log.warn("Found Dante device: '{}', but failed to retrieve IP address.", event.getName());
        }
    }
}
