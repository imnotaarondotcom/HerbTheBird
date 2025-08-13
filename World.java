public class World {
    private final int width;
    private final int height;
    private double[][] elevation;

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.elevation = new double[height][width];
        generateTerrain(seed);
    }

    private void generateTerrain(long seed) {
        PerlinNoise perlin = new PerlinNoise(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double nx = (double) x / width - 0.5;
                double ny = (double) y / height - 0.5;

                // More octaves for detail
                double frequency1 = 0.125;
                double frequency2 = frequency1 / 2;
                double frequency3 = frequency2 / 2;
                double frequency4 = frequency3 / 2;

                double e =
                    frequency1 * perlin.noise((1 / frequency1) * nx, (1 / frequency1) * ny) +
                    frequency2 * perlin.noise((1 / frequency2) * nx, (1 / frequency2) * ny) +
                    frequency3 * perlin.noise((1 / frequency3) * nx, (1 / frequency3) * ny) +
                    frequency4 * perlin.noise((1 / frequency4) * nx, (1 / frequency4) * ny);

                e /= (frequency1 + frequency2 + frequency3 + frequency4); // normalize
                elevation[y][x] = e;
            }
        }
    }

    public double getElevation(int x, int y) { return elevation[y][x]; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
