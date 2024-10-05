package UI;

import GameUtilities.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static GameUtilities.Constants.URM_Buttons.*;

public class UrmButton extends PauseButton {
    private BufferedImage[] UrmImages;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) throws IOException {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadUrmImages();
    }

    private void loadUrmImages() throws IOException {
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_URM_BUTTONS);
        UrmImages = new BufferedImage[3];
        for (int i = 0; i < UrmImages.length; i++) {
            UrmImages[i] = tmp.getSubimage(i * URM_BUTTON_DEFAULT_SIZE, rowIndex * URM_BUTTON_DEFAULT_SIZE, URM_BUTTON_DEFAULT_SIZE, URM_BUTTON_DEFAULT_SIZE);
        }

    }

    public void draw(Graphics g) {
        g.drawImage(UrmImages[index], x, y, URM_SIZE, URM_SIZE, null); // URM_SIZE = WIDTH / HEIGHT
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

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false; // basically same thing what we did in the pause button for SFX  & Music Button
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
}
