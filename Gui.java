import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class Gui {
    private JFrame frame;
    private JPanel worldPanel;

    private final World world;
    private int tileSize = 4;
    private final int minTileSize = 3;
    private final int maxTileSize = 20;


    // Camera follows bird
    private double camX = 0;
    private double camY = 0;

    // Bird properties
    private double birdX = 0;
    private double birdY = 0;
    private double birdAngle = 0; // radians
    private final double moveSpeed = 0.5;
    private final double rotationSpeed = Math.toRadians(4);

    // Terrain
    private List<TerrainType> terrainTypes;

    // Keys pressed
    private boolean forward, backward, leftTurn, rightTurn, upElev, downElev;

    public Gui(World world) {
        this.world = world;
        frame = new JFrame("Herb The Bird");
        initializeTerrainTypes();
        initializeWorldPanel();
        setupControls();
        startGameLoop();
    }

    public void initializeTerrainTypes() {
        terrainTypes = List.of(
            new TerrainType(-1.0, -0.05, new Color(30, 100, 177), new Color(30, 176, 251)), // water
            new TerrainType(-0.05, 0.00, new Color(215, 192, 158), new Color(255, 246, 193)), // sand
            new TerrainType(0.00, 0.5, new Color(2, 166, 155), new Color(118, 239, 124)), // grass
            new TerrainType(0.5, 1.0, new Color(22, 181, 141), new Color(10, 145, 113))    // trees
        );
    }

    public void initializeWorldPanel() {
        worldPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int screenW = getWidth() / tileSize;
                int screenH = getHeight() / tileSize;

                // Center camera on bird
                camX = birdX - screenW / 2.0;
                camY = birdY - screenH / 2.0;

                // Draw terrain
                for (int x = 0; x < screenW; x++) {
                    for (int y = 0; y < screenH; y++) {
                        double worldX = camX + x;
                        double worldY = camY + y;
                        double e = world.getElevation(worldX, worldY);
                        g.setColor(getTerrainColor(e));
                        g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                    }
                }

                // Draw bird as triangle
                drawBird(g);
            }
        };
        worldPanel.setPreferredSize(new Dimension(800, 600));
    }

    private Color getTerrainColor(double e) {
        for (TerrainType t : terrainTypes) {
            if (e <= t.maxHeight) {
                return t.getColor(e);
            }
        }
        return terrainTypes.get(terrainTypes.size() - 1).getColor(e);
    }

    private void drawBird(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);

        int centerX = worldPanel.getWidth() / 2;
        int centerY = worldPanel.getHeight() / 2;

        // Triangle points relative to facing angle
        int size = 12;
        double[][] points = {
            {0, -size},        // Nose
            {-size / 2.0, size / 2.0}, // Left wing
            {size / 2.0, size / 2.0}   // Right wing
        };

        Polygon triangle = new Polygon();
        for (double[] p : points) {
            double rotatedX = p[0] * Math.cos(birdAngle) - p[1] * Math.sin(birdAngle);
            double rotatedY = p[0] * Math.sin(birdAngle) + p[1] * Math.cos(birdAngle);
            triangle.addPoint(centerX + (int) rotatedX, centerY + (int) rotatedY);
        }

        g2.fillPolygon(triangle);
    }

    private void setupControls() {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> forward = true;
                    case KeyEvent.VK_S -> backward = true;
                    case KeyEvent.VK_A -> leftTurn = true;
                    case KeyEvent.VK_D -> rightTurn = true;
                    case KeyEvent.VK_UP -> upElev = true;
                    case KeyEvent.VK_DOWN -> downElev = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> forward = false;
                    case KeyEvent.VK_S -> backward = false;
                    case KeyEvent.VK_A -> leftTurn = false;
                    case KeyEvent.VK_D -> rightTurn = false;
                    case KeyEvent.VK_UP -> upElev = false;
                    case KeyEvent.VK_DOWN -> downElev = false;
                }
            }
        });
    }

    private void startGameLoop() {
        Timer timer = new Timer(16, e -> {
            if (leftTurn) birdAngle -= rotationSpeed;
            if (rightTurn) birdAngle += rotationSpeed;

            if (forward) {
                birdX += Math.sin(birdAngle) * moveSpeed;
                birdY -= Math.cos(birdAngle) * moveSpeed;
            }
            if (backward) {
                birdX -= Math.sin(birdAngle) * moveSpeed;
                birdY += Math.cos(birdAngle) * moveSpeed;
            }

            if (upElev) tileSize = Math.min(tileSize + 1, maxTileSize);
            if (downElev) tileSize = Math.max(tileSize - 1, minTileSize);


            worldPanel.repaint();
        });
        timer.start();
    }

    public void showWorldPanel() {
        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(worldPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
