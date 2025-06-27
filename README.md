# Arquetipo de Servidor MCP de Crédito Automotriz

## Arquitectura Hexagonal con Model Context Protocol

Este proyecto implementa un servidor completo del **Model Context Protocol (MCP)** para el dominio de crédito automotriz, utilizando **Arquitectura Hexagonal** (también conocida como Ports and Adapters). Esta combinación proporciona una base sólida, escalable y mantenible para sistemas de IA que necesitan integrarse con múltiples servicios externos y bases de datos.

---

## 🎯 ¿Qué es la Arquitectura Hexagonal?

La **Arquitectura Hexagonal**, creada por Alistair Cockburn, es un patrón arquitectónico que separa la lógica de negocio del mundo exterior mediante el uso de **puertos** y **adaptadores**. 

### Conceptos Clave:

- **Dominio (Core)**: La lógica de negocio pura, sin dependencias externas
- **Puertos**: Interfaces que definen contratos de comunicación
- **Adaptadores**: Implementaciones concretas que conectan con tecnologías específicas
- **Inversión de Dependencias**: El dominio no conoce los detalles de implementación externa

### ¿Por qué usar Arquitectura Hexagonal con MCP?

1. **Aislamiento del Dominio**: La lógica de negocio está protegida de cambios en APIs externas
2. **Testabilidad**: Fácil creación de mocks y tests unitarios
3. **Flexibilidad**: Cambiar proveedores de servicios sin afectar la lógica de negocio
4. **Escalabilidad**: Agregar nuevos adaptadores sin modificar el core
5. **Mantenibilidad**: Separación clara de responsabilidades

---

## 🏗️ Estructura del Proyecto

```
src/main/java/mx/regional/next/automotive/credit/
├── domain/                           # 🎯 CAPA DE DOMINIO
│   ├── entities/                     # Entidades de negocio
│   ├── valueobjects/                 # Objetos de valor
│   ├── services/                     # Servicios de dominio
│   └── ports/                        # Puertos (interfaces)
│       ├── in/                       # Puertos de entrada (use cases)
│       └── out/                      # Puertos de salida (repositories, clients)
├── application/                      # 🚀 CAPA DE APLICACIÓN
│   ├── usecases/                     # Casos de uso (orquestación)
│   └── dtos/                         # DTOs de transferencia
├── infrastructure/                   # 🔧 CAPA DE INFRAESTRUCTURA
│   ├── mcp/                          # Adaptadores MCP
│   │   ├── tools/                    # Herramientas MCP
│   │   ├── resources/                # Recursos MCP
│   │   └── prompts/                  # Prompts MCP
│   ├── persistence/                  # Adaptadores de persistencia
│   │   ├── jpa/                      # Implementaciones JPA
│   │   └── repositories/             # Repositorios concretos
│   ├── external/                     # Adaptadores externos
│   │   ├── clients/                  # Clientes Feign
│   │   ├── dtos/                     # DTOs externos
│   │   ├── fallbacks/                # Circuit breakers
│   │   └── config/                   # Configuraciones
│   └── config/                       # Configuraciones generales
├── shared/                           # 🛠️ UTILIDADES COMPARTIDAS
│   ├── constants/                    # Constantes de negocio
│   └── utils/                        # Utilidades
└── resources/                        # 📁 RECURSOS ESTÁTICOS
    ├── mcp/                          # Recursos MCP
    │   ├── prompts/                  # Templates de prompts
    │   └── policies/                 # Políticas de negocio
    └── application.yml               # Configuración Spring
```

---

## 🎯 Capa de Dominio (Domain Layer)

Esta es el **corazón** del sistema. Contiene toda la lógica de negocio pura, sin dependencias externas.

### 📦 Entities (Entidades)

Las entidades representan los conceptos fundamentales del negocio:

```java
// CreditApplication: Representa una solicitud de crédito
public class CreditApplication {
    private CreditApplicationId id;
    private CustomerId customerId;
    private VehicleInformation vehicle;
    private CreditAmount amount;
    private InterestRate rate;
    private LoanTerm term;
    private ApplicationStatus status;
    
    // Lógica de negocio pura
    public void approve() { /* validaciones y cambio de estado */ }
    public void reject(String reason) { /* lógica de rechazo */ }
}
```

**¿Por qué son importantes?**
- Encapsulan reglas de negocio críticas
- Son independientes de frameworks
- Garantizan consistencia de datos
- Facilitan testing unitario

### 💎 Value Objects (Objetos de Valor)

Representan conceptos que se identifican por su valor, no por identidad:

```java
// CreditAmount: Garantiza que los montos sean válidos
public record CreditAmount(BigDecimal value) {
    public CreditAmount {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        if (value.compareTo(MAX_CREDIT_AMOUNT) > 0) {
            throw new IllegalArgumentException("Credit amount exceeds maximum");
        }
    }
}
```

