/**
 * Created by nevyt on 4/29/2017.
 */

import org.opencv.core.Core;
import org.opencv.core.Mat;
import utils.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String path = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input";
        ImageProcessing process = new ImageProcessing();
        Utils utils = new Utils();
        ArrayList<String> filePathList = utils.filePathsToList(path);
        ArrayList<Mat> matImagesList = utils.filePathsToMatImageList(filePathList);
        ArrayList<Mat> processedImagesList = process.filterImages(matImagesList);
        ArrayList<BufferedImage> bufferedImagesList = utils.matToBufferedImage(processedImagesList);
        utils.outputImages(bufferedImagesList);

    }


}
