package com.leite.tester.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TESTE")
public class Teste {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TESTE")
    private String teste;

    public Teste(String teste) {
        this.teste = teste;
    }
}
