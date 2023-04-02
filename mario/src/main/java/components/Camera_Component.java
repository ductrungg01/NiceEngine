package components;

import editor.JImGui;
import imgui.ImGui;
import imgui.type.ImInt;
import org.joml.Vector4f;

import javax.print.DocFlavor;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Camera_Component extends Component{
    // Background
    List<String> backgroundTypeOptions = new ArrayList<String>(Arrays.asList("Solid color", "Image"));
    Vector4f color = new Vector4f();
    String imageFilePath = "";

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void editorUpdate(float dt) {
        super.editorUpdate(dt);
    }

    @Override
    public void imgui() {
        String[] tmp = backgroundTypeOptions.toArray(String[]::new);
        ImGui.combo("Background Type", new ImInt(0), tmp, 2) ;

        JImGui.colorPicker4("Background", this.color);
    }
}
