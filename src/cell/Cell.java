package cell;

import javafx.scene.paint.Color;

public class Cell {
    Color color;
    int wi;
    int hi;
    double brightness;
    //  int burningNeighbors;
    int timeToCollapse, putOutFire, rainTime;

    //double burningSpeed;

    // int probability;

    public Cell() {
        this.color = null;
        this.putOutFire = -100; // info nth
        this.rainTime = 100; //
    }

    public int getRainTime() {
        return rainTime;
    }

    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
    }

    public int getPutOutFire() {
        return putOutFire;
    }

    public void setPutOutFire(int putOutFire) {
        this.putOutFire = putOutFire;
    }

    public void decTime(double a) {
        this.timeToCollapse -= a;
    }


    public void decRainTime(double a) {
        this.rainTime -= a;
    }

    public void addRainTime(double a) {
        this.rainTime += a;
    }
    public void decPutOutFire(double a) {
        this.putOutFire -= a;
    }

    public int getTimeToCollapse() {
        return timeToCollapse;
    }

    public void setTimeToCollapse(int timeToCollapse) {
        this.timeToCollapse = timeToCollapse;
    }

//    public double getBurningSpeed() {
//        return burningSpeed;
//    }
//
//    public void setBurningSpeed(double burningSpeed) {
//        this.burningSpeed = burningSpeed;
//    }

    //    public int getBurningNeighbors() {
//        return burningNeighbors;
//    }
//
//    public void setBurningNeighbors(int burningNeighbors) {
//        this.burningNeighbors = burningNeighbors;
//    }

//    public int getProbability() {
//        return probability;
//    }
//
//    public void setProbability(int probability) {
//        this.probability = probability;
//    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getWi() {
        return wi;
    }

    public void setWi(int wi) {
        this.wi = wi;
    }

    public int getHi() {
        return hi;
    }

    public void setHi(int hi) {
        this.hi = hi;
    }


}
