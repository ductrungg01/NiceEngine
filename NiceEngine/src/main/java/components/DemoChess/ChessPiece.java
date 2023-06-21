package components.DemoChess;

import components.Component;
import editor.Debug;
import org.joml.Vector2f;
import system.MouseListener;

import static editor.uihelper.NiceShortCall.COLOR_Green;

public class ChessPiece extends Component {
    public enum Color {
        WHITE,
        BLACK
    }

    public enum Type {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public Color color;
    public Type type;

    private transient ChessPosition pos;

    @Override
    public void start() {
        pos = this.gameObject.getComponent(ChessPosition.class);
    }

    @Override
    public void update(float dt) {
        checkHover();
    }

    private void checkHover() {
        Vector2f pos = this.gameObject.transform.position;
        Vector2f size = this.gameObject.transform.scale;

        Vector2f start = new Vector2f(pos.x - size.x / 2f, pos.y - size.y / 2f);
        Vector2f end = new Vector2f(pos.x + size.x / 2f, pos.y + size.y / 2f);

        Vector2f mousePos = MouseListener.getWorld();

        if (mousePos.x >= start.x && mousePos.x <= end.x && mousePos.y >= start.y && mousePos.y <= end.y) {
            Debug.Clear();
            Debug.Log(this.color.name() + " " + this.type.name());
            Debug.Log(this.pos.column + " " + this.pos.row);
        }
    }
}
