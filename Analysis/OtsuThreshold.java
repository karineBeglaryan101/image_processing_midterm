import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class OtsuThreshold implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        int[] histogram = ip.getHistogram();
        int totalPixels = ip.getWidth() * ip.getHeight();
        double sum = 0;

        for (int i = 0; i < histogram.length; i++) {
            sum += i * histogram[i];
        }

        double sumB = 0;
        int wB = 0;
        int wF;
        double varMax = 0;
        int threshold = 0;

        for (int i = 0; i < histogram.length; i++) {
            wB += histogram[i];
            if (wB == 0) continue;
            wF = totalPixels - wB;
            if (wF == 0) break;

            sumB += i * histogram[i];
            double meanB = sumB / wB;
            double meanF = (sum - sumB) / wF;

            double varBetween = wB * wF * Math.pow((meanB - meanF), 2);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        ip.threshold(threshold);
    }
}
