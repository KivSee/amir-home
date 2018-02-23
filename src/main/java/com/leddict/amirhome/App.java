package com.leddict.amirhome;

import com.github.leddict.Network;

import java.util.ArrayList;
import java.util.List;

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
//        Sign1234 sign1234 = new Sign1234();
//        int allIndices[] = new int[300];
//        for(int i=0; i<300; i++) {
//            allIndices[i] = i;
//        }

        FlyingSaucer fs = new FlyingSaucer();
        int allIndices[] = new int[fs.GetNumberOfPixels()];
        for(int i=0; i<fs.GetNumberOfPixels(); i++) {
            allIndices[i] = i;
        }

//        physicalLedObjectMapping.addMapping(sign1234, allIndices, "balcony door", 0, 0);
        physicalLedObjectMapping.addMapping(fs, allIndices, "balcony door", 0, 0);
        int bottomIndices[] = {16, 17, 18, 19, 20, 21};
        physicalLedObjectMapping.addRGBOrdering(fs, RGBOrder.GRB, bottomIndices);
        physicalLedObjectMapping.globalBrightness = 0.5;

        List<Effect> effects = new ArrayList();
        effects.add(new EffectConstColor(fs.allPixelsArray, new EffectConstColor.Configuration(HSBColor.FULL_SAT)));
        effects.add(new EffectConstColor(fs.allPixelsArray, new EffectConstColor.Configuration(new HSBColor[] {HSBColor.YELLOW, HSBColor.RED})));
        effects.add(new EffectAlternateOnOff(fs.side, new EffectAlternateOnOff.Configuration(2.0)));
//        effects[0] = new EffectConstColor(sign1234.digit1, new EffectConstColor.Configuration(HSBColor.RED));
//        effects[1] = new EffectConstColor(sign1234.digit2, new EffectConstColor.Configuration(HSBColor.ORANGE));
//        effects[2] = new EffectConstColor(sign1234.digit3, new EffectConstColor.Configuration(HSBColor.BLUE));
//        effects[3] = new EffectConstColor(sign1234.digit4, new EffectConstColor.Configuration(HSBColor.GREEN));
//        effects[4] = new EffectFadeOut(sign1234.digit1, new EffectFadeOut.Configuration());
//        effects[5] = new EffectFadeOut(sign1234.digit2, new EffectFadeOut.Configuration());
//        effects[6] = new EffectFadeOut(sign1234.digit3, new EffectFadeOut.Configuration());
//        effects[7] = new EffectFadeOut(sign1234.digit4, new EffectFadeOut.Configuration());

        Effect blackEffect = new EffectConstColor(fs.allPixelsArray, new EffectConstColor.Configuration(HSBColor.BLACK));

        PositionReceiver positionRecevier = new PositionReceiver();
        positionRecevier.start();

        while(true) {

            physicalLedObjectMapping.sendLedObjectsOnNetwork();
            int currentPosition = positionRecevier.getPositionMs();
            int positionFromStart = currentPosition - 466;
            if(positionFromStart < 0) {
                blackEffect.apply(0.0, null);
            }
            else {
                int beatIndex = positionFromStart / 789;
                double beatBright = (positionFromStart % 789) / 789.0;
                for(Effect e: effects) {
                    e.apply(beatBright, beatIndex);
                }
            }


//            int posInSecond = currentPosition % 1000;
//            if(posInSecond < 100) {
//                for(Effect e: effects) {
//                    e.apply(0.0);
//                    System.out.println("here");
//                }
//            }
//            else {
//                blackEffect.apply(0.0);
//            }

            System.out.println(currentPosition);
            try {
                Thread.sleep(1000 / 30);
            }
            catch (InterruptedException e) {
                System.out.println("exception: " + e.getMessage());
            }
        }
    }
}