**Beneficios:**
- Inmutabilidad garantizada
- Validaciones en construcción
- Expresividad del código
- Prevención de errores de tipo

### 🧠 Domain Services (Servicios de Dominio)

Contienen lógica de negocio que no pertenece a una entidad específica:

```java
@Service
public class RiskCalculationService {
    
    public RiskCategory calculateRisk(CreditApplication application, 
                                     CreditScore score, 
                                     EmploymentInfo employment) {
        // Algoritmo complejo de cálculo de riesgo
        // Combina múltiples factores
        // Aplica reglas de negocio específicas
    }
}
```

### 🔌 Ports (Puertos)

Son interfaces que definen contratos de comunicación:

#### Puertos de Entrada (In Ports)
```java
// Define QUÉ puede hacer el sistema
public interface ProcessCreditApplicationUseCase {
    CreditApplicationResult process(ProcessCreditApplicationCommand command);
}
```

#### Puertos de Salida (Out Ports)
```java
// Define QUÉ necesita el sistema del exterior
public interface CreditApplicationRepository {
    void save(CreditApplication application);
    Optional<CreditApplication> findById(CreditApplicationId id);
}

public interface CreditBureauPort {
    CreditScore getCreditScore(DocumentNumber document);
}
```

---

## 🚀 Capa de Aplicación (Application Layer)

Esta capa orquesta los casos de uso y coordina el flujo de trabajo.

### 📋 Use Cases (Casos de Uso)

Implementan los puertos de entrada y orquestan la lógica:

```java
@UseCase
public class ProcessCreditApplicationUseCaseImpl implements ProcessCreditApplicationUseCase {
    
    private final CreditApplicationRepository repository;
    private final CreditBureauPort creditBureau;
    private final RiskCalculationService riskService;
    
    @Override
    public CreditApplicationResult process(ProcessCreditApplicationCommand command) {
        // 1. Validar datos de entrada
        // 2. Consultar score crediticio
        // 3. Calcular riesgo
        // 4. Determinar aprobación
        // 5. Guardar resultado
        // 6. Notificar resultado
    }
}
```

### 📄 DTOs (Data Transfer Objects)

Objetos para transferir datos entre capas:

```java
public record ProcessCreditApplicationCommand(
    String documentNumber,
    String documentType,
    BigDecimal requestedAmount,
    Integer termMonths,
    VehicleInformationDto vehicle,
    PersonalInformationDto personal
) {}
```

---

## 🔧 Capa de Infraestructura (Infrastructure Layer)

Contiene todas las implementaciones concretas y detalles técnicos.

### 🤖 Adaptadores MCP

#### Tools (Herramientas)
Las herramientas MCP permiten que los LLMs ejecuten acciones específicas:

```java
@Component
public class ProcessCreditApplicationTool {
    
    @McpTool(
        name = "process_credit_application",
        description = "Procesa una nueva solicitud de crédito automotriz"
    )
    public CreditApplicationResult process(
        @McpParameter(name = "document_number") String documentNumber,
        @McpParameter(name = "requested_amount") BigDecimal amount
        // ... más parámetros
    ) {
        // Delega al caso de uso correspondiente
        return processUseCase.process(new ProcessCreditApplicationCommand(...));
    }
}
```

**¿Por qué esto es poderoso?**
- Los LLMs pueden ejecutar lógica de negocio compleja
- Acceso controlado y validado a funcionalidades
- Trazabilidad completa de acciones
- Integración natural con conversaciones de IA

#### Resources (Recursos)
Los recursos MCP proporcionan datos estructurados a los LLMs:

```java
@Component
public class VehicleCatalogResource {
    
    @McpResource(
        uri = "vehicle://catalog/brands",
        name = "Catálogo de Marcas de Vehículos",
        description = "Lista completa de marcas autorizadas para crédito"
    )
    public List<VehicleBrandDto> getAuthorizedBrands() {
        return vehicleCatalogService.getAuthorizedBrands();
    }
}
```

#### Prompts (Templates)
Templates estructurados para guiar las conversaciones:

```java
@Component
public class DocumentValidatorPrompt {
    
    @McpPrompt(
        name = "validate_customer_documents",
        description = "Valida documentos de cliente para crédito automotriz"
    )
    public String createPrompt(
        @McpParameter(name = "documents") List<DocumentDto> documents
    ) {
        return promptTemplate.render("document_validation", Map.of(
            "documents", documents,
            "requirements", getDocumentRequirements()
        ));
    }
}
```

### 💾 Adaptadores de Persistencia

Implementan los puertos de salida para bases de datos:

