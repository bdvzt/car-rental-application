CREATE TABLE bookings
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    user_id    UUID        NOT NULL,
    car_id     UUID        NOT NULL,

    status     VARCHAR(50) NOT NULL,
    price      NUMERIC,

    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at TIMESTAMP
);

CREATE TABLE booking_history
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    booking_id UUID        NOT NULL REFERENCES bookings (id) ON DELETE CASCADE,
    status     VARCHAR(50) NOT NULL,
    changed_at TIMESTAMP   NOT NULL DEFAULT now()
);
