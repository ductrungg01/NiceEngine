package deserializers;

import com.google.gson.*;
import components.Component;
import editor.Debug;
import system.GameObject;
import system.Transform;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    //region Override methods
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String tag = jsonObject.get("tag").getAsString();
        String parentId = jsonObject.get("parentId").getAsString();
        boolean isPrefab = jsonObject.get("isPrefab").getAsBoolean();

        if (!isPrefab) {
            JsonArray components = jsonObject.getAsJsonArray("components");

            GameObject go = new GameObject(name);
            for (JsonElement e : components) {
                Component c = context.deserialize(e, Component.class);
                if (c != null) {
                    go.addComponent(c);
                }
            }
            go.transform = go.getComponent(Transform.class);
            go.tag = tag;
            go.parentId = parentId;
            return go;
        } else
            return null;
    }
    //endregion
}
