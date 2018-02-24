package com.leddict.amirhome.Effects;

import com.leddict.amirhome.Colors.HSBColor;

/*
changes only the hue value to rainbow
 */
public class EffectRainbow extends Effect<EffectRainbow.Configuration> {

    public static class Configuration extends EffectConfiguration {
        public double hueStart = 0.0;
        public double hueEnd = 1.0;
    }

    public EffectRainbow(HSBColor colorsArray[], Configuration config) {
        super(colorsArray);
        if(config == null) {
            config = new Configuration();
        }
        setConfig(config);
    }

    @Override
    public void apply(double timePercent, Integer beatIndex) {
        for(int i=0; i<colorsArray.length; i++) {
            double relativePos = ((double)i) / ((double)colorsArray.length); // [0.0, 1.0)
            colorsArray[i].hue = config.hueStart + relativePos * (config.hueEnd - config.hueStart);
        }
    }

}
