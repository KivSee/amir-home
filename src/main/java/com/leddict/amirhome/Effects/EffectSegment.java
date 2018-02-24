package com.leddict.amirhome.Effects;

import com.leddict.amirhome.Colors.HSBColor;

public class EffectSegment extends Effect {

    public class Configuration extends EffectConfiguration {
        public double segmentStart = 0.0;
        public double segmentEnd = 1.0;
    }

    public EffectSegment(HSBColor colorsArray[]) {
        super(colorsArray);
    }

    @Override
    public void apply(double timePercent, Integer beatIndex) {

    }

    public Configuration config;
}
