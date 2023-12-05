package imageresizer;

import java.io.File;
import java.io.IOException;
import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.nio.file.Path;


public class PhotoSliceCompressor implements  Runnable {
    private String outDir;
    private PhotoStorage storage;
    private boolean useImgScalarLibrary;

    public PhotoSliceCompressor(PhotoStorage storage, String outDir, boolean useImgScalarLibrary) {
        this.storage = storage;
        this.outDir = outDir;
        this.useImgScalarLibrary = useImgScalarLibrary;
    }

    private void compressPicturesImgScalar() {
        int targetSize = 300;

        for (File sourceFile : storage.getStorage()) {
            Path path = (Path)Paths.get(outDir, sourceFile.getName());
            String targetFilePath = path.toString();
            System.out.println("Image scalar compression method, compressed file --> " + targetFilePath);

            try {
                BufferedImage originalImage = ImageIO.read(sourceFile);
                BufferedImage resizedImage = Scalr.resize(originalImage, targetSize);

                File resizedFile = new File(targetFilePath);
                ImageIO.write(resizedImage, "jpg", resizedFile);

                originalImage.flush();
                resizedImage.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void compressPicturesPrimitive() {
        for (File sourceFile : storage.getStorage()) {
            Path path = (Path)Paths.get(outDir, sourceFile.getName());
            String targetFilePath = path.toString();
            System.out.println("Primitive compression method, compressed file --> " + targetFilePath);

            try {
                BufferedImage image = ImageIO.read(sourceFile.getAbsoluteFile());

                int newWidth = 300;
                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

                int widthStep = image.getWidth() / newWidth;
                int heightStep = image.getHeight() / newHeight;

                for (int x = 0; x < newWidth; x++) {
                    for (int y = 0; y < newHeight; y++) {
                        int rgb = image.getRGB(x * widthStep, y * heightStep);
                        newImage.setRGB(x, y, rgb);
                    }
                }
                File newFile = new File(targetFilePath);
                ImageIO.write(newImage, "jpg", newFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        if (useImgScalarLibrary) {
            compressPicturesImgScalar();
        }
        else {
            compressPicturesPrimitive();
        }
    }
}
