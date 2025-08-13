import java.awt.*;
import javax.swing.*;

public class Gui
{
    private JFrame frame;
    private JPanel worldPanel;

    private final World world;
    private final int tileSize = 5; // pixels per tile

    public Gui(World world)
    {
        this.world = world;
        frame = new JFrame("Herb The Bird");
        initializeWorldPanel();
    }

    public void initializeWorldPanel()
    {  
        worldPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                for (int x = 0; x < world.getWidth(); x++)
                {
                    for (int y = 0; y < world.getHeight(); y++)
                    {
                        int tile = world.getTile(x, y);

                        switch (tile)
                        {
                            case 0 -> g.setColor(Color.BLUE);       // water
                            case 1 -> g.setColor(Color.GREEN);      // grass
                            case 2 -> g.setColor(Color.DARK_GRAY);  // mountain
                        }

                        g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                    }
                }
            }
        };

        worldPanel.setPreferredSize(new Dimension(world.getWidth() * tileSize, world.getHeight() * tileSize));
    }

    public void showWorldPanel()
    {
        SwingUtilities.invokeLater(() -> 
        {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(worldPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
