package com.leddict.amirhome.LedObjects;

import com.leddict.amirhome.Colors.HSBColor;

public class Sign1234 extends LedObject {

    public Sign1234() {
        super(300);
        digit1 = getSubPixelsInRange(0, 40);
        digit2 = getSubPixelsInRange(55, 110);
        digit3 = getSubPixelsInRange(117, 196);
        digit4 = getSubPixelsInRange(222, 300);
        digitsArray = new HSBColor[][] {digit1, digit2, digit3, digit4};
        activePixels = this.mergeColorArrays(digitsArray);
    }

    @Override
    public HSBColor[] GetActivePixels() {
        return activePixels;
    }

    final public HSBColor digit1[];
    final public HSBColor digit2[];
    final public HSBColor digit3[];
    final public HSBColor digit4[];
    final public HSBColor digitsArray[][];
    final public HSBColor activePixels[];

}
