package ku.cs.kanison.services;

import java.io.File;

public class ImageDirectory {

    public ImageDirectory() {
        initialFileIfNotExist();
    }

    private void initialFileIfNotExist() {
        File file = new File("data");
        if (!file.exists()) {
            file.mkdir();
        }

        String userImagePath = "data" + File.separator + "user_image";
        String productImagePath = "data" + File.separator + "product_image";
        file = new File(userImagePath);
        if (!file.exists()) {
                file.mkdirs();
        }
        file = new File(productImagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
