CREATE TABLE users (
    id UUID PRIMARY KEY UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(11) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('OWNER', 'CUSTOMER','BARBER')) DEFAULT ('CUSTOMER')
);