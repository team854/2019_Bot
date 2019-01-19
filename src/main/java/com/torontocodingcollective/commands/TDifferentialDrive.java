package com.torontocodingcollective.commands;

import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.speedcontroller.TSpeeds;

/**
 * This class provides a calculator for a differential drive system.
 */
public class TDifferentialDrive {

    private static final double DEFAULT_INPUT_DEADBAND = 0.07;
    private static final double DEFAULT_MOTOR_DEADBAND = 0.03;
    private static final double MAX_DEADBAND           = .25;

    private double              inputDeadband          = 0.0;
    private double              motorSpeedDeadband     = 0.0;

    public TDifferentialDrive() {
        this(DEFAULT_INPUT_DEADBAND, DEFAULT_MOTOR_DEADBAND);
    }

    /**
     * Differential Drive
     * 
     * @param inputDeadband
     * @param motorSpeedDeadband
     */
    public TDifferentialDrive(double inputDeadband, double motorSpeedDeadband) {

        if (Math.abs(inputDeadband) > MAX_DEADBAND) {
            System.out.println(
                    "Invalid input deadband (" + inputDeadband + "). Default value " + DEFAULT_INPUT_DEADBAND
                            + " used.");
            inputDeadband = DEFAULT_INPUT_DEADBAND;
        }
        setInputDeadband(inputDeadband);

        if (Math.abs(motorSpeedDeadband) > MAX_DEADBAND) {
            System.out.println(
                    "Invalid motor deadband (" + motorSpeedDeadband + "). Default value " + DEFAULT_INPUT_DEADBAND
                            + " used.");
            motorSpeedDeadband = DEFAULT_INPUT_DEADBAND;
        }
        setMotorDeadband(motorSpeedDeadband);
    }

    private TSpeeds arcadeDrive(double speed, double rotation) {

        TSpeeds motorSpeeds = new TSpeeds();

        // Check for a speed or rotation greater than the deadband
        if (!(Math.abs(speed) > inputDeadband || Math.abs(rotation) > inputDeadband)) {
            return motorSpeeds;
        }

        double scaledSpeed = scale(speed);
        double scaledRotation = scale(rotation);

        double leftSpeed = 0;
        double rightSpeed = 0;

        if (Math.abs(scaledSpeed) > Math.abs(rotation)) {

            // Drive forward or reverse with steering

            leftSpeed = scaledSpeed;
            rightSpeed = scaledSpeed;

            if (speed > 0) {

                if (rotation > 0) {
                    rightSpeed -= scaledRotation;
                } else {
                    leftSpeed += scaledRotation;
                }

            } else {

                // Driving backwards
                // This routine uses an inverted backwards drive
                // FIXME: should be selectable
                // whether to drive in natural
                // or inverted direction.
                if (rotation > 0) {
                    leftSpeed += scaledRotation;
                } else {
                    rightSpeed -= scaledRotation;
                }
            }

        } else {

            // Rotate on the spot as the speed is adjusted, the
            // robot will move towards a pivot (around a stopped
            // side).
            leftSpeed = scaledRotation;
            rightSpeed = -scaledRotation;

            if (rotation > 0) {

                // Rotating clockwise
                if (speed > 0) {
                    rightSpeed += scaledSpeed;
                } else {
                    leftSpeed += scaledSpeed;
                }

            } else {

                // Rotating counter-clockwise
                if (speed > 0) {
                    leftSpeed += scaledSpeed;
                } else {
                    rightSpeed += scaledSpeed;
                }
            }

        }

        if (Math.abs(leftSpeed) <= motorSpeedDeadband) {
            leftSpeed = 0;
        }

        if (Math.abs(rightSpeed) <= motorSpeedDeadband) {
            rightSpeed = 0;
        }

        motorSpeeds.left = leftSpeed;
        motorSpeeds.right = rightSpeed;

        return motorSpeeds;
    }

    /**
     * Arcade Drive
     * <p>
     * Calculate the motor speeds required for arcade feel
     * <p>
     * NOTE: deadbands will be used for both the input stick position
     * {@see #setInputDeadband(double)} and the output motor speeds motor speed
     * {@see #setMotorDeadband(double)}.
     * 
     * @param singleStickPostion
     *            for the stick position to be used in the calculation. If the
     *            single stick position is {@code null}, then the calculated motor
     *            speeds will be zero.
     * @return TMotorSpeeds object containing the calculated left and right motor
     *         speeds.
     */
    public TSpeeds arcadeDrive(TStickPosition singleStickPosition) {

        if (singleStickPosition == null) {
            return new TSpeeds();
        }
        
        // When driving using a single stick, an axis value of 1, 1 cannot be
        // achieved since if the single stick is set to an angle of 45 degrees, the
        // x and y axis will be set to approx .7.
        //
        // Each of the dimensions should be adjusted to the magnitude of the 
        // vector distributed by the ratio of the x and y values.
        double y = -singleStickPosition.y;
        double x =  singleStickPosition.x;
        
        double magnitude = Math.sqrt(x*x + y*y);
        
        double scaledX = 0;
        double scaledY = 0;
        
        if (Math.abs(x) > Math.abs(y)) {
            scaledX = magnitude * Math.signum(x);
            if (x != 0) {
                scaledY = magnitude * Math.abs(y/x) * Math.signum(y);
            }
        } else {
            scaledY = magnitude * Math.signum(y);
            if (y != 0) {
                scaledX = magnitude * Math.abs(x/y) * Math.signum(x);
            }
        }
        return arcadeDrive(scaledY, scaledX);
    }

