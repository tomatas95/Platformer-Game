package GameUtilities;

import MainComponents.Game;
import Objects.*;
import GameEntities.Hoodler;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static GameUtilities.Constants.EnemyState.HOODLER;
import static GameUtilities.Constants.ObjectConstants.*;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if (!IsSolid(x, y, levelData)) {
            if (!IsSolid(x + width, y + height, levelData)) {
                if (!IsSolid(x + width, y, levelData)) {
                    if (!IsSolid(x, y + height, levelData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] levelData) {
        int maxWidth = levelData[0].length * Game.TILES_SIZE; // width in pixels from a level and tile size multiplication
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, levelData);
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] levelData){
        return IsSolid(p.getHitBox().x + p.getHitBox().width / 2, p.getHitBox().y + p.getHitBox().height / 2, levelData); // https://gyazo.com/e88bd8c7a9599a022abc43ea6e9d7a58 get the center ball
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {
        int value = levelData[yTile][xTile];
        if (value >= 48 || value < 0 || value != 11) {// 12th sprite is transparent, 48 is the max tile of a level
            // https://gyazo.com/76b1fb29f61fe837e9a8a13978830bcd
            return true;
        } else {
            return false;
        }
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // right
            int tileXPos = currentTile * Game.TILES_SIZE; // we need in px value
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width); // distance between the tile and the player model
            return tileXPos + xOffset - 1; // -1  so the player model isn't overlapping with the tile https://gyazo.com/0d7d903c55777400ba2a0f7546cf203a
        } else {
            // left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            // jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        // check the px below bottom left and bottom right corners if they're both not solid then we're in the air
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) {
                // + 1 since we took -1 offset while returning tileYPos in GetEntityYPosUnderRoofOrAboveFloor
                return false;
            }
        }
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        if (xSpeed > 0) { // going to the right
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, levelData);
        } else {
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData); // left

        }
        //  + 1 because we aren't checking bottom of our hitbox, but rather the pixel below the hitbox
    }

    public static boolean CanCannonSeePlayer(int[][] levelData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int tileY) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);
        // is there any obstacle between the hitboxes => https://gyazo.com/6ad9bbf58f09092fd14cfa67c593dda9
        if (firstXTile > secondXTile) {
            return IsAllTilesClear(secondXTile, firstXTile, tileY, levelData); // instead of all tiles are walkable we need to check
            // the line of sight!
        } else {
            return IsAllTilesClear(firstXTile, secondXTile, tileY, levelData);
        }
    }

    /*
    Check the tiles
     */
    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] levelData) { // no solid tiles from one point to another
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, levelData)) {
                return false;
            }
        }
        return true;
    }

    /*
    Checks also the tiles beneath the tiles we're checking a.k.a the ground for example
     */
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] levelData) {
        if (IsAllTilesClear(xStart, xEnd, y, levelData)) {
            for (int i = 0; i < xEnd - xStart; i++) {
                // tile between player and enemy https://gyazo.com/f44acb02ac0b36f8b0d80170b88ae476
                if (!IsTileSolid(xStart + i, y + 1, levelData)) // we check underneath  too
                    return false;
            }
        }
        return true;
    }

    public static boolean IsSightClear(int[][] levelData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int TileY) {
        // what x tile these both hitboxes are?
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);
        // is there any obstacle between the hitboxes => https://gyazo.com/6ad9bbf58f09092fd14cfa67c593dda9
        if (firstXTile > secondXTile) {
            return IsAllTilesWalkable(secondXTile, firstXTile, TileY, levelData);
        } else { // 1st tile is smaller than 2nd
            return IsAllTilesWalkable(firstXTile, secondXTile, TileY, levelData);
        }
    }

    public static int[][] GetLevelData(BufferedImage img) throws IOException {
        int[][] levelData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                levelData[j][i] = value;
            }
        }
        return levelData;
    }

    public static ArrayList<Hoodler> getHoodlerEnemies(BufferedImage img) throws IOException {
        ArrayList<Hoodler> hoodlerList = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen(); // we're using green for enemies, we used red for in game tiles
                // https://gyazo.com/f8a932c1ab62eab7a8542224b0af4ac1 and we have to edit the sprite itself with 0 green
                // value places since we said to add an enemy where the green is 0.
                if (value == HOODLER) {
                    hoodlerList.add(new Hoodler(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
                }
            }
        }
        return hoodlerList;
    }

    public static Point GetPlayerSpawn(BufferedImage img) { // Point is just a position with x & y
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100) {
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
                }
            }
        }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img) throws IOException {
        ArrayList<Potion> potionList = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue(); // like with getCrabs and levels, we use different color to add the objects
                // in the game, so we'll use blue transparency for 0,1,2,3 as in Constants we've listed
                // 0 and 1 being blue and red potions.
                if (value == RED_POTION || value == BLUE_POTION) {
                    potionList.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
                }
            }
        }
        return potionList;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img) throws IOException {
        ArrayList<GameContainer> containerList = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue(); // like with getCrabs and levels, we use different color to add the objects
                // in the game, so we'll use blue transparency for 0,1,2,3 as in Constants we've listed
                if (value == BARREL || value == BOX) {
                    containerList.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
                }
            }
        }
        return containerList;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> spikeList = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue(); // like with getCrabs and levels, we use different color to add the objects
                // in the game, so we'll use blue transparency for 0,1,2,3 as in Constants we've listed
                if (value == SPIKE) {
                    spikeList.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
                }
            }
        }
        return spikeList;
    }

    public static ArrayList<Cannon> GetCannons(BufferedImage img) {
        ArrayList<Cannon> cannonList = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue(); // like with getCrabs and levels, we use different color to add the objects
                // in the game, so we'll use blue transparency for 0,1,2,3 as in Constants we've listed
                if (value == CANNON_AIM_LEFT || value == CANNON_AIM_RIGHT) {
                    cannonList.add(new Cannon(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
                }
            }
        }
        return cannonList;
    }
}
