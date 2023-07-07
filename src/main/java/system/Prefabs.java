package system;

import components.*;
import editor.Debug;
import org.joml.Vector2f;
import util.AssetPool;
import util.Settings;

public class Prefabs {
    public static GameObject getPrefab(String name) {
        for (GameObject prefab : GameObject.PrefabLists) {
            if (prefab.name.equals(name)) {
                return prefab;
            }
        }

        System.out.println("Cannot find prefab '" + name + "'");
        Debug.Log("Cannot find prefab '" + name + "'");
        return null;
    }

    public static GameObject createChildFrom(String prefabName) {
        GameObject prefab = getPrefab(prefabName);

        if (prefab == null) {
            Debug.Log("Cannot find prefab '" + prefabName + "'");
            return null;
        }

        GameObject newGo = prefab.generateChildGameObject();

        return newGo;
    }
}
