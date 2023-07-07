package components.DemoChess;

public class MoveOfRook extends ChessMove {
    @Override
    public void calcTheCanMoveList() {
        ChessPosition rookPos = this.gameObject.getComponent(ChessPosition.class);
        canMoveList.clear();

        ChessPosition targetPos = ChessPosition.get(rookPos, ChessPosition.Direction.UP);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.UP);
        }

        targetPos = ChessPosition.get(rookPos, ChessPosition.Direction.DOWN);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.DOWN);
        }

        targetPos = ChessPosition.get(rookPos, ChessPosition.Direction.LEFT);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.LEFT);
        }

        targetPos = ChessPosition.get(rookPos, ChessPosition.Direction.RIGHT);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.RIGHT);
        }
    }
}
