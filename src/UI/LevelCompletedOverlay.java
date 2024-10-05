package UI;

import GameStates.GameState;
import GameStates.Playing;
import GameUtilities.LoadSave;
import MainComponents.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static GameUtilities.Constants.URM_Buttons.*;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage overlayImage;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;

    public LevelCompletedOverlay(Playing playing) throws IOException {
        this.playing = playing;
        initImage();
        initButtons();
    }

    private void initButtons() throws IOException {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0); // check sprites, this is next index
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2); // for menu this is 3rd sprite obj.


    }

    private void initImage() throws IOException {
        overlayImage = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETE);
        backgroundWidth = (int) (overlayImage.getWidth() * Game.SCALE);
        backgroundHeight = (int) (overlayImage.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * Game.SCALE);

    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(overlayImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update() {
        next.update();
        menu.update();
    }

    private boolean IsInButton(UrmButton button, MouseEvent e) { // check if we're in the button while clicking
        return button.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (IsInButton(menu, e)) {
            menu.setMouseOver(true);
        } else if (IsInButton(next, e)) {
            next.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) throws IOException {
        if (IsInButton(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(GameState.MENU);
            }
        } else if (IsInButton(next, e)) {
            if(next.isMousePressed()){
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }
        }
        menu.resetBooleans();
        next.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (IsInButton(menu, e)) {
            menu.setMousePressed(true);
        } else if (IsInButton(next, e)) {
            next.setMousePressed(true);
        }
    }

}
