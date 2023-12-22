package org.example;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.Binary;
import ij.process.ColorProcessor;

import java.awt.*;

public class Handwritten {

    private static final String PNG           = "PNG";
    private static final Integer TOLERANCE    = 30;

    public void extract(String path) {
        final ImagePlus image = IJ.openImage(path);
        final ColorProcessor colorProcessor = (ColorProcessor) image.getProcessor();
        final Binary binary = new Binary();

        binary.setup("", image);
        binary.run(colorProcessor);

        invertAndErode(colorProcessor);

        extractHandwrittenText(colorProcessor);

        colorProcessor.erode();

        cropImage(image);
        saveImage(image, path);
    }

    private void cropImage(final ImagePlus image) {
        IJ.run(image, "8-bit", "");
        IJ.run(image, "Convert to Mask", "");
        IJ.run(image, "Despeckle", "");
        IJ.run(image, "Create Selection", "");
        IJ.run(image, "Fit Rectangle", "");
        IJ.run(image, "Crop", "");
    }

    private void extractHandwrittenText(final ColorProcessor colorProcessor) {
        for (int x = 0; x < colorProcessor.getWidth(); x++) {
            for (int y = 0; y < colorProcessor.getHeight(); y++) {
                final Color pixelColor = colorProcessor.getColor(x, y);

                final int red = pixelColor.getRed();
                final int green = pixelColor.getGreen();
                final int blue = pixelColor.getBlue();

                if ((Math.abs(red - green) <= TOLERANCE
                        && Math.abs(red - blue) <= TOLERANCE
                        && Math.abs(green - blue) <= TOLERANCE)
                        || (green > TOLERANCE && blue > TOLERANCE && red < 3 * TOLERANCE)
                        || (y < 5)) {

                    colorProcessor.putPixel(x, y, 0);
                }
            }
        }
    }

    private void invertAndErode(final ColorProcessor colorProcessor) {
        colorProcessor.invert();
        colorProcessor.erode();
    }

    private void saveImage(final ImagePlus image, String path) {
        IJ.saveAs(
                image,
                PNG,
                "/Users/karinebeglaryan/desktop/image_midterm/H007/877.png"
        );
    }

}
