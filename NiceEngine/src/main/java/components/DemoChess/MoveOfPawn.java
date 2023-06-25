package components.DemoChess;

import components.Sprite;
import components.SpriteRenderer;

import javax.swing.text.Position;

public class MoveOfPawn extends ChessMove {
    private transient boolean isMovedFirstTime = false;
    private Sprite queenSpr;

    @Override
    public void move(ChessPosition newPos) {
        isMovedFirstTime = true;
        super.move(newPos);

        Piece.Color color = this.gameObject.getComponent(Piece.class).color;
        ChessPosition pos = this.gameObject.getComponent(ChessPosition.class);
        if ((color == Piece.Color.WHITE && pos.row == ChessPosition.Row.R8)
                || color == Piece.Color.BLACK && pos.row == ChessPosition.Row.R1) {
            promotion();
        }
    }

    private void promotion() {
        this.gameObject.addComponent(new MoveOfQueen());
        this.gameObject.getComponent(MoveOfQueen.class).start();
        this.gameObject.removeComponent(MoveOfPawn.class);

        SpriteRenderer spr = this.gameObject.getComponent(SpriteRenderer.class);
        spr.setSprite(queenSpr);
    }


    @Override
    public void calcTheCanMoveList() {
        canMoveList.clear();

        ChessPosition.Direction frontDirection = (this.gameObject.getComponent(Piece.class).color == Piece.Color.WHITE ? ChessPosition.Direction.UP : ChessPosition.Direction.DOWN);

        ChessPosition frontPos = ChessPosition.get(this.gameObject.getComponent(ChessPosition.class), frontDirection);
        if (isEmptyPos(frontPos)) {
            canMoveList.add(frontPos);
        }

        ChessPosition frontLeftPos = ChessPosition.get(frontPos, ChessPosition.Direction.LEFT);
        if (isEnemyPos(frontLeftPos)) {
            canMoveList.add(frontLeftPos);
        }

        ChessPosition frontRightPos = ChessPosition.get(frontPos, ChessPosition.Direction.RIGHT);
        if (isEnemyPos(frontRightPos)) {
            canMoveList.add(frontRightPos);
        }

        if (!isMovedFirstTime) {
            ChessPosition frontX2Pos = ChessPosition.get(frontPos, frontDirection);
            if (isEmptyPos(frontX2Pos)) {
                canMoveList.add(frontX2Pos);
            }
        }
    }
}
