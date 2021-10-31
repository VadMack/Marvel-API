package com.vadmack.superheroes.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Image entity")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Image {

  @Schema(description = "Image unique identifier. Autogenerated.")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Schema(description = "Path to file")
  @Column(name = "location")
  private String location;

  @Schema(description = "Name of file")
  @Column(name = "name")
  private String name;

  public Image(String name, String location) {
    this.name = name;
    this.location = location;
  }
}