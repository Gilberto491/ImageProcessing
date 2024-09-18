package com.api.imageprocessing.controller;

import com.api.imageprocessing.service.ImageProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageProcessingService imageProcessingService;

    public ImageController(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());

        imageProcessingService.detectAnomalies(image);

        return ResponseEntity.ok("Image processed successfully!");
    }
}
