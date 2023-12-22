package org.example;

import net.sourceforge.tess4j.*;

import java.io.File;

public class OCRExample {

    public static void main(String[] args) {
        String imagePath = "src/main/resources/OOP.MT.170317.H007.p1.copy.jpg";

        Tesseract tesseract = new Tesseract();

        tesseract.setDatapath("src/main/resources/eng.traineddata");

        try {
            File imageFile = new File(imagePath);
            String extractedText = tesseract.doOCR(imageFile);
            System.out.println("Extracted Text:");
            System.out.println(extractedText);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}

