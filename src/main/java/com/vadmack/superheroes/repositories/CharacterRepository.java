package com.vadmack.superheroes.repositories;

import com.vadmack.superheroes.entities.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

  Page<Character> findByNameStartsWithOrderByName(Pageable pageable, String nameStartsWith);

  Page<Character> findByName(Pageable pageable, String name);

  Page<Character> findByComics_Id_AndName(Pageable pageable, Long id, String name);

  Page<Character> findByComics_Id_AndNameStartingWithOrderByName(Pageable pageable, Long id,
      String nameStartsWith);
}
