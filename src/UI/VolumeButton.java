package UI;

import GameUtilities.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static GameUtilities.Constants.Volume_Buttons.*;


public class VolumeButton extends PauseButton {

    private BufferedImage[] Volimages;
    private BufferedImage Volslider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX;
    private int maximumX, minimumX; // so the slider button doesn't go over or before the slider
    private float floatValue = 0f;
    public VolumeButton(int x, int y, int width, int height) throws IOException {
        super(x + width / 2, y, VOLUME_WIDTH, height); // to make button start in the middle of the volume's slider
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2; // button of the slider
        this.x = x;
        this.width = width;
        minimumX = x + VOLUME_WIDTH/ 2; // slider minimum X
        maximumX = x + width - VOLUME_WIDTH / 2; // slider maximum X
        loadVolImages();
    }

    private void loadVolImages() throws IOException {
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        Volimages = new BufferedImage[3];
        for (int i = 0; i < Volimages.length; i++)
            Volimages[i] = tmp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        Volslider = tmp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        // https://gyazo.com/80107dfe51fea5a6ebe727b51e20b2b4 first we have vol buttons and THEEN the slider itself so we need to pass them
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

    public void draw(Graphics g) {
        // vol slider
        g.drawImage(Volslider, x, y, width, height, null);

        // vol buttons
        g.drawImage(Volimages[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null); // height is the same as volume slider's and button's
        // buttonX - VOLUME_WIDTH / 2 so the slider is in the middle of  mouse on an actual center and not left wise
    }

    public void changeSliderX(int x){
    if(x < minimumX){
        buttonX = minimumX; // minimum value possible
    } else if (x > maximumX) {
        buttonX = maximumX; // most maximum value possible
    }
    else{
        buttonX = x; // if it's within bounds so it's just x
    }
    updateFloatValue();
    bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    private void updateFloatValue() {
        float range = maximumX - minimumX; // changing values etc. how far the slider can go
        float value = buttonX - minimumX;
        floatValue = value / range; // change float volume values
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

    public float getFloatValue(){
        return floatValue;
    }
}
