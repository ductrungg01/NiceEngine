package components.DemoChess;

import components.Component;
import org.joml.Vector2f;
import system.GameObject;
import system.Window;

import java.util.List;

public class ChessPosition extends Component {
    public enum Column {
        A, B, C, D, E, F, G, H
    }

    public enum Row {
        R1, R2, R3, R4, R5, R6, R7, R8
    }

    public enum Direction {
        LEFT_UP, UP, RIGHT_UP,
        LEFT, RIGHT,
        LEFT_DOWN, DOWN, RIGHT_DOWN;
    }

    public Column column;
    public Row row;

    public boolean equals(ChessPosition other) {
        return this.column == other.column && this.row == other.row;
    }

    public static ChessPosition get(ChessPosition centerPos, Direction direction) {
        if (direction == null) return null;

        int rowIndex = centerPos.row.ordinal();
        int columnIndex = centerPos.column.ordinal();

        final int MAX_ROW_INDEX = Row.values().length - 1;
        final int MAX_COLUMN_INDEX = Column.values().length - 1;

        int rowOffset = 0;
        if (direction == Direction.UP || direction == Direction.LEFT_UP || direction == Direction.RIGHT_UP)
            rowOffset = 1;
        else if (direction == Direction.DOWN || direction == Direction.LEFT_DOWN || direction == Direction.RIGHT_DOWN)
            rowOffset = -1;
        int columnOffset = 0;
        if (direction == Direction.LEFT || direction == Direction.LEFT_UP || direction == Direction.LEFT_DOWN)
            columnOffset = -1;
        else if (direction == Direction.RIGHT || direction == Direction.RIGHT_UP || direction == Direction.RIGHT_DOWN)
            columnOffset = 1;

        int target_row_index = rowIndex + rowOffset;
        int target_column_index = columnIndex + columnOffset;

        if (target_row_index < 0 || target_row_index > MAX_ROW_INDEX || target_column_index < 0 || target_column_index > MAX_COLUMN_INDEX)
            return null;

        ChessPosition chessPosition = new ChessPosition();
        chessPosition.row = Row.values()[target_row_index];
        chessPosition.column = Column.values()[target_column_index];

        return chessPosition;
    }

    public static Vector2f getRealPositionOf(ChessPosition pos) {
        List<GameObject> squares = Window.getScene().findAllGameObjectWithTag("Square");

        for (GameObject s : squares) {
            if (s.getComponent(ChessPosition.class).equals(pos)) {
                return new Vector2f(s.transform.position);
            }
        }

        return new Vector2f();
    }
}
