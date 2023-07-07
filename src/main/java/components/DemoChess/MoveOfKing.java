package components.DemoChess;

public class MoveOfKing extends ChessMove {
    @Override
    public void calcTheCanMoveList() {
        canMoveList.clear();
        ChessPosition kingPos = this.gameObject.getComponent(ChessPosition.class);

        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.LEFT_UP));
        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.UP));
        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.RIGHT_UP));
        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.LEFT));
        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.RIGHT));
        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.LEFT_DOWN));
        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.DOWN));
        addToCanMoveList(ChessPosition.get(kingPos, ChessPosition.Direction.RIGHT_DOWN));
    }

    private void addToCanMoveList(ChessPosition pos) {
        if (isEmptyOrEnemyPos(pos)) {
            this.canMoveList.add(pos);
        }
    }
}
