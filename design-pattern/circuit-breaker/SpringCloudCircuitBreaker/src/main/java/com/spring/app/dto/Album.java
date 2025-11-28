package com.spring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Album {
    private String userId;
    private String id;
    private String title;
}
