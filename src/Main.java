import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Cell {
    private int x, y, size;

    public Cell(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void paint(Graphics g, boolean highlighted) {
        if (highlighted) {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, size, size);
        }
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }

    public boolean contains(int mx, int my) {
        return mx >= x && mx < x + size && my >= y && my < y + size;
    }
}

class Grid {
    private int rows, cols;
    private Cell[][] cells;

    public Grid(int rows, int cols, int cellSize, int startX, int startY) {
        cells = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(
                    startX + c * cellSize,
                    startY + r * cellSize,
                    cellSize
                );
            }
        }
    }

    public void paint(Graphics g, Point mouse) {
        int mx = mouse != null ? mouse.x : -1;
        int my = mouse != null ? mouse.y : -1;
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                boolean highlighted = cells[r][c].contains(mx, my);
                cells[r][c].paint(g, highlighted);
            }
        }
    }
}

public class Main extends JFrame {
    public static void main(String[] args) {
        Main window = new Main();
        window.run();
    }

    class Canvas extends JPanel {
        private Grid grid;
        private Point mouse = null;

        public Canvas() {
            setPreferredSize(new Dimension(720, 720));
            grid = new Grid(20, 20, 35, 10, 10);

            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    mouse = e.getPoint();
                    repaint();
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            grid.paint(g, mouse);
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
    }
}