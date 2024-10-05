package UI;

import GameStates.GameState;
import MainComponents.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static GameUtilities.Constants.MenuButtons.PauseButton.SOUND_SIZE_IN_GAME;
import static GameUtilities.Constants.Volume_Buttons.SLIDER_WIDTH;
import static GameUtilities.Constants.Volume_Buttons.VOLUME_HEIGHT;

public class AudioOptions {
    private VolumeButton volumeButton;
    private SoundButton musicButton, soundEffectsButton;
    private Game game;

    public AudioOptions(Game game) throws IOException {
        this.game = game;
        createSoundButtons();
        createVolumeButtons();

    }

    private void createVolumeButtons() throws IOException {
        int volumeX = (int) (309 * Game.SCALE);
        int volumeY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT); // slider and volume's height are the same
    }

    private void createSoundButtons() throws IOException {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE_IN_GAME, SOUND_SIZE_IN_GAME);
        soundEffectsButton = new SoundButton(soundX, sfxY, SOUND_SIZE_IN_GAME, SOUND_SIZE_IN_GAME);
    }

    public void update() {
        musicButton.update();
        soundEffectsButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        // Sound buttons
        musicButton.draw(g);
        soundEffectsButton.draw(g);

        // Volume & Slider
        volumeButton.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (isInPauseScreen(e, musicButton)) {
            musicButton.setMousePressed(true); // if we're in the button and we press the said button
        } else if (isInPauseScreen(e, soundEffectsButton)) {
            soundEffectsButton.setMousePressed(true);
        } else if (isInPauseScreen(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isInPauseScreen(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted()); // if we release above it as well, then we need to mute it or vice-versa
                game.getAudioPlayer().toggleSongMute();
            }
        } else if (isInPauseScreen(e, soundEffectsButton)) {
            if (soundEffectsButton.isMousePressed()) {
                soundEffectsButton.setMuted(!soundEffectsButton.isMuted()); // same here as the musicbutton
                game.getAudioPlayer().toggleEffectMute();
            }
        }
        musicButton.resetBools();
        soundEffectsButton.resetBools(); // we reset everything after we release the mouse.
        volumeButton.resetBooleans();

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        soundEffectsButton.setMouseOver(false); // resetting them right away
        volumeButton.setMouseOver(false);


        if (isInPauseScreen(e, musicButton)) {
            musicButton.setMouseOver(true); // doing this because we have different animations if we move on the button
        } else if (isInPauseScreen(e, soundEffectsButton)) {
            soundEffectsButton.setMouseOver(true);
        } else if (isInPauseScreen(e, volumeButton)) {

        }
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            // if we clicked the button, we are going to drag it
            float valueBefore = volumeButton.getFloatValue();
            volumeButton.changeSliderX(e.getX());
            float valueAfter = volumeButton.getFloatValue();
            if(valueBefore != valueAfter){ // if volume changes
                game.getAudioPlayer().setVolume(valueAfter);
            }
        }
    }

    private boolean isInPauseScreen(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}
