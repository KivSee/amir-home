package com.leddict.amirhome;

public class EffectAlternateOnOff extends Effect<EffectAlternateOnOff.Configuration> {

    public static class Configuration extends EffectConfiguration {

        public Configuration() { }

        public Configuration(double changeFreq) {
            this.changeFreq = changeFreq;
        }

        public double changeFreq = 1.0;
    }

    public EffectAlternateOnOff(HSBColor colorsArray[], EffectAlternateOnOff.Configuration config) {
        super(colorsArray);
        if(config == null) {
            config = new EffectAlternateOnOff.Configuration();
        }
        setConfig(config);
    }

    @Override
    public void apply(double timePercent, Integer beatIndex) {
        double currentTime = (double)beatIndex + timePercent;
        boolean timeAlternateValue = ( (int)(currentTime / config.changeFreq) % 2 == 0);
        for(int i=0; i<colorsArray.length; i++) {
            colorsArray[i].brightness *= ( ((i % 2 == 0) ^ timeAlternateValue) ? 1.0 : 0.0);
        }
    }



}
