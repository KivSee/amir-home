package com.leddict.amirhome.Colors;

public class HSBColor {

    public double hue;
    public double saturation;
    public double brightness;

    public HSBColor(HSBColor other) {
        copyFromOther(other);
    }

    public HSBColor() {
        this(0, 0, 0);
    }

    public HSBColor(double hue, double saturation, double brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public void copyFromOther(HSBColor other) {
        this.hue = other.hue;
        this.saturation = other.saturation;
        this.brightness = other.brightness;
    }

    public static HSBColor WHITE = new HSBColor(0.0, 0.0, 1.0);
    public static HSBColor BLACK = new HSBColor(0.0, 0.0, 0.0);
    public static HSBColor FULL_SAT = new HSBColor(0.0, 1.0, 1.0);

    public static HSBColor RED = new HSBColor(0.0, 1.0, 1.0);
    public static HSBColor GREEN = new HSBColor(1.0 / 3.0, 1.0, 1.0);
    public static HSBColor BLUE = new HSBColor(2.0 / 3.0, 1.0, 1.0);

    public static HSBColor ORANGE = new HSBColor(1.0 / 48.0, 1.0, 1.0);
    public static HSBColor YELLOW = new HSBColor(1.0 / 6.0, 1.0, 1.0);

}