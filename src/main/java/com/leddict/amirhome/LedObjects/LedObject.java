package com.leddict.amirhome.LedObjects;

import com.leddict.amirhome.Colors.HSBColor;

/*
The abstract com.leddict.amirhome.LedObjects.LedObject defines the methods which each com.leddict.amirhome.LedObjects.LedObject should support, and implements abstract behaviour.
Each com.leddict.amirhome.LedObjects.LedObject (in this context) is a group of individual addressable pixels, which create the physical object itself.
Abstract com.leddict.amirhome.LedObjects.LedObject holds an array of color per pixel, which can be accessed via the GetAllPixels() method.

com.leddict.amirhome.LedObjects.LedObject is an abstract class. If you want to use it you should extend it, and implement whatever logic you need
for the specific object you are dealing with.
 */
public abstract class LedObject {

    public LedObject(int totalNumberOfPixels) {
        this.allPixelsArray = new HSBColor[totalNumberOfPixels];
        for(int i=0; i<totalNumberOfPixels; i++) {
            this.allPixelsArray[i] = new HSBColor();
        }
    }

    /*
    The pixels array represents the actual pixels that assemble the led object.
    Some of them might not be active (always off), but they are still part of the led string and has unique index on
    the strip.
     */
    public HSBColor[] GetAllPixels() {
        return this.allPixelsArray;
    }

    public final int GetNumberOfPixels() {
        return this.allPixelsArray.length;
    }

    /*
    override if you want
     */
    public HSBColor[] GetActivePixels() { return this.allPixelsArray; }

    /*
    allPixelsArray is generate by the abstract class.
    There is only one copy for it for each com.leddict.amirhome.LedObjects.LedObject instance.
    The array does not aware nor does it care about the different segments that create the full object.
    It does not aware of the RGB order of the actual
     */
    final protected HSBColor allPixelsArray[];

    /*
    not including endIndex
     */
    protected HSBColor[] getSubPixelsInRange(int startIndex, int endIndex) {
        HSBColor relevantPixels[] = new HSBColor[endIndex - startIndex];
        for(int i=startIndex; i<endIndex; i++) {
            relevantPixels[i-startIndex] = allPixelsArray[i];
        }
        return relevantPixels;
    }

    protected static HSBColor[] mergeColorArrays(HSBColor sourceArrays[][]) {

        // create the receiving array
        int mergedSize = 0;
        for(HSBColor[] arr: sourceArrays) {
            mergedSize += arr.length;
        }
        HSBColor mergedArray[] = new HSBColor[mergedSize];

        // fill it with the colors
        int i=0;
        for(HSBColor[] arr: sourceArrays) {
            for(HSBColor c: arr) {
                mergedArray[i++] = c;
            }
        }

        return mergedArray;
    }

}
