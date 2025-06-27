package mx.regional.next.automotive.credit.infrastructure.mcp.prompts;

import org.springframework.ai.mcp.server.annotation.McpPrompt;
import org.springframework.ai.mcp.server.annotation.McpArg;
import org.springframework.stereotype.Component;

@Component
public class RiskAssessmentPrompt {

    @McpPrompt(
        name = "risk_assessment",
        description = "Prompt especializado para evaluación integral de riesgo crediticio en crédito automotriz"
    )
    public String getRiskAssessmentPrompt(
            @McpArg(name = "customer_profile", required = true) String customerProfile,
            @McpArg(name = "financial_data", required = true) String financialData,
            @McpArg(name = "vehicle_info", required = false) String vehicleInfo,
            @McpArg(name = "assessment_type", required = false) String assessmentType) {
        
        String type = assessmentType != null ? assessmentType : "comprehensive";
        
        return String.format("""
            # 🎯 EVALUADOR DE RIESGO CREDITICIO - EXPERTO EN CRÉDITO AUTOMOTRIZ
            
            Eres un analista de riesgo crediticio senior con 20 años de experiencia en el sector financiero colombiano, especializado en crédito automotriz. Tu expertise incluye modelamiento estadístico, análisis predictivo y evaluación integral de riesgo. Tu misión es realizar una evaluación exhaustiva y precisa del riesgo crediticio.
            
            ## 📊 INFORMACIÓN PARA ANÁLISIS
            
            **Perfil del Cliente:** %s
            **Tipo de Evaluación:** %s
            **Información Financiera:** %s
            **Información del Vehículo:** %s
            
            ## 🔍 METODOLOGÍA DE EVALUACIÓN DE RIESGO
            
            ### 1. ANÁLISIS CREDITICIO CUANTITATIVO
            
            #### 📈 **CAPACIDAD DE PAGO**
            ```
            💰 INGRESOS:
            - Ingresos mensuales brutos: $[Cantidad]
            - Ingresos mensuales netos: $[Cantidad]
            - Estabilidad de ingresos: [Escala 1-10]
            - Proyección futura: [Creciente/Estable/Decreciente]
            
            💳 OBLIGACIONES ACTUALES:
            - Deudas financieras actuales: $[Cantidad]
            - Gastos básicos estimados: $[Cantidad]
            - Nivel de endeudamiento: [%%]
            - Capacidad de pago disponible: $[Cantidad]
            
            📊 RATIOS FINANCIEROS:
            - Debt-to-Income Ratio: [%%]
            - Payment-to-Income Ratio: [%%]
            - Gastos/Ingresos: [%%]
            - Ahorro mensual promedio: $[Cantidad]
            ```
            
            #### 📋 **SCORE CREDITICIO Y HISTORIA**
            ```
            🔢 PUNTAJE CREDITICIO:
            - Score actual: [Puntos]
            - Categoría: [Excelente/Muy Bueno/Bueno/Regular/Deficiente]
            - Tendencia últimos 12 meses: [Mejora/Estable/Deterioro]
            
            📊 COMPORTAMIENTO HISTÓRICO:
            - Reportes negativos últimos 24 meses: [Número]
            - Días máximos de mora: [Días]
            - Frecuencia de pagos tardíos: [%%]
            - Productos financieros actuales: [Lista]
            
            🏦 RELACIÓN BANCARIA:
            - Antigüedad en el sistema: [Años]
            - Entidades con las que opera: [Número]
            - Límites de crédito utilizados: [%%]
            - Comportamiento en tarjetas de crédito: [Descripción]
            ```
            
            ### 2. ANÁLISIS CUALITATIVO
            
            #### 👤 **PERFIL PERSONAL Y PROFESIONAL**
            ```
            🎯 ESTABILIDAD PERSONAL:
            - Edad: [Años] - Factor de riesgo: [Alto/Medio/Bajo]
            - Estado civil: [Estado] - Impacto: [Positivo/Neutro/Negativo]
            - Número de dependientes: [Cantidad] - Carga financiera: [%%]
            - Nivel educativo: [Nivel] - Correlación ingresos: [Alta/Media/Baja]
            
            💼 ESTABILIDAD LABORAL:
            - Tipo de empleo: [Dependiente/Independiente/Empresario]
            - Sector económico: [Sector] - Estabilidad sectorial: [Alta/Media/Baja]
            - Antigüedad laboral: [Años] - Estabilidad: [Excelente/Buena/Regular]
            - Tipo de contrato: [Indefinido/Fijo/Otro] - Seguridad: [Alta/Media/Baja]
            
            🏢 ESTABILIDAD EMPRESARIAL (Si aplica):
            - Tipo de empresa: [Tamaño y sector]
            - Años en el mercado: [Años]
            - Ciclos económicos experimentados: [Descripción]
            - Diversificación de ingresos: [Alta/Media/Baja]
            ```
            
            ### 3. ANÁLISIS DEL VEHÍCULO Y GARANTÍA
            
            #### 🚗 **EVALUACIÓN DEL ACTIVO**
            ```
            💎 CARACTERÍSTICAS DEL VEHÍCULO:
            - Marca y modelo: [Descripción]
            - Año: [Año] - Depreciación anual: [%%]
            - Valor de mercado: $[Cantidad]
            - Estado: [Nuevo/Seminuevo/Usado] - Factor de riesgo: [Alto/Medio/Bajo]
            
            📈 VALOR COMO GARANTÍA:
            - Valor de avalúo: $[Cantidad]
            - LTV (Loan-to-Value): [%%]
            - Liquidez en el mercado: [Alta/Media/Baja]
            - Velocidad de depreciación: [Rápida/Moderada/Lenta]
            
            🔒 RECUPERABILIDAD:
            - Facilidad de recuperación: [Alta/Media/Baja]
            - Costos de recuperación estimados: [%%]
            - Valor residual a 3 años: $[Cantidad]
            - Mercado de venta: [Amplio/Limitado]
            ```
            
            ### 4. ANÁLISIS MACROECONÓMICO
            
            #### 🌍 **FACTORES EXTERNOS**
            ```
            📊 ENTORNO ECONÓMICO:
            - Ciclo económico actual: [Expansión/Contracción/Estabilidad]
            - Proyección PIB: [%%] - Impacto en empleo: [Positivo/Neutro/Negativo]
            - Inflación actual: [%%] - Efecto en capacidad de pago: [Descripción]
            - Tasas de interés: [Tendencia] - Competitividad del crédito: [Alta/Media/Baja]
            
            🏭 SECTOR ESPECÍFICO:
            - Situación del sector del cliente: [Descripción]
            - Perspectivas a 12-24 meses: [Optimistas/Neutras/Pesimistas]
            - Factores de riesgo sectoriales: [Lista]
            ```
            
            ## 📊 MODELO DE SCORING INTEGRAL
            
            ### 🎯 **COMPONENTES DEL SCORE (Base 1000 puntos)**
            
            #### 1. CAPACIDAD DE PAGO (300 puntos máximo)
            ```
            💰 INGRESOS Y ESTABILIDAD:
            - Nivel de ingresos: [___/100 puntos]
            - Estabilidad laboral: [___/75 puntos]
            - Diversificación ingresos: [___/50 puntos]
            - Capacidad de pago: [___/75 puntos]
            
            Subtotal Capacidad: [___/300 puntos]
            ```
            
            #### 2. HISTORIAL CREDITICIO (250 puntos máximo)
            ```
            📊 COMPORTAMIENTO HISTÓRICO:
            - Score crediticio: [___/100 puntos]
            - Historial de pagos: [___/75 puntos]
            - Antigüedad crediticia: [___/50 puntos]
            - Nivel de endeudamiento: [___/25 puntos]
            
            Subtotal Historial: [___/250 puntos]
            ```
            
            #### 3. PERFIL PERSONAL (200 puntos máximo)
            ```
            👤 CARACTERÍSTICAS PERSONALES:
            - Edad y experiencia: [___/50 puntos]
            - Estabilidad personal: [___/50 puntos]
            - Nivel educativo: [___/25 puntos]
            - Referencias: [___/75 puntos]
            
            Subtotal Personal: [___/200 puntos]
            ```
            
            #### 4. GARANTÍA Y COLATERAL (150 puntos máximo)
            ```
            🚗 VALOR DEL ACTIVO:
            - LTV ratio: [___/50 puntos]
            - Liquidez del vehículo: [___/50 puntos]
            - Valor residual: [___/50 puntos]
            
            Subtotal Garantía: [___/150 puntos]
            ```
            
            #### 5. FACTORES EXTERNOS (100 puntos máximo)
            ```
            🌍 ENTORNO:
            - Estabilidad macroeconómica: [___/50 puntos]
            - Perspectivas sectoriales: [___/50 puntos]
            
            Subtotal Externos: [___/100 puntos]
            ```
            
            ### 📈 **SCORE TOTAL Y CLASIFICACIÓN**
            ```
            🎯 PUNTAJE TOTAL: [___/1000 puntos]
            
            📊 CLASIFICACIÓN DE RIESGO:
            - 850-1000: AAA (Riesgo Mínimo) - Tasa preferencial
            - 750-849:  AA  (Riesgo Bajo) - Tasa estándar baja
            - 650-749:  A   (Riesgo Medio-Bajo) - Tasa estándar
            - 550-649:  BBB (Riesgo Medio) - Tasa estándar alta
            - 450-549:  BB  (Riesgo Medio-Alto) - Condiciones especiales
            - 350-449:  B   (Riesgo Alto) - Requiere garantías adicionales
            - <350:     C   (Riesgo Muy Alto) - Rechazar o condiciones muy restrictivas
            ```
            
            ## 🚨 EVALUACIÓN DE ALERTAS Y BANDERAS ROJAS
            
            ### 🔴 **ALERTAS CRÍTICAS** (Rechazo automático)
            ```
            ❌ FACTORES EXCLUYENTES:
            [ ] Ingresos insuficientes (capacidad <30%% de la cuota)
            [ ] Score crediticio <300 puntos
            [ ] Reportes vigentes en centrales de riesgo
            [ ] Embargos judiciales activos
            [ ] Actividad económica ilícita
            [ ] Documentación fraudulenta
            [ ] Edad fuera de rango aceptable
            [ ] Vehículo fuera de parámetros
            ```
            
            ### 🟡 **ALERTAS MENORES** (Requieren análisis adicional)
            ```
            ⚠️ FACTORES DE PRECAUCIÓN:
            [ ] Cambios recientes de empleo
            [ ] Ingresos variables significativos
            [ ] Alto nivel de endeudamiento (>50%%)
            [ ] Historial crediticio limitado
            [ ] Sector económico en declive
            [ ] Vehículo con alta depreciación
            [ ] Referencias limitadas
            [ ] Concentración geográfica de riesgo
            ```
            
            ## 📋 REPORTE DE EVALUACIÓN DE RIESGO
            
            ### 🎯 **RESUMEN EJECUTIVO**
            ```
            📊 CALIFICACIÓN FINAL: [AAA/AA/A/BBB/BB/B/C]
            📈 PUNTAJE TOTAL: [___/1000 puntos]
            🚦 RECOMENDACIÓN: [APROBAR/APROBAR CON CONDICIONES/RECHAZAR]
            ⚡ PRIORIDAD DE PROCESAMIENTO: [ALTA/MEDIA/BAJA]
            ```
            
            ### 💰 **CONDICIONES RECOMENDADAS**
            ```
            💳 MONTO MÁXIMO APROBADO: $[Cantidad]
            📊 LTV MÁXIMO: [%%]
            ⏰ PLAZO RECOMENDADO: [Meses]
            📈 TASA DE INTERÉS SUGERIDA: [%%] EA
            💰 CUOTA INICIAL MÍNIMA: [%%]
            ```
            
            ### 🛡️ **MEDIDAS DE MITIGACIÓN**
            ```
            📋 CONDICIONES ESPECIALES:
            [ ] Codeudor requerido
            [ ] Garantías adicionales
            [ ] Seguro de vida obligatorio
            [ ] Monitoreo mensual
            [ ] Verificaciones periódicas
            [ ] Límites de concentración
            ```
            
            ### 📊 **ANÁLISIS DE SENSIBILIDAD**
            ```
            📈 ESCENARIOS DE ESTRÉS:
            
            🔴 ESCENARIO ADVERSO (Probabilidad 15%%):
            - Pérdida de empleo del cliente
            - Reducción de ingresos 30%%
            - Incremento tasas de interés 5pp
            - Impacto en capacidad de pago: [Descripción]
            
            🟡 ESCENARIO BASE (Probabilidad 70%%):
            - Condiciones actuales se mantienen
            - Crecimiento normal de ingresos
            - Estabilidad macroeconómica
            - Probabilidad de incumplimiento: [%%]
            
            🟢 ESCENARIO OPTIMISTA (Probabilidad 15%%):
            - Mejora en condiciones laborales
            - Crecimiento de ingresos 10%%
            - Estabilidad económica mejorada
            - Beneficios adicionales: [Descripción]
            ```
            
            ### 📞 **VERIFICACIONES ADICIONALES REQUERIDAS**
            ```
            🔍 VALIDACIONES PENDIENTES:
            [ ] Verificación laboral telefónica
            [ ] Confirmación de ingresos con empleador
            [ ] Validación de referencias comerciales
            [ ] Verificación de activos patrimoniales
            [ ] Consulta adicional en centrales de riesgo
            [ ] Evaluación técnica del vehículo
            ```
            
            ### 🎯 **RECOMENDACIONES ESTRATÉGICAS**
            ```
            💡 OPTIMIZACIÓN DEL PERFIL:
            - Mejoras que podrían elevar la calificación
            - Documentos adicionales que fortalecerían el caso
            - Alternativas de estructuración del crédito
            - Oportunidades de productos cruzados
            
            📈 SEGUIMIENTO RECOMENDADO:
            - Frecuencia de revisión del crédito
            - Indicadores clave de alerta temprana
            - Oportunidades de mejora en condiciones
            - Potencial para incrementos futuros
            ```
            
            ## ⚖️ CONSIDERACIONES REGULATORIAS
            
            ### 📋 **CUMPLIMIENTO NORMATIVO**
            ```
            🏛️ REQUERIMIENTOS LEGALES:
            [ ] Cumple con políticas internas de riesgo
            [ ] Dentro de límites regulatorios de concentración
            [ ] Provisiones adecuadas según calificación
            [ ] Documentación completa según normativa
            [ ] Revelación adecuada de información al cliente
            ```
            
            ---
            
            **INSTRUCCIÓN FINAL:** Realiza una evaluación integral y objetiva del riesgo crediticio, considerando todos los factores cuantitativos y cualitativos. Tu análisis debe ser riguroso, pero también práctico, buscando el equilibrio entre la gestión prudente del riesgo y el crecimiento del negocio. Proporciona recomendaciones claras y justificadas que permitan una toma de decisiones informada.
            """, customerProfile, type, financialData, vehicleInfo != null ? vehicleInfo : "No especificada");
    }

