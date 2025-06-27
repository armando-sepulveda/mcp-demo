package mx.regional.next.automotive.credit.infrastructure.mcp.mappers;

import mx.regional.next.automotive.credit.application.dto.CreditApplicationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Component
public class CreditApplicationMcpMapper {
    
    private final ObjectMapper objectMapper;
    
    public CreditApplicationMcpMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    public CreditApplicationRequest mapToApplicationRequest(
            String customerDocument, String requestedAmount, String vehicleVin,
            String vehicleBrand, String vehicleModel, String vehicleYear,
            String vehicleValue, String vehicleKilometers, String documentsJson) {
        
        try {
            List<CreditApplicationRequest.DocumentRequest> documents = parseDocuments(documentsJson);
            
            return new CreditApplicationRequest(
                customerDocument,
                new BigDecimal(requestedAmount),
                vehicleVin,
                vehicleBrand,
                vehicleModel,
                Integer.parseInt(vehicleYear),
                new BigDecimal(vehicleValue),
                Integer.parseInt(vehicleKilometers),
                null, // color
                null, // engine
                null, // transmission
                documents
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Error mapeando solicitud MCP", e);
        }
    }
    
    private List<CreditApplicationRequest.DocumentRequest> parseDocuments(String documentsJson) {
        try {
            if (documentsJson == null || documentsJson.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            TypeReference<List<DocumentDto>> typeRef = new TypeReference<List<DocumentDto>>() {};
            List<DocumentDto> documentDtos = objectMapper.readValue(documentsJson, typeRef);
            
            return documentDtos.stream()
                .map(dto -> new CreditApplicationRequest.DocumentRequest(dto.type, dto.content))
                .toList();
                
        } catch (Exception e) {
            // Si falla el parsing, crear lista con documento básico
            List<CreditApplicationRequest.DocumentRequest> defaultDocs = new ArrayList<>();
            defaultDocs.add(new CreditApplicationRequest.DocumentRequest("CEDULA", "documento_cedula"));
            return defaultDocs;
        }
    }
    
    private static class DocumentDto {
        public String type;
        public String content;
    }
}