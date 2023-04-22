package util;

import java.io.File;

public class FileChecker {
    public static boolean isImageFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String[] imageExtensions = {"jpg", "jpeg", "png"};
        //, "gif", "bmp", "tiff", "webp"
        for (String ext : imageExtensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkFileExtension(String ext, File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return ext.equals(extension);
    }
}
