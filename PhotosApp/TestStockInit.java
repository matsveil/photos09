import java.io.File;

public class TestStockInit {
    public static void main(String[] args) {
        String DATA_FILE = "PhotosApp/data/users.dat";
        
        File dataFile = new File(DATA_FILE);
        File dataDir = dataFile.getParentFile();
        
        System.out.println("DATA_FILE: " + DATA_FILE);
        System.out.println("dataFile absolute: " + dataFile.getAbsolutePath());
        System.out.println("dataDir: " + dataDir);
        System.out.println("dataDir absolute: " + dataDir.getAbsolutePath());
        System.out.println("dataDir exists: " + dataDir.exists());
        System.out.println();
        
        String[] stockPhotoNames = {"stock1.jpg", "stock2.jpg", "stock3.jpg", "stock4.jpg", "stock5.jpg"};
        
        for (String photoName : stockPhotoNames) {
            File photoFile = new File(dataDir, photoName);
            System.out.println(photoName + ":");
            System.out.println("  Path: " + photoFile.getAbsolutePath());
            System.out.println("  Exists: " + photoFile.exists());
        }
    }
}

