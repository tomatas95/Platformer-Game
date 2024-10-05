package GameStates;

import GameUtilities.LoadSave;
import MainComponents.Game;
import UI.MenuButton;

import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Menu extends GameDesirableState implements StateFunctions {
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage background, menuBackgroundFillerImage;

    private int menuX, menuY, menuWidth, menuHeight;


    public Menu(Game game) throws IOException {
        super(game);
        loadButtons();
        loadBackground();
        menuBackgroundFillerImage =LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMAGE_FILLER);
    }

    private void loadBackground() throws IOException {
        background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int)(background.getWidth() * Game.SCALE);
        menuHeight = (int)(background.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2; // in the center like button[] xPosition, and we take away half the width
        menuY = (int)(45 * Game.SCALE);
    }

    private void loadButtons() throws IOException {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, GameState.PLAYING);
        //  Game_WIDTH / 2, so we have a centered menu buttons. Afterwards it's the y coords which is 150 as an example
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, GameState.QUIT);

    }

    @Override
    public void update() {
        for (MenuButton menubutton : buttons) {
            menubutton.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(menuBackgroundFillerImage,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
        g.drawImage(background,menuX,menuY,menuWidth,menuHeight,null);

        for(MenuButton menubutton : buttons){
            menubutton.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    for(MenuButton menubutton : buttons){
        if(isInButton(e,menubutton)){
            menubutton.setMousePressed(true); // if user just clicks the button normally
            break;
        }
    }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton menubutton : buttons){
            if(isInButton(e,menubutton)){
                if(menubutton.isMousePressed()){
                    menubutton.applyGameState(); // if the user presses the button inside the button and we change the game state
                }
                if(menubutton.getState() == GameState.PLAYING){
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for(MenuButton menubutton : buttons){
            menubutton.resetBooleans();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton menubutton : buttons) {
        menubutton.setMouseOver(false); // we may move from one button to another so we're resetting it back
        }

        for(MenuButton menubutton : buttons) {
            if(isInButton(e,menubutton)){
                menubutton.setMouseOver(true); // are we hovering on any type of button? if so... we make it true
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
