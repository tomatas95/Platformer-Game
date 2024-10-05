package Objects;

import MainComponents.Game;

public class Potion extends GameObject {
    private float hoverOffset;
    private int maxHoverOffset;
    private int hoverDirection = 1; // it goes down if it's 1 it'll be either -1 or 1 (we wanna go up or down)

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitbox(7, 14); // https://gyazo.com/edc73eaba3e632e2aaa3ef0067f33ba3 not exactly the same, because
        // they differ in size blue potion and red potion
        xDrawOffset = (int) (3 * Game.SCALE); // remember! this is because of drawOffset from the sprite's side.
        yDrawOffset = (int) (2 * Game.SCALE); // https://gyazo.com/e95de2e012ec431cccaafdc33c3d3cc7

        maxHoverOffset = (int) (10 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() { // makes cool potion animation going up and down
        hoverOffset += (0.075f * Game.SCALE * hoverDirection); // if it's 1 it increases if it's -1 it decreases
        if (hoverOffset >= maxHoverOffset) {
            hoverDirection = -1; // change direction goes up and now down
        } else if (hoverOffset < 0) {
            hoverDirection = 1;
        }
        hitBox.y = y + hoverOffset;
    }
}
