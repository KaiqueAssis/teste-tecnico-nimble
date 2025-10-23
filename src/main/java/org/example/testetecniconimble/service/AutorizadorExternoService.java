package org.example.testetecniconimble.service;

import org.example.testetecniconimble.dto.AutorizadorResponseDto;
import org.example.testetecniconimble.entity.Cobranca;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AutorizadorExternoService {

    private final RestTemplate restTemplate;

    public AutorizadorExternoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isAutorizadoExternamente() throws AutorizadorExternoException {
        try {
            String ENDPOINT_AUTORIZADOR = "https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer";
            AutorizadorResponseDto resultado = restTemplate.getForObject(
                    ENDPOINT_AUTORIZADOR,
                    AutorizadorResponseDto.class
            );

            return resultado != null && resultado.data().authorized();

        } catch (RestClientException e) {
            throw new AutorizadorExternoException("Erro de comunicação ao verificar a autorização.", e);
        }
    }

    public boolean solicitarEstorno(Cobranca cobranca) throws AutorizadorExternoException {
        return isAutorizadoExternamente();
    }

}
