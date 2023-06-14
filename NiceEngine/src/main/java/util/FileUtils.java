package util;

import editor.Debug;
import components.Sprite;
import editor.MessageBox;
import editor.ReferenceType;
import renderer.Texture;
import system.Spritesheet;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    final static String defaultAssetFolder = "assets";
    final static String defaultSprite = "system-assets/images/Default Sprite.png";

    public enum ICON_NAME {
        FOLDER,
        LEFT_ARROW,
        RIGHT_ARROW,
        JAVA,
        FILE,
        SOUND,
        GAME_OBJECT,
        REMOVE,
        ADD
    }

    public final static Map<ICON_NAME, String> icons = new HashMap<>() {
        {
            put(ICON_NAME.FOLDER, "system-assets/images/folder-icon.png");
            put(ICON_NAME.LEFT_ARROW, "system-assets/images/left-arrow-icon.png");
            put(ICON_NAME.RIGHT_ARROW, "system-assets/images/right-arrow-icon.png");
            put(ICON_NAME.JAVA, "system-assets/images/java-icon.png");
            put(ICON_NAME.FILE, "system-assets/images/file-icon.png");
            put(ICON_NAME.SOUND, "system-assets/images/sound-icon.png");
            put(ICON_NAME.GAME_OBJECT, "system-assets/images/gameobject-icon.png");
            put(ICON_NAME.REMOVE, "system-assets/images/remove-icon.png");
            put(ICON_NAME.ADD, "system-assets/images/add-icon.png");
        }
    };

    private static List<String> imageExtensions = List.of("jpg", "jpeg", "png");
    private static List<String> soundExtensions = List.of("ogg", "mp3", "wav", "flac", "aiff", "m4a");

    public static List<File> getAllFiles() {
        return getAllFiles(defaultAssetFolder);
    }

    public static List<File> getAllFiles(String folder) {
        List<File> files = new ArrayList<>();
        File directory = new File(folder);
        if (directory.isDirectory()) {
            File[] filesList = directory.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    if (file.isFile()) {
                        files.add(file);
                    } else if (file.isDirectory()) {
                        files.addAll(getAllFiles(file.getPath()));
                    }
                }
            }
        }
        return files;
    }

    // Get file only,
    public static List<File> getFilesWithReferenceType(ReferenceType referenceType) {
        List<File> files = new ArrayList<>();

        switch (referenceType) {
            case SPRITE -> {
                files.addAll(getAllFilesWithExtensions(imageExtensions));
            }
            case JAVA -> {
                List<String> tmp = List.of("java");
                files.addAll(getAllFilesWithExtensions(tmp));
            }
            case SOUND -> {
                files.addAll(getAllFilesWithExtensions(soundExtensions));
            }
        }

        return files;
    }

    public static List<File> getAllFilesWithExtensions(List<String> extensions) {
        return getAllFilesWithExtensions(defaultAssetFolder, extensions);
    }

    public static List<File> getAllFilesWithExtensions(String folder, List<String> extensions) {
        List<File> files = new ArrayList<>();
        File directory = new File(folder);
        if (directory.isDirectory()) {
            File[] filesList = directory.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        for (String extension : extensions) {
                            if (fileName.endsWith(extension)) {
                                files.add(file);
                                break;
                            }
                        }
                    } else if (file.isDirectory()) {
                        files.addAll(getAllFilesWithExtensions(file.getPath(), extensions));
                    }
                }
            }
        }
        return files;
    }

    public static boolean isImageFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        //, "gif", "bmp", "tiff", "webp"
        for (String ext : imageExtensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSoundFile(File file) {
        String extension = getFileExtension(file.getName());
        for (String ext : soundExtensions) {
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
            Path desPath = Paths.get(desFile.getPath());
            Files.copy(srcFile.toPath(), desPath);
            Debug.Log("Copy file " + srcFile.getName() + " to " + desPath);
            if (srcFile.isDirectory()) {
                File[] listOfFiles = srcFile.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    copyFile(listOfFiles[i], new File(desFile.getPath() + "/" + listOfFiles[i].getName()));
                }
            }
        } catch (Exception e) {
            MessageBox.setContext(true, MessageBox.TypeOfMsb.ERROR, "File already exist");
            Debug.Log("Failed to copy file: " + e.getMessage());
        }

    }

    public static String getShorterName(String fileName) {
        String name = getFileNameWithoutExtension(fileName);
        String ext = getFileExtension(fileName);

        final int MAX_LENGTH_ALLOW = 10;
        if (name.length() > MAX_LENGTH_ALLOW) {
            name = name.substring(0, MAX_LENGTH_ALLOW) + "..";
        }

        return name + "." + ext;
    }

    public static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        } else {
            return "";
        }
    }

    public static String getFileNameWithoutExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(0, dotIndex);
        } else {
            return filename;
        }
    }

    public static String getFileName(String filePath) {
        int slashIndex = filePath.lastIndexOf('/');
        if (slashIndex == -1) slashIndex = filePath.lastIndexOf('\\');

        if (slashIndex == -1) {
            return filePath;
        } else {
            return filePath.substring(slashIndex + 1);
        }
    }

    public static Sprite getIconByFile(File file) {
        Sprite spr = new Sprite();

        String extension = getFileExtension(file.getName()).toLowerCase();

        if (isImageFile(file)) {
            spr.setTexture(AssetPool.getTexture(file.getPath()));
            spr.calcWidthAndHeight();
        } else if (extension.equals("java")) {
            spr.setTexture(AssetPool.getTexture(icons.get(ICON_NAME.JAVA)));
            spr.calcWidthAndHeight();
        } else if (isSoundFile(file)) {
            spr.setTexture(AssetPool.getTexture(icons.get(ICON_NAME.SOUND)));
            spr.calcWidthAndHeight();
        } else {
            spr.setTexture(AssetPool.getTexture(icons.get(ICON_NAME.FILE)));
            spr.calcWidthAndHeight();
        }
        return spr;
    }

    public static Sprite getIcon(ICON_NAME iconName) {
        Sprite spr = new Sprite();
        spr.setTexture(AssetPool.getTexture(icons.get(iconName)));
        return spr;
    }

    public static Spritesheet getGizmosSprSheet() {
        Texture texture = AssetPool.getTexture("system-assets/images/gizmos.png");
        Spritesheet spr = new Spritesheet(texture, 24, 48, 3, 0, 0);
        return spr;
    }

    public static Sprite getDefaultSprite() {
        return new Sprite(defaultSprite);
    }
}
