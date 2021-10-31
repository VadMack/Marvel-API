package com.vadmack.superheroes.controllers;

import com.vadmack.superheroes.entities.Character;
import com.vadmack.superheroes.entities.Comic;
import com.vadmack.superheroes.repositories.CharacterRepository;
import com.vadmack.superheroes.repositories.ComicRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/public/characters")
public class CharacterController {

  private final CharacterRepository characterRepository;
  private final ComicRepository comicRepository;

  @Operation(
      summary = "Search characters",
      description = "Fetches lists of comic characters with optional filters."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Success"
  )
  @GetMapping
  public ResponseEntity<Page<Character>> findCharacters(Pageable pageable,
      @RequestParam(required = false) String name,
      @RequestParam(required = false, defaultValue = "") String nameStartsWith) {
    Page<Character> page;
    if (name != null) {
      page = characterRepository.findByName(pageable, name);
    } else {
      page = characterRepository.findByNameStartsWithOrderByName(pageable, nameStartsWith);
    }
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  @Operation(
      summary = "Search character by id",
      description = "This method fetches a single character resource."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Success"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Character not found."
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Character> findCharacterById(
      @PathVariable(value = "id") Long id) {
    return characterRepository.findById(id).map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(
      summary = "Search comics by character's id",
      description = "Fetches lists of comics with optional filters."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Success"
  )
  @GetMapping("/{id}/comics")
  public ResponseEntity<Page<Comic>> findComicsByCharacterId(
      Pageable pageable,
      @PathVariable(value = "id") Long id,
      @RequestParam(required = false) String title,
      @RequestParam(required = false, defaultValue = "") String titleStartsWith) {
    Page<Comic> page;
    if (title != null) {
      page = comicRepository.findByCharacters_Id_AndTitle(pageable, id, title);
    } else {
      page = comicRepository
          .findByCharacters_Id_AndTitleStartsWithOrderByTitle(pageable, id, titleStartsWith);
    }
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  @Operation(
      summary = "Create a new character"
  )
  @ApiResponse(
      responseCode = "201",
      description = "Character created"
  )
  @PostMapping
  public ResponseEntity<Character> createCharacter(@RequestBody Character character) {
    characterRepository.save(character);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update character",
      description = "Edit character that already exist"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "Character edited"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Character not found"
      )
  })
  @PutMapping("/{id}")
  public ResponseEntity<Character> updateCharacter(@RequestBody Character character,
      @PathVariable Long id) {
    if (!characterRepository.existsById(id)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      characterRepository.save(character);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }
}
