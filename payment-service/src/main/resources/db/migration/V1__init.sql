CREATE TABLE payments
(
    id             UUID PRIMARY KEY,
    user_id        UUID        NOT NULL,
    booking_id     UUID        NOT NULL,
    price          NUMERIC     NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITHOUT TIME ZONE
);