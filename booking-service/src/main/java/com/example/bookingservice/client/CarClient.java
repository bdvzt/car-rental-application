package com.example.bookingservice.client;

import dtos.responses.CarDetailDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CarClient {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${car-service.url}")
    private String carServiceBaseUrl;

    public CarDetailDTO getCarById(UUID carId) {
        try {
            HttpHeaders headers = new HttpHeaders();

            String token = request.getHeader("Authorization");
            if (token != null && !token.isBlank()) {
                headers.set("Authorization", token);
            }

            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<CarDetailDTO> response = restTemplate.exchange(
                    carServiceBaseUrl + "/car/" + carId,
                    HttpMethod.GET,
                    httpEntity,
                    CarDetailDTO.class
            );

            return response.getBody();

        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException("Машина с ID " + carId + " не найдена");
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new SecurityException("Ошибка авторизации при запросе в car-service");
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка при запросе в car-service: " + e.getMessage());
        }
    }
}

