package com.torontocodingcollective.subsystem;

import com.torontocodingcollective.TUtil;
import com.torontocodingcollective.pid.TSpeedPID;
import com.torontocodingcollective.sensors.encoder.TEncoder;
import com.torontocodingcollective.speedcontroller.TSpeedController;
import com.torontocodingcollective.speedcontroller.TSpeeds;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * DriveSubsystem
 * <p>
 * The DriveSubsystem is a left right drive with encoders on each side of the
 * drive train. The DriveSubsystm can be used with drive PIDs on or off.
 */
public abstract class TDriveSubsystem extends TSubsystem {

    public enum TSide { LEFT, RIGHT }
    
    protected final TSpeedController leftSpeedController;
    protected final TSpeedController rightSpeedController;

    protected TEncoder               leftEncoder          = null;
    protected TEncoder               rightEncoder         = null;

    private final TSpeedPID          leftSpeedPid;
    private final TSpeedPID          rightSpeedPid;

    private double                   maxEncoderSpeed      = 1.0;
    private double                   encoderCountsPerInch = 0;

    boolean                          speedPidsEnabled     = false;

    /**
     * Drive subsystem with left/right drive.
     * <p>
     * The drive subsystem has pids that are initialized to disabled. Use the
     * {@link #enableSpeedPids()} and {@link #disableSpeedPids()} routines to set
     * the PIDs on and off
     * 
     * @param leftSpeedController
     *            that implements the TSpeedController interface
     * @param rightSpeedController
     *            that implements the TSpeedController interface
     */
    public TDriveSubsystem(TSpeedController leftSpeedController, TSpeedController rightSpeedController) {

        this(leftSpeedController, rightSpeedController, null, null, 0, 0, 0, 0);
    }

    /**
     * Drive subsystem with left/right drive.
     * <p>
     * The drive subsystem has pids that are initialized to disabled. Use the
     * {@link #enableSpeedPids()} and {@link #disableSpeedPids()} routines to set
     * the PIDs on and off
     * 
     * @param leftSpeedController
     *            that implements the TSpeedController interface
     * @param rightSpeedController
     *            that implements the TSpeedController interface
     * @param leftEncoder
     *            encoder for the left motor
     * @param rightEncoder
     *            encoder for the right motor
     * @param encoderCountsPerInch
     * @param kP
     *            Proportional gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param kI
     *            Integral gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param maxEncoderSpeed
     *            the max loaded robot encoder rate used to normalize the PID input
     *            encoder feedback.
     */
    public TDriveSubsystem(TSpeedController leftSpeedController, TSpeedController rightSpeedController, 
            TEncoder leftEncoder,TEncoder rightEncoder, 
            double encoderCountsPerInch, double kP, double kI, double maxEncoderSpeed) {

        this.leftSpeedController = leftSpeedController;
        this.rightSpeedController = rightSpeedController;
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;

        this.encoderCountsPerInch = encoderCountsPerInch;

        leftSpeedPid = new TSpeedPID(kP, kI);
        rightSpeedPid = new TSpeedPID(kP, kI);

        speedPidsEnabled = false;
    }

    /**
     * Disable the speed PIDs for the Drive subsystem.
     * <p>
     * NOTE: This routine will not change the motor speeds. Use
     * {@link #setSpeed(double, double)} to update the motor speeds after disabling
     * the PIDs.
     * <p>
     * NOTE: If the speed PIDs are not currently enabled, this routine has no effect
     */
    public void disableSpeedPids() {

        if (speedPidsEnabled) {
            leftSpeedPid.disable();
            rightSpeedPid.disable();
            speedPidsEnabled = false;
        }
    }

    /**
     * Enable the speed PIDs for the Drive subsystem.
     * <p>
     * NOTE: This routine may change the motor speeds. Enabling a disabled speed PID
     * will clear the setpoint. Use {@link #setSpeed(double, double)} to update the
     * motor speeds after enabling the PIDs
     * <p>
     * NOTE: If the speed PIDs are already enabled, this routine has no effect. <br>
     * NOTE: If there are no encoders, speed pids cannot be enabled
     */
    public void enableSpeedPids() {

        if (speedPidsEnabled) {
            return;
        }

        // If there are no encoders, then do not enable the PID
        if (leftEncoder == null || rightEncoder == null) {
            return;
        }

        // If the proportional gain is not set, then do not enable the PID
        if (leftSpeedPid.getP() == 0 || rightSpeedPid.getP() == 0) {
            return;
        }

        if (!speedPidsEnabled) {
            leftSpeedPid.enable();
            rightSpeedPid.enable();
            speedPidsEnabled = true;
        }
    }

