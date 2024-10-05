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

import static GameUtilities.Constants.URM_Buttons.URM_SIZE;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage image;
    private int imageX, imageY, imageWidth, imageHeight;
    private UrmButton menu, play;

    public GameOverOverlay(Playing playing) throws IOException {
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() throws IOException {
        int menuX = (int) (335 * Game.SCALE);
        int playX = (int) (440 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0); // for menu this is 3rd sprite obj.
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2); // check sprites, this is next index

    }

    private void createImg() throws IOException {
        image = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imageWidth = (int) (image.getWidth() * Game.SCALE);
        imageHeight = (int) (image.getHeight() * Game.SCALE);
        imageX = Game.GAME_WIDTH / 2 - imageWidth / 2; // in the center of the game window
        imageY = (int) (100 * Game.SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(image, imageX, imageY, imageWidth, imageHeight, null);

        menu.draw(g);
        play.draw(g);

//        g.setColor(Color.WHITE);
//        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
//        g.drawString("Press esc to enter Main Menu!", Game.GAME_WIDTH / 2, 300);
    }

    public void update(){
        menu.update();
        play.update();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }

    private boolean IsInButton(UrmButton button, MouseEvent e) { // check if we're in the button while clicking
        return button.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if (IsInButton(menu, e)) {
            menu.setMouseOver(true);
        } else if (IsInButton(play, e)) {
            play.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) throws IOException {
        if (IsInButton(menu, e)) {
            if (menu.isMousePressed()) {
            playing.resetAll();
            playing.setGameState(GameState.MENU);
            }
        } else if (IsInButton(play, e)) {
            if (play.isMousePressed()) {
                playing.resetAll();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }
        }
        menu.resetBooleans();
        play.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (IsInButton(menu, e)) {
            menu.setMousePressed(true);
        } else if (IsInButton(play, e)) {
            play.setMousePressed(true);
        }
    }

}
