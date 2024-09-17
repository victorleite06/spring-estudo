package com.leite.tester.repository;

import com.leite.tester.model.Teste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteRepository extends JpaRepository<Teste, Integer> {

}