    /**
     * Get the distance in inches
     * <p>
     * The drive subsystem must have encoders and must have an non-zero encoder
     * counts per inch {@see #setEncoderCountsPerInch(double)} in order to return
     * the distance in inches
     * 
     * @return double containing the encoder distance in inches or the raw encoder
     *         counts if the encoder counts per inch are not set.
     */
    public double getDistanceInches() {

        if (encoderCountsPerInch == 0) {
            return getEncoderDistance();
        }
        
        // Round to 2 decimal places
        return TUtil.round(getEncoderDistance() / encoderCountsPerInch, 2);
    }

    /** 
     * Get the speed controller on the specified side.
     * <p>
     * This speed controller was specified on the constructor
     * @param side {@link TSide#LEFT} or {@link TSide#RIGHT}
     * @return TSpeedController for the specified side or 
     * {@code null} if the side is invalid
     */
    public TSpeedController getSpeedController(TSide side) {

        if (side == null) {
            return null;
        }
        
        switch (side) {
        
        case LEFT:
            return leftSpeedController;
            
        case RIGHT:
            return rightSpeedController;
            
        default:
            System.out.println("Cannot get speed controller on side " + side.toString());
            return null;
        }
    }
    
    /**
     * Get the raw distance covered since the last encoder reset
     * <p>
     * The distance returned is the average distance of the left and right encoders.
     * 
     * @return average of the left and right distance in counts or -1 if there are
     *         no encoders.
     */
    public int getEncoderDistance() {

        if (leftEncoder == null || rightEncoder == null) {
            return -1;
        }

        return (leftEncoder.get() + rightEncoder.get()) / 2;
    }

    /**
     * Get the average speed of the left and right encoders in counts/second
     * 
     * @return average of the left and right motor speeds or -1 if there are no
     *         encoders.
     */
    public double getEncoderSpeed() {

        if (leftEncoder == null || rightEncoder == null) {
            return -1;
        }

        return (leftEncoder.getRate() + rightEncoder.getRate()) / 2.0d;
    }

    /**
     * Reset the encoder counts on the encoders.
     */
    public void resetEncoders() {

        if (leftEncoder == null || rightEncoder == null) {
            return;
        }

        leftEncoder.reset();
        rightEncoder.reset();
    }

    /**
     * Set the encoder counts per inch
     * 
     * @param encoderCountsPerInch
     *            if this value is set to 0, then the raw encoder counts will be
     *            returned on the call to {@link #getDistanceInches()}
     */
    public void setEncoderCountsPerInch(double encoderCountsPerInch) {
        this.encoderCountsPerInch = encoderCountsPerInch;
    }

    /**
     * Initialize the encoders for this drive subsystem. This method is used when
     * the encoders are attached to a channel that is used by another device and
     * must be constructed after this subsystem. For example, if an encoder is
     * attached to a CAN based TalonSRX device, the encoder can be retrieved from
     * the TalonSRX using the code: <br>
     * {@code ((TCanSpeedController) xxxxSpeedController).getEncoder(); }
     * 
     * @param leftEncoder
     * @param leftInverted
     *            {@code true} if the encoder is inverted, {@code false} otherwise
     * @param rightEncoder
     * @param rightInverted
     *            {@code true} if the encoder is inverted, {@code false} otherwise
     * @param encoderCountsPerInch
     * @param kP
     *            Proportional gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param kI
     *            Integral gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param maxEncoderSpeed
     *            to use to scale the encoder feedback
     */
    public void setEncoders(TEncoder leftEncoder, boolean leftInverted, 
            TEncoder rightEncoder, boolean rightInverted,
            double encoderCountsPerInch, double kP, double kI, double maxEncoderSpeed) {

        leftEncoder.setInverted(leftInverted);
        rightEncoder.setInverted(rightInverted);

        setEncoders(leftEncoder, rightEncoder, encoderCountsPerInch, 
                kP, kI, maxEncoderSpeed);
    }

    /**
     * Initialize the encoders for this drive subsystem. This method is used when
     * the encoders are attached to a channel that is used by another device and
     * must be constructed after this subsystem. For example, if an encoder is
     * attached to a CAN based TalonSRX device, the encoder can be retrieved from
     * the TalonSRX using the code: <br>
     * {@code ((TCanSpeedController) xxxxSpeedController).getEncoder(); }
     * 
     * @param leftEncoder
     * @param rightEncoder
     * @param kP
     *            Proportional gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param kI
     *            Integral gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param maxEncoderSpeed
     *            to use to scale the encoder feedback
     */
    public void setEncoders(TEncoder leftEncoder, TEncoder rightEncoder, 
            double encoderCountsPerInch, 
            double kP, double kI, double maxEncoderSpeed) {

        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;

        setEncoderCountsPerInch(encoderCountsPerInch);

        // Disable the PIDs if either encoder is null
        if (leftEncoder == null || rightEncoder == null) {
            disableSpeedPids();
        }
        else {
            setSpeedPidGain(kP, kI);
            setMaxEncoderSpeed(maxEncoderSpeed);
        }
    }

