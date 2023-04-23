package util;

import editor.MessageBox;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
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

    public static void copyFile(File srcFile, File desFile) {
        try {
            Path desPath = Paths.get(desFile.getAbsolutePath());
            Files.copy(srcFile.toPath(), desPath);
            System.out.println("Copy file " + srcFile.getAbsolutePath() + " to " + desPath);
            if (srcFile.isDirectory()) {
                File[] listOfFiles = srcFile.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    copyFile(listOfFiles[i], new File(desFile.getPath() + "/" + listOfFiles[i].getName()));
                }
            }
        } catch (Exception e) {
            MessageBox.setContext(true, MessageBox.TypeOfMsb.ERROR, "File already exist");
            System.err.println("Failed to copy file: " + e.getMessage());
        }

    }
}
