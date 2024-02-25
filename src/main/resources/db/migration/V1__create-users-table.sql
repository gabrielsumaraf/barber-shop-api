CREATE TYPE user_role  AS ENUM ('CUSTOMER', 'OWNER');

CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL
)