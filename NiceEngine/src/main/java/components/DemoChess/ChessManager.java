package components.DemoChess;

import components.Component;
import components.Sprite;
import org.joml.Vector2f;
import system.GameObject;
import system.Prefabs;
import system.Window;

import java.util.List;

public class ChessManager extends Component {
    private transient List<GameObject> squares;
    public Sprite spr_white_king;
    public Sprite spr_white_queen;
    public Sprite spr_white_bishop;
    public Sprite spr_white_rook;
    public Sprite spr_white_knight;
    public Sprite spr_white_pawn;
    public Sprite spr_black_king;
    public Sprite spr_black_queen;
    public Sprite spr_black_bishop;
    public Sprite spr_black_rook;
    public Sprite spr_black_knight;
    public Sprite spr_black_pawn;

    @Override
    public void start() {
        squares = Window.getScene().findAllGameObjectWithTag("Square");
    }
}
