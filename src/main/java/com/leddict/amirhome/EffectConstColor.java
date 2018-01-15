package com.leddict.amirhome;

public class EffectConstColor extends Effect<EffectConstColor.Configuration> {

    public static class Configuration extends EffectConfiguration {

        public Configuration() { }

        public Configuration(HSBColor colorToApply) {
            this.colorToApply = colorToApply;
        }
        public HSBColor colorToApply = HSBColor.WHITE;
    }

    public EffectConstColor(HSBColor colorsArray[], Configuration config) {
        super(colorsArray);
        if(config == null) {
            config = new Configuration();
        }
        setConfig(config);
    }

    @Override
    public void apply(double timePercent) {
        for(HSBColor c: colorsArray) {
            c.copyFromOther(config.colorToApply);
        }
    }

}
