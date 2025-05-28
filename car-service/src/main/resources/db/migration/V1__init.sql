CREATE TABLE car_models
(
    id         UUID PRIMARY KEY,
    brand      VARCHAR(255) NOT NULL,
    model      VARCHAR(255) NOT NULL,
    year       INTEGER      NOT NULL,
    color      VARCHAR(255) NOT NULL,

    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL
);

CREATE TABLE cars
(
    id            UUID PRIMARY KEY,
    car_number    VARCHAR(20)    NOT NULL UNIQUE,
    car_model_id  UUID           NOT NULL,
    price_per_day NUMERIC(10, 2) NOT NULL,
    description   VARCHAR(1000),

    created_at    TIMESTAMP      NOT NULL,
    updated_at    TIMESTAMP,
    created_by    VARCHAR(255)   NOT NULL,

    CONSTRAINT fk_car_model
        FOREIGN KEY (car_model_id)
            REFERENCES car_models (id)
            ON DELETE CASCADE
);
