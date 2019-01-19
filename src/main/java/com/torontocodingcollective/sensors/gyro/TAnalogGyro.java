package com.torontocodingcollective.sensors.gyro;

import edu.wpi.first.wpilibj.AnalogGyro;

public class TAnalogGyro extends TGyro {

    /** Default sensitivity is for a VEX analog yaw rate gyro */
    public static final double DEFAULT_ANALOG_GYRO_SENSITIVITY = .00172;

    private final AnalogGyro   analogGyro;
    private final int          analogPort;

    private double             lastRawAngle;

    public TAnalogGyro(int analogPort) {
        this(analogPort, false);
    }

    public TAnalogGyro(int analogPort, boolean isInverted) {
        this(analogPort, isInverted, DEFAULT_ANALOG_GYRO_SENSITIVITY);
    }

    public TAnalogGyro(int analogPort, boolean isInverted, double sensitivity) {
        super(isInverted);
        this.analogGyro = new AnalogGyro(analogPort);
        this.analogPort = analogPort;
        this.analogGyro.setSensitivity(sensitivity);
    }

    @Override
    public void calibrate() {
        super.setGyroAngle(0);
        analogGyro.calibrate();
    }

    @Override
    public double getAngle() {

        // Filter out bad values coming from the
        // gyro. The analog gyros can occasionally
        // return a value that is very much different
        // from the previous reading and different than
        // the steady state from the gyro.
        double rawAngle = analogGyro.getAngle();

        if (Math.abs(rawAngle - lastRawAngle) > 360) {
            // If the gyro jumps by a large number then
            // just assume that it has not moved.
            rawAngle = lastRawAngle;
        }

        lastRawAngle = rawAngle;

        return super.getAngle(rawAngle);
    }

    /**
     * Get the current calibration values for the underlying analog gyro.
     * 
     * @return String containing the channel, offset and center calibration values
     *         for the underlying analog gyro.
     */
    public String getCalibrationValuesString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Analog Port: ").append(analogPort);
        sb.append(", Offset: ").append(analogGyro.getOffset());
        sb.append(", Center: ").append(analogGyro.getCenter());

        return sb.toString();
    }

    @Override
    public double getRate() {
        return super.getRate(analogGyro.getRate());
    }

    /**
     * Set the sensitivity of the analog gyro
     * 
     * @param voltsPerDegreePerSecond
     *            sensitivity of the gyro
     */
    public void setSensitivity(double voltsPerDegreePerSecond) {
        analogGyro.setSensitivity(voltsPerDegreePerSecond);
    }

}
