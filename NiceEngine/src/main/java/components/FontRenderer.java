package components;

public class FontRenderer extends Component {

    //region Override methods
    /**
     * Start is called before the first frame update
     */
    @Override
    public void start(){
        if (gameObject.getComponent(SpriteRenderer.class) != null){
            System.out.println("Found font renderer");
        }
    }

    /**
     * // Update is called once per frame
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt) {

    }
    //endregion
}
