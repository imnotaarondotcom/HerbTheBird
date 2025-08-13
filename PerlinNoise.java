import java.util.Random;

/**
 * Simple Perlin noise implementation in Java
 */
public class PerlinNoise
{
    private final int[] permutation;

    public PerlinNoise(long seed)
    {
        permutation = new int[512];
        int[] p = new int[256];
        for (int i = 0; i < 256; i++)
            p[i] = i;

        Random random = new Random(seed);
        for (int i = 255; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            int temp = p[i];
            p[i] = p[index];
            p[index] = temp;
        }

        for (int i = 0; i < 512; i++)
            permutation[i] = p[i % 256];
    }

    public double noise(double x, double y)
    {
        int X = (int)Math.floor(x) & 255;
        int Y = (int)Math.floor(y) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);

        double u = fade(x);
        double v = fade(y);

        int aa = permutation[permutation[X] + Y];
        int ab = permutation[permutation[X] + Y + 1];
        int ba = permutation[permutation[X + 1] + Y];
        int bb = permutation[permutation[X + 1] + Y + 1];

        double gradAA = grad(aa, x, y);
        double gradBA = grad(ba, x - 1, y);
        double gradAB = grad(ab, x, y - 1);
        double gradBB = grad(bb, x - 1, y - 1);

        double lerpX1 = lerp(gradAA, gradBA, u);
        double lerpX2 = lerp(gradAB, gradBB, u);

        return lerp(lerpX1, lerpX2, v);
    }

    private double fade(double t)
    {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t)
    {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y)
    {
        int h = hash & 7;
        double u = h < 4 ? x : y;
        double v = h < 4 ? y : x;
        return ((h & 1) == 0 ? u : -u) +
               ((h & 2) == 0 ? v : -v);
    }
}