package Objects;

import MainComponents.Game;

import static GameUtilities.Constants.ObjectConstants.*;

public class GameContainer extends GameObject {

    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitBox(); // we'll have 2 different hitboxes for box and barrels because they differ a lot
    }

    private void createHitBox() {
        if(objectType == BOX){
            initHitbox(25,18);
            xDrawOffset = (int)(7 * Game.SCALE);
            yDrawOffset = (int)(12 * Game.SCALE);
        }else{ // barrel
        initHitbox(23,25);
            xDrawOffset = (int)(8 * Game.SCALE);
            yDrawOffset = (int)(5 * Game.SCALE);
        }
        hitBox.y += yDrawOffset + (int)(Game.SCALE * 2); // https://gyazo.com/03f4d21861de33f838407e7cccf6a1f7
        // explains well why we need to offset the hitbox, so it properly touches the floor.
        // https://gyazo.com/d264bfcbd0da652848c66d375d9c654b because the pixels of game tiles are 32px we add the 2
        // because our containers are by 40x30
        hitBox.x += xDrawOffset / 2; // so we get this in the center in the tile
    }

    public void update(){
        if(doAnimation){
            updateAnimationTick();
        }
    }
}
