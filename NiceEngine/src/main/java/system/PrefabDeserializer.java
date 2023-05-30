package system;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class PrefabDeserializer implements JsonDeserializer<PrefabObject> {
    @Override
    public PrefabObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String prefabId = jsonObject.get("prefabId").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");

        PrefabObject prefab = new PrefabObject(name);
        prefab.prefabId = prefabId;
        for (JsonElement e : components) {
            Component c = context.deserialize(e, Component.class);
            prefab.addComponent(c);
        }
        prefab.transform = prefab.getComponent(Transform.class);

        return prefab;
    }
}
