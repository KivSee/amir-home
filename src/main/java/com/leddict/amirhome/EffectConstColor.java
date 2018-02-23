package com.leddict.amirhome;

public class EffectConstColor extends Effect<EffectConstColor.Configuration> {

    public static class Configuration extends EffectConfiguration {

        public Configuration() { }

        public Configuration(HSBColor colorToApply) {
            this(new HSBColor[]{colorToApply}, 1.0);
        }

        public Configuration(HSBColor colorsToApply[]) {
            this(colorsToApply, 1.0);
        }

        public Configuration(HSBColor colorsToApply[], double changeFreq) {
            this.changeFreq = changeFreq;
            this.colorToApply = new HSBColor[colorsToApply.length];
            for(int i=0; i<colorsToApply.length; i++) {
                this.colorToApply[i] = colorsToApply[i];
            }
        }

        public HSBColor colorToApply[] = {HSBColor.WHITE};
        double changeFreq = 1.0;
    }

    public EffectConstColor(HSBColor colorsArray[], Configuration config) {
        super(colorsArray);
        if(config == null) {
            config = new Configuration();
        }
        setConfig(config);
    }

    @Override
    public void apply(double timePercent, Integer beatIndex) {
        int currentBeat = beatIndex != null ? beatIndex : 1;
        double currentTime = (double)currentBeat + timePercent;
        int colorIndex =  (int)(currentTime / config.changeFreq) % config.colorToApply.length;
        HSBColor currentColor = config.colorToApply[colorIndex];
        for(HSBColor c: colorsArray) {
            c.copyFromOther(currentColor);
        }
    }

}
