package utils;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nevyt on 4/30/2017.
 */
public class Utils {


    /**
     * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
     *
     * @param frame the {@link Mat} representing the current frame
     * @return the {@link Image} to show
     */
    public static Image mat2Image(Mat frame) {
        try {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        } catch (Exception e) {
            System.err.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }

    /**
     * Generic method for putting element running on a non-JavaFX thread on the
     * JavaFX thread, to properly update the UI
     *
     * @param property a {@link ObjectProperty}
     * @param value    the value to set for the given {@link ObjectProperty}
     */
    public static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(() -> {
            property.set(value);
        });
    }

    /**
     * Support for the {@link mat2image()} method
     *
     * @param original the {@link Mat} object in BGR or grayscale
     * @return the corresponding {@link BufferedImage}
     */
    public static BufferedImage matToBufferedImage(Mat original) {
        // init
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

    /**
     * Method for getting absolute image paths
     *
     * @param path Input files path directory
     * @return Returns String list of absolute file paths
     */
    public static ArrayList<String> filePathsToList(String path) {

        File f = new File(path);
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(f.listFiles()));
        ArrayList<String> filePathList = new ArrayList<>();
        System.out.println("Loading file paths...");
        for (File file : fileList) {
            String filePath = file.getAbsolutePath();
            filePathList.add(filePath);
            //System.out.println(file.toString());
        }
        return filePathList;


    }

    public static ArrayList<Mat> filePathsToMatImageList(ArrayList<String> fileList) {
        ArrayList<Mat> matImageList = new ArrayList<>();
        System.out.println("Loading Mat images ...");
        for (String path : fileList) {
            //System.out.println(path);
            matImageList.add(Imgcodecs.imread(path));

            //System.out.println(path);
        }
//        for (Mat image :
//                matImageList) {
//            System.out.println(image.toString());
//        }
        return matImageList;
    }


    public static ArrayList<BufferedImage> matToBufferedImage(ArrayList<Mat> matImagesList) {
        ArrayList<BufferedImage> bufferedImageList = new ArrayList<>();

        for (int i = 0; i < matImagesList.size(); i++) {
            BufferedImage bufferedImage = matToBufferedImage(matImagesList.get(i));
            bufferedImageList.add(bufferedImage);
        }
        System.out.println("Turning Mat images to Buffered images...");
//        for (BufferedImage image: bufferedImageList) {
//            System.out.println(image.toString());
//
//        }
        return bufferedImageList;
    }

//    public static void calculateHistogram(ArrayList<Mat> someList,Mat x_hist){
//
//    }


    public static void outputImages(ArrayList<BufferedImage> imageList) {
        System.out.println("Outputing images...");
        String path = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Output";
        for (int i = 0; i < imageList.size(); i++) {
            File outputfile = new File(path+"\\output" + i + ".jpg");
            try {
                ImageIO.write(imageList.get(i), "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}


