package imageresizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;


public class ImageScalarCompressor extends AbstractPhotoSliceCompressor{
    public ImageScalarCompressor(PhotoStorage storage, String outDir) {
        super(storage, outDir);
    }

    @Override
    public void compressPictures() {
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
}
