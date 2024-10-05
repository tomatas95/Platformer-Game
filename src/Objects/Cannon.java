package Objects;

import MainComponents.Game;

public class Cannon extends GameObject {
    private int tileY; // if we're on the same tile, as enemy class we did

    public Cannon(int x, int y, int objectType) {
        super(x, y, objectType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
        hitBox.x -= (int) (4 * Game.SCALE); // offset issues https://gyazo.com/4ac9f8a6e3430bf7b55466a9cac86ec4
        hitBox.y += (int) (6 * Game.SCALE); // to match all sprites 32x32 pixel sprite as tile is 32px.
    }

    public void update(){
        if(doAnimation){
            updateAnimationTick();
        }
    }

    public int getTileY() {
        return tileY;
    }
}
