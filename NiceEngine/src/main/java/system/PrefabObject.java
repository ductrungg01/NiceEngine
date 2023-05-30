package system;

import java.util.List;
import java.util.Random;

public class PrefabObject extends GameObject {
    public String prefabId = "";

    public PrefabObject(String name) {
        super(name);

        generatePrefabId();
    }

    private String generateRandomString() {
        int length = 10;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public void generatePrefabId() {
        do {
            this.prefabId = generateRandomString();
        } while (isExistedId(this.prefabId));
    }

    private boolean isExistedId(String id) {
        for (PrefabObject prefabObject : Prefabs.prefabObjects) {
            String prefabId = prefabObject.prefabId;
            if (id.equals(prefabId)) {
                return true;
            }
        }
        return false;
    }
}
