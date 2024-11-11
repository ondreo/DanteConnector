package pl.ondreo.service.mdns_discovery;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Klasa obsługująca odkrywanie w sieci urządzeń Dante za pomocą protokołu mDNS.
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
        log.info("Z sieci zniknęło urządzenie Dante: '{}'", event.getName());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        log.debug("Service resolved: {}", event.getInfo());

        ServiceInfo serviceInfo = event.getInfo();
        String[] addresses = serviceInfo.getHostAddresses();
        if (addresses.length > 0) {
            String ipAddress = addresses[0];
            log.info("Znaleziono urządzenie Dante: '{}' z adresem IP: {}", event.getName(), ipAddress);
        } else {
            log.warn("Znaleziono urządzenie Dante: '{}', ale nie udało się pobrać adresu IP.", event.getName());
        }
    }
}
