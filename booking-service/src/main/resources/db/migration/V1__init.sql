CREATE TYPE booking_status AS ENUM ('RESERVED', 'PAID', 'CANCELLED', 'COMPLETED');

CREATE TABLE bookings
(
    id         UUID PRIMARY KEY        DEFAULT gen_random_uuid(),

    user_id    UUID           NOT NULL,
    car_id     UUID           NOT NULL,

    start_date TIMESTAMP      NOT NULL,
    end_date   TIMESTAMP      NOT NULL,

    status     booking_status NOT NULL,

    created_at TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at TIMESTAMP
);

CREATE TABLE booking_history
(
    id         UUID PRIMARY KEY        DEFAULT gen_random_uuid(),

    booking_id UUID           NOT NULL REFERENCES bookings (id) ON DELETE CASCADE,
    status     booking_status NOT NULL,
    changed_at TIMESTAMP      NOT NULL DEFAULT now()
);
