package components.DemoChess;

import components.Component;
import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;
import system.GameObject;
import system.Prefabs;
import system.Window;

import java.util.List;

public class ChessManager extends Component {
    private transient List<GameObject> squares;
    public Sprite spr_white_king;
    public Sprite spr_white_queen;
    public Sprite spr_white_bishop;
    public Sprite spr_white_rook;
    public Sprite spr_white_knight;
    public Sprite spr_white_pawn;
    public Sprite spr_black_king;
    public Sprite spr_black_queen;
    public Sprite spr_black_bishop;
    public Sprite spr_black_rook;
    public Sprite spr_black_knight;
    public Sprite spr_black_pawn;

    private transient List<GameObject> chessPieces;

    @Override
    public void start() {
        squares = Window.getScene().findAllGameObjectWithTag("Square");
        setupChessTable();
    }

    private void setupChessTable() {
        //region White
        GameObject king_white = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.KING);
        king_white.transform.position = getPositionOf("E", 1);
        Window.getScene().addGameObjectToScene(king_white);

        GameObject queen_white = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.QUEEN);
        queen_white.transform.position = getPositionOf("D", 1);
        Window.getScene().addGameObjectToScene(queen_white);

        GameObject bishop_white1 = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.BISHOP);
        bishop_white1.transform.position = getPositionOf("C", 1);
        Window.getScene().addGameObjectToScene(bishop_white1);

        GameObject knight_white1 = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.KNIGHT);
        knight_white1.transform.position = getPositionOf("B", 1);
        Window.getScene().addGameObjectToScene(knight_white1);

        GameObject rook_white1 = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.ROOK);
        rook_white1.transform.position = getPositionOf("A", 1);
        Window.getScene().addGameObjectToScene(rook_white1);

        GameObject bishop_white2 = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.BISHOP);
        bishop_white2.transform.position = getPositionOf("F", 1);
        Window.getScene().addGameObjectToScene(bishop_white2);

        GameObject knight_white2 = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.KNIGHT);
        knight_white2.transform.position = getPositionOf("G", 1);
        Window.getScene().addGameObjectToScene(knight_white2);

        GameObject rook_white2 = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.ROOK);
        rook_white2.transform.position = getPositionOf("H", 1);
        Window.getScene().addGameObjectToScene(rook_white2);

        for (int i = 0; i < 8; i++) {
            GameObject pawn = createAChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN);
            pawn.transform.position = getPositionOf(Character.toString((char) 65 + i), 2);
            Window.getScene().addGameObjectToScene(pawn);
        }
        //endregion

        //region Black
        GameObject king_black = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.KING);
        king_black.transform.position = getPositionOf("E", 8);
        Window.getScene().addGameObjectToScene(king_black);

        GameObject queen_black = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.QUEEN);
        queen_black.transform.position = getPositionOf("D", 8);
        Window.getScene().addGameObjectToScene(queen_black);

        GameObject bishop_black1 = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.BISHOP);
        bishop_black1.transform.position = getPositionOf("C", 8);
        Window.getScene().addGameObjectToScene(bishop_black1);

        GameObject knight_black1 = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.KNIGHT);
        knight_black1.transform.position = getPositionOf("B", 8);
        Window.getScene().addGameObjectToScene(knight_black1);

        GameObject rook_black1 = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.ROOK);
        rook_black1.transform.position = getPositionOf("A", 8);
        Window.getScene().addGameObjectToScene(rook_black1);

        GameObject bishop_black2 = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.BISHOP);
        bishop_black2.transform.position = getPositionOf("F", 8);
        Window.getScene().addGameObjectToScene(bishop_black2);

        GameObject knight_black2 = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.KNIGHT);
        knight_black2.transform.position = getPositionOf("G", 8);
        Window.getScene().addGameObjectToScene(knight_black2);

        GameObject rook_black2 = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.ROOK);
        rook_black2.transform.position = getPositionOf("H", 8);
        Window.getScene().addGameObjectToScene(rook_black2);

        for (int i = 0; i < 8; i++) {
            GameObject pawn = createAChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN);
            pawn.transform.position = getPositionOf(Character.toString((char) 65 + i), 7);
            Window.getScene().addGameObjectToScene(pawn);
        }
        //endregion
    }

    private GameObject createAChessPiece(ChessPieceColor color, ChessPieceType type) {
        GameObject go = new GameObject("Chess Piece");

        if (color == ChessPieceColor.WHITE) {
            switch (type) {
                case KING -> {
                    if (spr_white_king != null) {
                        go = Prefabs.generateSpriteObject(spr_white_king, 0.25f, 0.25f, "King - white");
                    }
                }
                case QUEEN -> {
                    if (spr_white_queen != null) {
                        go = Prefabs.generateSpriteObject(spr_white_queen, 0.25f, 0.25f, "Queen - white");
                    }
                }
                case BISHOP -> {
                    if (spr_white_bishop != null) {
                        go = Prefabs.generateSpriteObject(spr_white_bishop, 0.25f, 0.25f, "Bishop - white");
                    }
                }
                case KNIGHT -> {
                    if (spr_white_knight != null) {
                        go = Prefabs.generateSpriteObject(spr_white_knight, 0.25f, 0.25f, "Knight - white");
                    }
                }
                case ROOK -> {
                    if (spr_white_rook != null) {
                        go = Prefabs.generateSpriteObject(spr_white_rook, 0.25f, 0.25f, "Rook - white");
                    }
                }
                case PAWN -> {
                    if (spr_white_pawn != null) {
                        go = Prefabs.generateSpriteObject(spr_white_pawn, 0.25f, 0.25f, "Pawn - white");
                    }
                }
            }
        } else {
            switch (type) {
                case KING -> {
                    if (spr_black_king != null) {
                        go = Prefabs.generateSpriteObject(spr_black_king, 0.25f, 0.25f, "King - black");
                    }
                }
                case QUEEN -> {
                    if (spr_black_queen != null) {
                        go = Prefabs.generateSpriteObject(spr_black_queen, 0.25f, 0.25f, "Queen - black");
                    }
                }
                case BISHOP -> {
                    if (spr_black_bishop != null) {
                        go = Prefabs.generateSpriteObject(spr_black_bishop, 0.25f, 0.25f, "Bishop - black");
                    }
                }
                case KNIGHT -> {
                    if (spr_black_knight != null) {
                        go = Prefabs.generateSpriteObject(spr_black_knight, 0.25f, 0.25f, "Knight - black");
                    }
                }
                case ROOK -> {
                    if (spr_black_rook != null) {
                        go = Prefabs.generateSpriteObject(spr_black_rook, 0.25f, 0.25f, "Rook - black");
                    }
                }
                case PAWN -> {
                    if (spr_black_pawn != null) {
                        go = Prefabs.generateSpriteObject(spr_black_pawn, 0.25f, 0.25f, "Pawn - black");
                    }
                }
            }
        }

        go.setNoSerialize();

        return go;
    }

    private Vector2f getPositionOf(String column, int row) {
        for (GameObject square : squares) {
            SquareInfor squareInfor = square.getComponent(SquareInfor.class);
            if (squareInfor == null) continue;

            if (squareInfor.column.equals(column) && squareInfor.row == row) {
                return new Vector2f(square.transform.position);
            }
        }

        return new Vector2f();
    }
}
