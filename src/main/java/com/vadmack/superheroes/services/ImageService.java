package com.vadmack.superheroes.services;

import com.vadmack.superheroes.entities.Image;
import com.vadmack.superheroes.repositories.FileSystemRepository;
import com.vadmack.superheroes.repositories.ImageRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ImageService {

  private final FileSystemRepository fileSystemRepository;
  private final ImageRepository imageRepository;

  public Image createImage(byte[] bytes, String imageName) throws Exception {
    String location = fileSystemRepository.save(bytes, imageName);
    return imageRepository.save(new Image(imageName, location));
  }

  public Optional<FileSystemResource> getImageById(Long id) {
    Optional<Image> image = imageRepository.findById(id);

    if (image.isPresent()) {
      try {
        FileSystemResource resource = fileSystemRepository
            .findInFileSystem(image.get().getLocation());
        return Optional.of(resource);
      } catch (Exception e) {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }
}
