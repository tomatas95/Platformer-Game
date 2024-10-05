package GameStates;

import GameUtilities.LoadSave;
import MainComponents.Game;
import UI.AudioOptions;
import UI.PauseButton;
import UI.UrmButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static GameUtilities.Constants.URM_Buttons.*;

public class GameOptions extends GameDesirableState implements StateFunctions {

    private AudioOptions audioOptions;
    private BufferedImage backgroundImage, optionsBackgroundImage;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private UrmButton menuButton;

    public GameOptions(Game game) throws IOException {
        super(game);
        loadImages(); // background & controls
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    private void loadButton() throws IOException {
        int menuX = (int) (387 * Game.SCALE); // center
        int menuY = (int) (325 * Game.SCALE);

        menuButton = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2); // last index arrary of the image

    }

    private void loadImages() throws IOException {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMAGE_FILLER);
        optionsBackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

        backgroundWidth = (int) (optionsBackgroundImage.getWidth() * Game.SCALE);
        backgroundHeight = (int) (optionsBackgroundImage.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (33 * Game.SCALE);
    }

    @Override
    public void update() {
        menuButton.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        menuButton.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isInPauseScreen(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) throws IOException {
        if (isInPauseScreen(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else {
            audioOptions.mouseReleased(e);
        }
        menuButton.resetBooleans(); // once we release the mouse we just reset everything
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        if (isInPauseScreen(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private boolean isInPauseScreen(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}