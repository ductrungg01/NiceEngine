package components;

import editor.JImGui;
import imgui.ImGui;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Background extends Component{
    private String filePath;
    private boolean useColor = false;
    private Vector4f staticColor = new Vector4f(1, 1, 1, 1);
    @Override
    public void imgui() {
        try {
            Field[] fields =  this.getClass().getDeclaredFields();
            for (Field field : fields){
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient){
                    continue;
                }

                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate){
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == boolean.class){
                    boolean val = (boolean)value;
                    if (ImGui.checkbox(" " + name, val)){
                        field.set(this, !val);
                    }
                } else if (type == Vector4f.class){
                    Vector4f val = (Vector4f)value;
                    JImGui.colorPicker4(name, val);
                } if (type == String.class){
                    field.set(this,
                            JImGui.inputText(field.getName() + ": ",
                                    (String)value));
                }

                if (isPrivate){
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

}
