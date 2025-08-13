import java.awt.*;
import java.util.List;
import javax.swing.*;

public class Gui {
    private JFrame frame;
    private JPanel worldPanel;

    private final World world;
    private final int tileSize = 2;
    private List<TerrainType> terrainTypes; // store ordered by minHeight

    public Gui(World world) {
        this.world = world;
        frame = new JFrame("Herb The Bird");
        initializeWorldPanel();
        initializeTerrainTypes();
    }

    public void initializeTerrainTypes()
    {
        List<TerrainType> terrainTypes = List.of(
        new TerrainType(-1.0, -0.25, new Color(0, 0, 80), new Color(0, 102, 204)),  // Deep to shallow water
        new TerrainType(-0.25, -0.05, new Color(0, 102, 204), new Color(237, 201, 175)), // Shallow water → sand
        new TerrainType(-0.05, 0.3, new Color(237, 201, 175), new Color(85, 170, 85)),   // Sand → light grass
        new TerrainType(0.3, 0.5, new Color(85, 170, 85), new Color(34, 139, 34)),       // Light grass → dark grass
        new TerrainType(0.5, 0.7, new Color(34, 139, 34), new Color(100, 100, 100)),     // Grass → rock
        new TerrainType(0.7, 1.0, new Color(100, 100, 100), Color.WHITE)                 // Rock → snow
        );
    }
    public void initializeWorldPanel() {
        worldPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int x = 0; x < world.getWidth(); x++) {
                    for (int y = 0; y < world.getHeight(); y++) {
                        double e = world.getElevation(x, y);
                        g.setColor(getTerrainColor(e));
                        g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                    }
                }
            }
        };
        worldPanel.setPreferredSize(new Dimension(world.getWidth() * tileSize, world.getHeight() * tileSize));
    }

    /** Uses the terrain type definitions for smooth color blending */
    private Color getTerrainColor(double e) {
        for (TerrainType t : terrainTypes) {
            if (e <= t.maxHeight) {
                return t.getColor(e);
            }
        }
        // Fallback: highest terrain
        return terrainTypes.get(terrainTypes.size() - 1).getColor(e);
    }

    public void showWorldPanel() {
        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(worldPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
