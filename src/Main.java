import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {
    public static void main(String[] args) throws Exception {
        Main window = new Main();
        window.run();
    }

    class Canvas extends JPanel {
        public Canvas() {
            setPreferredSize(new Dimension(720, 720));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            int rows = 20;
            int cols = 20;
            int cellSize = 35;
            int startX = 10;
            int startY = 10;

            // Draw the grid lines
            for (int r = 0; r <= rows; r++) {
                int y = startY + r * cellSize;
                g.drawLine(startX, y, startX + cols * cellSize, y);
            }
            for (int c = 0; c <= cols; c++) {
                int x = startX + c * cellSize;
                g.drawLine(x, startY, x, startY + rows * cellSize);
            }
        }
    }

    private Main() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Canvas();
        this.setContentPane(canvas);
        this.pack();
        this.setVisible(true);
    }

    public void run() {
        while (true) {
            repaint();
        }
    }
}