```java
@Repository
public class JpaCreditApplicationRepository implements CreditApplicationRepository {
    
    private final CreditApplicationJpaRepository jpaRepository;
    private final CreditApplicationMapper mapper;
    
    @Override
    public void save(CreditApplication application) {
        CreditApplicationEntity entity = mapper.toEntity(application);
        jpaRepository.save(entity);
    }
}
```

### 🌐 Adaptadores Externos

#### Clientes Feign
Integración con servicios externos:

```java
@FeignClient(
    name = "credit-bureau-client",
    url = "${external.services.credit-bureau.url}",
    fallback = CreditBureauClientFallback.class
)
public interface CreditBureauClient {
    
    @PostMapping("/api/v1/credit-report")
    CreditBureauResponse getCreditReport(@RequestBody CreditBureauRequest request);
}
```

#### Fallbacks (Circuit Breakers)
Manejo de fallos y degradación elegante:

```java
@Component
public class CreditBureauClientFallback implements CreditBureauClient {
    
    @Override
    public CreditBureauResponse getCreditReport(CreditBureauRequest request) {
        log.warn("Credit Bureau service unavailable, using fallback");
        return CreditBureauResponse.builder()
            .status("SERVICE_UNAVAILABLE")
            .fallbackUsed(true)
            .build();
    }
}
```

---

## 🛠️ Utilidades Compartidas

### 📐 Constants (Constantes)
Centralizan todas las reglas de negocio:

```java
public final class CreditConstants {
    
    public static final class AmountLimits {
        public static final BigDecimal MIN_CREDIT_AMOUNT = BigDecimal.valueOf(50_000);
        public static final BigDecimal MAX_CREDIT_AMOUNT = BigDecimal.valueOf(2_000_000_000);
    }
    
    public static final class CreditScoreRanges {
        public static final int EXCELLENT_THRESHOLD = 750;
        public static final int GOOD_THRESHOLD = 550;
    }
}
```

### ✅ Validation Utils
Utilidades de validación reutilizables:

```java
public final class ValidationUtils {
    
    public static boolean isValidCreditAmount(BigDecimal amount) {
        return amount != null 
            && amount.compareTo(MIN_CREDIT_AMOUNT) >= 0 
            && amount.compareTo(MAX_CREDIT_AMOUNT) <= 0;
    }
    
    public static boolean isValidDocumentNumber(String documentNumber) {
        return documentNumber != null 
            && DOCUMENT_PATTERN.matcher(documentNumber).matches();
    }
}
```

---

## 🎨 ¿Por qué MCP se Beneficia de la Arquitectura Hexagonal?

### 1. **Separación Clara de Responsabilidades**
```
LLM Request → MCP Tool → Use Case → Domain Service → External API
     ↑                                    ↓
   MCP Response ← DTO ← Domain Entity ← Repository ← Database
```

### 2. **Testabilidad Superior**
```java
// Test unitario del dominio (sin dependencias externas)
@Test
void shouldCalculateRiskCorrectly() {
    // Given: objetos de dominio puros
    var application = new CreditApplication(...);
    var score = new CreditScore(750);
    
    // When: lógica de dominio pura
    var risk = riskService.calculateRisk(application, score);
    
    // Then: verificación directa
    assertThat(risk).isEqualTo(RiskCategory.LOW);
}

// Test de integración MCP (con mocks)
@Test
void shouldProcessCreditApplicationViaMcp() {
    // Given: mock de servicios externos
    when(creditBureauPort.getCreditScore(any())).thenReturn(mockScore);
    
    // When: llamada a través de MCP tool
    var result = mcpTool.process("12345678", BigDecimal.valueOf(100_000));
    
    // Then: verificación de resultado
    assertThat(result.isApproved()).isTrue();
}
```

### 3. **Flexibilidad de Integraciones**
```java
// Cambiar de provider sin afectar la lógica de negocio
@Profile("datacredito")
@Component
public class DatacreditoAdapter implements CreditBureauPort { ... }

@Profile("experian")
@Component
public class ExperianAdapter implements CreditBureauPort { ... }
```

### 4. **Evolución Independiente**
- **Dominio**: Evoluciona según reglas de negocio
- **MCP Tools**: Evolucionan según necesidades de IA
- **Integraciones**: Evolucionan según APIs externas
- **Persistencia**: Evoluciona según requerimientos de datos

### 5. **Observabilidad y Debugging**
```java
@McpTool(name = "diagnose_credit_application")
public DiagnosticResult diagnose(String applicationId) {
    return DiagnosticResult.builder()
        .domainState(getDomainState(applicationId))
        .externalServices(checkExternalServices())
        .mcpMetrics(getMcpMetrics())
        .build();
}
```

---

## 🚀 Casos de Uso del Sistema

