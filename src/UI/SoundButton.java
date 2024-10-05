package UI;

import GameUtilities.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static GameUtilities.Constants.MenuButtons.PauseButton.*;

public class SoundButton extends PauseButton {
    private BufferedImage[][] soundButtons;
    private boolean mouseOver,mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    public SoundButton(int x, int y, int width, int height) throws IOException {
        super(x, y, width, height);
        loadBackgrounds();
    }

    private void loadBackgrounds() throws IOException {
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_SOUND_BUTTONS);
        soundButtons = new BufferedImage[2][3];
        for(int j=0;j<soundButtons.length;j++){
            for(int i=0;i<soundButtons[j].length;i++){
                soundButtons[j][i] = tmp.getSubimage(i*SOUND_SIZE_DEFAULT,j * SOUND_SIZE_DEFAULT,SOUND_SIZE_DEFAULT,SOUND_SIZE_DEFAULT);
            }
        }
    }
    public void update(){
    if(muted){
        rowIndex = 1;
    }else{
        rowIndex = 0;
    }
    colIndex = 0;
    if(mouseOver){
        colIndex = 1;
    }
    if(mousePressed){
        colIndex = 2; // because we have different sprites for each 0,1,2 so depending on which mouse event we are doing
        // we want a different sprite for said such event
    }
    }

    public void draw(Graphics g){
        g.drawImage(soundButtons[rowIndex][colIndex],x,y,width,height,null);
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
