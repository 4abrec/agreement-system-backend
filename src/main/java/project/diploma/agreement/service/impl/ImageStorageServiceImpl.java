package project.diploma.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.diploma.agreement.domain.ImageDB;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.repository.ImageDBRepository;
import project.diploma.agreement.repository.UserRepository;
import project.diploma.agreement.service.ImageStorageService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService {

    private final ImageDBRepository imageDBRepository;
    private final UserRepository userRepository;

    @Override
    public ImageDB store(MultipartFile file, String username) throws IOException {

        User user = userRepository.findByUsername(username).get();

        List<ImageDB> imageDBList = user.getImage();

        for (ImageDB imageDB: imageDBList) {
            imageDBRepository.deleteById(imageDB.getId());
        }
        String imageName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        ImageDB imageDB = new ImageDB(imageName, file.getContentType(), file.getBytes(), user);
        imageDBRepository.save(imageDB);

        Optional<ImageDB> img = getImageByUsername(username);

        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/images/")
                .path(img.get().getId())
                .toUriString();

        List<ImageDB> images = user.getImage();

        images.add(imageDB);

        user.setImage(images);

        user.setPhotoUrl(fileDownloadUri);

        userRepository.save(user);

        return imageDB;

    }

    @Override
    public ImageDB getFile(String id) {
        return imageDBRepository.getById(id);
    }

    @Override
    public Stream<ImageDB> getAllFiles() {
        return imageDBRepository.findAll().stream();
    }

    @Override
    public Optional<ImageDB> getImageByUsername(String username) {
        return imageDBRepository.findAll().stream()
                .filter(image -> image.getUser().getUsername().equals(username))
                .findFirst();
    }

    @Override
    public MessageResponseDto deleteById(String id) {
        imageDBRepository.deleteById(id);
        return new MessageResponseDto("Файл удален");
    }
}
