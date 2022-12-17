package components;

public class Fireball extends Component{
    public transient boolean goingRight = false;
    private static int fireballCount = 0;
    public static boolean canSpawn(){
        return fireballCount < 4;
    }
}
