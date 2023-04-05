package components.scripts.test.mario;

import util.AssetPool;

public class BreakableBrick extends Block {
    //region Override methods
    @Override
    void playerHit(PlayerController playerController) {
        if (playerController.isSmall()) {
            AssetPool.getSound("assets/sounds/bump.ogg").play();
        } else {
            AssetPool.getSound("assets/sounds/break_block.ogg").play();
            gameObject.destroy();
        }
    }
    //endregion
}