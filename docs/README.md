# Documentación del Proyecto

Esta carpeta contiene documentación detallada sobre la implementación de Arquitectura Hexagonal en el proyecto MCP de Crédito Automotriz.

## 📚 Guías Disponibles

### [🏗️ Diferencias entre Capas Application e Infrastructure](./hexagonal-layers-explained.md)
**Guía completa que explica:**
- ¿Qué implementa cada capa y por qué?
- Diferencias fundamentales entre Application e Infrastructure
- Ejemplos prácticos de código
- Flujos de interacción entre capas
- Errores comunes y cómo evitarlos
- Principios clave para recordar

**¿Cuándo leer esta guía?**
- Si tienes dudas sobre dónde colocar una clase o método
- Si no entiendes por qué hay "dos implementaciones" 
- Si quieres entender el flujo completo de datos
- Si necesitas refactorizar código existente

### [🤖 Responsabilidades de las Capas MCP](./mcp-layers-responsibilities.md)
**Guía especializada en MCP que explica:**
- Responsabilidades específicas de Tools, Resources y Prompts
- Relación entre capas MCP y Arquitectura Hexagonal
- Flujos completos de interacción agente-sistema
- Dónde va la lógica de negocio vs adaptación MCP
- Errores comunes en implementaciones MCP
- Debugging y observabilidad por capas

**¿Cuándo leer esta guía?**
- Si trabajas con Model Context Protocol
- Si no sabes dónde poner lógica en herramientas MCP
- Si quieres entender cómo los agentes IA interactúan con el sistema
- Si necesitas implementar nuevas capacidades MCP

### [🔧 Análisis de Configuración MCP](./mcp-configuration-analysis.md)
**Análisis técnico que cubre:**
- Problemas en configuraciones MCP existentes
- Soluciones implementadas para auto-descubrimiento
- Configuración robusta de servidores MCP
- Manejo de proxies Spring y anotaciones
- Mejores prácticas de configuración

**¿Cuándo leer esta guía?**
- Si tienes problemas con configuración MCP
- Si las herramientas MCP no se registran automáticamente
- Si trabajas con Spring Boot y MCP
- Si quieres entender la configuración del servidor

---

## 🎯 Próximas Guías (Planificadas)

### 🔌 Ports and Adapters Pattern
- Cómo diseñar puertos efectivos
- Implementación de adaptadores robustos
- Patrones de conversión de datos

### 🧪 Testing en Arquitectura Hexagonal
- Estrategias de testing por capa
- Mocks vs Integration tests
- Test doubles y stubs

### 🚀 MCP Integration Patterns
- Mejores prácticas para herramientas MCP
- Diseño de recursos y prompts
- Manejo de errores en MCP

### 🔄 Event-Driven Architecture
- Integración con eventos de dominio
- Patrones asíncronos en hexagonal
- Consistencia eventual

---

## 💡 Cómo Contribuir

Si encuentras información que debería estar documentada:

1. **Crea un issue** describiendo qué documentación necesitas
2. **Propón mejoras** a la documentación existente
3. **Comparte experiencias** sobre implementación

---

## 🔗 Enlaces Útiles

- [README Principal](../README.md) - Visión general del proyecto
- [Código Fuente](../src/) - Implementación completa
- [Tests](../src/test/) - Ejemplos de testing por capa