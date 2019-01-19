package com.torontocodingcollective.sensors.encoder;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * TCanEncoder reads a quadrature encoder plugged into a TalonSRX
 * <p>
 * Extends {@link TEncoder}
 */
public class TCanEncoder extends TEncoder {

    private TalonSRX talonSRX;

    /**
     * Encoder constructor. Construct a Encoder given a TalonSRX device. 
     * The encoder must be a quadrature encoder plugged into the TalonSRX.
     * <p>
     * The encoder will be reset to zero when constructed
     * @param talonSRX where the quadrature encoder is attached
     * @param isInverted {@code true} if inverted, {@code false} otherwise
     */
    public TCanEncoder(TalonSRX talonSRX, boolean isInverted) {
        super(isInverted);
        this.talonSRX = talonSRX;
        talonSRX.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,  0);
        talonSRX.setSelectedSensorPosition(0, 0, 0);
    }

    @Override
    public int get() {
        // Convert the raw counts
        return super.get(talonSRX.getSelectedSensorPosition(0));
    }

    @Override
    public double getRate() {
        // Convert the raw rate
        return super.getRate(talonSRX.getSelectedSensorVelocity(0));
    }

}
