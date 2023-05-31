package system;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class PrefabDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        boolean isPrefab = jsonObject.get("isPrefab").getAsBoolean();
        String tag = jsonObject.get("tag").getAsString();
        String prefabId = jsonObject.get("prefabId").getAsString();

        if (isPrefab) {
            JsonArray components = jsonObject.getAsJsonArray("components");

            GameObject go = new GameObject(name, prefabId);
            for (JsonElement e : components) {
                Component c = context.deserialize(e, Component.class);
                go.addComponent(c);
            }
            go.transform = go.getComponent(Transform.class);
            go.tag = tag;
            return go;
        } else
            return null;
    }
}
