package components.DemoChess;

import imgui.ImGui;
import system.GameObject;

public class MoveOfQueen extends ChessMove {
    @Override
    public void calcTheCanMoveList() {
        canMoveList.clear();

        ChessPosition queenPos = this.gameObject.getComponent(ChessPosition.class);

        ChessPosition targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.LEFT_UP);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.LEFT_UP);
        }

        targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.LEFT_DOWN);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.LEFT_DOWN);
        }

        targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.RIGHT_UP);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.RIGHT_UP);
        }

        targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.RIGHT_DOWN);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.RIGHT_DOWN);
        }

        targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.UP);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.UP);
        }

        targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.DOWN);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.DOWN);
        }

        targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.LEFT);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.LEFT);
        }

        targetPos = ChessPosition.get(queenPos, ChessPosition.Direction.RIGHT);
        while (isEmptyOrEnemyPos(targetPos)) {
            canMoveList.add(targetPos);
            if (isEnemyPos(targetPos)) break;
            targetPos = ChessPosition.get(targetPos, ChessPosition.Direction.RIGHT);
        }
    }
}