package components.DemoChess;

import components.Component;
import org.joml.Vector2f;
import system.GameObject;
import system.Window;

import java.util.ArrayList;
import java.util.List;

public class ChessMove extends Component {
    private transient ChessManager manager;
    protected List<ChessPosition> canMoveList = new ArrayList<>();

    @Override
    public void start() {
        manager = Window.getScene().findGameObjectWith(ChessManager.class).getComponent(ChessManager.class);
        calcTheCanMoveList();
    }

    public void move(ChessPosition newPos) {
        ChessPosition pos = this.gameObject.getComponent(ChessPosition.class);
        pos.row = newPos.row;
        pos.column = newPos.column;

        Vector2f realPos = ChessPosition.getRealPositionOf(pos);
        this.gameObject.transform.position = realPos;
    }

    public List<ChessPosition> getListPositionCanMove() {
        return this.canMoveList;
    }

    public void calcTheCanMoveList() {

    }

    protected boolean isEmptyOrEnemyPos(ChessPosition targetPos) {
        return isEmptyPos(targetPos) || isEnemyPos(targetPos);
    }

    protected boolean isEmptyPos(ChessPosition targetPos) {
        if (targetPos == null) return false;

        return manager.getPieceHas(targetPos) == null;
    }

    protected boolean isEnemyPos(ChessPosition targetPos) {
        if (targetPos == null) return false;

        if (isEmptyPos(targetPos)) return false;

        GameObject piece = manager.getPieceHas(targetPos);
        return piece.getComponent(Piece.class).color != this.gameObject.getComponent(Piece.class).color;
    }
}
