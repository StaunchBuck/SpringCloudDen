package com.spring.app.controller;

import com.spring.app.dto.Album;
import com.spring.app.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AlbumController {

    private final AlbumService service;

    @GetMapping("albums")
    public List<Album> albums(@RequestParam(required = false,defaultValue = "") String append) {
        return service.getAlbumList(append);
    }

}