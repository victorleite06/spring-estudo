package com.leite.tester.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Data
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TESTE")
public class Teste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TESTE")
    private String teste;

    public Teste(String teste) {
        this.teste = teste;
    }
}
