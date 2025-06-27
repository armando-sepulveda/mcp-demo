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