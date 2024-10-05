package GameStates;

import GameUtilities.LoadSave;
import MainComponents.Game;
import Objects.ObjectManager;
import UI.GameOverOverlay;
import UI.LevelCompletedOverlay;
import UI.PauseOverlay;
import GameEntities.EnemyManager;
import GameEntities.Player;
import GameLevels.LevelManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;


public class Playing extends GameDesirableState implements StateFunctions {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private boolean paused = false;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    // Variables to make the level longer, a.k.a move as the player goes to the left
    private int xLevelOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxTilesOffsetX;

    // moving background
    private BufferedImage backgroundImage, bigClouds, smallClouds;
    private Random rnd = new Random();
    private int[] smallCloudPosition;
    private boolean gameOver = false;
    private boolean levelCompleted;
    private boolean playerDying;

    public Playing(Game game) throws IOException, URISyntaxException {
        super(game);
        initClasses();

        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMAGE_FILLER);

        calculateLevelOffset();
        loadStartLevel();
    }

    public void loadNextLevel() throws IOException {
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();

    }

    private void loadStartLevel() throws IOException {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calculateLevelOffset() {
        maxTilesOffsetX = levelManager.getCurrentLevel().getLevelOffset();
    }

    private void initClasses() throws IOException, URISyntaxException {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (50 * Game.SCALE), (int) (37 * Game.SCALE), this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (levelCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(),player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkBorders();
        }
    }

    private void checkBorders() {
        int playerX = (int) player.getHitBox().x;
        int difference = playerX - xLevelOffset;
        if (difference > rightBorder) {
            xLevelOffset += difference - rightBorder; // https://gyazo.com/0923eac571904e2763f1e27f198f1450
        } else if (difference < leftBorder) {
            xLevelOffset += difference - leftBorder; // https://gyazo.com/50ed53d09d0dde0b989e6809ea1e26c3
        }
        if (xLevelOffset > maxTilesOffsetX) {
            xLevelOffset = maxTilesOffsetX;
        } else if (xLevelOffset < 0) {
            xLevelOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        levelManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyManager.draw(g, xLevelOffset);
        objectManager.draw(g,xLevelOffset);
        if (paused) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (levelCompleted) {
            levelCompletedOverlay.draw(g);
        }
    }

    public void resetAll() {
        // Reset Player, Enemy, everything
        gameOver = false;
        paused = false;
        levelCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitBox) {
        objectManager.checkObjTouchedThePlayer(hitBox);
    }
    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouchPlayer(player);
    }
    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                player.powerAttack();
            }
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
            } else if (levelCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        }else {
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) throws IOException {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            } else if (levelCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        }else{
            gameOverOverlay.mouseReleased(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if (levelCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }
        }else{
            gameOverOverlay.mouseMoved(e);
        }
    }

    public void setMaxTilesOffsetX(int levelOffset) {
        this.maxTilesOffsetX = levelOffset;
    }

    public void unPauseGame() {
        paused = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(true);
                case KeyEvent.VK_D -> player.setRight(true);
                case KeyEvent.VK_SPACE -> player.setJump(true);
                case KeyEvent.VK_ESCAPE -> paused = !paused;

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(false);
                case KeyEvent.VK_D -> player.setRight(false);
                case KeyEvent.VK_SPACE -> player.setJump(false);

            }
        }
    }

    public void windowFocusLost() {
        player.resetDirectionBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
        if(levelCompleted){
            game.getAudioPlayer().lvlCompleted();
        }
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
