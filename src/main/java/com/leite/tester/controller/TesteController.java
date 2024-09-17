package com.leite.tester.controller;

import com.leite.tester.model.Teste;
import com.leite.tester.repository.TesteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/public/api/teste")
public class TesteController {

    private final TesteRepository testeRepository;

    @GetMapping("/{teste}")
    public ResponseEntity<String> testePersist(@PathVariable(name = "teste") String teste) {
        return ResponseEntity.ok(testeRepository.save(new Teste(teste)).getTeste());
    }
}
