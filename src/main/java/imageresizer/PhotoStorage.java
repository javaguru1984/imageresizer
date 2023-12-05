package imageresizer;

import java.io.File;
import java.util.ArrayList;
import lombok.Getter;


@Getter
public class PhotoStorage {
    private ArrayList<File> storage = new ArrayList<>();

    public void addFileToStorage(File file) {
        storage.add(file);
    }
}
