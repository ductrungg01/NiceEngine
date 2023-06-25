package components.DemoChess;

import components.Component;
import system.Window;

public class Piece extends Component {
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
    private transient ChessManager chessManager;

    @Override
    public void start() {
        pos = this.gameObject.getComponent(ChessPosition.class);
        chessManager = Window.getScene().findGameObjectWith(ChessManager.class).getComponent(ChessManager.class);
    }
}
