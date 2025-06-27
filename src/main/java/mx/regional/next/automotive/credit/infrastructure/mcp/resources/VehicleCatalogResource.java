package mx.regional.next.automotive.credit.infrastructure.mcp.resources;

import org.springframework.ai.mcp.server.annotation.McpResource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class VehicleCatalogResource {
    
    private static final Logger log = LoggerFactory.getLogger(VehicleCatalogResource.class);

    @McpResource(
        uri = "credit://vehicles/catalog",
        name = "Vehicle Catalog",
        description = "Catálogo completo de vehículos elegibles para crédito automotriz con especificaciones y criterios de financiación"
    )
    public String getVehicleCatalog() {
        log.debug("Proporcionando catálogo de vehículos elegibles");
        
        return """
            🚗 **CATÁLOGO DE VEHÍCULOS ELEGIBLES**
            
            ## 📋 Marcas Autorizadas
            
            ### 🏆 **MARCAS PREMIUM**
            
            #### **TOYOTA**
            - **Modelos Elegibles:**
              - COROLLA (Sedan/Cross): $65M - $95M
              - CAMRY (Sedan): $120M - $180M
              - RAV4 (SUV): $140M - $220M
              - HILUX (Pickup): $110M - $170M
              - PRADO (SUV): $280M - $350M
            - **Características:** Excelente valor de reventa, bajo costo de mantenimiento
            - **Financiación:** Hasta 84 meses, tasas preferenciales desde 12%
            
            #### **CHEVROLET**
            - **Modelos Elegibles:**
              - SPARK (Hatchback): $50M - $70M
              - AVEO (Sedan): $55M - $75M
              - CRUZE (Sedan): $85M - $115M
              - TRACKER (SUV): $90M - $130M
              - CAPTIVA (SUV): $130M - $180M
            - **Características:** Amplia red de servicio, repuestos accesibles
            - **Financiación:** Hasta 72 meses, tasas desde 13.5%
            
            #### **RENAULT**
            - **Modelos Elegibles:**
              - LOGAN (Sedan): $55M - $75M
              - SANDERO (Hatchback): $50M - $70M
              - DUSTER (SUV): $80M - $120M
              - STEPWAY (Crossover): $75M - $105M
              - KOLEOS (SUV): $140M - $190M
            - **Características:** Económico en combustible y mantenimiento
            - **Financiación:** Hasta 72 meses, tasas desde 14%
            
            ### 🌟 **MARCAS ASIÁTICAS**
            
            #### **NISSAN**
            - **Modelos Elegibles:**
              - VERSA (Sedan): $60M - $80M
              - MARCH (Hatchback): $55M - $75M
              - SENTRA (Sedan): $85M - $115M
              - KICKS (SUV): $85M - $115M
              - X-TRAIL (SUV): $140M - $200M
            - **Características:** Tecnología avanzada, seguridad
            - **Financiación:** Hasta 72 meses, tasas desde 13.8%
            
            #### **HYUNDAI**
            - **Modelos Elegibles:**
              - ACCENT (Sedan): $55M - $75M
              - i10 (Hatchback): $50M - $65M
              - i20 (Hatchback): $65M - $85M
              - TUCSON (SUV): $120M - $170M
              - SANTA FE (SUV): $200M - $280M
            - **Características:** Garantía extendida, tecnología moderna
            - **Financiación:** Hasta 72 meses, tasas desde 13.5%
            
            #### **KIA**
            - **Modelos Elegibles:**
              - RIO (Hatchback/Sedan): $50M - $70M
              - PICANTO (Hatchback): $45M - $60M
              - CERATO (Sedan): $75M - $105M
              - SPORTAGE (SUV): $110M - $160M
              - SORENTO (SUV): $180M - $250M
            - **Características:** Diseño moderno, garantía competitiva
            - **Financiación:** Hasta 72 meses, tasas desde 14%
            
            ### 🔧 **MARCAS ESPECIALIZADAS**
            
            #### **MAZDA**
            - **Modelos Elegibles:**
              - MAZDA2 (Hatchback): $60M - $80M
              - MAZDA3 (Sedan/Hatchback): $85M - $125M
              - CX-30 (SUV): $110M - $150M
              - CX-5 (SUV): $130M - $180M
              - CX-9 (SUV): $220M - $300M
            - **Características:** Tecnología SkyActiv, diseño premium
            - **Financiación:** Hasta 72 meses, tasas desde 13.8%
            
            #### **FORD**
            - **Modelos Elegibles:**
              - FIESTA (Hatchback): $55M - $75M
              - FOCUS (Hatchback/Sedan): $75M - $105M
              - ESCAPE (SUV): $110M - $150M
              - EXPLORER (SUV): $180M - $250M
              - RANGER (Pickup): $120M - $180M
            - **Características:** Robustez, tecnología Ford SYNC
            - **Financiación:** Hasta 72 meses, tasas desde 14.2%
            
            ## 📊 Criterios de Elegibilidad por Categoría
            
            ### 🏆 **VEHÍCULOS PREMIUM** (>$200M)
            - **Edad máxima:** 4 años
            - **Kilometraje máximo:** 60,000 km
            - **Cuota inicial mínima:** 30%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 12% - 16% EA
            
            ### 🌟 **VEHÍCULOS ESTÁNDAR** ($100M - $200M)
            - **Edad máxima:** 5 años
            - **Kilometraje máximo:** 80,000 km
            - **Cuota inicial mínima:** 25%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 13% - 17% EA
            
            ### 💰 **VEHÍCULOS ECONÓMICOS** ($50M - $100M)
            - **Edad máxima:** 6 años
            - **Kilometraje máximo:** 100,000 km
            - **Cuota inicial mínima:** 20%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 14% - 18% EA
            
            ## 🔍 Factores Especiales de Evaluación
            
            ### ✅ **VENTAJAS POR MARCA**
            - **Toyota/Lexus:** -0.5% tasa por excelente valor de reventa
            - **Chevrolet:** Promociones especiales en modelos nuevos
            - **Renault:** Paquetes de mantenimiento incluidos
            - **Hyundai/Kia:** Garantía extendida transferible
            
            ### ⚠️ **CONSIDERACIONES ESPECIALES**
            - **Vehículos modificados:** Evaluación caso por caso
            - **Vehículos de alta gama:** Avalúo obligatorio
            - **Vehículos comerciales:** Criterios específicos
            - **Vehículos importados:** Documentación adicional requerida
            
            ### 📈 **TENDENCIAS DEL MERCADO** (2024)
            - **Mayor demanda:** SUV compactas y Crossovers
            - **Tecnología valorada:** Sistemas de seguridad activa
            - **Eficiencia:** Motores turbo de baja cilindrada
            - **Conectividad:** Android Auto/Apple CarPlay estándar
            
            ## 🎯 Recomendaciones por Perfil de Cliente
            
            ### 👨‍💼 **EJECUTIVO JOVEN**
            - **Recomendado:** Toyota Corolla, Mazda3, Nissan Sentra
            - **Presupuesto:** $65M - $95M
            - **Características:** Eficiencia, tecnología, imagen profesional
            
            ### 👨‍👩‍👧‍👦 **FAMILIA**
            - **Recomendado:** Toyota RAV4, Chevrolet Captiva, Nissan X-Trail
            - **Presupuesto:** $120M - $180M
            - **Características:** Espacio, seguridad, versatilidad
            
            ### 🏢 **EMPRESA**
            - **Recomendado:** Toyota Hilux, Ford Ranger, Chevrolet Tracker
            - **Presupuesto:** $90M - $170M
            - **Características:** Durabilidad, capacidad de carga, servicio
            
            ### 🌟 **PRIMER VEHÍCULO**
            - **Recomendado:** Chevrolet Spark, Kia Picanto, Renault Sandero
            - **Presupuesto:** $45M - $70M
            - **Características:** Economía, facilidad de manejo, bajo mantenimiento
            
            ---
            
            💡 **Nota:** Todos los precios son referencias del mercado colombiano 2024. 
            Los valores finales pueden variar según año, kilometraje, estado y equipamiento específico.
            
            📞 **Para consultas específicas** sobre un modelo no listado, 
            contacte nuestro departamento de evaluación vehicular.
            """;
    }

    @McpResource(
        uri = "credit://vehicles/brands",
        name = "Authorized Vehicle Brands",
        description = "Lista de marcas de vehículos autorizadas para financiación"
    )
    public String getAuthorizedBrands() {
        log.debug("Proporcionando lista de marcas autorizadas");
        
        return """
            🏷️ **MARCAS AUTORIZADAS PARA CRÉDITO AUTOMOTRIZ**
            
            ## ✅ Marcas Principales
            
            ### 🇯🇵 **Marcas Japonesas**
            1. **TOYOTA** - Líder en confiabilidad
            2. **NISSAN** - Innovación y tecnología
            3. **MAZDA** - Diseño y eficiencia
            4. **HONDA** - Calidad y durabilidad (*)
            5. **MITSUBISHI** - Robustez y versatilidad (*)
            
            ### 🇰🇷 **Marcas Coreanas**
            1. **HYUNDAI** - Garantía extendida y tecnología
            2. **KIA** - Diseño moderno y valor
            
            ### 🇺🇸 **Marcas Americanas**
            1. **CHEVROLET** - Tradición y servicio
            2. **FORD** - Robustez y tecnología
            
            ### 🇫🇷 **Marcas Francesas**
            1. **RENAULT** - Economía y practicidad
            2. **PEUGEOT** - Diseño europeo (*)
            
            ### 🇩🇪 **Marcas Alemanas Premium**
            1. **VOLKSWAGEN** - Calidad alemana (*)
            2. **BMW** - Lujo y deportividad (**)
            3. **MERCEDES-BENZ** - Prestigio y tecnología (**)
            4. **AUDI** - Innovación y diseño (**)
            
            ## 📋 Criterios por Categoría
            
            ### ⭐ **ESTÁNDAR** - Aprobación directa
            - Toyota, Chevrolet, Renault, Nissan, Hyundai, Kia, Mazda, Ford
            - Proceso de evaluación estándar
            - Tasas regulares aplicables
            
            ### ⭐⭐ **PREMIUM** (*) - Evaluación especial
            - Honda, Mitsubishi, Peugeot, Volkswagen
            - Requiere evaluación adicional de valor de reventa
            - Posibles ajustes en tasa según modelo
            
            ### ⭐⭐⭐ **LUJO** (**) - Criterios estrictos
            - BMW, Mercedes-Benz, Audi
            - Monto mínimo de financiación: $150M
            - Cuota inicial mínima: 40%
            - Evaluación crediticia estricta
            - Seguro todo riesgo obligatorio
            
            ## 🚫 Marcas NO Autorizadas
            
            ### ❌ **Restricciones Específicas**
            - **Marcas chinas:** Sin soporte local adecuado
            - **Marcas de nicho:** Lotus, McLaren, Ferrari, etc.
            - **Vehículos eléctricos puros:** En evaluación (próximamente)
            - **Marcas descontinuadas:** Daewoo, Pontiac, Oldsmobile
            
            ## 📞 Excepciones y Casos Especiales
            
            Para vehículos de marcas no listadas o modelos especiales:
            - Contactar departamento de riesgos vehiculares
            - Evaluación caso por caso
            - Posible aprobación con condiciones especiales
            - Tiempo de respuesta: 3-5 días hábiles
            """;
    }

    @McpResource(
        uri = "credit://vehicles/specifications/{brand}",
        name = "Vehicle Brand Specifications",
        description = "Especificaciones detalladas y criterios de financiación por marca específica"
    )
    public String getBrandSpecifications(String brand) {
        log.debug("Proporcionando especificaciones para marca: {}", brand);
        
        if (brand == null || brand.trim().isEmpty()) {
            return "Error: Debe especificar una marca válida.";
        }
        
        String upperBrand = brand.trim().toUpperCase();
        
        return switch (upperBrand) {
            case "TOYOTA" -> getToyotaSpecifications();
            case "CHEVROLET" -> getChevroletSpecifications();
            case "RENAULT" -> getRenaultSpecifications();
            case "NISSAN" -> getNissanSpecifications();
            case "HYUNDAI" -> getHyundaiSpecifications();
            case "KIA" -> getKiaSpecifications();
            case "MAZDA" -> getMazdaSpecifications();
            case "FORD" -> getFordSpecifications();
            default -> String.format("""
                ⚠️ **MARCA NO ENCONTRADA: %s**
                
                Las marcas disponibles son:
                - TOYOTA, CHEVROLET, RENAULT, NISSAN
                - HYUNDAI, KIA, MAZDA, FORD
                
                Para consultar especificaciones de otras marcas,
                contacte nuestro departamento comercial.
                """, brand);
        };
    }

    private String getToyotaSpecifications() {
        return """
            🏆 **ESPECIFICACIONES TOYOTA**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **COROLLA** - Sedan Compacto
            - **Precio:** $65M - $95M
            - **Motor:** 1.8L CVT / 2.0L CVT
            - **Combustible:** 16-18 km/l ciudad
            - **Seguridad:** Toyota Safety Sense 2.0
            - **Garantía:** 3 años/100,000 km
            
            ### **CAMRY** - Sedan Ejecutivo
            - **Precio:** $120M - $180M
            - **Motor:** 2.5L Dynamic Force
            - **Características:** Híbrido disponible
            - **Tecnología:** Multimedia 9" con navegación
            
            ### **RAV4** - SUV Compacta
            - **Precio:** $140M - $220M
            - **Motor:** 2.0L / 2.5L AWD
            - **Tracción:** AWD disponible
            - **Capacidad:** 5 pasajeros, 547L de baúl
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS TOYOTA**
            - Descuento de 0.5% en tasa de interés
            - Excelente valor de reventa (70% a 3 años)
            - Red de servicio más amplia del país
            - Repuestos originales garantizados
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 5 años (6 para Hilux)
            - **Kilometraje máximo:** 100,000 km
            - **Cuota inicial mínima:** 20% (15% para empleados públicos)
            - **Plazo máximo:** 84 meses
            - **Tasa de interés:** 12% - 15.5% EA
            """;
    }

    private String getChevroletSpecifications() {
        return """
            🌟 **ESPECIFICACIONES CHEVROLET**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **SPARK** - Hatchback Urbano
            - **Precio:** $50M - $70M
            - **Motor:** 1.4L DOHC
            - **Tecnología:** MyLink con Android Auto
            - **Eficiencia:** 19 km/l mixto
            
            ### **TRACKER** - SUV Compacta
            - **Precio:** $90M - $130M
            - **Motor:** 1.2L Turbo
            - **Características:** Turbocompresor, 6 airbags
            - **Conectividad:** WiFi hotspot integrado
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS CHEVROLET**
            - Promociones especiales mensuales
            - Mantenimiento programado incluido (1 año)
            - Red de servicio autorizada amplia
            - Garantía OnStar de emergencia
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 6 años
            - **Kilometraje máximo:** 100,000 km
            - **Cuota inicial mínima:** 20%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 13.5% - 17% EA
            """;
    }

    private String getRenaultSpecifications() {
        return """
            🔧 **ESPECIFICACIONES RENAULT**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **SANDERO** - Hatchback Económico
            - **Precio:** $50M - $70M
            - **Motor:** 1.0L SCe / 1.6L 16V
            - **Espacio:** 5 puertas, amplio interior
            - **Economía:** Bajo costo de mantenimiento
            
            ### **DUSTER** - SUV Robusta
            - **Precio:** $80M - $120M
            - **Motor:** 1.6L / 2.0L 4x4
            - **Capacidad:** 445L de baúl
            - **Robustez:** Diseñada para todo terreno
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS RENAULT**
            - Paquetes de mantenimiento incluidos
            - Excelente relación precio-valor
            - Repuestos económicos y disponibles
            - Garantía extendida opcional
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 6 años
            - **Kilometraje máximo:** 100,000 km
            - **Cuota inicial mínima:** 20%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 14% - 17.5% EA
            """;
    }

    private String getNissanSpecifications() {
        return """
            ⚡ **ESPECIFICACIONES NISSAN**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **VERSA** - Sedan Compacto
            - **Precio:** $60M - $80M
            - **Motor:** 1.6L CVT
            - **Tecnología:** NissanConnect con navegación
            - **Confort:** Climatización automática
            
            ### **KICKS** - Crossover Urbana
            - **Precio:** $85M - $115M
            - **Motor:** 1.6L HR16
            - **Diseño:** Look SUV, manejo de auto
            - **Tecnología:** Around View Monitor
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS NISSAN**
            - Tecnología ProPILOT en modelos superiores
            - Programa de mantenimiento Nissan Care
            - Garantía extendida hasta 7 años
            - Sistema de seguridad Nissan Safety Shield
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 5 años
            - **Kilometraje máximo:** 90,000 km
            - **Cuota inicial mínima:** 22%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 13.8% - 16.5% EA
            """;
    }

    private String getHyundaiSpecifications() {
        return """
            🚀 **ESPECIFICACIONES HYUNDAI**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **ACCENT** - Sedan Confiable
            - **Precio:** $55M - $75M
            - **Motor:** 1.4L MPI / 1.6L GDI
            - **Garantía:** 5 años/150,000 km
            - **Tecnología:** Display touchscreen 8"
            
            ### **TUCSON** - SUV Moderna
            - **Precio:** $120M - $170M
            - **Motor:** 2.0L Nu MPI / 1.6L Turbo
            - **Diseño:** Lenguaje Sensuous Sportiness
            - **Seguridad:** Hyundai SmartSense
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS HYUNDAI**
            - Garantía más larga del mercado
            - Programa de mantenimiento incluido
            - Asistencia en carretera 24/7
            - Blue Link conectividad
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 5 años
            - **Kilometraje máximo:** 100,000 km
            - **Cuota inicial mínima:** 20%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 13.5% - 16.8% EA
            """;
    }

    private String getKiaSpecifications() {
        return """
            🎨 **ESPECIFICACIONES KIA**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **RIO** - Hatchback Dinámico
            - **Precio:** $50M - $70M
            - **Motor:** 1.4L MPI / 1.0L T-GDI
            - **Diseño:** Tiger Nose signature
            - **Tecnología:** UVO Connect
            
            ### **SPORTAGE** - SUV Deportiva
            - **Precio:** $110M - $160M
            - **Motor:** 1.6L T-GDI / 2.0L MPI
            - **Capacidad:** AWD inteligente
            - **Estilo:** Diseño bold y moderno
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS KIA**
            - Garantía 7 años/150,000 km
            - Programa Kia Care mantenimiento
            - Diseño galardonado internacionalmente
            - UVO telemática avanzada
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 5 años
            - **Kilometraje máximo:** 100,000 km
            - **Cuota inicial mínima:** 22%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 14% - 17% EA
            """;
    }

    private String getMazdaSpecifications() {
        return """
            🏎️ **ESPECIFICACIONES MAZDA**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **MAZDA3** - Sedan/Hatchback Premium
            - **Precio:** $85M - $125M
            - **Motor:** 2.0L SKYACTIV-G / 2.5L SKYACTIV-G
            - **Tecnología:** i-ACTIVSENSE safety
            - **Diseño:** KODO - Soul of Motion
            
            ### **CX-5** - SUV Refinada
            - **Precio:** $130M - $180M
            - **Motor:** 2.0L / 2.5L SKYACTIV-G
            - **Tracción:** i-ACTIV AWD
            - **Interior:** Materiales premium
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS MAZDA**
            - Tecnología SKYACTIV eficiencia superior
            - Diseño premiado internacionalmente
            - i-ACTIVSENSE seguridad estándar
            - Conectividad Mazda Connect
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 5 años
            - **Kilometraje máximo:** 90,000 km
            - **Cuota inicial mínima:** 25%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 13.8% - 16.5% EA
            """;
    }

    private String getFordSpecifications() {
        return """
            🛻 **ESPECIFICACIONES FORD**
            
            ## 🚗 Modelos y Rangos de Precio
            
            ### **FIESTA** - Hatchback Ágil
            - **Precio:** $55M - $75M
            - **Motor:** 1.6L Ti-VCT
            - **Tecnología:** SYNC 3 con Apple CarPlay
            - **Manejo:** Suspensión deportiva
            
            ### **RANGER** - Pickup Robusta
            - **Precio:** $120M - $180M
            - **Motor:** 2.2L TDCi / 3.2L TDCi
            - **Capacidad:** 1 tonelada de carga
            - **Tracción:** 4x4 con diferencial
            
            ## 💰 Condiciones de Financiación
            
            ### ✅ **VENTAJAS FORD**
            - Robustez y durabilidad comprobada
            - Tecnología SYNC conectividad
            - Red de servicio especializada
            - Programas corporativos disponibles
            
            ### 📋 **CRITERIOS ESPECÍFICOS**
            - **Edad máxima:** 6 años (8 años para Ranger)
            - **Kilometraje máximo:** 120,000 km
            - **Cuota inicial mínima:** 25%
            - **Plazo máximo:** 72 meses
            - **Tasa de interés:** 14.2% - 17.5% EA
            """;
    }
}