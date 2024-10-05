package Objects;

import GameStates.Playing;
import GameUtilities.LoadSave;
import MainComponents.Game;
import GameEntities.Player;
import GameLevels.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static GameUtilities.Constants.ObjectConstants.*;
import static GameUtilities.Constants.Projectiles.*;
import static GameUtilities.HelpMethods.CanCannonSeePlayer;
import static GameUtilities.HelpMethods.IsProjectileHittingLevel;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImages, containerImages; // we got two differences in each sprite
    private BufferedImage spikeImage, cannonBallImage;
    private BufferedImage[] cannonImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> barrels;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectile = new ArrayList<>();

    private Rectangle2D.Float attackBox;

    public ObjectManager(Playing playing) throws IOException {
        this.playing = playing;
        loadImages();
    }

    public void checkSpikesTouchPlayer(Player player) {
        for (Spike s : spikes) {
            if (s.getHitBox().intersects(player.getHitBox())) {
                player.Kill();
            }
        }
    }

    public void checkObjTouchedThePlayer(Rectangle2D.Float hitBox) {
        // if players hitbox overlapped a potion hitbox I.e
        for (Potion p : potions) {
            if (p.isActive()) { // otherwise we already picked it up
                if (hitBox.intersects(p.getHitBox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }

    }

    public void applyEffectToPlayer(Potion p) {
        // if we did then we need to apply the effects to the player
        if (p.getObjectType() == RED_POTION) {
            playing.getPlayer().changeHealth(RED_POTION_VALUE);

        } else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);

        }
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        // if barrels got hit by the player they need to be destroyed obviously
        for (GameContainer gc : barrels) {
            if (gc.isActive() && !gc.doAnimation) {
                // check if player attackbox hits the barrels / box
                if (gc.getHitBox().intersects(attackBox)) {
                    gc.setAnimation(true); // we need to animate it once it's attacked
                    int type = 0;
                    if (gc.getObjectType() == BARREL) { // if its a box it'l
                        type = 1;
                    }
                    potions.add(new Potion((int) (gc.getHitBox().x + gc.getHitBox().width / 2),
                            (int) (gc.getHitBox().y - gc.getHitBox().height / 2), type));
                    return; // to avoid the player destroying two boxes at a time
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        barrels = new ArrayList<>(newLevel.getBarrels()); // we return arraylists of the objects both potions & boxes.
        spikes = newLevel.getSpikes(); // we'll never reset the spikes and there won't be any spikes spawning, they'll be static
        cannons = newLevel.getCannons();
        projectile.clear(); // clear all projectiles
    }

    private void loadImages() throws IOException {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImages = new BufferedImage[2][7]; // https://gyazo.com/db9ae4d5c850da6d31b4fae2935ce879
        for (int j = 0; j < potionImages.length; j++) {
            for (int i = 0; i < potionImages[j].length; i++) {
                potionImages[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);
            }
        }
        BufferedImage barrelSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8]; // https://gyazo.com/eace35d73534f27322a5bf128d5df6d0
        for (int j = 0; j < containerImages.length; j++) {
            for (int i = 0; i < containerImages[j].length; i++) {
                containerImages[j][i] = barrelSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }
        spikeImage = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImages = new BufferedImage[7]; // 7 sprites
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImages.length; i++) {
            cannonImages[i] = tmp.getSubimage(i * 40, 0, 40, 26);
        }

        cannonBallImage = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
    }

    public void update(int[][] levelData, Player player) {
        for (Potion p : potions) {
            if (p.isActive()) {
                p.update();
            }
        }
        for (GameContainer gc : barrels) {
            if (gc.isActive()) {
                gc.update(); // only update the animation if they're active!
            }
        }
        updateCannons(levelData, player);
        updateProjectile(levelData,player);
    }

    private void updateProjectile(int[][] levelData, Player player) {
        for(Projectile p : projectile){
            if(p.isActive()){
                p.updatePosition();
            }
            if(p.getHitBox().intersects(player.getHitBox())){
                // hitting player
                player.changeHealth(-25);
                p.setActive(false);
            } else if (IsProjectileHittingLevel(p,levelData)) {
                p.setActive(false);
            }
        }
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInFrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitBox(), c.getHitBox(), c.getTileY())) {
                                c.setAnimation(true);
                            }
            c.update();
            if(c.getAnimationIndex() == 4 && c.getAnimationTick() == 0){
                shootCannon(c); // we don't want to shoot instantly, but to make it sync with loading up and etc.
            }
        }
    }
        /*
        if the cannon is not animating as it's about to shoot or just shot
        if they're on the same tileY, then we check if player is within a certain range
        is player is infront of the cannon?
        check line of sight
        if so, then shoot / fire the cannon
         */
    private void shootCannon(Cannon c) {
        c.setAnimation(true);
        int direction = 1; // right
        if(c.getObjectType() == CANNON_AIM_LEFT){
            direction = -1;
        }
        projectile.add(new Projectile((int) c.getHitBox().x, (int) c.getHitBox().y, direction));
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if (c.getObjectType() == CANNON_AIM_LEFT) {
            if (c.getHitBox().x > player.getHitBox().x) { // player is on the left side https://gyazo.com/c9c15dd51078551d03742859fdb24fd1
                return true;
            }
        } else if (c.getHitBox().x < player.getHitBox().x) { // player is on the right side
            return true;
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitBox().x - c.getHitBox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
        drawTraps(g, xLevelOffset);
        drawCannons(g, xLevelOffset);
        drawProjectiles(g,xLevelOffset);
    }

    private void drawProjectiles(Graphics g, int xLevelOffset) {
        for(Projectile p : projectile){
            if(p.isActive()){
                g.drawImage(cannonBallImage, (int)(p.getHitBox().x  - xLevelOffset), (int)(p.getHitBox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int) (c.getHitBox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjectType() == CANNON_AIM_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImages[c.getAnimationIndex()], x, (int) (c.getHitBox().y), width, CANNON_HEIGHT, null);
        }

    }


    private void drawTraps(Graphics g, int xLevelOffset) {
        for (Spike s : spikes) {
            g.drawImage(spikeImage, (int) (s.getHitBox().x - xLevelOffset), (int) (s.getHitBox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
    }

    private void drawContainers(Graphics g, int xLevelOffset) {
        for (GameContainer gc : barrels) {
            if (gc.isActive()) {
                int type = 0; // box is 0th array element
                if (gc.getObjectType() == BARREL) {
                    type = 1; // barrels are 2nd items so 1 array slot
                }
                g.drawImage(containerImages[type][gc.getAnimationIndex()],
                        (int) (gc.getHitBox().x - gc.getxDrawOffset() - xLevelOffset),
                        (int) (gc.getHitBox().y - gc.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT, null);
            }
        }
    }

    private void drawPotions(Graphics g, int xLevelOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                int type = 0; // 0th is the blue potion and 1st is the health. https://gyazo.com/b5f0c7c7c41cfa4c6f7df0dbf649ceec
                if (p.getObjectType() == RED_POTION) {
                    type = 1;
                }
                g.drawImage(potionImages[type][p.getAnimationIndex()],
                        (int) (p.getHitBox().x - p.getxDrawOffset() - xLevelOffset),
                        (int) (p.getHitBox().y - p.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT, null);
            }
        }
    }

    public void resetAllObjects() {
        System.out.println("Size of arrays: " + potions.size() + " - " + barrels.size());
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion p : potions) {
            p.resetObj();
        }
        for (GameContainer gc : barrels) {
            gc.resetObj(); // just to reset them
        }
        for (Cannon c : cannons) {
            c.resetObj();
        }
//        System.out.println("After Size of arrays: " + potions.size() + " - " + barrels.size());

    }
}