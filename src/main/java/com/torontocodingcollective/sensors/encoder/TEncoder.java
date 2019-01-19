package com.torontocodingcollective.sensors.encoder;

/**
 * TEncoder class used as the base for all TEncoders
 * <p>
 * The encoder interface is not consistent for PWM and CAN encoders, and this
 * interface is used to unify that interface
 * <p>
 * Known implementations: {@link TCanEncoder}, {@link TDioQuadEncoder},
 * {@link TDioCounterEncoder}
 */
public abstract class TEncoder {

    boolean isInverted = false;
    int     offset     = 0;

    /**
     * TEncoder default constructor
     * <p>
     * Sets the encoder as not inverted
     */
    protected TEncoder() {
        this(false);
    }

    protected TEncoder(boolean isInverted) {
        this.isInverted = isInverted;
    }

    /**
     * Get the distance of this encoder
     * 
     * @return distance in encoder counts
     */
    public abstract int get();

    /**
     * Invert the raw distance if required
     * 
     * @param rawDistance
     * @return int inverted distance
     */
    protected int get(int rawDistance) {

        if (isInverted) {
            rawDistance = -rawDistance;
        }

        return rawDistance + offset;
    }

    /**
     * Get the rate (speed) of this encoder
     * 
     * @return speed in encoder counts/second
     */
    public abstract double getRate();

    /**
     * Invert the raw rate if required
     * 
     * @param rawRate
     * @return double raw rate inverted if required
     */
    protected double getRate(double rawRate) {

        if (isInverted) {
            return -rawRate;
        }

        return rawRate;
    }

    /**
     * Returns whether the current speed controller is 
     * inverted
     * @return {@code true} if inverted, {@code false} otherwise
     */
    public boolean isInverted() {
        return this.isInverted;
    }

    /**
     * Reset the encoder counts for this encoder
     */
    public void reset() {
        // set the offset for this encoder in order to
        // get the distance to zero
        // clear the previous offset
        offset = 0;

        // set the offset to the current encoder counts
        // in order to zero the output.
        offset = -get();
    }

    /**
     * Set the encoder counts to a known value
     * 
     * @param encoderCount
     *            to set the encoder to
     */
    public void set(int encoderCount) {
        offset = 0;
        offset = -get() + encoderCount;

    }

    /**
     * Set the encoder inversion
     * <p>
     * A call to setInverted also resets the encoder if the inversion changes
     * 
     * @param isInverted
     *            {@code true} if the output should be inverted {@code false}
     *            otherwise
     */
    public void setInverted(boolean isInverted) {

        if (this.isInverted != isInverted) {
            this.isInverted = isInverted;
            reset();
        }
    }

}
