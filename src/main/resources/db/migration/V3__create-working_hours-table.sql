CREATE TABLE working_hours (
    id UUID PRIMARY KEY UNIQUE NOT NULL,
    day_of_week VARCHAR(20) NOT NULL CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY')),
    hour_of_day TIME NOT NULL
)
