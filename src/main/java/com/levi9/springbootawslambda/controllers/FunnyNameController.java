package com.levi9.springbootawslambda.controllers;

import com.levi9.springbootawslambda.dtos.GenerateFunnyNameDto;
import com.levi9.springbootawslambda.entities.FunnyName;
import com.levi9.springbootawslambda.services.FunnyNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/funny-name")
@Slf4j
@RequiredArgsConstructor
public class FunnyNameController {

    private final FunnyNameService funnyNameService;

    @GetMapping("/{id}")
    public FunnyName getOneById(@PathVariable String id) {
        log.info("getOneById executed for funny name with id " + id);
        return funnyNameService.getOneById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOne(@RequestBody GenerateFunnyNameDto dto) {
        log.info("createOne executed for funny name with fields " + dto);
        funnyNameService.createOne(dto);
    }
}