    /**
     * Set the max encoder speed on the encoders. This routine would be used when
     * there is a gear shifting robot and the max encoder speed changes between the
     * gears.
     * 
     * @param rawEncoderSpeed
     */
    public void setMaxEncoderSpeed(double rawEncoderSpeed) {
        this.maxEncoderSpeed = rawEncoderSpeed;
    }

    /**
     * Set the max encoder speed on the encoders and adjust the proportional gain on
     * the PIDs. This routine would be used when there is a gear shifting robot and
     * the max encoder speed changes between the gears and the proportional control
     * value changes.
     * 
     * @param maxEncoderSpeed
     *            the maximum speed of the encoder when the output is driven at full
     *            value under a typical load scenario.
     * @param kP
     *            Proportional gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param kI
     *            Integral gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     */

    public void setMaxEncoderSpeed(double maxEncoderSpeed, double kP, double kI) {
        setMaxEncoderSpeed(maxEncoderSpeed);
        setSpeedPidGain(kP, kI);
    }

    /**
     * Set the speeds on the motors. This command will be used to set the setpoint
     * of the controller if the PIDs are enabled, or to set the left and right motor
     * speeds directly if the PIDs are not enabled.
     * 
     * @param leftSpeedSetpoint
     * @param rightSpeedSetpoint
     */
    public void setSpeed(double leftSpeedSetpoint, double rightSpeedSetpoint) {

        if (speedPidsEnabled) {

            // If the PIDs are enabled, then only change the setpoint
            // and allow the periodic update to set the speed controller output
            leftSpeedPid.setSetpoint(leftSpeedSetpoint);
            rightSpeedPid.setSetpoint(rightSpeedSetpoint);

        } else {

            // If the speed PIDs are disabled, then drive the motors
            // with the setpoint.
            leftSpeedController.set(leftSpeedSetpoint);
            rightSpeedController.set(rightSpeedSetpoint);

        }
    }

    /**
     * Set the speeds on the motors. This command will be used to set the setpoint
     * of the controller if the PIDs are enabled, or to set the left and right motor
     * speeds directly if the PIDs are not enabled.
     * 
     * @param motorSpeeds
     *            object containing the left and right motor speeds
     */
    public void setSpeed(TSpeeds motorSpeeds) {
        setSpeed(motorSpeeds.left, motorSpeeds.right);
    }

    /**
     * Set the Pid gain for the PID controller and disable the pids if the gain is
     * set to zero.
     * 
     * @param kP
     */
    public void setSpeedPidGain(double kP, double kI) {

        leftSpeedPid.setP(kP);
        leftSpeedPid.setI(kI);

        rightSpeedPid.setP(kP);
        rightSpeedPid.setI(kI);

        if (kP == 0 && kI == 0) {
            disableSpeedPids();
        }
    }

    public boolean speedPidsEnabled() {
        return speedPidsEnabled;
    }

    @Override
    public void updatePeriodic() {

        // Only update the encoders and pids if there are encoders.
        if (leftEncoder != null && rightEncoder != null) {

            // Update all of the PIDS
            if (speedPidsEnabled) {

                // Speed PID calculations require a normalized rate
                leftSpeedPid.calculate(leftEncoder.getRate() / maxEncoderSpeed);
                rightSpeedPid.calculate(rightEncoder.getRate() / maxEncoderSpeed);

                leftSpeedController.set(leftSpeedPid.get());
                rightSpeedController.set(rightSpeedPid.get());

            }

            // Update all SmartDashboard values
            SmartDashboard.putNumber("L Enc Dist", leftEncoder.get());
            SmartDashboard.putNumber("L Enc Speed", leftEncoder.getRate());
            SmartDashboard.putNumber("R Enc Dist", rightEncoder.get());
            SmartDashboard.putNumber("R Enc Speed", rightEncoder.getRate());
            SmartDashboard.putNumber("AvgEnc Dist", getEncoderDistance());
            SmartDashboard.putNumber("AvgEnc Speed", getEncoderSpeed());
            SmartDashboard.putNumber("Dist Inches", getDistanceInches());

            SmartDashboard.putBoolean("Speed PIDs Active", speedPidsEnabled);
            SmartDashboard.putData("LeftPid", leftSpeedPid);
            SmartDashboard.putData("RightPid", rightSpeedPid);
        }

        // Always print the current motor set speeds.
        SmartDashboard.putNumber("Left Output", leftSpeedController.get());
        SmartDashboard.putNumber("Right Output", rightSpeedController.get());
    }

}
