package io.generaid.holes.noteplacing.draw;

import java.awt.*;
import javax.swing.*;

public class Drawer extends JFrame {

    private class DrawingPanel extends JScrollPane {
        private JPanel panel;

        public DrawingPanel() {

            panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.RED);
                    g.fillRect(250,250,100,100);
                    g.fillRect(1,1,100,100);
                }
            };
            panel.setPreferredSize(new Dimension(1000,1000));  // Adjust size as needed
            setViewportView(panel);
        }
    }

    public Drawer() {
        setTitle("Red Rectangle Drawer");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel);
        setVisible(true);
    }

}
