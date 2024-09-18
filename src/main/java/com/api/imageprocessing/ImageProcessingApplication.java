package com.api.imageprocessing;

import nu.pattern.OpenCV;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageProcessingApplication {

    public static void main(String[] args) {
        OpenCV.loadLocally();
        SpringApplication.run(ImageProcessingApplication.class, args);
    }

}
