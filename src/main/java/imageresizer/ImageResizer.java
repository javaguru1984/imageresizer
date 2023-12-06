package imageresizer;

import java.io.File;
import org.apache.commons.cli.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;


public class ImageResizer {
    private static final int coresNum = Runtime.getRuntime().availableProcessors();

    private static String getTimeExecutionReport(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        long milliseconds = millis % 1000;

        StringBuilder sb = new StringBuilder(128);
        sb.append("\n**************Image resizer time execution report*****************\n");
        sb.append(minutes);
        sb.append(" Minutes, ");
        sb.append(seconds);
        sb.append(" Seconds, ");
        sb.append(milliseconds);
        sb.append(" Milliseconds. \n");
        sb.append("Processor cores number: ");
        sb.append(ImageResizer.coresNum);
        sb.append("\n**************Image resizer time execution report*****************\n");

        return sb.toString();
    }

    private static PhotoStorage[] getFileStorages(File[] allPhotos) {
        int allPhotosNumber = allPhotos.length;
        int photosNumPerCore = allPhotosNumber / ImageResizer.coresNum;
        int restPhotosNum = allPhotosNumber % ImageResizer.coresNum;

        PhotoStorage[] storages = new PhotoStorage[ImageResizer.coresNum];
        for (int i =0; i < ImageResizer.coresNum; i++) {
            storages[i] = new PhotoStorage();
        }

        for (int coreNum=0; coreNum < ImageResizer.coresNum; coreNum++) {
            int startIndex = (coreNum * photosNumPerCore);
            PhotoStorage storage = storages[coreNum];

            for (int index = startIndex; index < startIndex + photosNumPerCore; index++) {
                File file = allPhotos[index];
                storage.addFileToStorage(file);
            }
        }

        int restPhotosIndex = ImageResizer.coresNum * photosNumPerCore;
        for (int i = 0; i < restPhotosNum; i++) {
            PhotoStorage storage = storages[i];
            File file = allPhotos[restPhotosIndex + i];
            storage.addFileToStorage(file);
        }

        return storages;
    }

    public static void main(String[] args) {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input images folder");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output images folder");
        output.setRequired(true);
        options.addOption(output);

        options.addOption( "imgscalr",false,"Optional parameter: use image scalar library to compress. By default - use primitive algorithm ");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(" java -jar ImageResizer.jar -i <InputImageFolder> -o <OutputImageFolder> -imgscalr <Optional>", options);
            System.exit(1);
        }

        String inputFolderName = cmd.getOptionValue("input");
        String outputFolderName = cmd.getOptionValue("output");

        boolean useImgScalarLibrary = false;

        if(cmd.hasOption("imgscalr")) useImgScalarLibrary = true;

        Path path = Paths.get(inputFolderName);
        if (Files.notExists(path)) {
            System.out.println("\nInput folder with photos does not exists. Check it, please!\n");
            System.exit(1);
        }

        path = Paths.get(outputFolderName);
        if (Files.notExists(path)) {
            if ( !new File(outputFolderName).mkdirs()){
                System.out.println("Can not create new folder: " + outputFolderName);
                System.out.println("Exit from program");
                System.exit(1);
            }
        }

        File srcDir = new File(inputFolderName);
        File[] allPhotos = srcDir.listFiles();

        if (allPhotos == null) {
            System.out.println("Photo list in folder " + inputFolderName + " is empty");
            System.out.println("Exit from program");
            System.exit(1);
        }

        PhotoStorage[] storages = getFileStorages(allPhotos);
        ExecutorService exec = Executors.newFixedThreadPool(ImageResizer.coresNum);

        AbstractPhotoSliceCompressor compressor;

        long start = System.currentTimeMillis();
        for (PhotoStorage storage : storages) {
            if (useImgScalarLibrary) {
                compressor = new ImageScalarCompressor(storage, outputFolderName);
            }
            else {
                compressor = new PrimitiveImageCompressor(storage, outputFolderName);
            }
            exec.execute(compressor);
        }
        exec.shutdown();

        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            long millis = System.currentTimeMillis() - start;

            String executionReport = getTimeExecutionReport(millis);
            System.out.println(executionReport);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
