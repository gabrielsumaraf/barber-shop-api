CREATE TABLE barber_services (
    id UUID PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    price FLOAT NOT NULL,
    image OID
)