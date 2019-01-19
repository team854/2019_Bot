package com.torontocodingcollective.sensors.encoder;

import edu.wpi.first.wpilibj.Counter;

/**
 * Counter Encoder
 * <p>
 * The counter encoder is a single channel encoder that cannot measure direction
 * <br>
 * Inversion of the counter encoder is not supported
 */
public class TDioCounterEncoder extends TEncoder {

    Counter counter;

    /**
     * Encoder constructor. Construct a Encoder on the given DIO channel.
     * <p>
     * 
     * @param pwmChannel
     *            The DIO channel. 0-9 are on-board, 10-25 are on the MXP port
     */
    public TDioCounterEncoder(int pwmChannel) {
        super(false);
        this.counter = new Counter(pwmChannel);

        // Distance per pulse is set to 1.0 to get the raw count and rate in counts/sec
        counter.setDistancePerPulse(1.0);
    }

    /**
     * Inversion is not supported for counter encoders
     */
    @Override
    public void setInverted(boolean isInverted) {
        if (isInverted) {
            System.out.println("Inversion is not supported for counter encoders");
        }
    }

    @Override
    public int get() {
        return super.get(counter.get());
    }

    @Override
    public double getRate() {
        return super.getRate(counter.getRate());
    }

}
