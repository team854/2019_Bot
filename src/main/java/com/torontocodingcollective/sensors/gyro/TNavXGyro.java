package com.torontocodingcollective.sensors.gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

public class TNavXGyro extends TGyro {

    private final AHRS navXGyro;

    public TNavXGyro() {
        this(false);
    }

    public TNavXGyro(boolean inverted) {
        super(inverted);
        this.navXGyro = new AHRS(SPI.Port.kMXP);
    }

    @Override
    public void calibrate() {
        super.setGyroAngle(0);
    }

    @Override
    public double getAngle() {
        double getAngle_val = super.getAngle(navXGyro.getAngle());
        System.out.println(getAngle_val);
        return getAngle_val;
        
    }

    @Override
    public double getPitch() {
        return navXGyro.getRoll();
    }

    @Override
    public double getRate() {
        return super.getRate(navXGyro.getRate());
    }

    @Override
    public boolean supportsPitch() {
        return true;
    }

}
