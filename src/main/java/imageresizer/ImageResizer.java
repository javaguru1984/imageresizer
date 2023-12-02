package imageresizer;


public class ImageResizer {
    public static void main(String[] args) {
        ParallelThreads threads = new ParallelThreads();
        System.out.println(threads.getThreadsNumber());
    }
}
