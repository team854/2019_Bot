package com.torontocodingcollective.oi;

/**
 * The TStickPosition class is used to represent the x,y coordinates (position)
 * of a stick.
 * <p>
 * The stick position is immutable.
 */
public class TStickPosition {

    /** X coordinate of this stick position */
    public final double x;
    /** Y coordinate of this stick position */
    public final double y;

    /**
     * Construct a stick position based on the passed in x and y
     * <p>
     * Stick positions are immutable
     * 
     * @param x
     * @param y
     */
    public TStickPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(x).append(',').append(y).append(')');
        return sb.toString();
    }
}
