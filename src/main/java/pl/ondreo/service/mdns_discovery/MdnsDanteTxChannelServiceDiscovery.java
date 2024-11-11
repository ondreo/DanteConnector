package pl.ondreo.service.mdns_discovery;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Klasa obsługująca odkrywanie w sieci kanałów transmitujących Dante za pomocą protokołu mDNS.
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
        log.info("Z sieci zniknął kanał transmitujący Dante: '{}'", event.getName());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        log.debug("Service resolved: {}", event.getInfo());
        log.info("Znaleziono kanał transmitujący Dante: '{}'", event.getName());
    }
}
