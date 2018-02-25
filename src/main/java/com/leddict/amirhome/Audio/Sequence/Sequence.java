package com.leddict.amirhome.Audio.Sequence;

class SongPart {

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    double startTime;

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    double endTime;

    public int getNumberOfBeats() {
        return numberOfBeats;
    }

    public void setNumberOfBeats(int numberOfBeats) {
        this.numberOfBeats = numberOfBeats;
    }

    int numberOfBeats;
}

public class Sequence {

    public SongPart[] getParts() {
        return parts;
    }

    public void setParts(SongPart[] parts) {
        this.parts = parts;
    }

    private SongPart parts[];

}
