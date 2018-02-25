package com.leddict.amirhome.Audio.Sequence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.leddict.amirhome.Colors.HSBColor;
import com.leddict.amirhome.Effects.Effect;
import com.leddict.amirhome.Effects.EffectAlternateOnOff;
import com.leddict.amirhome.Effects.EffectConstColor;
import com.leddict.amirhome.LedObjects.SceneObjects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SequenceExecuter {

    public String getSeqFileName() {
        return seqFileName;
    }

    private String seqFileName;
    private SceneObjects sceneObjects;

    public SequenceExecuter(String seqFileName, SceneObjects sceneObjects) throws IOException {
        this.seqFileName = seqFileName;
        this.sceneObjects = sceneObjects;

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Sequences/" + seqFileName).getFile());

        m_seq = mapper.readValue(file, Sequence.class);
        System.out.println(m_seq.getParts().length);

        effects.add(new EffectConstColor(sceneObjects.flyingSaucer.GetAllPixels(), new EffectConstColor.Configuration(HSBColor.FULL_SAT)));
        effects.add(new EffectConstColor(sceneObjects.flyingSaucer.GetAllPixels(), new EffectConstColor.Configuration(new HSBColor[] {HSBColor.YELLOW, HSBColor.RED})));
        effects.add(new EffectAlternateOnOff(sceneObjects.flyingSaucer.side, new EffectAlternateOnOff.Configuration(2.0)));
        blackEffect = new EffectConstColor(sceneObjects.flyingSaucer.GetAllPixels(), new EffectConstColor.Configuration(HSBColor.BLACK));
    }

    public void paint(int positionInMs) {

        blackEffect.apply(0.0, null);

        for(SongPart part: m_seq.getParts()) {
            int startTimeMs = (int)(part.getStartTime() * 1000);
            int endTimeMs = (int)(part.getEndTime() * 1000);
            if(positionInMs >= startTimeMs && positionInMs <= endTimeMs) {
                int durationInMs = endTimeMs - startTimeMs;
                int beatTimeMs = durationInMs / part.getNumberOfBeats();
                int timeFromPartBegin = positionInMs - startTimeMs;

                int beatIndex = timeFromPartBegin / beatTimeMs;
                double beatRelTime = (timeFromPartBegin % beatTimeMs) / beatTimeMs;
                for(Effect e: effects) {
                    e.apply(beatRelTime, beatIndex);
                }
            }
        }
    }

    Effect blackEffect;
    List<Effect> effects = new ArrayList();
    private Sequence m_seq;
}
