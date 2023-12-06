package imageresizer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class PrimitiveImageCompressor extends AbstractPhotoSliceCompressor {
    public PrimitiveImageCompressor(PhotoStorage storage, String outDir) {
        super(storage, outDir);
    }

    @Override
    public void compressPictures() {
        for (File sourceFile : storage.getStorage()) {
            Path path = (Path) Paths.get(outDir, sourceFile.getName());
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
}
