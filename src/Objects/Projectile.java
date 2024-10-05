package Objects;

import MainComponents.Game;

import java.awt.geom.Rectangle2D;

import static GameUtilities.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitBox;
    private int direction; // either to the left or to the right
    private boolean active = true; // to see if it has to be drawn or not

    public Projectile(int x, int y, int direction){
        int xOffset = (int) (-3 * Game.SCALE); // facing to the left because of https://gyazo.com/0b52ca6023f63c733119e2b809aad857
        int yOffset = (int) (5 * Game.SCALE);

        if(direction == 1){
            // right, we gotta check both differently because x wise they differ a lot unlike y
            xOffset = (int) (29 * Game.SCALE);
        }
        hitBox = new Rectangle2D.Float(x + xOffset,y + yOffset,CANNON_BALL_WIDTH,CANNON_BALL_HEIGHT);
        this.direction = direction;
    }

    public void updatePosition(){
        hitBox.x += direction * SPEED; // https://gyazo.com/3ed9b5c229a62f8594a48631464da32f
    }

    public void setPosition(int x, int y){
        hitBox.x = x;
        hitBox.y = y;
    }

    public Rectangle2D.Float getHitBox(){
        return hitBox; // to check if it hits the player or not
    }
    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return active;
    }
}
