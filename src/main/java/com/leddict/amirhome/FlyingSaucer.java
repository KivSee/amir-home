package com.leddict.amirhome;

public class FlyingSaucer extends LedObject {

    public FlyingSaucer() {
        super(16 + 6 + 49);
        top = getSubPixelsInRange(0, 16);
        bottom = getSubPixelsInRange(16, 22);
        side = getSubPixelsInRange(22, 71);
    }

    final public HSBColor top[];
    final public HSBColor bottom[];
    final public HSBColor side[];

}
