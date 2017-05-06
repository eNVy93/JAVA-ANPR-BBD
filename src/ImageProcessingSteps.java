import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.Utils;

import java.util.ArrayList;

/**
 * Created by nevyt on 5/6/2017.
 */
public class ImageProcessingSteps {


    public static final String GRAYSCALE = "1step_toGrayscale" ;
    public static final String GAUSSIAN = "2step_gaussianBlur";
    public static final String DILATION = "3step_dilation";
    public static final String SOBEL = "4step_sobel";
    public static final String THRESHOLD = "4step_threshold";


    public static ArrayList<Mat> toGrayscaleList(ArrayList<Mat> matImagesList) {

        ArrayList<Mat> matImageListOut = new ArrayList<>();
        System.out.println("Turning images to grayscale...");
        for (int i = 0; i < matImagesList.size(); i++) {
            Mat inputImage = matImagesList.get(i);
            Mat outputImage = new Mat(); //tai ka grazins metodas
            Imgproc.cvtColor(inputImage, outputImage, Imgproc.COLOR_RGB2GRAY);
            matImageListOut.add(outputImage);
        }

        return matImageListOut;


    }

    public static ArrayList<Mat> gausianBlur(ArrayList<Mat> matImagesList) {

        ArrayList<Mat> matImageListOut = new ArrayList<>();
        System.out.println("Applying gaussian blur...");
        for (int i = 0; i < matImagesList.size(); i++) {
            Mat inputImage = matImagesList.get(i);
            Mat outputImage = new Mat(); //tai ka grazins metodas
            Imgproc.GaussianBlur(inputImage, outputImage, new Size(7, 7), 0);
            matImageListOut.add(outputImage);
        }
        return matImageListOut;


    }

    public static ArrayList<Mat> imageDilate(ArrayList<Mat> matImagesList) {
        ArrayList<Mat> matImageListOut = new ArrayList<>();
        System.out.println("Dilating image/Eroding image...");
        for (int i = 0; i < matImagesList.size(); i++) {
            Mat inputImage = matImagesList.get(i);
            Mat outputImage = new Mat(); //tai ka grazins metodas
            int erosion_size = 3;
            Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(2 * erosion_size + 1, 2 * erosion_size + 1));
            Imgproc.dilate(inputImage, outputImage, element);

            matImageListOut.add(outputImage);
        }
        return matImageListOut;

    }

    public static ArrayList<Mat> imageSobel(ArrayList<Mat> matImagesList) {
        ArrayList<Mat> matImageListOut = new ArrayList<>();
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

        System.out.println("Dilating image/Eroding image...");
        for (int i = 0; i < matImagesList.size(); i++) {
            Mat inputImage = matImagesList.get(i);
            Mat outputImage = new Mat(); //tai ka grazins metodas
            Imgproc.filter2D(inputImage, outputImage, -1, verticalKernel);
            matImageListOut.add(outputImage);
        }
        return matImageListOut;

    }

    public static ArrayList<Mat> imageThreshold(ArrayList<Mat> matImagesList) {
        ArrayList<Mat> matImageListOut = new ArrayList<>();
        System.out.println("Dilating image/Eroding image...");
        for (int i = 0; i < matImagesList.size(); i++) {
            Mat inputImage = matImagesList.get(i);
            Mat outputImage = new Mat(); //tai ka grazins metodas
            Imgproc.threshold(inputImage, outputImage, 73, 255, Imgproc.THRESH_BINARY);
            matImageListOut.add(outputImage);
        }
        return matImageListOut;

    }

}
