package com.utiles;

public class LocationPointDouble {

    private double x, y;

    public LocationPointDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString() {
        return "x=" + x + ", y=" + y;
    }
}
