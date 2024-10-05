package GameStates;

import MainComponents.Game;
import UI.MenuButton;
import GameAudio.AudioPlayer;

import java.awt.event.MouseEvent;


public class GameDesirableState {
    protected Game game;

    public GameDesirableState(Game game){
    this.game = game;
    }

    public boolean isInButton(MouseEvent e, MenuButton mb){
    return mb.getBounds().contains(e.getX(),e.getY());
    }

    public Game getGame(){
        return game;
    }

    public void setGameState(GameState state){
        switch(state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }
        GameState.state = state; // we'll also change the state not only change the music
    }
}
