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
@RequestMapping("/v1/public/comics")
public class ComicController {

  private final ComicRepository comicRepository;
  private final CharacterRepository characterRepository;

  @Operation(
      summary = "Search comics",
      description = "Fetches lists of comics with optional filters."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Success"
  )
  @GetMapping
  public ResponseEntity<Page<Comic>> findComics(Pageable pageable,
      @RequestParam(required = false) String title,
      @RequestParam(required = false, defaultValue = "") String titleStartsWith) {
    Page<Comic> page;
    if (title != null) {
      page = comicRepository.findByTitle(pageable, title);
    } else {
      page = comicRepository.findByTitleStartsWithOrderByTitle(pageable, titleStartsWith);
    }
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  @Operation(
      summary = "Search characters by comic's id",
      description = "Fetches lists of characters with optional filters."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Success"
  )
  @GetMapping("/{id}/characters")
  public ResponseEntity<Page<Character>> findCharacterByComicId(
      Pageable pageable,
      @PathVariable(value = "id") Long id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false, defaultValue = "") String nameStartsWith) {
    Page<Character> page;
    if (name != null) {
      page = characterRepository.findByComics_Id_AndName(pageable, id, name);
    } else {
      page = characterRepository
          .findByComics_Id_AndNameStartingWithOrderByName(pageable, id, nameStartsWith);
    }
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  @Operation(
      summary = "Search comic by id",
      description = "This method fetches a single comic resource."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Success"
      ),
      @ApiResponse(
          responseCode = "200",
          description = "Comic not found."
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Comic> findCharacterById(
      @PathVariable(value = "id") Long id) {
    return comicRepository.findById(id).map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(
      summary = "Create new comic"
  )
  @ApiResponse(
      responseCode = "201",
      description = "Comic created"
  )
  @PostMapping
  public ResponseEntity<Comic> createComic(@RequestBody Comic comic) {
    comicRepository.save(comic);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update comic",
      description = "Edit comic that already exist"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "Comic edited"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Comic not found"
      )
  })
  @PutMapping("/{id}")
  public ResponseEntity<Comic> updateCharacter(@RequestBody Comic comic,
      @PathVariable Long id) {
    if (!comicRepository.existsById(id)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      comicRepository.save(comic);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }
}
