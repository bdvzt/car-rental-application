package com.example.carservice.kafka.listener;

import com.example.carservice.entities.Car;
import com.example.carservice.entities.enums.CarStatus;
import com.example.carservice.kafka.sender.KafkaCarSender;
import com.example.carservice.repositories.CarRepository;
import dtos.kafka.BookingCreatedEvent;
import dtos.kafka.CarReservedEvent;
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
            groupId = "car-group",
            containerFactory = "bookingCreatedListenerFactory"
    )
    public void handleBookingCreated(BookingCreatedEvent event) {
        UUID carId = event.getCarId();
        UUID bookingId = event.getBookingId();

        Optional<Car> optionalCar = carRepository.findById(carId);

        if (optionalCar.isEmpty()) {
            kafkaCarSender.sendCarReservedEvent(new CarReservedEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Машина не найдена"
            ));
            return;
        }

        Car car = optionalCar.get();
// TODO: - ПОМЕНЯТЬ ЛОГИКУ ПРОВЕРКИ БРОНИРОВАНИЯ, СДЕЛАТЬ ПРОВЕРКУ БРОНИРОВАНИЯ НА ДАТЫ, А НЕ ПРОСТО ПО СТАТУСУ
        if (car.getStatus() != CarStatus.AVAILABLE) {
            kafkaCarSender.sendCarReservedEvent(new CarReservedEvent(
                    bookingId,
                    carId,
                    car.getPricePerDay(),
                    false,
                    "Машина уже забронирована"
            ));
            return;
        }

        try {
            car.setStatus(CarStatus.BOOKED);
            carRepository.save(car);
            kafkaCarSender.sendCarReservedEvent(new CarReservedEvent(
                    bookingId,
                    carId,
                    car.getPricePerDay(),
                    true,
                    "Машина успешно забронирована"
            ));

        } catch (Exception e) {
            kafkaCarSender.sendCarReservedEvent(new CarReservedEvent(
                    bookingId,
                    carId,
                    null,
                    false,
                    "Ошибка при сохранении машины"
            ));
        }
    }
}
