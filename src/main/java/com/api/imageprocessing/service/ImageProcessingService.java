package com.api.imageprocessing.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageProcessingService {

    public void detectAnomalies(BufferedImage image) throws IOException {
        Mat matImage = bufferedImageToMat(image);

        Imgproc.resize(matImage, matImage, new Size(500, 500));

        Mat gray = new Mat();
        Imgproc.cvtColor(matImage, gray, Imgproc.COLOR_BGR2GRAY);

        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 150, 250);

        detectContours(edges, matImage);

        BufferedImage edgeImage = matToBufferedImage(matImage);
        ImageIO.write(edgeImage, "jpg", new File("edges_detected.jpg"));
    }

    private void detectContours(Mat edges, Mat image) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            Rect boundingBox = Imgproc.boundingRect(contour);

            double aspectRatio = (double) boundingBox.width / boundingBox.height;
            if (aspectRatio < 0.5 || aspectRatio > 2.0) {
                Imgproc.rectangle(image, boundingBox.tl(), boundingBox.br(), new Scalar(0, 255, 0), 2);
                System.out.println("Detecção encontrada");
            }
        }
    }

    private Mat bufferedImageToMat(BufferedImage bi) throws IOException {
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Imgcodecs.imdecode(new MatOfByte(byteArray), Imgcodecs.IMREAD_UNCHANGED);
    }

    private BufferedImage matToBufferedImage(Mat mat) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return ImageIO.read(new java.io.ByteArrayInputStream(byteArray));
    }
}