    /**
     * Arcade Drive
     * <p>
     * Calculate the motor speeds required for arcade feel
     * <p>
     * The left stick is used as the speed, and the right stick is used as the
     * rotation.
     * <p>
     * NOTE: deadbands will be used for both the input stick position
     * {@see #setInputDeadband(double)} and the output motor speeds motor speed
     * {@see #setMotorDeadband(double)}.
     * 
     * @param leftStickPostion
     *            for the stick position to be used for the speed calculation. If
     *            the leftStickPosition is {@code null}, then the calculated motor
     *            speeds will be zero.
     * @param rightStickPostion
     *            for the stick position to be used for the rotational calculation.
     *            If the rightStickPosition is {@code null}, then the calculated
     *            motor speeds will be zero.
     * @return TMotorSpeeds object containing the calculated left and right motor
     *         speeds.
     */
    public TSpeeds arcadeDrive(TStickPosition leftStickPosition, TStickPosition rightStickPosition) {

        if (leftStickPosition == null || rightStickPosition == null) {
            return new TSpeeds();
        }

        // By convention the y axis of a joystick is inverted
        return arcadeDrive(-leftStickPosition.y, rightStickPosition.x);
    }

    /**
     * Tank Drive
     * <p>
     * Calculate the motor speeds required for tank feel
     * <p>
     * The left and right stick Y-axis are used to drive the wheels directly.
     * <p>
     * NOTE: deadbands will be used for both the input stick position
     * {@see #setInputDeadband(double)} and the output motor speeds motor speed
     * {@see #setMotorDeadband(double)}.
     * 
     * @param leftStickPostion
     *            for the stick position to be used for the left side speed
     *            calculation. If the leftStickPosition is {@code null}, then the
     *            calculated motor speeds will be zero.
     * @param rightStickPostion
     *            for the stick position to be used for the right side speed
     *            calculation. If the rightStickPosition is {@code null}, then the
     *            calculated motor speeds will be zero.
     * @return TMotorSpeeds object containing the calculated left and right motor
     *         speeds.
     */
    public TSpeeds tankDrive(TStickPosition leftStickPosition, TStickPosition rightStickPosition) {

        if (leftStickPosition == null || rightStickPosition == null) {
            return new TSpeeds();
        }

        double leftSpeed = -leftStickPosition.y;
        double rightSpeed = -rightStickPosition.y;

        if (Math.abs(leftSpeed) <= inputDeadband || Math.abs(leftSpeed) <= motorSpeedDeadband) {
            leftSpeed = 0;
        }

        if (Math.abs(rightSpeed) <= inputDeadband || Math.abs(rightSpeed) <= motorSpeedDeadband) {
            rightSpeed = 0;
        }

        return new TSpeeds(leftSpeed, rightSpeed);
    }

    // This routine scales a joystick value to make the
    // acceleration and turning more smooth. All values that are
    // less than 0.6 are cut in half, and values above 0.6 are
    // scaled to be from 0.3 to 1.0.
    private double scale(double value) {

        if (value > 1.0) {
            value = 1.0;
        }

        double absValue = Math.abs(value);

        if (absValue <= inputDeadband) {
            return 0;
        }

        if (absValue <= 0.6) {
            return value / 2;
        }

        // Follow a y=mx + b curve to scale inputs from
        // 0.6 to 1.0 to outputs of 0.3 to 1.0
        if (value > 0) {
            return 0.3 + (value - 0.6) * 7.0 / 4.0;
        }

        return -0.3 + (value + 0.6) * 7.0 / 4.0;
    }

    /**
     * Set the deadband of the input speed and rotation values which
     * 
     * @param inputDeadband
     *            value. Any values of Math.abs(input) <= inputDeadband will be
     *            treated as zero.
     */
    public void setInputDeadband(double inputDeadband) {
        if (Math.abs(inputDeadband) > MAX_DEADBAND) {
            System.out.println("Invalid input deadband (" + inputDeadband + "). setInputDeadband ignored");
            return;
        }
        this.inputDeadband = Math.abs(inputDeadband);
    }

    /**
     * Set the deadband of the motor speed values
     * <p>
     * Any motor speeds that are calculated as Math.abs() < motorDeadband are
     * returned as zero
     * 
     * @param motorSpeedDeadband
     *            value.
     */
    public void setMotorDeadband(double motorSpeedDeadband) {
        if (Math.abs(motorSpeedDeadband) > MAX_DEADBAND) {
            System.out
                    .println("Invalid motorSpeed deadband (" + motorSpeedDeadband + "). setMotorSpeedDeadband ignored");
            return;
        }
        this.motorSpeedDeadband = motorSpeedDeadband;
    }

}
