package com.example.carservice.kafka.listener;

import com.example.carservice.entities.Car;
import com.example.carservice.security.JwtUtils;
import dtos.responses.CarStatus;
import com.example.carservice.kafka.sender.KafkaCarSender;
import com.example.carservice.repositories.CarRepository;
import dtos.kafka.BookingEvent;
import dtos.kafka.CarEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final CarRepository carRepository;
    private final KafkaCarSender kafkaCarSender;
    @KafkaListener(
            topics = "booking-event",
            containerFactory = "bookingListenerFactory"
    )
    public void handleBookingCreated(BookingEvent event) {
        UUID carId = event.getCarId();
        UUID bookingId = event.getBookingId();
        String email = event.getEmail();

        Optional<Car> optionalCar = carRepository.findById(carId);

        if (optionalCar.isEmpty()) {
            kafkaCarSender.sendCarReservedEvent(new CarEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Машина не найдена",
                    email
            ));
            return;
        }

        Car car = optionalCar.get();

        if (car.getStatus() == CarStatus.BOOKED) {
            kafkaCarSender.sendCarReservedEvent(new CarEvent(
                    bookingId,
                    carId,
                    car.getPricePerDay(),
                    false,
                    "Машина уже забронирована",
                    email
            ));
            return;
        } else if (car.getStatus() == CarStatus.UNDER_REPAIR) {
            kafkaCarSender.sendCarReservedEvent(new CarEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Машина в ремонте",
                    email
            ));
            return;
        }

        try {
            car.setStatus(CarStatus.BOOKED);
            carRepository.save(car);
            kafkaCarSender.sendCarReservedEvent(new CarEvent(
                    bookingId,
                    carId,
                    car.getPricePerDay(),
                    true,
                    "Машина успешно забронирована",
                    email
            ));

        } catch (Exception e) {
            kafkaCarSender.sendCarReservedEvent(new CarEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Ошибка при сохранении машины",
                    email
            ));
        }
    }

    @KafkaListener(
            topics = "booking-completed-event",
            groupId = "car-group",
            containerFactory = "bookingListenerFactory"
    )

    public void handleBookingCompleted(BookingEvent event) {
        UUID carId = event.getCarId();
        UUID bookingId = event.getBookingId();
        String email = event.getEmail();

        Optional<Car> optionalCar = carRepository.findById(carId);

        if (optionalCar.isEmpty()) {
            kafkaCarSender.sendCarFreedEvent(new CarEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Машина не найдена при завершении аренды",
                    email
            ));
            return;
        }

        Car car = optionalCar.get();

        if (car.getStatus() != CarStatus.BOOKED) {
            kafkaCarSender.sendCarFreedEvent(new CarEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Машина не находится в статусе BOOKED",
                    email
            ));
            return;
        }

        try {
            car.setStatus(CarStatus.AVAILABLE);
            carRepository.save(car);

            kafkaCarSender.sendCarFreedEvent(new CarEvent(
                    bookingId,
                    carId,
                    car.getPricePerDay(),
                    true,
                    "Аренда завершена, машина освобождена",
                    email
            ));

        } catch (Exception e) {
            kafkaCarSender.sendCarFreedEvent(new CarEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Ошибка при завершении аренды",
                    email
            ));
        }
    }
}
