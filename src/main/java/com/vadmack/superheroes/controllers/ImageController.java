package com.vadmack.superheroes.controllers;

import com.vadmack.superheroes.entities.Image;
import com.vadmack.superheroes.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/public/images")
public class ImageController {

  private final ImageService imageService;

  @Operation(
      summary = "Download image",
      description = "Download image from server"
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
  @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<FileSystemResource> downloadImage(@PathVariable(name = "id") Long id) {
    final Optional<FileSystemResource> image = imageService.getImageById(id);
    return image
        .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(
      summary = "Upload image",
      description = "Upload image to server."
  )
  @ApiResponse(
      responseCode = "201",
      description = "Image successfully uploaded"
  )
  @PostMapping
  public ResponseEntity<Image> uploadImage(@RequestParam MultipartFile image) throws Exception {
    return new ResponseEntity<>(
        imageService.createImage(image.getBytes(), image.getOriginalFilename()),
        HttpStatus.CREATED);
  }
}
