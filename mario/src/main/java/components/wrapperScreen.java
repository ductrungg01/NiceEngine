package components;

public class wrapperScreen extends Component{
    private transient float v = 1;
    @Override
    public void update(float dt){
        this.gameObject.transform.position.y -= dt*v;
        if (this.gameObject.transform.position.y < 0){
            this.gameObject.transform.position.y = 5;
        }
    }
}
