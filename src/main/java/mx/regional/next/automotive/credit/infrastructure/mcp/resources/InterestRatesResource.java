package mx.regional.next.automotive.credit.infrastructure.mcp.resources;

import org.springframework.ai.mcp.server.annotation.McpResource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InterestRatesResource {
    
    private static final Logger log = LoggerFactory.getLogger(InterestRatesResource.class);

    @McpResource(
        uri = "credit://interest-rates",
        name = "Interest Rates Table",
        description = "Tabla completa de tasas de interés para crédito automotriz según perfil crediticio y condiciones"
    )
    public String getInterestRates() {
        log.debug("Proporcionando tabla de tasas de interés");
        
        return """
            📈 **TABLA DE TASAS DE INTERÉS - CRÉDITO AUTOMOTRIZ**
            
            ## 🎯 Tasas Base por Score Crediticio
            
            ### 🏆 **EXCELENTE** (Score 750+)
            - **Tasa Base:** 12.0% - 14.0% EA
            - **Perfil:** Historial crediticio impecable
            - **Requisitos:** Sin reportes negativos últimos 2 años
            - **Beneficios:** 
              - Aprobación express (24 horas)
              - Hasta 90% financiación
              - Plazo hasta 84 meses
              - Sin comisión de estudio
            
            ### ⭐ **MUY BUENO** (Score 650-749)
            - **Tasa Base:** 14.0% - 16.5% EA
            - **Perfil:** Buen comportamiento crediticio
            - **Requisitos:** Máximo 2 reportes menores último año
            - **Beneficios:**
              - Aprobación en 48 horas
              - Hasta 85% financiación
              - Plazo hasta 72 meses
              - Comisión de estudio reducida (0.5%)
            
            ### 🌟 **BUENO** (Score 550-649)
            - **Tasa Base:** 16.5% - 19.0% EA
            - **Perfil:** Historial crediticio regular
            - **Requisitos:** Reportes saldados, ingresos demostrables
            - **Beneficios:**
              - Aprobación en 72 horas
              - Hasta 80% financiación
              - Plazo hasta 60 meses
              - Comisión de estudio estándar (1.0%)
            
            ### ⚠️ **REGULAR** (Score 450-549)
            - **Tasa Base:** 19.0% - 22.0% EA
            - **Perfil:** Historial con observaciones
            - **Requisitos:** Codeudor o garantías adicionales
            - **Condiciones:**
              - Evaluación caso por caso
              - Hasta 70% financiación
              - Plazo hasta 48 meses
              - Cuota inicial mínima 30%
            
            ### 🔍 **EVALUACIÓN ESPECIAL** (Score <450)
            - **Tasa Base:** 22.0% - 25.0% EA
            - **Perfil:** Requiere reestructuración crediticia
            - **Requisitos:** Codeudor con score >650, garantías
            - **Condiciones:**
              - Comité de crédito especializado
              - Hasta 60% financiación
              - Plazo máximo 36 meses
              - Cuota inicial mínima 40%
            
            ## 🎯 Ajustes por Características del Cliente
            
            ### 👔 **TIPO DE CLIENTE**
            
            #### **Empleado Público**
            - **Descuento:** -1.0% sobre tasa base
            - **Requisitos:** Certificado laboral vigente
            - **Beneficios:** Descuento por nómina automático
            - **Plazo especial:** Hasta 84 meses
            
            #### **Empleado Privado - Empresa Grande**
            - **Descuento:** -0.5% sobre tasa base
            - **Requisitos:** Empresa con >500 empleados
            - **Convenios:** Descuentos adicionales por convenio corporativo
            
            #### **Profesional Independiente**
            - **Ajuste:** +0.5% sobre tasa base
            - **Requisitos:** Declaración de renta últimos 2 años
            - **Garantías:** Certificados de ingresos adicionales
            
            #### **Persona Jurídica**
            - **Descuento:** -0.5% sobre tasa base
            - **Requisitos:** Estados financieros auditados
            - **Beneficios:** Tratamiento fiscal preferencial
            
            ### 🚗 **TIPO DE VEHÍCULO**
            
            #### **Vehículo Nuevo (0 km)**
            - **Descuento:** -0.5% sobre tasa base
            - **Garantía:** Total del fabricante
            - **Financiación:** Hasta 90% del valor
            - **Plazo:** Hasta 84 meses
            
            #### **Vehículo Seminuevo (1-2 años)**
            - **Tasa:** Base sin ajustes
            - **Condiciones:** Evaluación técnica obligatoria
            - **Financiación:** Hasta 85% del avalúo
            
            #### **Vehículo Usado (3-6 años)**
            - **Ajuste:** +0.5% - 1.0% según edad
            - **Evaluación:** Peritaje técnico requerido
            - **Financiación:** Hasta 80% del avalúo
            
            ## 💰 Ajustes por Monto y Plazo
            
            ### 🎯 **POR MONTO FINANCIADO**
            
            #### **Monto Alto** (>$150M)
            - **Descuento:** -0.3% sobre tasa base
            - **Requisitos:** Evaluación patrimonial
            - **Beneficios:** Condiciones preferenciales
            
            #### **Monto Medio** ($75M - $150M)
            - **Tasa:** Base sin ajustes
            - **Condiciones:** Evaluación estándar
            
            #### **Monto Bajo** (<$75M)
            - **Ajuste:** +0.2% sobre tasa base
            - **Motivo:** Costos administrativos proporcionalmente mayores
            
            ### ⏱️ **POR PLAZO DE FINANCIACIÓN**
            
            #### **12-24 meses**
            - **Descuento:** -1.5% sobre tasa base
            - **Beneficio:** Menor riesgo crediticio
            
            #### **25-48 meses**
            - **Descuento:** -0.5% sobre tasa base
            - **Condiciones:** Plazo estándar
            
            #### **49-60 meses**
            - **Tasa:** Base sin ajustes
            - **Equilibrio:** Riesgo-beneficio balanceado
            
            #### **61-72 meses**
            - **Ajuste:** +0.5% sobre tasa base
            - **Consideración:** Mayor exposición al riesgo
            
            #### **73-84 meses**
            - **Ajuste:** +1.0% sobre tasa base
            - **Restricciones:** Solo para scores >650
            
            ## 🏅 Programas Especiales
            
            ### 🎓 **PROGRAMA UNIVERSITARIOS**
            - **Beneficiarios:** Recién graduados (<2 años)
            - **Descuento:** -1.0% sobre tasa base
            - **Requisitos:** Título profesional, primer empleo formal
            - **Plazo gracia:** 6 meses para primer pago
            
            ### 👩‍⚕️ **PROGRAMA PROFESIONALES DE LA SALUD**
            - **Beneficiarios:** Médicos, enfermeras, terapeutas
            - **Descuento:** -0.8% sobre tasa base
            - **Convenios:** Con colegios profesionales
            - **Flexibilidad:** Pagos ajustados a ingresos variables
            
            ### 🏭 **PROGRAMA EMPRESARIAL**
            - **Beneficiarios:** Flotas empresariales (>3 vehículos)
            - **Descuento:** -1.5% sobre tasa base
            - **Beneficios:** Gestión centralizada, facturación única
            - **Servicios:** Mantenimiento y seguros corporativos
            
            ### 🌱 **PROGRAMA VEHÍCULOS ECOLÓGICOS**
            - **Beneficiarios:** Híbridos, eléctricos, GNV
            - **Descuento:** -2.0% sobre tasa base
            - **Incentivo:** Promoción de movilidad sostenible
            - **Alianzas:** Con fabricantes eco-friendly
            
            ## 📊 Simulación de Tasas Efectivas
            
            ### Ejemplo: Cliente Score 700, Empleado Público, Toyota Corolla Nuevo
            
            **Cálculo de Tasa:**
            - Tasa base score 700: 15.0%
            - Descuento empleado público: -1.0%
            - Descuento vehículo nuevo: -0.5%
            - **Tasa final: 13.5% EA**
            
            **Simulación Crédito $80M a 60 meses:**
            - Cuota mensual: $1,823,000
            - Total a pagar: $109,380,000
            - Total intereses: $29,380,000
            
            ## ⚠️ Consideraciones Importantes
            
            ### 📋 **Condiciones Generales**
            - Tasas sujetas a cambios según condiciones del mercado
            - Evaluación crediticia individual para cada caso
            - Seguros de vida y todo riesgo obligatorios
            - Comisiones adicionales según producto elegido
            
            ### 🔄 **Actualizaciones de Tasas**
            - Revisión mensual según indicadores económicos
            - DTF, IBR, inflación como referencias
            - Notificación previa de cambios a clientes en proceso
            
            ### 📞 **Consultas Personalizadas**
            Para simulaciones específicas o consultas sobre tasas preferenciales,
            contacte nuestro equipo comercial especializado.
            """;
    }

    @McpResource(
        uri = "credit://interest-rates/calculator",
        name = "Interest Rate Calculator",
        description = "Calculadora interactiva de tasas de interés según parámetros específicos"
    )
    public String getRateCalculator() {
        log.debug("Proporcionando calculadora de tasas");
        
        return """
            🧮 **CALCULADORA DE TASAS DE INTERÉS**
            
            ## 📋 Parámetros de Cálculo
            
            ### 👤 **1. PERFIL DEL CLIENTE**
            
            #### **Score Crediticio** (Obligatorio)
            ```
            [ ] 750+ puntos    → Tasa base: 12.0% - 14.0%
            [ ] 650-749 puntos → Tasa base: 14.0% - 16.5%
            [ ] 550-649 puntos → Tasa base: 16.5% - 19.0%
            [ ] 450-549 puntos → Tasa base: 19.0% - 22.0%
            [ ] <450 puntos    → Tasa base: 22.0% - 25.0%
            ```
            
            #### **Tipo de Cliente**
            ```
            [ ] Empleado Público        → Descuento: -1.0%
            [ ] Empleado Privado Grande → Descuento: -0.5%
            [ ] Empleado Privado Pyme   → Sin ajuste: 0.0%
            [ ] Independiente           → Ajuste: +0.5%
            [ ] Persona Jurídica        → Descuento: -0.5%
            ```
            
            ### 🚗 **2. CARACTERÍSTICAS DEL VEHÍCULO**
            
            #### **Edad del Vehículo**
            ```
            [ ] Nuevo (0 km)      → Descuento: -0.5%
            [ ] Seminuevo (1-2 años) → Sin ajuste: 0.0%
            [ ] Usado (3-4 años)  → Ajuste: +0.5%
            [ ] Usado (5-6 años)  → Ajuste: +1.0%
            ```
            
            #### **Marca del Vehículo**
            ```
            [ ] Toyota/Lexus   → Descuento: -0.5%
            [ ] Premium (BMW, Mercedes) → Evaluación especial
            [ ] Otras marcas autorizadas → Sin ajuste: 0.0%
            ```
            
            ### 💰 **3. CONDICIONES DE FINANCIACIÓN**
            
            #### **Monto a Financiar**
            ```
            [ ] >$150M         → Descuento: -0.3%
            [ ] $75M - $150M   → Sin ajuste: 0.0%
            [ ] <$75M          → Ajuste: +0.2%
            ```
            
            #### **Plazo de Financiación**
            ```
            [ ] 12-24 meses    → Descuento: -1.5%
            [ ] 25-48 meses    → Descuento: -0.5%
            [ ] 49-60 meses    → Sin ajuste: 0.0%
            [ ] 61-72 meses    → Ajuste: +0.5%
            [ ] 73-84 meses    → Ajuste: +1.0%
            ```
            
            ## 🎯 Ejemplo de Cálculo Paso a Paso
            
            ### **Caso: Juan Pérez**
            
            **Datos del Cliente:**
            - Score crediticio: 680 puntos
            - Empleado público con 5 años de antigüedad
            - Ingresos mensuales: $4,500,000
            
            **Vehículo Deseado:**
            - Toyota Corolla Cross 2024 (nuevo)
            - Valor: $95,000,000
            - Monto a financiar: $76,000,000 (cuota inicial 20%)
            - Plazo deseado: 60 meses
            
            **Cálculo de Tasa:**
            ```
            1. Tasa base (score 680):           15.5%
            2. Descuento empleado público:      -1.0%
            3. Descuento vehículo nuevo:        -0.5%
            4. Descuento marca Toyota:          -0.5%
            5. Sin ajuste por monto ni plazo:    0.0%
            ────────────────────────────────────────
            TASA FINAL:                        13.5% EA
            ```
            
            **Simulación Financiera:**
            ```
            Monto financiado:      $76,000,000
            Tasa de interés:       13.5% EA (1.06% mensual)
            Plazo:                 60 meses
            ────────────────────────────────────────
            Cuota mensual:         $1,731,458
            Total a pagar:         $103,887,480
            Total intereses:       $27,887,480
            Costo financiero:      36.7% del monto
            ```
            
            ## 📊 Tabla de Simulaciones Rápidas
            
            ### **Crédito $50M a 60 meses por Score**
            
            | Score | Tasa | Cuota Mensual | Total Intereses |
            |-------|------|---------------|-----------------|
            | 750+  | 13.0%| $1,139,284   | $18,357,040    |
            | 700   | 15.0%| $1,186,193   | $21,171,580    |
            | 650   | 17.0%| $1,234,847   | $24,090,820    |
            | 600   | 19.0%| $1,285,183   | $27,110,980    |
            | 550   | 21.0%| $1,337,134   | $30,228,040    |
            
            ### **Crédito $100M por Plazo (Score 700, Tasa 15%)**
            
            | Plazo | Cuota Mensual | Total Intereses |
            |-------|---------------|-----------------|
            | 36m   | $3,466,908   | $24,808,688    |
            | 48m   | $2,784,559   | $33,658,832    |
            | 60m   | $2,372,386   | $42,343,160    |
            | 72m   | $2,099,647   | $51,174,984    |
            
            ## ⚙️ Herramientas de Cálculo
            
            ### 🔢 **Fórmulas Utilizadas**
            
            **Cuota Mensual (Sistema Francés):**
            ```
            PMT = PV × [r(1+r)^n] / [(1+r)^n - 1]
            
            Donde:
            PMT = Cuota mensual
            PV  = Valor presente (monto del crédito)
            r   = Tasa de interés mensual
            n   = Número de cuotas
            ```
            
            **Conversión Tasa EA a Mensual:**
            ```
            r_mensual = (1 + r_anual)^(1/12) - 1
            ```
            
            ### 📱 **Aplicaciones Recomendadas**
            
            Para cálculos más detallados, puede usar:
            - Calculadora financiera HP 12C
            - Excel con función PMT
            - App móvil "Crédito Automotriz Simulador"
            - Portal web del banco (simulador online)
            
            ## 💡 Consejos para Mejores Tasas
            
            ### ✅ **Optimización de Tasa**
            
            1. **Mejore su score crediticio**
               - Pague todas las obligaciones a tiempo
               - Mantenga baja utilización de tarjetas de crédito
               - No solicite múltiples créditos simultáneamente
            
            2. **Elija el plazo adecuado**
               - Plazos cortos = menores tasas, mayores cuotas
               - Evalúe su capacidad de pago mensual
               - Considere pagos extraordinarios para reducir plazo
            
            3. **Aproveche programas especiales**
               - Convenios corporativos
               - Programas profesionales
               - Promociones estacionales
            
            4. **Negocie condiciones**
               - Compare con otras entidades
               - Presente ofertas competitivas
               - Considere productos adicionales (cuentas, seguros)
            
            ---
            
            📞 **Para cálculos personalizados**, contacte nuestro equipo comercial 
            con sus datos específicos para obtener una cotización exacta.
            """;
    }

    @McpResource(
        uri = "credit://interest-rates/comparison",
        name = "Market Rate Comparison",
        description = "Comparación de tasas de interés del mercado automotriz colombiano"
    )
    public String getMarketComparison() {
        log.debug("Proporcionando comparación de tasas del mercado");
        
        return """
            📊 **COMPARACIÓN TASAS MERCADO AUTOMOTRIZ COLOMBIANO**
            
            ## 🏦 Tasas por Entidad Financiera (Enero 2024)
            
            ### 🏆 **BANCOS TRADICIONALES**
            
            #### **Banco de Bogotá**
            - **Tasa:** 14.5% - 21.0% EA
            - **Especialidad:** Créditos corporativos
            - **Ventajas:** Red de oficinas amplia
            - **Requisitos:** Score mínimo 550
            
            #### **Bancolombia**
            - **Tasa:** 13.8% - 20.5% EA
            - **Especialidad:** Crédito digital
            - **Ventajas:** Aprobación online rápida
            - **Requisitos:** Score mínimo 580
            
            #### **Banco Popular**
            - **Tasa:** 14.0% - 19.8% EA
            - **Especialidad:** Empleados públicos
            - **Ventajas:** Descuentos por nómina
            - **Requisitos:** Score mínimo 520
            
            #### **BBVA Colombia**
            - **Tasa:** 14.2% - 21.5% EA
            - **Especialidad:** Banca premium
            - **Ventajas:** Condiciones preferenciales VIP
            - **Requisitos:** Score mínimo 600
            
            ### 🚗 **FINANCIERAS ESPECIALIZADAS**
            
            #### **Toyota Financial Services**
            - **Tasa:** 12.0% - 18.0% EA
            - **Especialidad:** Solo vehículos Toyota
            - **Ventajas:** Tasas preferenciales marca
            - **Requisitos:** Score mínimo 500
            
            #### **GM Financial**
            - **Tasa:** 13.5% - 19.5% EA
            - **Especialidad:** Solo vehículos Chevrolet
            - **Ventajas:** Promociones de fábrica
            - **Requisitos:** Score mínimo 480
            
            #### **RCI Banque (Renault)**
            - **Tasa:** 14.8% - 22.0% EA
            - **Especialidad:** Vehículos Renault
            - **Ventajas:** Flexibilidad en documentación
            - **Requisitos:** Score mínimo 450
            
            ### 🏢 **COOPERATIVAS Y FONDOS**
            
            #### **Cooperativa Colpatria**
            - **Tasa:** 13.0% - 18.5% EA
            - **Especialidad:** Asociados sector financiero
            - **Ventajas:** Tasas muy competitivas
            - **Requisitos:** Membresía cooperativa
            
            #### **Fondo Nacional del Ahorro**
            - **Tasa:** 11.5% - 16.0% EA
            - **Especialidad:** Empleados públicos
            - **Ventajas:** Mejores tasas del mercado
            - **Requisitos:** Afiliación FNA
            
            ## 📈 Comparación por Segmento de Cliente
            
            ### 👔 **EMPLEADOS PÚBLICOS**
            
            | Entidad | Tasa Mínima | Tasa Máxima | Plazo Max | Financiación |
            |---------|-------------|-------------|-----------|--------------|
            | **FNA** | 11.5% | 16.0% | 84 meses | 90% |
            | Coop. Colpatria | 13.0% | 18.5% | 72 meses | 85% |
            | Banco Popular | 13.5% | 18.0% | 72 meses | 85% |
            | **NUESTRA ENTIDAD** | **12.0%** | **17.0%** | **84 meses** | **90%** |
            
            ### 🏢 **EMPLEADOS PRIVADOS**
            
            | Entidad | Tasa Mínima | Tasa Máxima | Plazo Max | Financiación |
            |---------|-------------|-------------|-----------|--------------|
            | Bancolombia | 13.8% | 20.5% | 72 meses | 80% |
            | Banco de Bogotá | 14.5% | 21.0% | 72 meses | 80% |
            | Toyota Financial | 12.0% | 18.0% | 72 meses | 85% |
            | **NUESTRA ENTIDAD** | **13.0%** | **19.0%** | **72 meses** | **85%** |
            
            ### 💼 **PROFESIONALES INDEPENDIENTES**
            
            | Entidad | Tasa Mínima | Tasa Máxima | Requisitos Adicionales |
            |---------|-------------|-------------|------------------------|
            | BBVA | 15.0% | 22.0% | Declaración renta 2 años |
            | Bancolombia | 14.5% | 21.5% | Estados financieros |
            | GM Financial | 16.0% | 22.5% | Certificación ingresos |
            | **NUESTRA ENTIDAD** | **14.0%** | **20.0%** | **Declaración renta 1 año** |
            
            ## 🎯 Ventajas Competitivas
            
            ### ✅ **FORTALEZAS DE NUESTRA ENTIDAD**
            
            #### **1. Tasas Competitivas**
            - Score 750+: **12.0%** vs. mercado 13.5% promedio
            - Empleados públicos: **Desde 11.0%** con descuentos
            - Vehículos nuevos: **Descuento adicional 0.5%**
            
            #### **2. Flexibilidad en Aprobación**
            - Score mínimo: **400 puntos** (vs. 500+ competencia)
            - Evaluación integral: Más allá del score
            - Segundas oportunidades: Programa de reestructuración
            
            #### **3. Condiciones Superiores**
            - Financiación hasta: **90%** del valor
            - Plazo máximo: **84 meses** (todos los perfiles)
            - Sin comisión de estudio: Score >650
            - Seguro de vida: **Incluido primer año**
            
            #### **4. Proceso Eficiente**
            - Aprobación: **24-48 horas**
            - Documentación: **Mínima requerida**
            - Desembolso: **48 horas post-aprobación**
            - Atención: **Asesores especializados**
            
            ### ⚡ **INNOVACIONES EXCLUSIVAS**
            
            #### **Programa "Primer Vehículo"**
            - Para clientes sin historial automotriz
            - Tasa especial: -1% descuento adicional
            - Acompañamiento personalizado
            
            #### **Tasa Variable Voluntaria**
            - Opción de migrar a tasa DTF + margen
            - Reducción potencial cuando bajen las tasas
            - Sin penalizaciones por cambio
            
            #### **Cuotas Flexibles**
            - Meses de gracia en enero (prima navideña)
            - Cuotas estacionales para independientes
            - Pagos extraordinarios sin penalización
            
            ## 📊 Ranking de Tasas (Score 650, Empleado Privado, 60 meses)
            
            ### 🥇 **TOP 5 MEJORES TASAS**
            
            1. **NUESTRA ENTIDAD:** 14.5% EA
            2. Toyota Financial: 15.0% EA (solo Toyota)
            3. Coop. Colpatria: 15.2% EA (solo asociados)
            4. Bancolombia: 15.8% EA
            5. GM Financial: 16.0% EA (solo Chevrolet)
            
            ### ⚠️ **CONSIDERACIONES IMPORTANTES**
            
            #### **Más Allá de la Tasa**
            - **Comisiones:** Algunas entidades cobran hasta 2% estudio
            - **Seguros:** Obligatorios, varían en costo y cobertura
            - **Flexibilidad:** Pagos anticipados, cambios de fecha
            - **Servicio:** Calidad de atención y resolución de problemas
            
            #### **Costos Ocultos a Evaluar**
            ```
            📋 Checklist de Comparación:
            [ ] Tasa de interés efectiva anual
            [ ] Comisión de estudio
            [ ] Costo del seguro obligatorio
            [ ] Penalizaciones por pago anticipado
            [ ] Flexibilidad para cambios
            [ ] Calidad del servicio al cliente
            [ ] Red de oficinas para trámites
            ```
            
            ## 🎯 Recomendaciones por Perfil
            
            ### **Score Excelente (750+)**
            ✅ **Nuestra Entidad** - Mejores tasas sin restricciones
            ✅ Toyota Financial - Si compra Toyota
            ✅ FNA - Si es empleado público
            
            ### **Score Bueno (600-749)**
            ✅ **Nuestra Entidad** - Balance entre tasa y flexibilidad
            ✅ Bancolombia - Proceso digital ágil
            ✅ Cooperativas - Si es elegible
            
            ### **Score Regular (450-599)**
            ✅ **Nuestra Entidad** - Mayor probabilidad de aprobación
            ✅ RCI Banque - Más flexible en requisitos
            ⚠️ Evitar: Bancos tradicionales (tasas muy altas)
            
            ---
            
            💡 **Consejo:** Siempre compare el **costo total** del crédito, 
            no solo la tasa de interés. Una tasa menor con comisiones altas 
            puede resultar más costosa que una tasa ligeramente mayor sin comisiones.
            """;
    }
}