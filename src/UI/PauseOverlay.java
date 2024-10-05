package UI;

import GameStates.GameState;
import GameStates.Playing;
import GameUtilities.LoadSave;
import MainComponents.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

import static GameUtilities.Constants.MenuButtons.PauseButton.*;
import static GameUtilities.Constants.URM_Buttons.*;
import static GameUtilities.Constants.Volume_Buttons.*;

public class PauseOverlay {
    private BufferedImage backgroundImage;
    private Playing playing;
    private AudioOptions audioOptions;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private UrmButton menuButton, replayB, unpauseB;

    public PauseOverlay(Playing playing) throws IOException {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createURMButtons();
    }

    private void createURMButtons() throws IOException {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int resumeX = (int) (462 * Game.SCALE);

        int URMButtonsY = (int) (325 * Game.SCALE);

        menuButton = new UrmButton(menuX, URMButtonsY, URM_SIZE, URM_SIZE, 2); // which row is this button on?
        replayB = new UrmButton(replayX, URMButtonsY, URM_SIZE, URM_SIZE, 1); // which row is this button on?
        unpauseB = new UrmButton(resumeX, URMButtonsY, URM_SIZE, URM_SIZE, 0); // which row is this button on?

    }

    private void loadBackground() throws IOException {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = (int) (backgroundImage.getWidth() * Game.SCALE);
        backgroundHeight = (int) (backgroundImage.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (25 * Game.SCALE);
    }

    public void update() {
        menuButton.update();
        replayB.update();
        unpauseB.update();

        audioOptions.update();

    }

    public void draw(Graphics g) {
        // Background pause
        g.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        // URM Buttons
        menuButton.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        audioOptions.draw(g);

    }

    public void mousePressed(MouseEvent e) {
        if (isInPauseScreen(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isInPauseScreen(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isInPauseScreen(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isInPauseScreen(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(GameState.MENU);
                playing.unPauseGame();
            }
        } else if (isInPauseScreen(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAll();
                playing.unPauseGame();
            }
        } else if (isInPauseScreen(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unPauseGame();
            }
        }else{
            audioOptions.mouseReleased(e);
        }
        menuButton.resetBooleans();
        unpauseB.resetBooleans();
        replayB.resetBooleans();

    }

    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);

        if(isInPauseScreen(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isInPauseScreen(e, replayB)) {
            replayB.setMouseOver(true);
        } else if (isInPauseScreen(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else{
            audioOptions.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    private boolean isInPauseScreen(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}
