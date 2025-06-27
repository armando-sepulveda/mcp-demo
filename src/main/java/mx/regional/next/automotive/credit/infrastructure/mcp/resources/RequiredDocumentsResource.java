package mx.regional.next.automotive.credit.infrastructure.mcp.resources;

import org.springframework.ai.mcp.server.annotation.McpResource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RequiredDocumentsResource {
    
    private static final Logger log = LoggerFactory.getLogger(RequiredDocumentsResource.class);

    @McpResource(
        uri = "credit://documents/requirements",
        name = "Required Documents",
        description = "Lista completa de documentos requeridos para solicitud de crédito automotriz según tipo de cliente"
    )
    public String getRequiredDocuments() {
        log.debug("Proporcionando lista de documentos requeridos");
        
        return """
            📋 **DOCUMENTOS REQUERIDOS - CRÉDITO AUTOMOTRIZ**
            
            ## 👤 PERSONA NATURAL
            
            ### 📄 **DOCUMENTOS DE IDENTIDAD Y PERSONALES**
            
            #### ✅ **OBLIGATORIOS**
            1. **Cédula de Ciudadanía**
               - Original y copia legible por ambas caras
               - Vigente (no vencida)
               - Si es extranjero: Cédula de extranjería vigente
            
            2. **Registro Civil de Nacimiento**
               - Expedido máximo 30 días antes de la solicitud
               - Para mayores de edad (solo si se requiere verificación)
            
            3. **Certificado Electoral**
               - Vigente (últimas elecciones)
               - Solo para ciudadanos colombianos mayores de 18 años
            
            ### 💼 **DOCUMENTOS LABORALES E INGRESOS**
            
            #### ✅ **EMPLEADOS DEPENDIENTES**
            
            1. **Certificado Laboral**
               - Expedido máximo 30 días antes
               - Debe incluir:
                 - Cargo actual
                 - Fecha de ingreso
                 - Tipo de contrato (indefinido/término fijo)
                 - Salario básico
                 - Promedio de ingresos variables (si aplica)
                 - Membrete y firma autorizada de la empresa
            
            2. **Últimas 3 Nóminas**
               - Desprendibles de pago originales
               - Consecutivas (no salteadas)
               - Que evidencien ingresos regulares
            
            3. **Certificado de Ingresos y Retenciones**
               - Año fiscal anterior completo
               - Expedido por la empresa
               - Firmado por contador o revisor fiscal
            
            #### ✅ **EMPLEADOS PÚBLICOS**
            
            1. **Certificado Laboral Oficial**
               - De la entidad pública empleadora
               - Con sello y firma de funcionario autorizado
               - Especificando grado y escalafón
            
            2. **Últimas 3 Nóminas**
               - Sistema de información de la entidad
               - Incluyendo descuentos y deducciones
            
            3. **Resolución de Nombramiento**
               - En caso de cargos de libre nombramiento
               - Para verificar estabilidad laboral
            
            #### ✅ **INDEPENDIENTES/PROFESIONALES LIBRES**
            
            1. **Declaración de Renta**
               - Últimos 2 años consecutivos
               - Copia con sello de presentación DIAN
               - Formulario 210 completo con anexos
            
            2. **Estados Financieros Personales**
               - Balance General y Estado de Resultados
               - Máximo 6 meses de antigüedad
               - Firmado por contador público si aplica
            
            3. **Certificación de Ingresos**
               - Expedida por contador público
               - Promedio mensual últimos 12 meses
               - Con tarjeta profesional vigente
            
            4. **Matrícula Mercantil**
               - Si es comerciante
               - Vigente del año en curso
               - Cámara de Comercio correspondiente
            
            5. **RUT Actualizado**
               - Expedido máximo 30 días antes
               - Mostrando actividades económicas
            
            ### 🏦 **DOCUMENTOS FINANCIEROS**
            
            #### ✅ **OBLIGATORIOS PARA TODOS**
            
            1. **Extractos Bancarios**
               - Últimos 6 meses consecutivos
               - Cuenta de ahorros y/o corriente principal
               - Todas las cuentas donde recibe ingresos
               - Legibles y completos (todas las páginas)
            
            2. **Certificación Bancaria**
               - Saldos promedio últimos 6 meses
               - Comportamiento de la cuenta
               - Expedida por la entidad financiera
            
            3. **Extractos Tarjetas de Crédito**
               - Últimos 3 meses
               - Todas las tarjetas activas
               - Para evaluar capacidad de pago
            
            ### 🏠 **DOCUMENTOS PATRIMONIALES**
            
            #### ✅ **SI POSEE BIENES INMUEBLES**
            
            1. **Certificado de Libertad y Tradición**
               - Máximo 30 días de expedición
               - De todos los inmuebles que posea
               - Superintendencia de Notariado
            
            2. **Avalúo Comercial**
               - Si el inmueble es garantía adicional
               - Avaluador autorizado por SIC
               - Máximo 1 año de antigüedad
            
            3. **Recibo Predial**
               - Último año fiscal
               - Al día en pagos
            
            #### ✅ **SI POSEE VEHÍCULOS**
            
            1. **Tarjeta de Propiedad**
               - Original y copia
               - Vigente y al día
            
            2. **SOAT Vigente**
               - Póliza actual
               - Al día en pagos
            
            ### 📝 **REFERENCIAS**
            
            #### ✅ **REFERENCIAS COMERCIALES** (Mínimo 2)
            
            1. **Establecimientos Comerciales**
               - Carta en papel membrete
               - Tiempo de relación comercial
               - Cupo de crédito y comportamiento
               - Datos de contacto verificables
            
            2. **Entidades Financieras**
               - Certificación de productos actuales
               - Historial crediticio
               - Comportamiento de pagos
            
            #### ✅ **REFERENCIAS PERSONALES** (Mínimo 2)
            
            1. **Personas No Familiares**
               - Carta de referencia firmada
               - Copia de cédula del referencista
               - Teléfonos de contacto
               - Tiempo de conocerse
            
            ## 🏢 PERSONA JURÍDICA
            
            ### 📄 **DOCUMENTOS CONSTITUTIVOS**
            
            #### ✅ **OBLIGATORIOS**
            
            1. **Certificado de Existencia y Representación Legal**
               - Expedido máximo 30 días antes
               - Cámara de Comercio correspondiente
               - Con todas las reformas y modificaciones
            
            2. **Acta de Constitución**
               - Copia autenticada
               - Con reformas estatutarias si las hay
            
            3. **Estatutos Sociales**
               - Versión actualizada
               - Con todas las modificaciones
            
            ### 👥 **DOCUMENTOS DEL REPRESENTANTE LEGAL**
            
            #### ✅ **OBLIGATORIOS**
            
            1. **Cédula de Ciudadanía**
               - Del representante legal
               - Original y copia legible
            
            2. **Certificado Electoral**
               - Del representante legal vigente
            
            3. **Carta de Autorización**
               - Si va a actuar tercero en nombre de la empresa
               - Firmada por representante legal
            
            ### 📊 **DOCUMENTOS FINANCIEROS EMPRESARIALES**
            
            #### ✅ **OBLIGATORIOS**
            
            1. **Estados Financieros**
               - Últimos 2 años completos
               - Balance General, Estado de Resultados
               - Estado de Flujo de Efectivo
               - Notas a los estados financieros
               - Dictamen de revisor fiscal (si aplica)
            
            2. **Declaraciones de Renta**
               - Últimos 2 años
               - Formulario 110 completo
               - Con sello de presentación DIAN
            
            3. **Declaraciones de IVA**
               - Últimos 6 períodos
               - Al día en presentación y pago
            
            4. **Extractos Bancarios**
               - Últimos 6 meses
               - Cuentas principales de la empresa
               - Donde se manejan los mayores flujos
            
            ### 🔍 **DOCUMENTOS ADICIONALES EMPRESARIALES**
            
            #### ✅ **SEGÚN EL CASO**
            
            1. **Referencias Comerciales**
               - Mínimo 3 proveedores principales
               - Certificaciones de cumplimiento
               - Cartas en papel membrete
            
            2. **Autorizaciones Especiales**
               - Si la actividad requiere permisos especiales
               - Licencias de funcionamiento
               - Registros sanitarios (si aplica)
            
            3. **Contratos Principales**
               - Si tiene contratos de suministro importantes
               - Para evaluar estabilidad de ingresos
            
            ## 🚗 DOCUMENTOS DEL VEHÍCULO
            
            ### ✅ **VEHÍCULO NUEVO**
            
            1. **Proforma o Cotización**
               - Del concesionario autorizado
               - Especificando modelo, año, equipamiento
               - Precio final con descuentos aplicados
               - Vigencia de la oferta
            
            2. **Ficha Técnica del Vehículo**
               - Especificaciones completas
               - Motor, transmisión, equipamiento
               - Consumo de combustible
            
            ### ✅ **VEHÍCULO USADO**
            
            1. **Tarjeta de Propiedad**
               - Original del vendedor
               - Al día en impuestos
               - Sin limitaciones judiciales
            
            2. **Certificado de Tradición**
               - RUNT (Registro Único Nacional de Tránsito)
               - Máximo 8 días de expedición
               - Sin reportes de hurto o siniestros
            
            3. **Avalúo Técnico**
               - Realizado por perito autorizado
               - Máximo 30 días de antigüedad
               - Estado mecánico y carrocería
            
            4. **Revisión Técnico-Mecánica**
               - Vigente si aplica por antigüedad
               - Al día en renovaciones
            
            5. **SOAT Vigente**
               - Póliza de seguro obligatorio
               - Al día en pagos
            
            ## 📝 FORMATOS Y SOLICITUDES
            
            ### ✅ **DOCUMENTOS INTERNOS**
            
            1. **Solicitud de Crédito**
               - Formato oficial de la entidad
               - Completamente diligenciado
               - Firmado por el solicitante
            
            2. **Autorización Centrales de Riesgo**
               - Consulta en DataCrédito, Cifin, etc.
               - Firmada por el solicitante y codeudor
            
            3. **Autorización de Verificaciones**
               - Laborales, comerciales, personales
               - Referencias, ingresos, patrimonio
            
            4. **Pagaré en Blanco**
               - Como garantía del crédito
               - Firmado por deudor y codeudor
               - Con carta de instrucciones
            
            ## ⚠️ CONSIDERACIONES ESPECIALES
            
            ### 📋 **CALIDAD DE DOCUMENTOS**
            
            #### ✅ **REQUISITOS GENERALES**
            - Documentos legibles y completos
            - Sin tachones, enmendaduras o alteraciones
            - Originales para verificación
            - Copias en buen estado
            - Información actualizada y coherente
            
            #### ❌ **NO SE ACEPTAN**
            - Documentos vencidos
            - Fotocopias de fotocopias
            - Documentos con información incompleta
            - Tachaduras o correcciones sin firmar
            - Documentos de terceros no autorizados
            
            ### 🕐 **TIEMPOS DE VIGENCIA**
            
            | Documento | Vigencia Máxima |
            |-----------|-----------------|
            | Certificado laboral | 30 días |
            | Extractos bancarios | 3 meses |
            | Estados financieros | 6 meses |
            | Certificaciones bancarias | 30 días |
            | RUT | 30 días |
            | Certificado libertad y tradición | 30 días |
            | Existencia y representación legal | 30 días |
            
            ### 🎯 **TIPS PARA AGILIZAR**
            
            #### ✅ **RECOMENDACIONES**
            
            1. **Preparación Previa**
               - Solicite todos los documentos al mismo tiempo
               - Verifique vigencias antes de entregarlos
               - Mantenga copias de respaldo
            
            2. **Organización**
               - Entregue en carpeta organizada
               - Use separadores por categorías
               - Liste los documentos incluidos
            
            3. **Verificación**
               - Revise que toda la información sea legible
               - Confirme que los datos coincidan entre documentos
               - Asegúrese de tener todos los documentos completos
            
            4. **Digitalización**
               - Escanee en alta resolución
               - Formato PDF preferible
               - Archivos no mayores a 5MB cada uno
            
            ## 📞 SOPORTE Y CONSULTAS
            
            ### 🆘 **SI TIENE DUDAS**
            
            - **Asesor asignado:** Contacte su ejecutivo comercial
            - **Línea de atención:** Disponible durante horario laboral
            - **Portal web:** Consulte checklist digital
            - **Oficinas:** Visite nuestras sucursales
            
            ### ⏰ **HORARIOS DE ATENCIÓN**
            
            - **Lunes a Viernes:** 8:00 AM - 6:00 PM
            - **Sábados:** 8:00 AM - 12:00 PM
            - **Línea telefónica:** 24/7 para consultas básicas
            
            ---
            
            💡 **Recuerde:** La entrega completa y oportuna de documentos 
            es clave para la aprobación rápida de su crédito automotriz.
            """;
    }

    @McpResource(
        uri = "credit://documents/checklist/{customerType}",
        name = "Documents Checklist",
        description = "Lista de verificación específica de documentos por tipo de cliente"
    )
    public String getDocumentsChecklist(String customerType) {
        log.debug("Proporcionando checklist de documentos para: {}", customerType);
        
        if (customerType == null || customerType.trim().isEmpty()) {
            return "Error: Debe especificar el tipo de cliente (natural o juridica).";
        }
        
        String type = customerType.trim().toLowerCase();
        
        return switch (type) {
            case "natural" -> getNaturalPersonChecklist();
            case "juridica", "juridico" -> getJuridicalPersonChecklist();
            case "empleado" -> getEmployeeChecklist();
            case "independiente" -> getIndependentChecklist();
            default -> """
                ⚠️ **TIPO DE CLIENTE NO VÁLIDO**
                
                Tipos disponibles:
                - `natural` - Persona natural
                - `juridica` - Persona jurídica
                - `empleado` - Empleado dependiente
                - `independiente` - Profesional independiente
                
                Ejemplo de uso: `/documents/checklist/natural`
                """;
        };
    }

    private String getNaturalPersonChecklist() {
        return """
            ✅ **CHECKLIST DOCUMENTOS - PERSONA NATURAL**
            
            ## 📋 Lista de Verificación
            
            ### 👤 **DOCUMENTOS PERSONALES**
            ```
            [ ] Cédula de ciudadanía (original + copia)
            [ ] Certificado electoral vigente
            [ ] Registro civil (si se requiere)
            ```
            
            ### 💼 **DOCUMENTOS LABORALES**
            ```
            [ ] Certificado laboral (máx. 30 días)
            [ ] Últimas 3 nóminas consecutivas
            [ ] Certificado ingresos y retenciones año anterior
            ```
            
            ### 🏦 **DOCUMENTOS FINANCIEROS**
            ```
            [ ] Extractos bancarios últimos 6 meses
            [ ] Certificación bancaria de saldos
            [ ] Extractos tarjetas de crédito (últimos 3 meses)
            ```
            
            ### 🏠 **DOCUMENTOS PATRIMONIALES** (Si aplica)
            ```
            [ ] Certificado libertad y tradición inmuebles
            [ ] Avalúo comercial (si es garantía)
            [ ] Recibo predial al día
            [ ] Tarjeta de propiedad vehículos actuales
            [ ] SOAT vigente
            ```
            
            ### 📝 **REFERENCIAS**
            ```
            [ ] 2 Referencias comerciales (cartas + contactos)
            [ ] 2 Referencias personales (cartas + cédulas)
            ```
            
            ### 🚗 **DOCUMENTOS DEL VEHÍCULO**
            ```
            [ ] Proforma/cotización (vehículo nuevo)
            [ ] Tarjeta de propiedad (vehículo usado)
            [ ] Certificado RUNT (vehículo usado)
            [ ] Avalúo técnico (vehículo usado)
            ```
            
            ### 📄 **FORMULARIOS INTERNOS**
            ```
            [ ] Solicitud de crédito diligenciada
            [ ] Autorización centrales de riesgo
            [ ] Autorización verificaciones
            [ ] Pagaré en blanco firmado
            ```
            
            ## ⏰ **VERIFICACIÓN DE VIGENCIAS**
            
            | Documento | Vigencia | ✓ |
            |-----------|----------|---|
            | Certificado laboral | 30 días | [ ] |
            | Extractos bancarios | Últimos 6 meses | [ ] |
            | Certificación bancaria | 30 días | [ ] |
            | Cert. libertad y tradición | 30 días | [ ] |
            | Proforma vehículo | Según concesionario | [ ] |
            
            ## 🎯 **TIPS DE ENTREGA**
            
            ✅ **Antes de entregar, verifique:**
            - Todos los documentos son legibles
            - Las fechas están vigentes
            - Los datos coinciden entre documentos
            - Tiene copias de respaldo
            - Organizó por categorías
            
            📞 **Si le falta algún documento:**
            Contacte su asesor comercial para evaluar alternativas
            o documentos sustitutos según su caso específico.
            """;
    }

    private String getJuridicalPersonChecklist() {
        return """
            ✅ **CHECKLIST DOCUMENTOS - PERSONA JURÍDICA**
            
            ## 📋 Lista de Verificación
            
            ### 🏢 **DOCUMENTOS CONSTITUTIVOS**
            ```
            [ ] Certificado existencia y representación legal (máx. 30 días)
            [ ] Acta de constitución autenticada
            [ ] Estatutos sociales actualizados
            [ ] Reformas estatutarias (si las hay)
            ```
            
            ### 👤 **REPRESENTANTE LEGAL**
            ```
            [ ] Cédula del representante legal (original + copia)
            [ ] Certificado electoral del representante
            [ ] Carta de autorización (si actúa tercero)
            ```
            
            ### 📊 **DOCUMENTOS FINANCIEROS EMPRESARIALES**
            ```
            [ ] Estados financieros últimos 2 años completos
            [ ] Balance General y Estado de Resultados
            [ ] Estado de Flujo de Efectivo
            [ ] Notas a los estados financieros
            [ ] Dictamen revisor fiscal (si aplica)
            ```
            
            ### 🏛️ **DOCUMENTOS TRIBUTARIOS**
            ```
            [ ] Declaraciones de renta últimos 2 años
            [ ] Declaraciones de IVA últimos 6 períodos
            [ ] RUT actualizado (máx. 30 días)
            [ ] Certificado de cumplimiento tributario
            ```
            
            ### 🏦 **DOCUMENTOS FINANCIEROS**
            ```
            [ ] Extractos bancarios últimos 6 meses
            [ ] Certificaciones bancarias
            [ ] Estados de cuenta inversiones (si las hay)
            ```
            
            ### 📝 **REFERENCIAS EMPRESARIALES**
            ```
            [ ] 3 Referencias comerciales de proveedores
            [ ] Certificaciones de cumplimiento
            [ ] Referencias bancarias
            ```
            
            ### 🔍 **DOCUMENTOS ADICIONALES** (Según actividad)
            ```
            [ ] Licencias de funcionamiento
            [ ] Registros sanitarios (si aplica)
            [ ] Permisos especiales de operación
            [ ] Contratos principales de suministro
            ```
            
            ### 🚗 **DOCUMENTOS DEL VEHÍCULO**
            ```
            [ ] Proforma/cotización (vehículo nuevo)
            [ ] Tarjeta de propiedad (vehículo usado)
            [ ] Certificado RUNT (vehículo usado)
            [ ] Avalúo técnico (vehículo usado)
            ```
            
            ### 📄 **FORMULARIOS INTERNOS**
            ```
            [ ] Solicitud de crédito empresarial
            [ ] Autorización centrales de riesgo
            [ ] Autorización verificaciones empresariales
            [ ] Pagaré empresarial firmado
            [ ] Acta junta directiva (autorización crédito)
            ```
            
            ## ⏰ **VERIFICACIÓN DE VIGENCIAS**
            
            | Documento | Vigencia | ✓ |
            |-----------|----------|---|
            | Cert. existencia y rep. legal | 30 días | [ ] |
            | Estados financieros | 6 meses | [ ] |
            | RUT | 30 días | [ ] |
            | Extractos bancarios | Últimos 6 meses | [ ] |
            | Licencias funcionamiento | Vigente | [ ] |
            
            ## 📊 **DOCUMENTOS POR TAMAÑO DE EMPRESA**
            
            ### 🏭 **GRAN EMPRESA** (>200 empleados)
            ```
            Adicionales requeridos:
            [ ] Estados financieros auditados
            [ ] Dictamen revisor fiscal obligatorio
            [ ] Gobierno corporativo
            [ ] Políticas de riesgo
            ```
            
            ### 🏢 **MEDIANA EMPRESA** (51-200 empleados)
            ```
            Adicionales requeridos:
            [ ] Estados financieros certificados
            [ ] Revisor fiscal (si aplica)
            [ ] Manual de procedimientos
            ```
            
            ### 🏪 **PEQUEÑA EMPRESA** (<50 empleados)
            ```
            Documentos mínimos:
            [ ] Estados financieros básicos
            [ ] Certificación contador público
            [ ] Flujo de caja proyectado
            ```
            
            ## 🎯 **VALIDACIONES ESPECIALES**
            
            ✅ **Coherencia Financiera:**
            - Estados financieros vs declaraciones de renta
            - Flujos bancarios vs ingresos reportados
            - Patrimonio vs capacidad de pago
            - Crecimiento sostenible en el tiempo
            
            ✅ **Aspectos Legales:**
            - Vigencia de representación legal
            - Autorización para contraer obligaciones
            - Limitaciones estatutarias
            - Embargos o demandas pendientes
            
            📞 **Soporte Especializado:**
            Las personas jurídicas cuentan con asesoría
            especializada en banca empresarial para resolver
            dudas sobre documentación específica.
            """;
    }

    private String getEmployeeChecklist() {
        return """
            ✅ **CHECKLIST DOCUMENTOS - EMPLEADO DEPENDIENTE**
            
            ## 📋 Lista de Verificación Específica
            
            ### 👤 **IDENTIFICACIÓN**
            ```
            [ ] Cédula de ciudadanía vigente
            [ ] Certificado electoral vigente
            ```
            
            ### 💼 **DOCUMENTOS LABORALES OBLIGATORIOS**
            ```
            [ ] Certificado laboral (máx. 30 días) que incluya:
                [ ] Cargo actual
                [ ] Fecha de ingreso
                [ ] Tipo de contrato
                [ ] Salario básico
                [ ] Promedio ingresos variables
                [ ] Firma autorizada empresa
            
            [ ] Últimas 3 nóminas consecutivas
            [ ] Certificado ingresos y retenciones año anterior
            ```
            
            ### 🏛️ **EMPLEADOS PÚBLICOS - ADICIONALES**
            ```
            [ ] Certificado laboral oficial con sello entidad
            [ ] Resolución de nombramiento (si aplica)
            [ ] Grado y escalafón especificado
            [ ] Certificado de antigüedad
            ```
            
            ### 🏢 **EMPLEADOS PRIVADOS - ADICIONALES**
            ```
            [ ] Contrato de trabajo (primera página y firma)
            [ ] Carta del empleador confirmando estabilidad
            [ ] Organigrama empresa (si se requiere)
            ```
            
            ### 🏦 **INFORMACIÓN FINANCIERA**
            ```
            [ ] Extractos bancarios últimos 6 meses (cuenta nómina)
            [ ] Certificación bancaria con promedio de saldos
            [ ] Extractos otras cuentas (si recibe ingresos adicionales)
            [ ] Extractos tarjetas de crédito (últimos 3 meses)
            ```
            
            ### 💳 **CAPACIDAD DE PAGO**
            ```
            [ ] Relación de gastos mensuales
            [ ] Compromisos crediticios actuales
            [ ] Descuentos de nómina detallados
            [ ] Ingresos adicionales certificados (si los hay)
            ```
            
            ## 📊 **VALIDACIONES POR TIPO DE CONTRATO**
            
            ### ✅ **CONTRATO INDEFINIDO**
            ```
            Ventajas: Máxima estabilidad laboral
            [ ] Certificado laboral estándar
            [ ] Última nómina con descuentos
            [ ] Antigüedad mínima: 6 meses
            ```
            
            ### ⚠️ **CONTRATO TÉRMINO FIJO**
            ```
            Consideraciones especiales:
            [ ] Contrato vigente por mínimo 1 año
            [ ] Carta empleador sobre renovación
            [ ] Historial de renovaciones anteriores
            [ ] Justificación estabilidad laboral
            ```
            
            ### 🔍 **PERÍODO DE PRUEBA**
            ```
            Evaluación caso por caso:
            [ ] Superar período de prueba
            [ ] Carta confirmación continuidad
            [ ] Experiencia laboral previa
            [ ] Referencias laborales anteriores
            ```
            
            ## 💰 **CÁLCULO DE INGRESOS**
            
            ### 📈 **INGRESOS FIJOS**
            ```
            [ ] Salario básico mensual
            [ ] Auxilio de transporte
            [ ] Bonificaciones fijas mensuales
            [ ] Comisiones fijas garantizadas
            ```
            
            ### 📊 **INGRESOS VARIABLES** (Promedio 6 meses)
            ```
            [ ] Comisiones por ventas
            [ ] Bonificaciones por productividad
            [ ] Horas extras habituales
            [ ] Incentivos por cumplimiento
            ```
            
            ### 🎁 **INGRESOS OCASIONALES** (No se incluyen)
            ```
            - Prima de servicios
            - Prima de navidad
            - Bonificaciones extraordinarias
            - Vacaciones compensadas
            ```
            
            ## ⏰ **TIEMPOS DE EVALUACIÓN**
            
            ### 🚀 **APROBACIÓN RÁPIDA** (24-48 horas)
            ```
            Requisitos:
            [ ] Empleado público o empresa reconocida
            [ ] Antigüedad mayor a 2 años
            [ ] Ingresos >$3,000,000
            [ ] Score crediticio >650
            [ ] Documentos completos
            ```
            
            ### 📋 **EVALUACIÓN ESTÁNDAR** (48-72 horas)
            ```
            Casos regulares:
            [ ] Empleado empresa mediana/grande
            [ ] Antigüedad 6 meses - 2 años
            [ ] Ingresos $1,500,000 - $3,000,000
            [ ] Score crediticio 500-650
            ```
            
            ### 🔍 **EVALUACIÓN ESPECIAL** (3-5 días)
            ```
            Requiere análisis:
            [ ] Empresa pequeña o nueva
            [ ] Antigüedad menor a 6 meses
            [ ] Ingresos <$1,500,000
            [ ] Score crediticio <500
            [ ] Contratos temporales
            ```
            
            ## 🎯 **CONSEJOS PARA EMPLEADOS**
            
            ### ✅ **OPTIMICE SU PERFIL**
            
            1. **Estabilidad Laboral**
               - Mantenga su empleo durante el proceso
               - Evite cambios laborales recientes
               - Documente promociones o aumentos
            
            2. **Manejo Financiero**
               - Mantenga cuenta nómina activa y con movimiento
               - Evite sobregiros o rechazos de pagos
               - Reduzca uso de tarjetas de crédito antes de aplicar
            
            3. **Documentación**
               - Solicite certificado laboral actualizado mensualmente
               - Mantenga copias de nóminas organizadas
               - Actualice datos en centrales de riesgo
            
            ### 📞 **VERIFICACIONES LABORALES**
            
            ⚠️ **Su empleador será contactado para:**
            - Confirmar información del certificado laboral
            - Verificar antigüedad y estabilidad
            - Validar salarios y tipo de contrato
            - Consultar sobre proyección laboral
            
            📝 **Prepare a su empleador informando:**
            - Solicitud de crédito en proceso
            - Posible llamada de verificación
            - Datos del contacto autorizado en la empresa
            
            ---
            
            💡 **Recuerde:** Como empleado dependiente tiene ventajas
            en tasas de interés y condiciones de aprobación.
            Aproveche su estabilidad laboral para obtener las mejores condiciones.
            """;
    }

    private String getIndependentChecklist() {
        return """
            ✅ **CHECKLIST DOCUMENTOS - PROFESIONAL INDEPENDIENTE**
            
            ## 📋 Lista de Verificación Específica
            
            ### 👤 **IDENTIFICACIÓN PERSONAL**
            ```
            [ ] Cédula de ciudadanía vigente
            [ ] Certificado electoral vigente
            [ ] Registro civil (si se requiere)
            ```
            
            ### 🏛️ **DOCUMENTOS TRIBUTARIOS OBLIGATORIOS**
            ```
            [ ] Declaración de renta últimos 2 años consecutivos
                [ ] Formulario 210 completo
                [ ] Sello de presentación DIAN
                [ ] Anexos correspondientes
            
            [ ] RUT actualizado (máx. 30 días)
                [ ] Actividades económicas detalladas
                [ ] Régimen tributario especificado
            ```
            
            ### 💼 **DOCUMENTOS PROFESIONALES**
            ```
            [ ] Tarjeta profesional vigente (si aplica)
            [ ] Matrícula mercantil vigente (comerciantes)
            [ ] Licencias o permisos especiales de operación
            [ ] Registro en cámaras o colegios profesionales
            ```
            
            ### 📊 **ESTADOS FINANCIEROS PERSONALES**
            ```
            [ ] Balance General personal (máx. 6 meses)
            [ ] Estado de Resultados personal
            [ ] Flujo de caja proyectado 12 meses
            [ ] Certificación de ingresos por contador público
                [ ] Promedio mensual últimos 12 meses
                [ ] Tarjeta profesional contador vigente
                [ ] Firma y sello contador
            ```
            
            ### 🏦 **INFORMACIÓN FINANCIERA BANCARIA**
            ```
            [ ] Extractos bancarios últimos 6 meses consecutivos
                [ ] Cuenta principal de ingresos
                [ ] Todas las cuentas de manejo comercial
                [ ] Movimientos coherentes con actividad
            
            [ ] Certificaciones bancarias
                [ ] Saldos promedio últimos 6 meses
                [ ] Comportamiento de cuentas
                [ ] Productos financieros actuales
            ```
            
            ### 💳 **MANEJO CREDITICIO**
            ```
            [ ] Extractos tarjetas de crédito últimos 3 meses
            [ ] Certificación centrales de riesgo
            [ ] Relación de obligaciones financieras actuales
            [ ] Historial de pagos proveedores principales
            ```
            
            ## 📋 **DOCUMENTOS SEGÚN ACTIVIDAD PROFESIONAL**
            
            ### ⚕️ **PROFESIONALES DE LA SALUD**
            ```
            Adicionales específicos:
            [ ] Registro médico vigente
            [ ] Certificación EPS (si atiende por este sistema)
            [ ] Contratos con clínicas/hospitales
            [ ] Seguros de responsabilidad civil
            [ ] Certificación ingresos por procedimientos
            ```
            
            ### ⚖️ **ABOGADOS**
            ```
            Adicionales específicos:
            [ ] Tarjeta profesional vigente
            [ ] Certificación Consejo Superior de la Judicatura
            [ ] Relación de casos principales
            [ ] Contratos con clientes corporativos
            [ ] Oficina jurídica registrada
            ```
            
            ### 🏗️ **INGENIEROS/ARQUITECTOS**
            ```
            Adicionales específicos:
            [ ] Tarjeta profesional vigente
            [ ] Registro ante COPNIA
            [ ] Contratos de obra principales
            [ ] Certificación de experiencia en proyectos
            [ ] Seguros de responsabilidad profesional
            ```
            
            ### 💻 **CONSULTORES/TECNOLOGÍA**
            ```
            Adicionales específicos:
            [ ] Certificaciones técnicas
            [ ] Contratos de servicios principales
            [ ] Portafolio de clientes
            [ ] Facturación electrónica últimos 12 meses
            [ ] Registro de propiedad intelectual (si aplica)
            ```
            
            ### 🎨 **PROFESIONES CREATIVAS**
            ```
            Adicionales específicos:
            [ ] Portafolio de trabajos
            [ ] Contratos con agencias/estudios
            [ ] Certificación de regalías (si aplica)
            [ ] Facturación por proyectos
            [ ] Referencias de clientes principales
            ```
            
            ## 💰 **CERTIFICACIÓN DE INGRESOS**
            
            ### 📊 **METODOLOGÍA DE CÁLCULO**
            ```
            [ ] Promedio ingresos mensuales últimos 12 meses
            [ ] Eliminar ingresos extraordinarios no recurrentes
            [ ] Considerar estacionalidad del negocio
            [ ] Aplicar factor de riesgo según actividad
            [ ] Verificar coherencia con patrimonio declarado
            ```
            
            ### 📈 **INGRESOS VARIABLES - ESTACIONALIDAD**
            ```
            [ ] Identificar meses de mayor/menor actividad
            [ ] Calcular promedio ponderado
            [ ] Proyectar ingresos conservadoramente
            [ ] Documentar reservas para meses bajos
            [ ] Plan de contingencia financiera
            ```
            
            ### 🔍 **VALIDACIÓN CRUZADA**
            ```
            [ ] Ingresos declarados vs. movimientos bancarios
            [ ] Patrimonio vs. capacidad de generación
            [ ] Gastos personales vs. ingresos netos
            [ ] Inversiones vs. flujo de efectivo
            ```
            
            ## 📝 **REFERENCIAS ESPECIALIZADAS**
            
            ### 🏢 **REFERENCIAS COMERCIALES** (Mínimo 3)
            ```
            [ ] Principales clientes corporativos
                [ ] Carta en papel membrete
                [ ] Tiempo de relación comercial
                [ ] Volumen de operaciones
                [ ] Comportamiento de pagos
            
            [ ] Proveedores principales
                [ ] Certificación de cumplimiento
                [ ] Líneas de crédito otorgadas
                [ ] Historial de pagos
            ```
            
            ### 🏦 **REFERENCIAS FINANCIERAS**
            ```
            [ ] Entidades bancarias actuales
            [ ] Productos financieros manejados
            [ ] Comportamiento crediticio
            [ ] Capacidad de endeudamiento utilizada
            ```
            
            ### 👥 **REFERENCIAS PROFESIONALES**
            ```
            [ ] Colegas de profesión
            [ ] Asociaciones profesionales
            [ ] Clientes de larga data
            [ ] Socios comerciales
            ```
            
            ## ⏰ **CRONOGRAMA DE EVALUACIÓN**
            
            ### 📅 **FASE 1: DOCUMENTACIÓN** (2-3 días)
            ```
            [ ] Entrega documentos completos
            [ ] Verificación formal de requisitos
            [ ] Solicitud de aclaraciones si es necesario
            ```
            
            ### 📅 **FASE 2: VERIFICACIONES** (3-5 días)
            ```
            [ ] Validación con contador público
            [ ] Verificación referencias comerciales
            [ ] Consulta centrales de riesgo
            [ ] Verificación ingresos con DIAN
            ```
            
            ### 📅 **FASE 3: ANÁLISIS** (2-3 días)
            ```
            [ ] Evaluación capacidad de pago
            [ ] Análisis estabilidad ingresos
            [ ] Proyección financiera
            [ ] Decisión crediticia
            ```
            
            ## 🎯 **CONSEJOS PARA INDEPENDIENTES**
            
            ### ✅ **FORTALEZCA SU PERFIL**
            
            1. **Documentación Contable**
               - Mantenga contabilidad ordenada y actualizada
               - Use contador público reconocido
               - Presente declaraciones de renta oportunamente
               - Conserve soportes de todos los ingresos
            
            2. **Flujos Bancarios**
               - Maneje cuentas empresariales separadas de personales
               - Evite manejar efectivo en exceso
               - Registre todos los ingresos bancariamente
               - Mantenga saldos coherentes con la actividad
            
            3. **Estabilidad del Negocio**
               - Diversifique su cartera de clientes
               - Establezca contratos a largo plazo
               - Mantenga reservas para contingencias
               - Documente el crecimiento del negocio
            
            ### 💡 **OPTIMIZACIÓN DE APROBACIÓN**
            
            - **Timing:** Aplique en meses de buenos ingresos
            - **Documentación:** Presente información adicional que respalde la estabilidad
            - **Referencias:** Use clientes de prestigio como referencias
            - **Patrimonio:** Incluya bienes que respalden la capacidad de pago
            
            ---
            
            📞 **Asesoría Especializada:** Los profesionales independientes
            cuentan con acompañamiento personalizado para optimizar
            su perfil crediticio y obtener las mejores condiciones.
            """;
    }
}