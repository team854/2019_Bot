package com.torontocodingcollective.pid;

import edu.wpi.first.wpilibj.PIDController;

/**
 * Class implements a Proportional (PID) Control Loop for motor speed control.
 * <p>
 * The PID controller calculate routine must be called at a consistent rate
 * (every control loop) when the PID is enabled.
 * <p>
 * This PID controller uses the Smartdashboard communications features of the
 * wpiLib PID controller, but does not use its control loop functionality.
 */
public class TSpeedPID extends PIDController {

    private double output;
    private double totalError;

    public TSpeedPID(double kP) {
        super(kP, 0.0d, 0.0d, 1.0d, new NullPIDSource(), new NullPIDOutput());
        this.totalError = 0;
    }

    public TSpeedPID(double kP, double kI) {
        super(kP, kI, 0.0d, 1.0d, new NullPIDSource(), new NullPIDOutput());
        this.totalError = 0;
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
     * @param normalizedRate
     *            the encoder rate scaled for use as used as feedback for this PID
     *            (should be set to rawRate/maxEncodeSpeed)
     * @return the calculated result. This result can also be retrieved with
     *         subsequent calls to {@link #get()}.
     */
    public double calculate(double normalizedRate) {

        // If the PID is not enabled, this routine does nothing.
        if (!this.isEnabled()) {
            return 0;
        }

        if (normalizedRate > 1.5) {
            normalizedRate = 1.5;
        }

        if (normalizedRate < -1.5) {
            normalizedRate = -1.5;
        }

        // Calculate the error
        double error = super.getSetpoint() - normalizedRate;

        // Get proportional output
        double proportionalOutput = getP() * error;

        // Calculate the total output for the proportional
        // and feed forward terms. In a speed controller
        // the feed forward term is always 1.0 (the speed
        // setpoint is used as the feed forward term).

        // Use a temporary totalOutput variable for
        // all calculations. Limit the totalOutput
        // and assign the value to the output variable at the
        // end in order to prevent reads of intermediary
        // output results.
        double totalOutput = super.getSetpoint() + proportionalOutput;

        // The output cannot drive more than 1.0
        if (totalOutput > 1.0) {
            totalOutput = 1.0;
        }

        // The output cannot drive less than -1.0
        if (totalOutput < -1.0) {
            totalOutput = -1.0;
        }

        // Calculate the integral contribution.
        // Integral controllers are very prone to saturation
        // and "wind-up". In order to avoid "wind-up",
        // do not allow the integral error total to exceed
        // the total required to saturate the output (-1.0 or 1.0).
        double kI = super.getI();

        if (kI != 0) {

            // If the setpoint is zero, then disable the 
            // integral PID.
            if (super.getSetpoint() == 0) {
                totalError = 0;
            }
            else {
                totalError += error;

                double integralOutput = totalError * kI;

                if (integralOutput + totalOutput > 1.0) {
                    // Clamp the total error
                    totalError = (1.0 - totalOutput) / kI;
                    // set the total output to the limit
                    totalOutput = 1.0;
                } else if (integralOutput + totalOutput < -1.0) {
                    // Clamp the total error
                    totalError = (-1.0 - totalOutput) / kI;
                    // set the total output to the limit
                    totalOutput = -1.0;
                } else {
                    totalOutput += integralOutput;
                }
            }
        }

        output = totalOutput;

        return output;
    }

    @Override
    public void disable() {
        super.disable();
        totalError = 0;
        output = 0;
    }

    /**
     * Set the setpoint for the TSpeedPID.
     * <p>
     * The setpoint for a TSpeedPid must be between -1.0 and 1.0 (full range for a
     * speed controller).
     * <p>
     * Inherits: {@inheritDoc}
     */
    @Override
    public void setSetpoint(double setpoint) {
        if (setpoint > 1.0) {
            System.out.println(
                    "Cannot set TSpeedPID setpoint > 1.0.  Attempted to set value to " + setpoint
                            + ". Overriding to 1.0");
            setpoint = 1.0;
        }

        if (setpoint < -1.0) {
            System.out.println(
                    "Cannot set TSpeedPID setpoint < -1.0.  Attempted to set value to " + setpoint
                            + ". Overriding to -1.0");
            setpoint = -1.0;
        }
        super.setSetpoint(setpoint);
    }

    @Override
    public double get() {
        return output;
    }
}
