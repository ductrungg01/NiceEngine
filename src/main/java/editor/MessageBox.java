package editor;

import editor.windows.AssetsWindow;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import system.Window;

import java.io.File;
import java.io.IOException;

public class MessageBox {

    public enum TypeOfMsb {
        ERROR,
        CREATE_FILE,
        CREATE_FILE_SUCCESS,
        NORMAL_MESSAGE
    }

    public static boolean showMsb = false;
    private static TypeOfMsb typeOfMsb = TypeOfMsb.ERROR;
    private static String msbText = "";

    public static void setContext(boolean showMsb, TypeOfMsb typeOfMsb, String msbText) {
        setShowMsb(showMsb);
        setTypeOfMsb(typeOfMsb);
        setMsbText(msbText);
    }

    public static void setShowMsb(boolean showMsb) {
        MessageBox.showMsb = showMsb;
    }

    public static void setTypeOfMsb(TypeOfMsb typeOfMsb) {
        MessageBox.typeOfMsb = typeOfMsb;
    }

    public static void setMsbText(String msbText) {
        MessageBox.msbText = msbText;
    }

    public void imgui() {
        if (!showMsb) return;
        //region Message Box
        if (showMsb) {
            ImGui.openPopup("Message Box");
            float popupWidth = Window.getWidth() * 0.3f;
            float popupHeight = Window.getHeight() * 0.25f;
            ImGui.setNextWindowSize(popupWidth, popupHeight);

            float popupPosX = (float) Window.getWidth() / 2 - popupWidth / 2;
            float popupPosY = (float) Window.getHeight() / 2 - popupHeight / 2;
            ImGui.setNextWindowPos(popupPosX, popupPosY, ImGuiCond.Always);
        }
        if (ImGui.beginPopupModal("Message Box", ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize)) {
            ImGui.text(msbText);
            ImGui.separator();

            switch (typeOfMsb) {
                case CREATE_FILE:
                    String newFile[] = NiceImGui.inputTextNoLabel("");
                    ImGui.text("Press enter to confirm");
                    if (ImGui.button("Cancel")) {
                        showMsb = false;
                        ImGui.closeCurrentPopup();
                    }
                    if (newFile[0].equals("true")) {
                        File file = new File(AssetsWindow.getCurrentOpenFolder() + "/" + newFile[1]);
                        try {
                            if (file.createNewFile()) {
                                showMsb = true;
                                typeOfMsb = TypeOfMsb.CREATE_FILE_SUCCESS;
                                msbText = "File created";
                            } else {
                                showMsb = true;
                                typeOfMsb = TypeOfMsb.ERROR;
                                msbText = "Error! File already exist";

                            }
                        } catch (IOException e) {
                            showMsb = true;
                            typeOfMsb = TypeOfMsb.ERROR;
                            msbText = "Error! Can't create file";
                        }
                    }
                    break;
                default:
                    if (ImGui.button("OK")) {
                        showMsb = false;
                        ImGui.closeCurrentPopup();
                    }
                    break;
            }


            ImGui.endPopup();
        }
        //endregion
    }
}
