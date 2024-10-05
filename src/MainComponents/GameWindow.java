package MainComponents;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame {

    public GameWindow(GamePanel gamePanel) {

        JFrame jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setResizable(false);
        jframe.pack(); // set the window to fit the size of the window to the preferred size  based in our GamePanel
        jframe.setLocationRelativeTo(null); // Frame starts middle of the screen

        jframe.setVisible(true); //  should be in the bottom so when you open the program it doesn't appear to be only white screen for one frame
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}
