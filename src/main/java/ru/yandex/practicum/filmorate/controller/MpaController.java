package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService service;

    @GetMapping("/{mpaId}")
    public Optional<Mpa> getMpa(@PathVariable("mpaId") String id) {
        return service.getMpaById(Integer.parseInt(id));
    }

    @GetMapping
    public Optional<List<Mpa>> getAllMpa() {
        return service.getAllMpa();
    }
}