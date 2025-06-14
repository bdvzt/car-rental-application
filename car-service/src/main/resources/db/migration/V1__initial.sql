CREATE TYPE car_status AS ENUM ('AVAILABLE', 'BOOKED', 'UNDER_REPAIR');

CREATE TABLE car_models
(
    id         UUID PRIMARY KEY,
    brand      VARCHAR(255) NOT NULL,
    model      VARCHAR(255) NOT NULL,
    year       INT          NOT NULL,
    color      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    created_by UUID         NOT NULL
);

CREATE TABLE cars
(
    id            UUID PRIMARY KEY,
    car_number    VARCHAR(20) UNIQUE NOT NULL,
    car_model_id  UUID               NOT NULL REFERENCES car_models (id),
    price_per_day NUMERIC(10, 2)     NOT NULL,
    description   VARCHAR(1000),
    status        car_status         NOT NULL,
    created_at    TIMESTAMP          NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP,
    created_by    UUID               NOT NULL
);

