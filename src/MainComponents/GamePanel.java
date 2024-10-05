package MainComponents;

import GameInputs.KeyboardInputs;
import GameInputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static MainComponents.Game.GAME_HEIGHT;
import static MainComponents.Game.GAME_WIDTH;


public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;
    public GamePanel(Game game) throws IOException {
        mouseInputs = new MouseInputs(this);
        this.game = game;

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs); // so we don't create 2 new objects, but rather one
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("Size : " + GAME_WIDTH + " : " + GAME_HEIGHT);
    }

    public void updateGame(){

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // does the JComponents paint method before I do stuff myself
        game.render(g);

//        subImage = image.getSubimage(1*64,8*40,64,40); // by the photo since we need last row 2nd pic, it's x 1 y 8

    }

    public Game getGame(){
        return game;
    }

}
