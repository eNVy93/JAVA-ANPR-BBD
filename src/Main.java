/**
 * Created by nevyt on 4/29/2017.
 */

import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import utils.Utils;

import java.awt.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        ImageProcessing process = new ImageProcessing();
        ImageProcessingSteps processingWithSteps = new ImageProcessingSteps();
        Utils utils = new Utils();

        String testImagePath = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input\\mazda.jpg";
        Mat testImage = Imgcodecs.imread(testImagePath);
        String path = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input";

        //Sobel kernels
        int kernelSize = 9;
        Mat verticalKernel = new Mat(kernelSize, kernelSize, CvType.CV_32F) {  // veritcal mask
            {
                put(0, 0, -1);
                put(0, 1, 0);
                put(0, 2, 1);

                put(1, 0, -1);
                put(1, 1, 0);
                put(1, 2, 1);

                put(2, 0, -1);
                put(2, 1, 0);
                put(2, 2, 1);
            }
        };
        Mat horizontalKernel = new Mat(kernelSize, kernelSize, CvType.CV_32F) {  // horizontal mask
            {
                put(0, 0, -1);
                put(0, 1, -1);
                put(0, 2, 1);

                put(1, 0, 0);
                put(1, 1, 0);
                put(1, 2, 0);

                put(2, 0, 1);
                put(2, 1, 1);
                put(2, 2, 1);
            }
        };
        Mat verticalSobelKernel = new Mat(kernelSize, kernelSize, CvType.CV_32F) {  // veritcal mask
            {
                put(0, 0, -1);
                put(0, 1, -2);
                put(0, 2, -1);

                put(1, 0, 0);
                put(1, 1, 0);
                put(1, 2, 0);

                put(2, 0, 1);
                put(2, 1, 2);
                put(2, 2, 1);
            }
        };
        Mat horizontalSobelKernel = new Mat(kernelSize, kernelSize, CvType.CV_32F) {  // horizontal mask
            {
                put(0, 0, -1);
                put(0, 1, 0);
                put(0, 2, 1);

                put(1, 0, -2);
                put(1, 1, 0);
                put(1, 2, 2);

                put(2, 0, -1);
                put(2, 1, 0);
                put(2, 2, 1);
            }
        };

        ArrayList<String> filePathList = utils.filePathsToList(path);
        ArrayList<Mat> matImagesList = utils.filePathsToMatImageList(filePathList);
        ArrayList<BufferedImage> processedImagesList = process.filterImages(matImagesList);
        utils.outputImages(processedImagesList,"final"); // all steps together


        //STEPS.............................................................................\
        //1 - toGrayscale -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_
        ArrayList<Mat> grayImageList = processingWithSteps.toGrayscaleList(matImagesList);
        ArrayList<BufferedImage> grayImageListBuffered = utils.matToBufferedImage(grayImageList);
        utils.outputImages(grayImageListBuffered,ImageProcessingSteps.GRAYSCALE);
        //2 - Gaussian blur -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_
        ArrayList<Mat> gaussianImageList = processingWithSteps.gausianBlur(grayImageList);
        ArrayList<BufferedImage> gaussianImageListBuffered = utils.matToBufferedImage(gaussianImageList);
        utils.outputImages(gaussianImageListBuffered,ImageProcessingSteps.GAUSSIAN);
        //3 - Dilation -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_
        ArrayList<Mat> dilatedImageList = processingWithSteps.imageDilate(gaussianImageList);
        ArrayList<BufferedImage> dilatedImageListBuffered = utils.matToBufferedImage(dilatedImageList);
        utils.outputImages(dilatedImageListBuffered,ImageProcessingSteps.DILATION);
        //4 - Sobel  -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_
        ArrayList<Mat> sobelImageList = processingWithSteps.imageSobel(dilatedImageList); // default kernel - vertical
        ArrayList<BufferedImage> sobelImageListBuffered = utils.matToBufferedImage(sobelImageList);
        utils.outputImages(sobelImageListBuffered,ImageProcessingSteps.SOBEL);
        //5 - Threshold -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_
        ArrayList<Mat> thresholdImageList = processingWithSteps.imageThreshold(dilatedImageList);
        ArrayList<BufferedImage> thresholdImageListBuffered = utils.matToBufferedImage(thresholdImageList);
        utils.outputImages(thresholdImageListBuffered,ImageProcessingSteps.THRESHOLD);

        //6 - Canny edge detection. (Bandau su grayscale)
        ArrayList<Mat> cannyImageList = processingWithSteps.cannyEdgeDetection(grayImageList);
        ArrayList<BufferedImage> cannyImageListBuffered = utils.matToBufferedImage(cannyImageList);
        utils.outputImages(cannyImageListBuffered,ImageProcessingSteps.CANNY);
    }

}
