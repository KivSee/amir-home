package com.leddict.amirhome;

public class EffectFadeOut extends Effect<EffectFadeOut.Configuration> {

    public static class Configuration extends EffectConfiguration {

        public Configuration() { }
    }

    public EffectFadeOut(HSBColor colorsArray[], Configuration config) {
        super(colorsArray);
        if(config == null) {
            config = new Configuration();
        }
        setConfig(config);
    }

    @Override
    public void apply(double timePercent, Integer beatIndex) {
        double brightness = 1.0 - timePercent;
        for(HSBColor c: colorsArray) {
            c.brightness = brightness;
        }
    }

}
