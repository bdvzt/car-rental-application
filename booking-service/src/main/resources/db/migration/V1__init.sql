CREATE TABLE bookings
(
    id         UUID PRIMARY KEY,
    user_id    UUID        NOT NULL,
    car_id     UUID        NOT NULL,
    start_date TIMESTAMP   NOT NULL,
    end_date   TIMESTAMP   NOT NULL,
    status     VARCHAR(20) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE booking_history
(
    id         UUID PRIMARY KEY,
    booking_id UUID        NOT NULL,
    status     VARCHAR(20) NOT NULL,
    changed_at TIMESTAMP   NOT NULL DEFAULT NOW()
);
