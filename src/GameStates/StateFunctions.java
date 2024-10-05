package GameStates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

public interface StateFunctions {
    // we'll use all functions we'll list here in playing & menu, so making interface is more professional

    public void update();
    public void draw(Graphics g);

    public void mouseClicked(MouseEvent e);
    public void mousePressed(MouseEvent e);
    public void mouseReleased(MouseEvent e) throws IOException;
    public void mouseMoved(MouseEvent e);

    public void keyPressed(KeyEvent e);
    public void keyReleased(KeyEvent e);

}
