CREATE TABLE appointments (
    id UUID PRIMARY KEY UNIQUE NOT NULL,
    barber_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    barber_service_id UUID NOT NULL,
    date DATE NOT NULL,
    working_hour_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING','FINISHED','CANCELED')) DEFAULT ('CREATED'),
    total FLOAT,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (barber_id) REFERENCES users(id),
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (barber_service_id) REFERENCES barber_services(id),
    FOREIGN KEY (working_hour_id) REFERENCES working_hours(id)
)