### 1. **Procesamiento de Solicitudes de Crédito**
```
Cliente → Conversación con IA → MCP Tool → Validación de Documentos
    ↓
Consulta Score Crediticio → Valuación de Vehículo → Cálculo de Riesgo
    ↓
Decisión de Aprobación → Cálculo de Tasas → Generación de Contrato
```

### 2. **Consulta de Estados**
```
Cliente: "¿Cuál es el estado de mi solicitud 12345?"
    ↓
MCP Tool: GetCreditApplicationStatusTool
    ↓
Use Case: GetApplicationStatusUseCase
    ↓
Response: Estado detallado con explicaciones
```

### 3. **Asesoría Inteligente**
```
Cliente: "¿Qué vehículo puedo comprar con mi presupuesto?"
    ↓
MCP Resource: VehicleCatalogResource
    ↓
MCP Prompt: VehicleRecommendationPrompt
    ↓
Respuesta: Recomendaciones personalizadas
```

---

## 🔧 Configuración y Ejecución

### Prerrequisitos
- Java 21+
- Spring Boot 3.5.3
- Maven 3.9+
- Base de datos (H2 para desarrollo, PostgreSQL para producción)

### Configuración
```yaml
# application.yml
mcp:
  server:
    port: 8080
    name: "Automotive Credit MCP Server"
    version: "1.0.0"

external:
  services:
    credit-bureau:
      url: "https://api.creditbureau.com"
      timeout: 30000
    vehicle-valuation:
      url: "https://api.vehiclevaluation.com"
      timeout: 25000

spring:
  datasource:
    url: jdbc:h2:mem:automotive_credit
    driver-class-name: org.h2.Driver
```

### Ejecución
```bash
# Desarrollo
mvn spring-boot:run

# Producción
java -jar automotive-credit-mcp-server.jar
```

---

## 📊 Beneficios de esta Arquitectura

### ✅ Para Desarrolladores
- **Código limpio y mantenible**
- **Tests rápidos y confiables**
- **Fácil agregar nuevas funcionalidades**
- **Debugging simplificado**

### ✅ Para el Negocio
- **Tiempo de desarrollo reducido**
- **Menor riesgo de errores**
- **Facilidad para cambiar proveedores**
- **Escalabilidad natural**

### ✅ Para IA/LLMs
- **Acceso estructurado a funcionalidades**
- **Consistencia en respuestas**
- **Capacidad de introspección**
- **Evolución controlada de capacidades**

---

## 🔮 Casos de Uso Avanzados

### 1. **Multi-tenancy**
```java
// Diferentes configuraciones por tenant
@Component
public class TenantAwareCreditRules implements CreditRulesPort {
    
    public CreditRules getRules(TenantId tenantId) {
        return switch (tenantId.value()) {
            case "BANCO_A" -> bankASpecificRules();
            case "BANCO_B" -> bankBSpecificRules();
            default -> defaultRules();
        };
    }
}
```

### 2. **Event Sourcing**
```java
// Historial completo de cambios
@Entity
public class CreditApplicationEvent {
    private EventId id;
    private CreditApplicationId aggregateId;
    private EventType type;
    private String eventData;
    private Instant timestamp;
    private UserId causedBy;
}
```

### 3. **Machine Learning Integration**
```java
// Modelos de ML como servicios de dominio
@Component
public class MlRiskScoringService implements RiskScoringPort {
    
    public RiskScore calculateRisk(CreditApplicationData data) {
        // Llamada a modelo ML
        var prediction = mlModelClient.predict(data);
        return new RiskScore(prediction.score(), prediction.confidence());
    }
}
```

---

## 📚 Referencias y Documentación Adicional

### Arquitectura Hexagonal
- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture by Robert Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

### Model Context Protocol
- [MCP Specification](https://modelcontextprotocol.io/specification)
- [MCP SDK Documentation](https://modelcontextprotocol.io/docs/sdk)

### Spring Boot y Feign
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)

---

## 🤝 Contribución

Este arquetipo está diseñado para ser un punto de partida robusto. Para contribuir:

1. **Fork el repositorio**
2. **Crear una rama feature**
3. **Seguir los principios de arquitectura hexagonal**
4. **Agregar tests comprehensivos**
5. **Documentar cambios**

---

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Ver `LICENSE` para más detalles.

---

## 🎯 Conclusión

La combinación de **Arquitectura Hexagonal** con **Model Context Protocol** proporciona una base excepcional para construir sistemas de IA robustos, mantenibles y escalables. Este arquetipo demuestra cómo separar claramente las responsabilidades mientras se mantiene la flexibilidad para evolucionar tanto la lógica de negocio como las integraciones con IA.

La arquitectura hexagonal garantiza que los cambios en las tecnologías externas (APIs, bases de datos, frameworks de IA) no afecten el corazón del sistema, mientras que MCP proporciona una interfaz estándar para que los LLMs interactúen de manera natural y poderosa con el dominio de negocio.