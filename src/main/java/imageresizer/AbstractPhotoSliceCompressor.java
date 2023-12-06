package imageresizer;

public abstract  class AbstractPhotoSliceCompressor implements  Runnable {
    protected String outDir;
    protected PhotoStorage storage;

    public AbstractPhotoSliceCompressor(PhotoStorage storage, String outDir) {
        this.storage = storage;
        this.outDir = outDir;
    }

    public abstract void compressPictures();

    @Override
    public void run() {
            compressPictures();
    }
}
