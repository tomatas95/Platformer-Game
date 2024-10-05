package UI;

import GameStates.GameState;
import GameUtilities.LoadSave;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static GameUtilities.Constants.MenuButtons.Buttons.*;

public class MenuButton {
    private int xOffsetCenter = BUTTON_WIDTH / 2;
    private int xPosition, yPosition, rowIndex, index;
    private BufferedImage[] MenuImages;
    private boolean mouseOver, mousePressed;
    private Rectangle buttonBounds; // a.k.a a hitbox
    private GameState state;

    public MenuButton(int xPosition, int yPosition, int rowIndex, GameState state) throws IOException {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        initBounds();
    }

    private void initBounds() {
        buttonBounds = new Rectangle(xPosition - xOffsetCenter,yPosition,BUTTON_WIDTH,BUTTON_HEIGHT);
    }

    private void loadImages() throws IOException {
        MenuImages = new BufferedImage[3]; // because there's 3 different buttons in the menu sprite
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < MenuImages.length; i++) {
            MenuImages[i] = temp.getSubimage(i * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(MenuImages[index], xPosition - xOffsetCenter, yPosition, BUTTON_WIDTH, BUTTON_HEIGHT, null);

    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return buttonBounds;
    }


    public void applyGameState(){
        GameState.state = state;
    }
    public void resetBooleans(){
        mouseOver = false;
        mousePressed = false;
    }

    public GameState getState(){
        return state;
    }
}
