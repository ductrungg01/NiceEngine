package components;

public class ObjectInfo extends Component implements INonAddableComponent {
    public String name;
    public String tag;

    public ObjectInfo(String name) {
        this.name = name;
        this.tag = "";
    }

    public ObjectInfo() {
        if (this.gameObject != null){
            this.name = this.gameObject.name;
        }
    }

    @Override
    public void editorUpdate(float dt) {
        gameObject.name = this.name;
    }

}
