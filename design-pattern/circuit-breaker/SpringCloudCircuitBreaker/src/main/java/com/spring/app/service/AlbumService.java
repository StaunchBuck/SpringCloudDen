package com.spring.app.service;

import com.spring.app.dto.Album;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class AlbumService {

    private final CircuitBreakerFactory circuitBreakerFactory;
    private final RestTemplate restTemplate;

    public List<Album> getAlbumList(String failureAppend) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("albumCircuitBreaker");
        String url = "https://jsonplaceholder.typicode.com/albums"+failureAppend;
        //if we have some value in failureAppend then call will fail and fallback to defaultalbumlist

        return circuitBreaker.run(() -> {
                        ResponseEntity<List<Album>> response = restTemplate.exchange(
                                url,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<Album>>() {}
                        );
                    return response.getBody();
                },
                throwable -> getDefaultAlbumList());
    }

    private List<Album> getDefaultAlbumList() {
        return List.of(new Album("5","5","Default Album"));
    }
}