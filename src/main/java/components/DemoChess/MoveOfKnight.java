package components.DemoChess;

public class MoveOfKnight extends ChessMove {
    @Override
    public void calcTheCanMoveList() {
        this.canMoveList.clear();

        ChessPosition knightPos = this.gameObject.getComponent(ChessPosition.class);

        addToCanMoveList(knightPos, ChessPosition.Direction.LEFT, ChessPosition.Direction.LEFT_UP);
        addToCanMoveList(knightPos, ChessPosition.Direction.UP, ChessPosition.Direction.LEFT_UP);
        addToCanMoveList(knightPos, ChessPosition.Direction.UP, ChessPosition.Direction.RIGHT_UP);
        addToCanMoveList(knightPos, ChessPosition.Direction.RIGHT, ChessPosition.Direction.RIGHT_UP);
        addToCanMoveList(knightPos, ChessPosition.Direction.RIGHT, ChessPosition.Direction.RIGHT_DOWN);
        addToCanMoveList(knightPos, ChessPosition.Direction.DOWN, ChessPosition.Direction.RIGHT_DOWN);
        addToCanMoveList(knightPos, ChessPosition.Direction.DOWN, ChessPosition.Direction.LEFT_DOWN);
        addToCanMoveList(knightPos, ChessPosition.Direction.LEFT, ChessPosition.Direction.LEFT_DOWN);
    }

    private void addToCanMoveList(ChessPosition knightPos, ChessPosition.Direction d1, ChessPosition.Direction d2) {
        ChessPosition targetPos = ChessPosition.get(knightPos, d1);
        if (targetPos != null) {
            targetPos = ChessPosition.get(targetPos, d2);
            if (isEmptyOrEnemyPos(targetPos)) {
                canMoveList.add(targetPos);
            }
        }
    }
}
