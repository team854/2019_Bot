package com.torontocodingcollective.pid;

import edu.wpi.first.wpilibj.PIDController;

/**
 * Proportional (PID) Control Loop for heading (gyro angle) control.
 * <p>
 * The PID controller calculate routine must be called at a consistent rate
 * (every control loop is good enough) when the PID is enabled.
 * <p>
 * This PID controller uses the Smartdashboard communications features of the
 * wpiLib PID controller, but does not use its control loop functionality.
 */
public class TGyroPID extends PIDController {

    /** Output is the steering value to apply to the motor speed */
    private double output;
    private double error;
    private double totalError;

    /**
     * Construct a TGyroPid using the supplied proportional gain
     */
    public TGyroPID(double kP) {
        // The super class is used in order to
        // support a SmartDashboard pid object
        super(kP, 0.0d, 0.0d, 0.0d, new NullPIDSource(), new NullPIDOutput());
        reset();
    }

    /**
     * Construct a TGyroPid using the supplied proportional and integral gain
     */
    public TGyroPID(double kP, double kI) {
        super(kP, kI, 0.0d, 0.0d, new NullPIDSource(), new NullPIDOutput());
        reset();
    }

    /**
     * Calculate the PID output.
     * <p>
     * In order to generate proper PID behaviour, this routine must be called at a
     * consistent periodic rate. Calling this routine anywhere in the main robot
     * periodic loops is sufficient.
     * <p>
     * NOTE: If the PID is disabled, this routine returns 0.
     * 
     * @param currentGyroAngle the angle from the gyro
     * @return the calculated result. This result can also be retrieved with
     *         subsequent calls to {@link #get()}.
     */
    public double calculate(double currentGyroAngle) {

        error = getError(currentGyroAngle);

        // Add the proportional output
        double proportionalOutput = super.getP() * error;

        // The output cannot steer more than 1.0
        if (proportionalOutput > 1.0) {
            proportionalOutput = 1.0;
        }

        // The output cannot steer less than -1.0
        if (proportionalOutput < -1.0) {
            proportionalOutput = -1.0;
        }

        double totalOutput = proportionalOutput;

        // Calculate the integral contribution.
        // Integral controllers are very prone to saturation
        // and "wind-up". In order to avoid "wind-up",
        // do not allow the integral error total to exceed
        // the total required to saturate the output (-1.0 or 1.0).
        double kI = super.getI();

        if (kI != 0) {

            totalError += error; // sum of all errors

            double integralOutput = totalError * kI;

            // Clamp the total error to prevent "windup"
            if (integralOutput + proportionalOutput > 1.0) {
                totalError = (1.0 - proportionalOutput) / kI;
            }

            if (integralOutput + proportionalOutput < -1.0) {
                totalError = (-1.0 - proportionalOutput) / kI;
            }

            // Recalculate the integral output after clamping
            integralOutput = totalError * kI;

            totalOutput = proportionalOutput + integralOutput;

            // The output cannot steer more than 1.0
            if (totalOutput > 1.0) {
                totalOutput = 1.0;
            }

            // The output cannot steer less than -1.0
            if (totalOutput < -1.0) {
                totalOutput = -1.0;
            }
        }

        output = totalOutput;
        return output;
    }

    /**
     * Get the error given the current angle
     * <p>
     * This routine uses the setpoint for the PID and returns an error in the range
     * of -180 to + 180 degrees
     */
    public double getError(double currentGyroAngle) {

        // If the PID is not enabled, this routine does nothing.
        if (!this.isEnabled()) {
            return 0;
        }

        // Normalize the gyro angle.
        // Current gyro angle is -infinity to +infinity
        double normalizedGyroAngle = currentGyroAngle % 360.0d;

        if (normalizedGyroAngle < 0) {
            normalizedGyroAngle = normalizedGyroAngle + 360.0;
        }

        // Calculate the error
        // Normalize the error for the shortest path.
        // The normalized error should be -180 and +180.
        error = super.getSetpoint() - normalizedGyroAngle;

        if (error > 180) {
            error = error - 360.0;
        }

        if (error < -180) {
            error = error + 360.0;
        }
        return error;
    }

    @Override
    public void disable() {
        super.disable();
        reset();
    }

    @Override
    public void reset() {
        if (!isEnabled()) {
            error = 0;
        }
        totalError = 0;
        output = 0;
    }

    @Override
    public double get() {
        return output;
    }

    @Override
    public double getError() {
        return error;
    }
}
