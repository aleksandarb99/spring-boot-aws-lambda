package com.levi9.springbootawslambda.services;

import com.levi9.springbootawslambda.dtos.GenerateFunnyNameDto;
import com.levi9.springbootawslambda.entities.FunnyName;
import com.levi9.springbootawslambda.repositories.FunnyNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FunnyNameService {
    final static List<String> namePrefixes = Arrays.asList("Huckleberry", "Sizzle Pants", "Itsy Bitsy", "Speedy", "Mighty", "Legendary", "Jumpy");
    private final Random randomizer = new Random();
    private final FunnyNameRepository funnyNameRepository;

    public FunnyName getOneById(String funnyNameId) {
        return funnyNameRepository.getName(funnyNameId).orElseThrow();
    }

    public void createOne(GenerateFunnyNameDto dto) {
        funnyNameRepository.saveName(
                FunnyName.builder()
                        .id(UUID.randomUUID().toString())
                        .name(namePrefixes.get(randomizer.nextInt(namePrefixes.size())) + " " + dto.getName())
                        .generatedAt(Instant.now())
                        .build()

        );
    }
}
