package com.leddict.amirhome;

import com.github.leddict.Network;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Network n = new Network();
        n.configureControllerWithIp("balcony door", "10.0.0.215");

        PhysicalLedObjectMapping physicalLedObjectMapping = new PhysicalLedObjectMapping(n);
        Sign1234 sign1234 = new Sign1234();
        int allIndices[] = new int[300];
        for(int i=0; i<300; i++) {
            allIndices[i] = i;
        }

        physicalLedObjectMapping.addMapping(sign1234, allIndices, "balcony door", 0, 0);
        physicalLedObjectMapping.globalBrightness = 0.5;

        new EffectConstColor(sign1234.digit1, new EffectConstColor.Configuration(HSBColor.RED)).apply(0.0);
        new EffectConstColor(sign1234.digit2, new EffectConstColor.Configuration(HSBColor.ORANGE)).apply(0.0);
        new EffectConstColor(sign1234.digit3, new EffectConstColor.Configuration(HSBColor.BLUE)).apply(0.0);
        new EffectConstColor(sign1234.digit4, new EffectConstColor.Configuration(HSBColor.GREEN)).apply(0.0);

        for(HSBColor c: sign1234.GetActivePixels()) {
            c.brightness = 1.0;
        }

        while(true) {
            physicalLedObjectMapping.sendLedObjectsOnNetwork();
            try {
                Thread.sleep(1000 / 30);
            }
            catch (InterruptedException e) {
                System.out.println("exception: " + e.getMessage());
            }
        }
    }
}
