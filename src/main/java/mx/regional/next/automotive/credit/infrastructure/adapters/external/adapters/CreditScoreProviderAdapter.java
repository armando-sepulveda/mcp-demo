package mx.regional.next.automotive.credit.infrastructure.adapters.external.adapters;

import mx.regional.next.automotive.credit.application.ports.out.CreditScoreProviderPort;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditScore;
import mx.regional.next.automotive.credit.domain.valueobjects.DocumentNumber;
import mx.regional.next.automotive.credit.infrastructure.adapters.external.clients.CreditScoreServiceClient;
import mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.request.CreditScoreRequestDto;
import mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.response.CreditScoreResponseDto;
import mx.regional.next.shared.common.annotations.Adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Adapter
public class CreditScoreProviderAdapter implements CreditScoreProviderPort {
    
    private static final Logger log = LoggerFactory.getLogger(CreditScoreProviderAdapter.class);
    
    private final CreditScoreServiceClient creditScoreServiceClient;
    
    @Value("${security.default-auth-token:default-token}")
    private String defaultAuthToken;
    
    public CreditScoreProviderAdapter(CreditScoreServiceClient creditScoreServiceClient) {
        this.creditScoreServiceClient = creditScoreServiceClient;
    }
    
    @Override
    public CreditScore getCreditScore(DocumentNumber documentNumber) {
        try {
            log.info("Consultando score crediticio para documento: {}", documentNumber.getValue());
            
            CreditScoreRequestDto request = CreditScoreRequestDto.builder()
                .documentNumber(documentNumber.getValue())
                .documentType("CEDULA")
                .includeCreditHistory(true)
                .includePaymentBehavior(true)
                .build();
            
            String authToken = "Bearer " + defaultAuthToken;
            
            CreditScoreResponseDto response = creditScoreServiceClient.getCreditScore(request, authToken);
            
            if (response.isFallbackActivated()) {
                log.warn("Fallback activado para score crediticio - documento: {} - usando score por defecto: {}", 
                        documentNumber.getValue(), response.getCreditScore());
            }
            
            if (!response.isValid()) {
                if (response.getCreditScore() != null) {
                    // Si el fallback proporcionó un score por defecto, usarlo
                    log.info("Usando score por defecto debido a error en servicio: {}", response.getCreditScore());
                    return new CreditScore(response.getCreditScore());
                } else {
                    throw new RuntimeException("Error obteniendo score crediticio: " + response.getErrorMessage());
                }
            }
            
            if (response.getCreditScore() == null) {
                throw new RuntimeException("Score crediticio no disponible para documento: " + documentNumber.getValue());
            }
            
            log.info("Score crediticio obtenido exitosamente: {} - Score: {}", 
                    documentNumber.getValue(), response.getCreditScore());
            
            return new CreditScore(response.getCreditScore());
            
        } catch (Exception e) {
            log.error("Error consultando score crediticio para documento: {}", documentNumber.getValue(), e);
            
            // En caso de error total, usar un score por defecto conservador
            log.warn("Usando score por defecto debido a error: 620");
            return new CreditScore(620);
        }
    }
}