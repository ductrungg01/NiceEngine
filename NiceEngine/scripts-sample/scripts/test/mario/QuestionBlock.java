package components.scripts.test.mario;

import components.StateMachine;
import components.scripts.test.MarioMoving;
import system.GameObject;
import system.Prefabs;
import system.Window;

public class QuestionBlock extends Block {
    private enum BlockType {
        Coin,
        Powerup,
        Invincibility
    }

    //region Fields
    public BlockType blockType = BlockType.Coin;
    //endregion

    //region Override methods
    @Override
    void playerHit(MarioMoving marioMoving) {
        switch (blockType) {
            case Coin:
                doCoin(marioMoving);
                break;
            case Powerup:
                doPowerup(marioMoving);
                break;
            case Invincibility:
                doInvincibility(marioMoving);
                break;
        }

        StateMachine stateMachine = gameObject.getComponent(StateMachine.class);
        if (stateMachine != null) {
            stateMachine.trigger("setInactive");
            this.setInactive();
        }
    }
    //endregion

    //region Methods
    private void doCoin(MarioMoving marioMoving) {
        GameObject coin = Prefabs.generateBlockCoin();
        coin.transform.position.set(this.gameObject.transform.position);
        coin.transform.position.y += 0.25f;
        Window.getScene().addGameObjectToScene(coin);
    }

    private void doPowerup(MarioMoving marioMoving) {
        if (marioMoving.isSmall()) {
            spawnMushroom();
        } else {
            spawnFlower();
        }
    }

    private void doInvincibility(MarioMoving marioMoving) {

    }

    private void spawnMushroom() {
        GameObject mushroom = Prefabs.generateMushroom();
        mushroom.transform.position.set(gameObject.transform.position);
        mushroom.transform.position.y += 0.25f;
        Window.getScene().addGameObjectToScene(mushroom);
    }

    private void spawnFlower() {
        GameObject flower = Prefabs.generateFlower();
        flower.transform.position.set(gameObject.transform.position);
        flower.transform.position.y += 0.25f;
        Window.getScene().addGameObjectToScene(flower);
    }
    //endregion
}