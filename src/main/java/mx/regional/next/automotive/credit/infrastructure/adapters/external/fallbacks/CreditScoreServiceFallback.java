package mx.regional.next.automotive.credit.infrastructure.adapters.external.fallbacks;

import mx.regional.next.automotive.credit.infrastructure.adapters.external.clients.CreditScoreServiceClient;
import mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.request.CreditScoreRequestDto;
import mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.response.CreditScoreResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreditScoreServiceFallback implements CreditScoreServiceClient {
    
    private static final Logger log = LoggerFactory.getLogger(CreditScoreServiceFallback.class);
    
    @Override
    public CreditScoreResponseDto getCreditScore(CreditScoreRequestDto request, String authorization) {
        log.warn("Circuit breaker activo para getCreditScore - documento: {}", 
                request.getDocumentNumber());
        
        return CreditScoreResponseDto.builder()
            .documentNumber(request.getDocumentNumber())
            .valid(false)
            .errorCode("SERVICE_UNAVAILABLE")
            .errorMessage("Servicio de score crediticio temporalmente no disponible. Usando score por defecto.")
            .fallbackActivated(true)
            .creditScore(650) // Score por defecto para permitir evaluación
            .scoreCategory("DEFAULT")
            .build();
    }
    
    @Override
    public CreditScoreResponseDto getCreditScoreByDocument(String documentNumber, String authorization) {
        log.warn("Circuit breaker activo para getCreditScoreByDocument - documento: {}", documentNumber);
        
        return CreditScoreResponseDto.builder()
            .documentNumber(documentNumber)
            .valid(false)
            .errorCode("SERVICE_UNAVAILABLE")
            .errorMessage("Servicio de score crediticio temporalmente no disponible. Usando score por defecto.")
            .fallbackActivated(true)
            .creditScore(650) // Score por defecto para permitir evaluación
            .scoreCategory("DEFAULT")
            .build();
    }
}