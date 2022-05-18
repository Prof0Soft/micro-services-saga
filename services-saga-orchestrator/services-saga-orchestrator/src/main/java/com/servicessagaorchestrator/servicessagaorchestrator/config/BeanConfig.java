package com.servicessagaorchestrator.servicessagaorchestrator.config;

import com.servicessagaorchestrator.servicessagaorchestrator.service.client.McAClient;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.McBClient;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.McCClient;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.StartProcessClientService;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Configuration
public class BeanConfig {

    @Bean
    public Map<BookingFlow, StartProcessClientService> clientMc(final McAClient mcAClient,
                                                                final McBClient mcBClient,
                                                                final McCClient mcCClient) {
        final Map<BookingFlow, StartProcessClientService> beansClients = new HashMap<>();

        beansClients.put(BookingFlow.SERVICE_A, mcAClient);
        beansClients.put(BookingFlow.SERVICE_B, mcBClient);
        beansClients.put(BookingFlow.SERVICE_C, mcCClient);

        return beansClients;
    }
}
