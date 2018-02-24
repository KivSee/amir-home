package com.leddict.amirhome;

import com.github.leddict.FSTColor;
import com.github.leddict.Network;
import com.leddict.amirhome.Colors.HSBColor;
import com.leddict.amirhome.LedObjects.LedObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SegmentMapping {

    public LedObject o;
    public int indices[];
    public String controllerName;
    public int stripId;
    public int pixelId;

    public FSTColor fstColors[];
    // the rgbOrders array should be in the same length as fstColors.
    // each index in the rgbOrders array matches the corresponding index in fstColors
    public RGBOrder rgbOrders[];
}

class RgbConfiguration {
    public LedObject o;
    public RGBOrder rgbOrder;
    public int indices[];
}

public class PhysicalLedObjectMapping {

    public PhysicalLedObjectMapping(Network networkIfc) {
        this.network = networkIfc;
    }

    private Network network;

    List<SegmentMapping> mappings = new ArrayList<>();
    List<RgbConfiguration> rgbOrderingConfigurations = new ArrayList<>();

    public double globalBrightness = 1.0;

    /*
    The function addMapping should be called once for every segment mapping.
     */
    public void addMapping(LedObject o, int indices[], final String controllerName, final int stripId, final int pixelId) {

        // create the mapping object and initialize it
        SegmentMapping m = new SegmentMapping();
        m.o = o;
        m.indices = indices;
        m.controllerName = controllerName;
        m.stripId = stripId;
        m.pixelId = pixelId;

        // initialize fstColors array once per mapping, so we don't need to 'new' it every time.
        m.fstColors = new FSTColor[indices.length];
        for(int i=0; i < indices.length; i++) {
            m.fstColors[i] = new FSTColor();
        }

        // initialize the rgbOrders array with default values.
        // specific configuration will be set later
        m.rgbOrders = new RGBOrder[indices.length];
        for(int i=0; i < indices.length; i++) {
            m.rgbOrders[i] = RGBOrder.RGB; // the default ordering is RGB.
        }

        this.mappings.add(m);
    }

    public void addRGBOrdering(LedObject o, RGBOrder rgbOrder, int indices[]) {
        RgbConfiguration rgbConfiguration = new RgbConfiguration();
        rgbConfiguration.o = o;
        rgbConfiguration.rgbOrder = rgbOrder;
        rgbConfiguration.indices = indices;
        rgbOrderingConfigurations.add(rgbConfiguration);
    }

    public void sendLedObjectsOnNetwork() {

        if(!this.rgbOrderingSet) {
            this.configurationDone();
            System.gc();
        }

        for(SegmentMapping m: this.mappings) {

            HSBColor allPixelsInObject[] = m.o.GetAllPixels();
            for(int i=0; i<m.indices.length; i++) {
                convertHSBtoFST(allPixelsInObject[m.indices[i]], m.fstColors[i], m.rgbOrders[i]);
            }

            this.network.addSegment(m.controllerName, m.fstColors, m.stripId, m.pixelId);
        }
        this.network.send();
    }

    private void convertHSBtoFST(final HSBColor hsbColor, final FSTColor outFstColor, final RGBOrder rgbOrder) {

        double brightness = hsbColor.brightness;
        brightness *= globalBrightness;
        brightness = brightness * brightness; // fix led non-linear brightness behaviour

        final int rgbAsInt = Color.HSBtoRGB((float)hsbColor.hue, (float)hsbColor.saturation, (float)brightness);
        final byte r = (byte)((rgbAsInt>>16)&0xFF);
        final byte g = (byte)((rgbAsInt>>8)&0xFF);
        final byte b = (byte)((rgbAsInt>>0)&0xFF);

        switch (rgbOrder) {
            case RGB:
                outFstColor.first = r;
                outFstColor.second = g;
                outFstColor.third = b;
                break;
            case GRB:
                outFstColor.first = g;
                outFstColor.second = r;
                outFstColor.third = b;
                break;
            case RBG:
                outFstColor.first = r;
                outFstColor.second = b;
                outFstColor.third = g;
                break;
            case GBR:
                outFstColor.first = g;
                outFstColor.second = b;
                outFstColor.third = r;
                break;
            case BRG:
                outFstColor.first = b;
                outFstColor.second = r;
                outFstColor.third = g;
                break;
            case BGR:
                outFstColor.first = b;
                outFstColor.second = g;
                outFstColor.third = r;
                break;
        }
    }

    /*
    Should be called after all the configuration is done
     */
    public void configurationDone() {

        class SegmentWithPixelId {
            SegmentMapping sm;
            int pixelIndexInSegment;
        }

        // for efficiency, we will map each pixel in each ledObject, to the relevant SegmentMapping and index within
        // the SegmentMapping.rgbOrders array.
        Map<LedObject, Map<Integer, SegmentWithPixelId>> pixelToSegment = new HashMap<>();
        for(SegmentMapping sm: this.mappings) {
            if(!pixelToSegment.containsKey(sm.o)) {
                pixelToSegment.put(sm.o, new HashMap<>());
            }
            Map<Integer, SegmentWithPixelId> mapToSegment = pixelToSegment.get(sm.o);
            for(int i=0; i<sm.indices.length; i++) {
                int indexInLedObject = sm.indices[i];
                SegmentWithPixelId segmentWithPixelId = new SegmentWithPixelId();
                segmentWithPixelId.sm = sm;
                segmentWithPixelId.pixelIndexInSegment = i;
                mapToSegment.put(indexInLedObject, segmentWithPixelId);
            }
        }

        // we iterate the ordered rgbOrderingConfigurations collection.
        // so if the same pixel is set more then once, the last configuration counts (not that it should happen)
        // this function might not be efficient with large setup. it should be refactored if necessary
        for(RgbConfiguration rgbConf: rgbOrderingConfigurations) {

            Map<Integer, SegmentWithPixelId> mapToSegmentData = pixelToSegment.get(rgbConf.o);
            if(mapToSegmentData == null) {
                continue;
            }

            for(int ledObjectArrayIndex: rgbConf.indices) {

                SegmentWithPixelId segmentWithPixelId = mapToSegmentData.get(ledObjectArrayIndex);
                if(segmentWithPixelId == null) {
                    continue;
                }

                segmentWithPixelId.sm.rgbOrders[segmentWithPixelId.pixelIndexInSegment] = rgbConf.rgbOrder;
            }
        }

        this.rgbOrderingSet = true;
    }
    private boolean rgbOrderingSet = false;
}
