public class World {
    private final long seed;
    private final PerlinNoise perlin;

    public World(long seed) {
        this.seed = seed;
        this.perlin = new PerlinNoise(seed);
    }

    // Compute noise on the fly
    public double getElevation(double worldX, double worldY) {
        double nx = worldX / 200.0;  // Adjust scaling
        double ny = worldY / 200.0;
        return generateNoise(7, 2, nx, ny);
    }

    private double generateNoise(int octaves, double frequency, double nx, double ny) {
        double e = 0;
        double amp = 1.0;
        double freq = frequency;

        for (int i = 0; i < octaves; i++) {
            e += amp * perlin.noise(nx * freq, ny * freq);
            amp *= 0.5;
            freq *= 2.0;
        }
        return e;
    }
}
