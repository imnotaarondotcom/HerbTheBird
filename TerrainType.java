import java.awt.*;

public class TerrainType {
    double minHeight;
    double maxHeight;
    Color minColor;
    Color maxColor;

    public TerrainType(double minHeight, double maxHeight, Color minColor, Color maxColor) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.minColor = minColor;
        this.maxColor = maxColor;
    }

    public Color getColor(double noiseValue) {
        if (noiseValue <= minHeight) return minColor;
        if (noiseValue >= maxHeight) return maxColor;

        double t = (noiseValue - minHeight) / (maxHeight - minHeight);
        return lerpColor(minColor, maxColor, t);
    }

    private Color lerpColor(Color c1, Color c2, double t) {
        int r = (int) (c1.getRed()   + t * (c2.getRed()   - c1.getRed()));
        int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue()  + t * (c2.getBlue()  - c1.getBlue()));
        return new Color(r, g, b);
    }
}
