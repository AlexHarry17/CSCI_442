
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class MyPanel extends JPanel {

    int startX, flag, startY, endX, endY;

    BufferedImage grid;
    Graphics2D gc;
    private int[] color;
    private Map<String, Color> colorMap = Map.of(
            "red", Color.red,
            "blue", Color.blue,
            "green", Color.green
    );
    private String currentColor;
    public MyPanel(int[] inColor, String inCurrentColor) {
        color = inColor;
        currentColor = inCurrentColor;
//	   startX = startY = 0;
//           endX = endY = 100;
    }

    public void clear() {
        grid = null;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (grid == null) {
            int w = this.getWidth();
            int h = this.getHeight();
            grid = (BufferedImage) (this.createImage(w, h));
            gc = grid.createGraphics();

        }
        g2.drawImage(grid, null, 0, 0);
    }

    public void drawing(int height) {
        repaint();
        gc.setColor(colorMap.get(currentColor));
        for (int i = 0; i < color.length; i++) {
            gc.fill3DRect(i, height - (color[i]/2), 305/256, color[i]/2, true);
        }
    }

}
