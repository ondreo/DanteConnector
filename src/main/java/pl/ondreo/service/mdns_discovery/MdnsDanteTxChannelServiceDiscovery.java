package pl.ondreo.service.mdns_discovery;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Class handling the discovery of transmitting Dante channels on the network using the mDNS protocol.
 */
@Slf4j
public class MdnsDanteTxChannelServiceDiscovery implements ServiceListener {
    @Override
    public void serviceAdded(ServiceEvent event) {
        log.debug("Service added: {}", event.getInfo());
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        log.debug("Service removed: {}", event.getInfo());
        log.info("Dante transmitting channel disappeared from the network: '{}'", event.getName());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        log.debug("Service resolved: {}", event.getInfo());
        log.info("Found Dante transmitting channel: '{}'", event.getName());
    }
}
