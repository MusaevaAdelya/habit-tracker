package com.example.habittracker.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.habittracker.exceptions.EmptyFileException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Service
public class ImageUploadService {
    @SneakyThrows
    public String saveImage(MultipartFile multipartfile) {
        if (multipartfile.isEmpty()) {
            throw new EmptyFileException("File is empty");
        }

        final String urlKey = "cloudinary://842186626728578:pfeQ0_a9EVGk5jtxd7yM1_C45Sw@ddwcakuc7";


        Cloudinary cloudinary = new Cloudinary((urlKey));

        File saveFile = Files.createTempFile(
                        System.currentTimeMillis() + "",
                        Objects.requireNonNull
                                (multipartfile.getOriginalFilename(), "File must have an extension")
                )
                .toFile();

        multipartfile.transferTo(saveFile);

        Map uploadOptions = ObjectUtils.asMap( "folder", "habit_tracker_profile_pictures");

        Map upload = cloudinary.uploader().upload(saveFile, uploadOptions);

        return (String) upload.get("url");
    }
}
