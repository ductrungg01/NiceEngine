package components.DemoChess;

import components.Component;
import components.Sprite;
import editor.Debug;
import org.joml.Vector2f;
import scenes.GamePlayingSceneInitializer;
import system.GameObject;
import system.MouseListener;
import system.Prefabs;
import system.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class ChessManager extends Component {
    private transient List<GameObject> squares;
    private transient List<GameObject> chessPieces;
    private transient GameObject selectedPiece;

    private Sprite selectedPieceEffectSpr;
    private transient GameObject highLightSelectedPieceEffect;
    private Sprite squareCanGoEffectSpr;
    private transient List<GameObject> highlightSquareCanGoEffect = new ArrayList<>();
    private Sprite wrongPieceSpr;
    private transient GameObject highLightWrongPieceEffect;

    private transient List<ChessPosition> positionCanGo = new ArrayList<>();
    private transient boolean whiteTurn = true;

    @Override
    public void start() {
        squares = Window.getScene().findAllGameObjectWithTag("Square");
        chessPieces = Window.getScene().findAllGameObjectWithTag("ChessPiece");

        //region Create Effect
        highLightSelectedPieceEffect = new GameObject("HighLightSelectedChessPiece", selectedPieceEffectSpr, new Vector2f(0.25f, 0.25f));
        highLightSelectedPieceEffect.transform.position = new Vector2f(-100, -100);
        Window.getScene().addGameObjectToScene(highLightSelectedPieceEffect);

        highLightWrongPieceEffect = new GameObject("HighLightWrongChessPiece", wrongPieceSpr, new Vector2f(0.25f, 0.25f));
        highLightWrongPieceEffect.transform.position = new Vector2f(-100, -100);
        Window.getScene().addGameObjectToScene(highLightWrongPieceEffect);

        for (int i = 0; i < 64; i++) {
            GameObject go = new GameObject("squareCanGo", squareCanGoEffectSpr, new Vector2f(0.15f, 0.15f));
            go.transform.position = new Vector2f(-100, -100);
            Window.getScene().addGameObjectToScene(go);
            highlightSquareCanGoEffect.add(go);
        }
        //endregion

        resetEffect();
    }

    @Override
    public void update(float dt) {
        highLightWrongPieceEffect.transform.position = new Vector2f(-100, -100);

        GameObject squareBeClicked = checkClick();

        if (squareBeClicked != null) {
            ChessPosition squarePos = squareBeClicked.getComponent(ChessPosition.class);

            if (this.selectedPiece == null) {
                GameObject piece = getPieceHas(squarePos);
                if (piece != null) {
                    setAsNewSelectedPiece(piece);
                }
            } else {
                if (canGoTo(squarePos)) {
                    GameObject enemy = getPieceHas(squarePos);
                    if (enemy != null) {
                        eat(enemy);
                    }

                    this.selectedPiece.getComponent(ChessMove.class).move(squarePos);
                    recalcAllPiecePosCanMove();
                    this.selectedPiece = null;
                    resetEffect();

                    this.whiteTurn = !this.whiteTurn;
                } else {
                    GameObject piece = getPieceHas(squarePos);
                    if (piece != null) {
                        setAsNewSelectedPiece(piece);
                    } else {
                        this.selectedPiece = null;
                        resetEffect();
                    }
                }
            }
        }

    }

    private void setAsNewSelectedPiece(GameObject newSelectedPiece) {
        if (this.whiteTurn != (newSelectedPiece.getComponent(Piece.class).color == Piece.Color.WHITE)) {
            highLightWrongPieceEffect.transform.position = new Vector2f(newSelectedPiece.transform.position);
            return;
        }

        this.selectedPiece = newSelectedPiece;
        positionCanGo = this.selectedPiece.getComponent(ChessMove.class).getListPositionCanMove();

        highLightSelectedPieceEffect.transform.position = this.selectedPiece.transform.position;
        for (int i = 0; i < 64; i++) {
            if (i < positionCanGo.size()) {
                highlightSquareCanGoEffect.get(i).transform.position = ChessPosition.getRealPositionOf(positionCanGo.get(i));
            } else {
                highlightSquareCanGoEffect.get(i).transform.position = new Vector2f(-100, -100);
            }
        }
    }

    private void resetEffect() {
        highLightSelectedPieceEffect.transform.position = new Vector2f(-100, -100);
        highLightWrongPieceEffect.transform.position = new Vector2f(-100, -100);
        for (int i = 0; i < 64; i++) {
            highlightSquareCanGoEffect.get(i).transform.position = new Vector2f(-100, -100);
        }
    }

    private void recalcAllPiecePosCanMove() {
        for (GameObject piece : chessPieces) {
            piece.getComponent(ChessMove.class).calcTheCanMoveList();
        }
    }

    private boolean canGoTo(ChessPosition targetPos) {
        for (ChessPosition p : positionCanGo) {
            if (p.equals(targetPos)) {
                return true;
            }
        }

        return false;
    }

    private GameObject checkClick() {
        for (GameObject square : squares) {
            Vector2f pos = square.transform.position;
            Vector2f size = square.transform.scale;

            Vector2f start = new Vector2f(pos.x - size.x / 2f, pos.y - size.y / 2f);
            Vector2f end = new Vector2f(pos.x + size.x / 2f, pos.y + size.y / 2f);

            Vector2f mousePos = MouseListener.getWorld();

            if (mousePos.x >= start.x && mousePos.x <= end.x && mousePos.y >= start.y && mousePos.y <= end.y) {
                if (MouseListener.mouseBeginPress(GLFW_MOUSE_BUTTON_LEFT)) {
                    return square;
                }
            }
        }

        return null;
    }

    public GameObject getPieceHas(ChessPosition pos) {
        for (GameObject piece : chessPieces) {
            if (piece.getComponent(ChessPosition.class).equals(pos)) {
                return piece;
            }
        }

        return null;
    }

    private void eat(GameObject target) {
        this.chessPieces.remove(target);

        if (target.getComponent(Piece.class).type == Piece.Type.KING) {
            Window.changeScene(new GamePlayingSceneInitializer());
        }

        target.destroy();
    }
}
