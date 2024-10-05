package Objects;

import MainComponents.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static GameUtilities.Constants.ANIMATION_SPEED;
import static GameUtilities.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(objectType)) {
                animationIndex = 0;
                if (objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    active = false; // we don't want to draw nor animate once the objects gets destroyed!
                } else if (objectType == CANNON_AIM_LEFT || objectType == CANNON_AIM_RIGHT) {
                    doAnimation = false;
                }
            }
        }
    }

    public void resetObj() {
        animationIndex = 0;
        animationTick = 0;
        active = true;
        if (objectType == BARREL || objectType == BOX || objectType == CANNON_AIM_LEFT || objectType == CANNON_AIM_RIGHT) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }

    }

    protected void initHitbox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLevelOffset) {
        // debug the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    public int getObjectType() {
        return objectType;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }


    public boolean isActive() {
        return active;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public void setAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }
}