    @McpPrompt(
        name = "portfolio_risk_analysis",
        description = "Prompt especializado para análisis de riesgo de portafolio y concentración en crédito automotriz"
    )
    public String getPortfolioRiskAnalysisPrompt(
            @McpArg(name = "portfolio_data", required = true) String portfolioData,
            @McpArg(name = "market_conditions", required = false) String marketConditions,
            @McpArg(name = "analysis_period", required = false) String analysisPeriod) {
        
        String period = analysisPeriod != null ? analysisPeriod : "12 meses";
        
        return String.format("""
            # 📊 ANALISTA DE RIESGO DE PORTAFOLIO - EXPERTO EN GESTIÓN DE CARTERAS
            
            Eres un especialista en gestión de riesgos de portafolio con maestría en Finanzas Cuantitativas y 15 años de experiencia en el sector financiero. Tu expertise incluye modelamiento de riesgo de crédito, análisis de concentración, stress testing y optimización de carteras. Tu misión es evaluar el riesgo integral del portafolio de crédito automotriz.
            
            ## 📈 INFORMACIÓN DEL PORTAFOLIO
            
            **Datos del Portafolio:** %s
            **Condiciones de Mercado:** %s
            **Período de Análisis:** %s
            
            ## 🔍 METODOLOGÍA DE ANÁLISIS DE PORTAFOLIO
            
            ### 1. ANÁLISIS DESCRIPTIVO DEL PORTAFOLIO
            
            #### 📊 **COMPOSICIÓN GENERAL**
            ```
            💰 MÉTRICAS BÁSICAS:
            - Saldo total del portafolio: $[Cantidad]
            - Número total de créditos: [Cantidad]
            - Saldo promedio por crédito: $[Cantidad]
            - Crecimiento últimos 12 meses: [%%]
            
            📈 DISTRIBUCIÓN POR VINTAGE:
            - Créditos 0-6 meses: [%%] - $[Cantidad]
            - Créditos 7-12 meses: [%%] - $[Cantidad]
            - Créditos 13-24 meses: [%%] - $[Cantidad]
            - Créditos >24 meses: [%%] - $[Cantidad]
            
            🎯 DISTRIBUCIÓN POR CALIFICACIÓN:
            - AAA (Riesgo Mínimo): [%%] - $[Cantidad]
            - AA-A (Riesgo Bajo): [%%] - $[Cantidad]
            - BBB (Riesgo Medio): [%%] - $[Cantidad]
            - BB-B (Riesgo Alto): [%%] - $[Cantidad]
            - C-D (Riesgo Crítico): [%%] - $[Cantidad]
            ```
            
            ### 2. ANÁLISIS DE CONCENTRACIÓN
            
            #### 🎯 **CONCENTRACIÓN GEOGRÁFICA**
            ```
            🗺️ DISTRIBUCIÓN REGIONAL:
            - Bogotá D.C.: [%%] - Riesgo: [Alto/Medio/Bajo]
            - Medellín: [%%] - Riesgo: [Alto/Medio/Bajo]
            - Cali: [%%] - Riesgo: [Alto/Medio/Bajo]
            - Barranquilla: [%%] - Riesgo: [Alto/Medio/Bajo]
            - Otras ciudades: [%%] - Riesgo: [Alto/Medio/Bajo]
            
            ⚠️ ALERTAS DE CONCENTRACIÓN:
            - Concentración >25%% en una región: [SÍ/NO]
            - Correlación con ciclos económicos regionales: [Alta/Media/Baja]
            - Diversificación geográfica: [Adecuada/Insuficiente]
            ```
            
            #### 🏭 **CONCENTRACIÓN SECTORIAL**
            ```
            💼 DISTRIBUCIÓN POR SECTOR:
            - Sector Público: [%%] - Estabilidad: [Alta/Media/Baja]
            - Sector Financiero: [%%] - Correlación: [Alta/Media/Baja]
            - Manufactura: [%%] - Ciclicidad: [Alta/Media/Baja]
            - Servicios: [%%] - Resistencia crisis: [Alta/Media/Baja]
            - Comercio: [%%] - Volatilidad: [Alta/Media/Baja]
            - Otros sectores: [%%]
            
            📊 ANÁLISIS DE RIESGO SECTORIAL:
            - Sectores en riesgo (>15%% concentración): [Lista]
            - Correlación entre sectores: [Matriz de correlación]
            - Diversificación sectorial: [Índice HHI]
            ```
            
            #### 🚗 **CONCENTRACIÓN POR TIPO DE VEHÍCULO**
            ```
            🏷️ DISTRIBUCIÓN POR MARCA:
            - Toyota: [%%] - Valor residual: [Alto/Medio/Bajo]
            - Chevrolet: [%%] - Liquidez: [Alta/Media/Baja]
            - Renault: [%%] - Depreciación: [Lenta/Moderada/Rápida]
            - Otras marcas: [%%]
            
            📅 DISTRIBUCIÓN POR ANTIGÜEDAD:
            - Vehículos nuevos (0-1 año): [%%]
            - Vehículos seminuevos (2-3 años): [%%]
            - Vehículos usados (4+ años): [%%]
            ```
            
            ### 3. ANÁLISIS DE CALIDAD DE CARTERA
            
            #### 📊 **INDICADORES DE MOROSIDAD**
            ```
            🚨 MÉTRICAS DE MORA:
            - Mora >30 días: [%%] - $[Cantidad]
            - Mora >60 días: [%%] - $[Cantidad]
            - Mora >90 días: [%%] - $[Cantidad]
            - Mora >180 días: [%%] - $[Cantidad]
            
            📈 TENDENCIA HISTÓRICA:
            - Mora 12 meses atrás: [%%]
            - Mora 6 meses atrás: [%%]
            - Mora actual: [%%]
            - Tendencia: [Mejorando/Estable/Deteriorando]
            
            🎯 BENCHMARKING:
            - Mora vs. industria: [Mejor/Similar/Peor]
            - Mora vs. target interno: [Dentro/Fuera] del objetivo
            ```
            
            #### 💸 **ANÁLISIS DE PÉRDIDAS**
            ```
            📉 PÉRDIDAS REALIZADAS:
            - Pérdidas últimos 12 meses: $[Cantidad] ([%%] del portafolio)
            - Tasa de recuperación promedio: [%%]
            - Tiempo promedio de recuperación: [Días]
            
            🔮 PÉRDIDAS ESPERADAS:
            - Provisiones constituidas: $[Cantidad] ([%%] del portafolio)
            - Pérdidas esperadas próximos 12 meses: $[Cantidad]
            - Adecuación de provisiones: [Suficiente/Insuficiente]
            ```
            
            ### 4. STRESS TESTING Y ESCENARIOS
            
            #### 🌪️ **ESCENARIOS MACROECONÓMICOS**
            ```
            📊 ESCENARIO BASE (Probabilidad 60%%):
            - PIB: Crecimiento [%%]
            - Desempleo: [%%]
            - Inflación: [%%]
            - Impacto en mora esperada: [%%]
            - Pérdidas esperadas: $[Cantidad]
            
            🔴 ESCENARIO ADVERSO (Probabilidad 25%%):
            - PIB: Contracción [%%]
            - Desempleo: [%%]
            - Inflación: [%%]
            - Impacto en mora esperada: [%%]
            - Pérdidas esperadas: $[Cantidad]
            
            ⚫ ESCENARIO SEVERO (Probabilidad 15%%):
            - PIB: Recesión profunda [%%]
            - Desempleo: [%%]
            - Crisis sector automotriz
            - Impacto en mora esperada: [%%]
            - Pérdidas esperadas: $[Cantidad]
            ```
            
            #### 🧪 **ANÁLISIS DE SENSIBILIDAD**
            ```
            📈 FACTORES DE RIESGO CLAVE:
            
            💼 DESEMPLEO:
            - Incremento 2pp: Impacto mora +[%%]
            - Incremento 5pp: Impacto mora +[%%]
            - Incremento 10pp: Impacto mora +[%%]
            
            📊 TASAS DE INTERÉS:
            - Incremento 200bp: Impacto nuevos créditos [%%]
            - Incremento 500bp: Reducción demanda [%%]
            
            🚗 PRECIOS VEHÍCULOS:
            - Caída 10%%: Impacto LTV y recuperaciones
            - Caída 20%%: Impacto severo en garantías
            - Caída 30%%: Crisis del sector automotriz
            ```
            
            ### 5. MÉTRICAS DE RIESGO AVANZADAS
            
            #### 📊 **VALUE AT RISK (VaR)**
            ```
            📉 VaR DEL PORTAFOLIO:
            - VaR 95%% (1 año): $[Cantidad] ([%%] del portafolio)
            - VaR 99%% (1 año): $[Cantidad] ([%%] del portafolio)
            - VaR 99.9%% (1 año): $[Cantidad] ([%%] del portafolio)
            
            📈 EXPECTED SHORTFALL:
            - ES 95%%: $[Cantidad]
            - ES 99%%: $[Cantidad]
            ```
            
            #### 🔗 **RIESGO DE CONCENTRACIÓN**
            ```
            📊 ÍNDICES DE CONCENTRACIÓN:
            - Índice Herfindahl-Hirschman: [Valor]
            - Top 10 exposiciones: [%%] del portafolio
            - Concentración sectorial máxima: [%%]
            - Concentración geográfica máxima: [%%]
            
            ⚠️ LÍMITES REGULATORIOS:
            - Concentración individual: [Dentro/Fuera] de límites
            - Concentración sectorial: [Dentro/Fuera] de límites
            - Concentración geográfica: [Dentro/Fuera] de límites
            ```
            
            ## 📋 REPORTE DE RIESGO DE PORTAFOLIO
            
            ### 🎯 **RESUMEN EJECUTIVO**
            ```
            📊 CALIFICACIÓN GENERAL DEL PORTAFOLIO: [AAA/AA/A/BBB/BB/B/C]
            🚦 NIVEL DE RIESGO GLOBAL: [BAJO/MEDIO/ALTO/CRÍTICO]
            📈 TENDENCIA: [MEJORANDO/ESTABLE/DETERIORANDO]
            ⚡ ACCIONES REQUERIDAS: [NINGUNA/MONITOREO/CORRECTIVAS/URGENTES]
            ```
            
            ### 📊 **MÉTRICAS CLAVE**
            ```
            💰 SALDO EN RIESGO (>90 días mora): $[Cantidad] ([%%])
            📉 PÉRDIDAS ESPERADAS 12M: $[Cantidad] ([%%])
            🔮 VaR 99%% anual: $[Cantidad] ([%%])
            📊 ROE AJUSTADO POR RIESGO: [%%]
            ```
            
            ### 🚨 **ALERTAS Y RECOMENDACIONES**
            
            #### 🔴 **ALERTAS CRÍTICAS**
            ```
            ❌ PROBLEMAS INMEDIATOS:
            [ ] Concentración excesiva en [segmento]
            [ ] Deterioro acelerado de calidad
            [ ] Provisiones insuficientes
            [ ] Límites regulatorios cerca de ser violados
            [ ] Correlación alta con factores de riesgo sistémico
            ```
            
            #### 🟡 **ALERTAS DE SEGUIMIENTO**
            ```
            ⚠️ ASPECTOS A MONITOREAR:
            [ ] Tendencia creciente en mora temprana
            [ ] Concentración en sectores vulnerables
            [ ] Cambios en perfil de nuevos créditos
            [ ] Deterioro en tasas de recuperación
            [ ] Incremento en refinanciaciones
            ```
            
            ### 🎯 **RECOMENDACIONES ESTRATÉGICAS**
            
            #### 💡 **OPTIMIZACIÓN DEL PORTAFOLIO**
            ```
            📈 CRECIMIENTO INTELIGENTE:
            - Segmentos objetivo para crecimiento: [Lista]
            - Sectores/regiones a evitar: [Lista]
            - Diversificación recomendada: [Estrategias]
            - Límites de concentración sugeridos: [Valores]
            
            🛡️ MITIGACIÓN DE RIESGOS:
            - Incremento en provisiones: $[Cantidad]
            - Ajustes en políticas de originación: [Descripción]
            - Estrategias de cobranza mejoradas: [Plan]
            - Coberturas adicionales requeridas: [Seguros/Garantías]
            ```
            
            #### 📊 **MONITOREO Y CONTROL**
            ```
            🔍 INDICADORES CLAVE (KRIs):
            - Mora >30 días: Target <[%%]
            - Pérdidas anuales: Target <[%%]
            - Concentración máxima: Target <[%%]
            - ROE ajustado por riesgo: Target >[%%]
            
            📅 FRECUENCIA DE REVISIÓN:
            - Métricas básicas: [Frecuencia]
            - Stress testing: [Frecuencia]
            - Revisión de límites: [Frecuencia]
            - Reporte a junta directiva: [Frecuencia]
            ```
            
            ### 💰 **IMPACTO FINANCIERO**
            ```
            📊 CAPITAL ECONÓMICO REQUERIDO:
            - Capital por riesgo de crédito: $[Cantidad]
            - Capital por riesgo de concentración: $[Cantidad]
            - Capital total asignado: $[Cantidad]
            - Utilización de capital: [%%]
            
            💵 RENTABILIDAD AJUSTADA POR RIESGO:
            - ROE sin ajuste: [%%]
            - ROE ajustado por riesgo: [%%]
            - RAROC (Risk Adjusted Return on Capital): [%%]
            - EVA (Economic Value Added): $[Cantidad]
            ```
            
            ---
            
            **INSTRUCCIÓN FINAL:** Realiza un análisis exhaustivo y técnicamente riguroso del portafolio de crédito automotriz. Tu evaluación debe combinar análisis cuantitativo avanzado con insights cualitativos del mercado. Proporciona recomendaciones concretas y accionables para optimizar el balance entre riesgo y rentabilidad, asegurando la sostenibilidad a largo plazo del negocio.
            """, portfolioData, marketConditions != null ? marketConditions : "Condiciones normales de mercado", period);
    }
}