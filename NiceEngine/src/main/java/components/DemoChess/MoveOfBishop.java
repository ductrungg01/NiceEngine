package components.DemoChess;

public class MoveOfBishop extends ChessMove {
    @Override
    public void calcTheCanMoveList() {
        canMoveList.clear();
        ChessPosition bishopPos = this.gameObject.getComponent(ChessPosition.class);

        ChessPosition targetPos = ChessPosition.get(bishopPos, ChessPosition.Direction.LEFT_UP);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.LEFT_UP);
        }

        targetPos = ChessPosition.get(bishopPos, ChessPosition.Direction.LEFT_DOWN);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.LEFT_DOWN);
        }

        targetPos = ChessPosition.get(bishopPos, ChessPosition.Direction.RIGHT_UP);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.RIGHT_UP);
        }

        targetPos = ChessPosition.get(bishopPos, ChessPosition.Direction.RIGHT_DOWN);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.RIGHT_DOWN);
        }
    }
}
