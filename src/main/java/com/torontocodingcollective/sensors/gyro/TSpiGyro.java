package com.torontocodingcollective.sensors.gyro;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class TSpiGyro extends TGyro {

    private final ADXRS450_Gyro spiGyro;

    public TSpiGyro() {
        this(false);
    }

    public TSpiGyro(boolean isInverted) {
        super(isInverted);
        this.spiGyro = new ADXRS450_Gyro();
    }

    @Override
    public void calibrate() {
        super.setGyroAngle(0);
        spiGyro.calibrate();
    }

    @Override
    public double getAngle() {
        return super.getAngle(spiGyro.getAngle());
    }

    @Override
    public double getRate() {
        return super.getRate(spiGyro.getRate());
    }

    @Override
    public void reset() {
        super.setGyroAngle(0);
    }

}
