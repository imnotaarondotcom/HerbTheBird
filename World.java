public class World
{
    private final int width;
    private final int height;
    private final int[][] terrain; // 0 = water, 1 = grass, 2 = mountain

    public World(int width, int height, long seed)
    {
        this.width = width;
        this.height = height;
        this.terrain = new int[width][height];

        generateTerrain(seed);
    }
    // penis
    private void generateTerrain(long seed)
    {
        PerlinNoise perlin = new PerlinNoise(seed);
        double frequency = 0.2; // Higher frequency = more zoomed in

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                double value = perlin.noise(x * frequency, y * frequency);

                if (value < -0.1)       terrain[x][y] = 0; // water
                else if (value < 0.4)   terrain[x][y] = 1; // grass
                else                    terrain[x][y] = 2; // mountain
            }
        }
    }

    public int getTile(int x, int y) { return terrain[x][y]; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

}
