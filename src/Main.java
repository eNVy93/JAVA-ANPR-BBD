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
        //4 - Sobel  -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_  // step excluded for now
//        ArrayList<Mat> sobelImageList = processingWithSteps.imageSobel(dilatedImageList); // default kernel - vertical
//        ArrayList<BufferedImage> sobelImageListBuffered = utils.matToBufferedImage(sobelImageList);
//        utils.outputImages(sobelImageListBuffered,ImageProcessingSteps.SOBEL);
        //5 - Threshold -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_
        ArrayList<Mat> thresholdImageList = processingWithSteps.imageThreshold(dilatedImageList);
        ArrayList<BufferedImage> thresholdImageListBuffered = utils.matToBufferedImage(thresholdImageList);
        utils.outputImages(thresholdImageListBuffered,ImageProcessingSteps.THRESHOLD);
    }

//    public static void toHistogram(Mat matImage) {
//        Mat outputImage = new Mat();
//        Imgproc.cvtColor(matImage, outputImage, Imgproc.COLOR_BGR2HSV);
//
//        ArrayList<Mat> hsv_planes = new ArrayList<>();
//        Core.split(matImage, hsv_planes);
//        //hsv_planes.add(Core.split(matImage,hsv_planes))
//
//        MatOfInt histSize = new MatOfInt(256);
//
//        final MatOfFloat histRange = new MatOfFloat(0f, 256f);
//
//        boolean accumulate = false;
//
//        Mat h_hist = new Mat();
//        Mat s_hist = new Mat();
//        Mat v_hist = new Mat();
//
//        Imgproc.calcHist((List<Mat>) hsv_planes.get(0), new MatOfInt(3), new Mat(), h_hist, histSize, histRange, accumulate);
//        Imgproc.calcHist((List<Mat>) hsv_planes.get(1), new MatOfInt(3), new Mat(), s_hist, histSize, histRange, accumulate);
//        Imgproc.calcHist((List<Mat>) hsv_planes.get(2), new MatOfInt(3), new Mat(), v_hist, histSize, histRange, accumulate);
//
//        int hist_w = 512;
//        int hist_h = 600;
//        long bin_w = Math.round((double) hist_w / 256);
//        //bin_w = Math.round((double) (hist_w / 256));
//
//        Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC1);
//        Core.normalize(h_hist, h_hist, 3, histImage.rows(), Core.NORM_MINMAX);
//        Core.normalize(s_hist, s_hist, 3, histImage.rows(), Core.NORM_MINMAX);
//        Core.normalize(v_hist, v_hist, 3, histImage.rows(), Core.NORM_MINMAX);
//
//        System.out.println("Here comes the histogram...");
//        for (int i = 1; i < 256; i++) {
//            Point p1 = new Point(bin_w * (i - 1), hist_h - Math.round(h_hist.get(i - 1, 0)[0]));
//            Point p2 = new Point(bin_w * (i), hist_h - Math.round(h_hist.get(i, 0)[0]));
//            Imgproc.line(histImage, p1, p2, new Scalar(255, 0, 0), 2, 8, 0);
//
//            Point p3 = new Point(bin_w * (i - 1), hist_h - Math.round(s_hist.get(i - 1, 0)[0]));
//            Point p4 = new Point(bin_w * (i), hist_h - Math.round(s_hist.get(i, 0)[0]));
//            Imgproc.line(histImage, p3, p4, new Scalar(0, 255, 0), 2, 8, 0);
//
//            Point p5 = new Point(bin_w * (i - 1), hist_h - Math.round(v_hist.get(i - 1, 0)[0]));
//            Point p6 = new Point(bin_w * (i), hist_h - Math.round(v_hist.get(i, 0)[0]));
//            Imgproc.line(histImage, p5, p6, new Scalar(0, 0, 255), 2, 8, 0);
//
//        }
//
//        Imgcodecs.imwrite("histogram.jpg", histImage);
//
//    }
}
