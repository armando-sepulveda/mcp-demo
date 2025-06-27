-- V1__Create_tables.sql
-- Creación de tablas para el sistema de crédito automotriz

-- Tabla de clientes
CREATE TABLE customers (
    document_number VARCHAR(20) PRIMARY KEY,
    document_type VARCHAR(20) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    birth_date DATE NOT NULL,
    monthly_income DECIMAL(15,2) NOT NULL,
    current_monthly_debts DECIMAL(15,2) DEFAULT 0,
    occupation VARCHAR(100) NOT NULL,
    work_experience_months INTEGER,
    created_date DATE DEFAULT CURRENT_DATE,
    last_updated DATE DEFAULT CURRENT_DATE
);

-- Tabla de aplicaciones de crédito
CREATE TABLE credit_applications (
    id VARCHAR(36) PRIMARY KEY,
    customer_document VARCHAR(20) NOT NULL,
    vehicle_vin VARCHAR(17) NOT NULL,
    requested_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    credit_score INTEGER,
    rejection_reason TEXT,
    application_date TIMESTAMP NOT NULL,
    last_update_date TIMESTAMP NOT NULL,
    vehicle_brand VARCHAR(50),
    vehicle_model VARCHAR(50),
    vehicle_year INTEGER,
    vehicle_value DECIMAL(15,2),
    vehicle_kilometers INTEGER,
    FOREIGN KEY (customer_document) REFERENCES customers(document_number)
);

-- Índices para optimizar consultas
CREATE INDEX idx_credit_applications_customer ON credit_applications(customer_document);
CREATE INDEX idx_credit_applications_status ON credit_applications(status);
CREATE INDEX idx_credit_applications_date ON credit_applications(application_date);
CREATE INDEX idx_customers_email ON customers(email);

-- Comentarios de documentación
COMMENT ON TABLE customers IS 'Tabla de clientes del sistema de crédito automotriz';
COMMENT ON TABLE credit_applications IS 'Tabla de aplicaciones de crédito automotriz';

COMMENT ON COLUMN customers.document_number IS 'Número de documento único del cliente';
COMMENT ON COLUMN customers.monthly_income IS 'Ingresos mensuales declarados en pesos colombianos';
COMMENT ON COLUMN credit_applications.id IS 'Identificador único de la aplicación de crédito';
COMMENT ON COLUMN credit_applications.requested_amount IS 'Monto solicitado en pesos colombianos';