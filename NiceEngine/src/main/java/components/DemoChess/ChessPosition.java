package components.DemoChess;

import components.Component;

public class ChessPosition extends Component {
    public enum Column {
        A, B, C, D, E, F, G, H
    }

    public enum Row {
        R1, R2, R3, R4, R5, R6, R7, R8
    }

    public Column column;
    public Row row;
}
