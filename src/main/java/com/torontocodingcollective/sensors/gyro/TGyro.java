package com.torontocodingcollective.sensors.gyro;

import com.torontocodingcollective.TUtil;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * TGyro class is the base class for all TGyros
 * <p>
 * The TGyro class provides a unifying interface for all gyro types and is
 * guaranteed to return a value of 0 <= angle < 360 degrees.
 * <p>
 * The TGyro class supports for clarity the methods of {@link Gyro} except for
 * the {@link #free()} method.
 */
public abstract class TGyro extends GyroBase {

    private boolean isInverted;
    private double  offset = 0;

    /**
     * Construct a gyro with the specified inversion
     * 
     * @param isInverted
     *            {@code true} if the gyro is inverted {@code false} otherwise.
     */
    protected TGyro(boolean isInverted) {
        this.isInverted = isInverted;
    }

    /**
     * Calibrate the gyro by running the gyro calibration routines.
     * <p>
     * NOTE: some gyros can only be calibrated on power up and do not support
     * subsequent calibration. NOTE: ensure the robot is first turned on while it's
     * sitting at rest before the competition starts to allow for correct gyro
     * calibration.
     */
    @Override
    public abstract void calibrate();

    /**
     * Returns the current angle of the gyro
     * 
     * @returns angle in the range 0 <= angle <= 360
     */
    @Override
    public abstract double getAngle();

    /**
     * Get the angle from the rawAngle
     * 
     * @param rawAngle
     * @return normalized angle 0 <= angle < 360 where the inversion of the gyro is
     *         taken into account
     */
    protected double getAngle(double rawAngle) {

        // Invert before subtracting the offset.
        if (isInverted) {
            rawAngle = -rawAngle;
        }

        return normalizedAngle(rawAngle + offset);
    }

    /**
     * Return the pitch read off the gyro
     * <p>
     * Not all gyros allow for multiple axis and this routine will return 0 always
     * if the pitch is not supported.
     * 
     * @return pitch in degrees or 0 if pitch is not supported
     */
    public double getPitch() {
        return 0;
    }

    /**
     * Return the rate of change of the angle
     * <p>
     * This value is in degrees/sec
     * 
     * @returns double degrees/sec
     */
    @Override
    public abstract double getRate();

    /**
     * Get the gyro rate from the rawRate
     * 
     * @param rawRate
     * @return rate normalized for inversion
     */
    protected double getRate(double rawRate) {

        if (isInverted) {
            return -rawRate;
        }

        return rawRate;
    }

    @Override
    public void free() {
        System.out.println("The free() method is not supported for TGyro");
    }

    /**
     * Returns whether or not this gyro is inverted
     * 
     * @return boolean {@code true} if the gyro is inverted, {@code false} otherwise
     */
    public boolean isInverted() {
        return isInverted;
    }

    /**
     * Get the angle normalized to a value between 0 and 360 degrees
     * 
     * @param rawAngle
     *            value
     * @return normalized angle value
     */
    private double normalizedAngle(double rawAngle) {

        double angle = rawAngle % 360.0;

        if (angle < 0) {
            angle += 360.0;
        }

        // Round the angle to 3 decimal places
        return TUtil.round(angle, 3);
    }

    /**
     * Reset the gyro angle to zero.
     * <p>
     * NOTE: This routine never performs a calibration of the gyro, so the amount of
     * drift will not change. In order to calibrate the gyro, use the
     * {@link #calibrate()} routine.
     */
    @Override
    public void reset() {
        setGyroAngle(0);
    }

    public void setGyroAngle(double angle) {

        // clear the previous offset
        offset = 0;

        // set the offset to the current angle
        // in order to zero the output.
        offset = -getAngle();

        // This offset will result in an output
        // of zero. Add the passed in angle
        // to make the desired angle
        offset += angle;
    }

    /**
     * Indicates whether this gyro supports pitch
     * 
     * @return boolean {@code true} indicates that pitch is supported {@code false}
     *         indicates pitch is not supported
     */
    public boolean supportsPitch() {
        return false;
    }

}
