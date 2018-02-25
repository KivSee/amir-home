package com.leddict.amirhome;

import com.github.leddict.Network;
import com.leddict.amirhome.Audio.Position.PositionReceiver;
import com.leddict.amirhome.Audio.Sequence.SequenceExecuter;
import com.leddict.amirhome.Colors.HSBColor;
import com.leddict.amirhome.Effects.Effect;
import com.leddict.amirhome.Effects.EffectAlternateOnOff;
import com.leddict.amirhome.Effects.EffectConstColor;
import com.leddict.amirhome.LedObjects.FlyingSaucer;
import com.leddict.amirhome.LedObjects.SceneObjects;

import java.io.IOException;
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

        SceneObjects sceneObjects = new SceneObjects();
        int allIndices[] = new int[sceneObjects.flyingSaucer.GetNumberOfPixels()];
        for(int i=0; i<sceneObjects.flyingSaucer.GetNumberOfPixels(); i++) {
            allIndices[i] = i;
        }

        SequenceExecuter se;
        try {
             se = new SequenceExecuter("xfiles.yaml", sceneObjects);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

//        physicalLedObjectMapping.addMapping(sign1234, allIndices, "balcony door", 0, 0);
        physicalLedObjectMapping.addMapping(sceneObjects.flyingSaucer, allIndices, "balcony door", 0, 0);
        int bottomIndices[] = {16, 17, 18, 19, 20, 21};
        physicalLedObjectMapping.addRGBOrdering(sceneObjects.flyingSaucer, RGBOrder.GRB, bottomIndices);
        physicalLedObjectMapping.globalBrightness = 0.5;

        List<Effect> effects = new ArrayList();
        effects.add(new EffectConstColor(sceneObjects.flyingSaucer.GetAllPixels(), new EffectConstColor.Configuration(HSBColor.FULL_SAT)));
        effects.add(new EffectConstColor(sceneObjects.flyingSaucer.GetAllPixels(), new EffectConstColor.Configuration(new HSBColor[] {HSBColor.YELLOW, HSBColor.RED})));
        effects.add(new EffectAlternateOnOff(sceneObjects.flyingSaucer.side, new EffectAlternateOnOff.Configuration(2.0)));
//        effects[0] = new EffectConstColor(sign1234.digit1, new EffectConstColor.Configuration(HSBColor.RED));
//        effects[1] = new EffectConstColor(sign1234.digit2, new EffectConstColor.Configuration(HSBColor.ORANGE));
//        effects[2] = new EffectConstColor(sign1234.digit3, new EffectConstColor.Configuration(HSBColor.BLUE));
//        effects[3] = new EffectConstColor(sign1234.digit4, new EffectConstColor.Configuration(HSBColor.GREEN));
//        effects[4] = new EffectFadeOut(sign1234.digit1, new EffectFadeOut.Configuration());
//        effects[5] = new EffectFadeOut(sign1234.digit2, new EffectFadeOut.Configuration());
//        effects[6] = new EffectFadeOut(sign1234.digit3, new EffectFadeOut.Configuration());
//        effects[7] = new EffectFadeOut(sign1234.digit4, new EffectFadeOut.Configuration());

        Effect blackEffect = new EffectConstColor(sceneObjects.flyingSaucer.GetAllPixels(), new EffectConstColor.Configuration(HSBColor.BLACK));

        PositionReceiver positionRecevier = new PositionReceiver();
        positionRecevier.start();

        while(true) {

            physicalLedObjectMapping.sendLedObjectsOnNetwork();
            int currentPosition = positionRecevier.getPositionMs() + 15; // adjust position for latency losses
            se.paint(currentPosition);


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

            //System.out.println(currentPosition);
            try {
                Thread.sleep(1000 / 30);
            }
            catch (InterruptedException e) {
                System.out.println("exception: " + e.getMessage());
            }
        }
    }
}
