package com.torontocodingcollective.sensors.gyro;

import com.analog.adis16448.frc.ADIS16448_IMU; 

public class TAdis16448Gyro extends TGyro {

    private final ADIS16448_IMU imuGyro;

    public TAdis16448Gyro() {
        this(false);
    }

    public TAdis16448Gyro(boolean isInverted) {
        super(isInverted);
        imuGyro = new ADIS16448_IMU();
        imuGyro.calibrate();
    }

    @Override
    public void calibrate() {
        super.setGyroAngle(0);
        imuGyro.calibrate();
    }

    @Override
    public double getAngle() {
        return super.getAngle(imuGyro.getAngleZ());
    }

    @Override
    public double getRate() {
        return super.getRate(imuGyro.getRateZ());
    }

    @Override
    public void reset() {
        super.setGyroAngle(0);
    }

}
