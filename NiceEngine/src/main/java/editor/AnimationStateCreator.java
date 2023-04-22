package editor;

import components.Sprite;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiDir;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import renderer.Texture;
import util.AssetPool;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AnimationStateCreator {

    private AnimationStateCreator() {
    }

    private static AnimationStateCreator instance = null;

    public static AnimationStateCreator getInstance() {
        if (instance == null) {
            instance = new AnimationStateCreator();
        }

        return instance;
    }

    public static boolean isShow = true;
    private boolean showFileDialog = false;

    ImString imagePath = new ImString("Assets/images/something.png");
    ImString deviceByX = new ImString("1");
    ImString deviceByY = new ImString("1");
    ImString frameTimeSmall = new ImString("0.02s");
    ImString title = new ImString("This is the name of this Anim State");
    ImBoolean isLoop = new ImBoolean(true);
    List<ImInt> frameList = new List<ImInt>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<ImInt> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] ts) {
            return null;
        }

        @Override
        public boolean add(ImInt imInt) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends ImInt> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, Collection<? extends ImInt> collection) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public ImInt get(int i) {
            return null;
        }

        @Override
        public ImInt set(int i, ImInt imInt) {
            return null;
        }

        @Override
        public void add(int i, ImInt imInt) {

        }

        @Override
        public ImInt remove(int i) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<ImInt> listIterator() {
            return null;
        }

        @Override
        public ListIterator<ImInt> listIterator(int i) {
            return null;
        }

        @Override
        public List<ImInt> subList(int i, int i1) {
            return null;
        }
    };

    public void imgui() {
        if (!isShow) return;

//        if (ImGui.begin("Animation State Creator",
//                new ImBoolean(true),
//                ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoSavedSettings)) {
//
//            ImVec2 size = new ImVec2(300, 200);
//            ImVec2 center = new ImVec2(ImGui.getMainViewport().getPosX() + ImGui.getMainViewport().getSizeX() / 2 - size.x / 2, ImGui.getMainViewport().getPosY() + ImGui.getMainViewport().getSizeY() / 2 - size.y / 2);
//            ImGui.setNextWindowPos(center.x, center.y);
//
//
//            ImGui.text("Image:");
//            ImGui.inputText("##image", new ImString("assets/images/folder-icon.png"), 256);
//
//            if (ImGui.button("Load")) {
//                // Load image
//            }
//
//            Sprite spr = new Sprite();
//            spr.setTexture(AssetPool.getTexture("assets/images/folder-icon.png"));
//            ImGui.image(spr.getTexId(), 200, 200);
//
//            ImGui.end();
//        } else {
//            System.out.println("Animation State Creator windows is closed");
//            AnimationStateCreator.getInstance().isShow = false;
//            ImGui.end();
//        }
        ImGui.begin("Animation State Creator");

        // Bên trái
        ImGui.columns(2);

        // Dòng 1
        ImGui.text("Image path:");
        ImGui.sameLine();
        int INPUT_TEXT_BUF_SIZE = 100;
        ImGui.inputText("", imagePath, INPUT_TEXT_BUF_SIZE);
        ImGui.sameLine();
        if (ImGui.button("Browse")) {
            // Handle browse button click event
            showFileDialog = true;
        }

        // Dòng 2
        ImGui.text("Device by:  ");
        ImGui.sameLine();
        ImGui.inputText("", deviceByX, ImGuiInputTextFlags.CharsDecimal);
        ImGui.sameLine();
        ImGui.inputText("", deviceByY, ImGuiInputTextFlags.CharsDecimal);

//        ImGui.inputTextWithHint("", "", deviceByX, 5);
//        ImGui.sameLine();
//        ImGui.inputTextWithHint("", "", deviceByY, 5);


        // Dòng 3
        ImGui.text("Image preview:");
        ImGui.sameLine();
        ImGui.dummy(500, 500);

        // Dòng 4
        ImGui.arrowButton("##down", ImGuiDir.Down);

        // Dòng 5
        ImGui.beginChild("##frame-preview", 250, 250, true);
        ImGui.dummy(250, 250);
        ImGui.endChild();
        ImGui.sameLine();
        ImGui.text("Frame time:");
        ImGui.sameLine();
        ImGui.inputText("", frameTimeSmall, INPUT_TEXT_BUF_SIZE);
        if (ImGui.button("Add to frame list")) {
            // Handle add frame button click event
        }

        // Bên phải
        ImGui.nextColumn();

        // Dòng 1
        ImGui.text("Title:");
        ImGui.sameLine();
        ImGui.inputText("", title, INPUT_TEXT_BUF_SIZE);

        // Dòng 2
        ImGui.checkbox("Is loop?", isLoop);

        // Dòng 3
        ImGui.beginChild("##frame-list", 500, 500, true);
        for (int i = 0; i < 11; i++) {
            ImGui.pushID(i);
            if (ImGui.button("Frame: " + i + " | " + "Time: " + 0.04 + " ")) {

            }
            ImGui.sameLine();
            if (ImGui.button("Delete")) {
                // Handle delete button click event
            }
            ImGui.popID();
        }
        ImGui.endChild();

        // Dòng 4
        ImGui.dummy(250, 250);

        // Dòng 5
        ImGui.separator();

        // Dòng cuối
        if (ImGui.button("Cancel")) {
            // Handle cancel button click event
        }
        ImGui.sameLine();
        if (ImGui.button("Save")) {
            // Handle save button click event
        }


        if (showFileDialog) {
            ImGui.openPopup("File Dialog");
        }

        if (ImGui.beginPopupModal("File Dialog")) {
            ImGui.text("Select an image file");
            ImGui.separator();

            // File dialog contents go here

            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
                showFileDialog = false;
            }

            ImGui.endPopup();
        }


        ImGui.end();
    }
}
