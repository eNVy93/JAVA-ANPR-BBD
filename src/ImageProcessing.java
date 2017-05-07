import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utils.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by nevyt on 4/30/2017.
 */
public class ImageProcessing {
    String step1 = "";
    String step2 = "";
    String step3 = "";
    String step4 = "";
    String step5 = "";

    public static Utils utils = new Utils();

    /**
     * Method that turns a colored Mat image to grayscale
     *
     * @param image - source image file
     * @return returns filtered image
     */
    public static Mat toGrayscale(Mat image) {
        Mat imageOut = new Mat(); //tai ka grazins metodas
        Imgproc.cvtColor(image, imageOut, Imgproc.COLOR_RGB2GRAY);
        return imageOut;
    }

    /**
     * Method that adds gaussian blur to an image
     *
     * @param image - Mat image
     * @return returns filtered image
     */
    public static Mat gausianBlur(Mat image) {
        Mat imageOut = new Mat(); //tai ka grazins metodas
        Imgproc.GaussianBlur(image, imageOut, new Size(7, 7), 0);
        return imageOut;
    }

    /**
     * Method that dilates or erodes an image
     *
     * @param image - Mat image
     * @return returns filtered image
     */
    public static Mat imageDilate(Mat image) {
        Mat imageOut = new Mat(); //tai ka grazins metodas
        int erosion_size = 3;
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(2 * erosion_size + 1, 2 * erosion_size + 1));
        Imgproc.dilate(image, imageOut, element);
        return imageOut;
    }

    /**
     * Method that adds sobel filter to an image
     *
     * @param image  - Mat image
     * @param kernel - matrix used for filtering
     * @return returns filtered image
     */
    public static Mat imageSobel(Mat image, Mat kernel) {
        Mat imageOut = new Mat();
        int kernelSize = 9;
        Imgproc.filter2D(image, imageOut, -1, kernel);
        return imageOut;
    }

    /**
     * Method that turns a Mat image to binary Mat image
     *
     * @param image - Mat image
     * @return returns filtered image
     */
    public static Mat imageThreshold(Mat image) {
        Mat imageOut = new Mat(); //tai ka grazins metodas
        Imgproc.threshold(image, imageOut, 73, 255, Imgproc.THRESH_BINARY); //57 thresh wtih blur was nice; 63 thresh without blur was nice
        return imageOut;
    }

    /**
     * A method that uses the filters of this class to process an image
     *
     * @param matImagesList - list of Mat images
     * @return returns filtered image
     */
    public static ArrayList<BufferedImage> filterImages(ArrayList<Mat> matImagesList) {

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

        ArrayList<Mat> matImageListOut = new ArrayList<>();
        System.out.println("Processing...");
        for (int i = 0; i < matImagesList.size(); i++) {
            Mat inputImage = matImagesList.get(i);
            inputImage = toGrayscale(inputImage);
            inputImage = gausianBlur(inputImage);
            inputImage = imageDilate(inputImage);
            Mat outputImage = imageThreshold(inputImage);
//          Mat outputImage = imageSobel(inputImage,verticalSobelKernel);
            matImageListOut.add(outputImage);
        }
        return utils.matToBufferedImage(matImageListOut);
    }


}
