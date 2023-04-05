package components;

public class ObjectInfo extends Component {
    public String name;
    public String[] tag;

    public ObjectInfo(String name) {
        this.name = name;
        this.tag = new String[] { "Object"};
    }

    @Override
    public void editorUpdate(float dt) {
        gameObject.name = this.name;
    }

}
