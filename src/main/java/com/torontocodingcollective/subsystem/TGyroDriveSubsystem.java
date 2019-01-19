package com.torontocodingcollective.subsystem;

import com.torontocodingcollective.pid.TGyroPID;
import com.torontocodingcollective.sensors.encoder.TEncoder;
import com.torontocodingcollective.sensors.gyro.TGyro;
import com.torontocodingcollective.speedcontroller.TSpeedController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class TGyroDriveSubsystem extends TDriveSubsystem {

    private enum Mode {
        DRIVE_ON_HEADING, ROTATE_TO_HEADING, DISABLED
    };

    protected TGyro  gyro;
    private TGyroPID gyroPid;

    private double   maxRotationOutput;
    private double   speedSetpoint = 0;
    private Mode     mode;

    /**
     * Drive subsystem with left/right drive and gyro.
     * <p>
     * The GyroDrive subsystem extends the DriveSubsystem and adds has a gyro and
     * PID controller to control the angle heading of the robot. The GyroPID is
     * initialized to disabled. Use the {@link #driveOnHeading(double, double)},
     * {@link #rotateToHeading(double)} to enable the gyroPID, and
     * {@link #disableGyroPid()} to disable the gyroPID.
     * 
     * @param leftSpeedController
     *            that extends the {@link TSpeedController}
     * @param rightSpeedController
     *            that extends {@link TSpeedController}
     * @param gyro
     *            that extends {@link TGyro}
     * @param gyroKP
     *            Default Proportional gain for the gyro angle pid. The gyro PID is
     *            displayed on the SmartDashboard and can be adjusted through that
     *            interface
     * @param gyroKI
     *            Default Integral gain for the gyro angle pid. The gyro PID is
     *            displayed on the SmartDashboard and can be adjusted through that
     *            interface
     * @param maxRotationOutput
     *            used to control the rotation of the robot when rotating to an
     *            angle
     */
    public TGyroDriveSubsystem(TSpeedController leftSpeedController, TSpeedController rightSpeedController, 
            TGyro gyro, double gyroKP, double gyroKI, double maxRotationOutput) {
        
        super(leftSpeedController, rightSpeedController);

        this.gyro = gyro;
        gyroPid = new TGyroPID(gyroKP, gyroKI);
        this.maxRotationOutput = maxRotationOutput;
    }

    /**
     * Drive subsystem with left/right drive and gyro.
     * <p>
     * The GyroDrive subsystem extends the DriveSubsystem and adds has a gyro and
     * PID controller to control the angle heading of the robot. The GyroPID is
     * initialized to disabled. Use the {@link #driveOnHeading(double, double)},
     * {@link #rotateToHeading(double)} to enable the gyroPID, and
     * {@link #disableGyroPid()} to disable the gyroPID.
     * 
     * @param leftSpeedController
     *            that extends the {@link TSpeedController}
     * @param rightSpeedController
     *            that extends {@link TSpeedController}
     * @param leftEncoder
     *            encoder for the left motor
     * @param rightEncoder
     *            encoder for the right motor
     * @param encoderCountsPerInch
     * @param speedKP
     *            Proportional gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param speedKI
     *            Integral gain for the motor speed pid. The speed PIDs
     *            are displayed on the SmartDashboard and can be adjusted through
     *            that interface
     * @param maxEncoderSpeed
     *            the max loaded robot encoder rate used to normalize the PID input
     *            encoder feedback.
     * @param gyro
     *            that extends {@link TGyro}
     * @param gyroKP
     *            Default Proportional gain for the gyro angle pid. The gyro PID is
     *            displayed on the SmartDashboard and can be adjusted through that
     *            interface
     * @param gyroKI
     *            Default Integral gain for the gyro angle pid. The gyro PID is
     *            displayed on the SmartDashboard and can be adjusted through that
     *            interface
     * @param maxRotationOutput
     *            used to control the rotation of the robot when rotating to an
     *            angle
     */
    public TGyroDriveSubsystem(TSpeedController leftSpeedController, TSpeedController rightSpeedController, 
            TEncoder leftEncoder, TEncoder rightEncoder, 
            double encoderCountsPerInch, double speedKP, double speedKI, double maxEncoderSpeed, 
            TGyro gyro, double gyroKP, double gyroKI, double maxRotationOutput) {

        super(leftSpeedController, rightSpeedController, 
                leftEncoder, rightEncoder, 
                encoderCountsPerInch, 
                speedKP, speedKI, maxEncoderSpeed);

        this.gyro = gyro;
        gyroPid = new TGyroPID(gyroKP, gyroKI);
        this.maxRotationOutput = maxRotationOutput;
        this.mode = Mode.DISABLED;
    }

    /**
     * Disable the angle PID for the Drive subsystem.
     * <p>
     * NOTE: If the angle PIDs is not currently enabled, this routine has no effect
     */
    public void disableGyroPid() {
        gyroPid.disable();
        this.speedSetpoint = 0;
        this.mode = Mode.DISABLED;
    }

    /**
     * Set the speeds on the motors using a gyroPID to follow the specified heading
     * at the specified speed.
     * <p>
     * If the heading is not currently close to the desired heading, the robot may
     * pivot on the spot before driving on the specified angle. The PID is only
     * enabled when the angle is within 20 degrees of the target.
     * 
     * @param speedSetpoint
     *            0 < speed < 1.0 negative speeds are not allowed
     * @param heading
     *            to drive at 0 <= heading < 360
     */
    public void driveOnHeading(double speedSetpoint, double heading) {

        // If the gain is set to zero, the pid cannot be enabled
        if (gyroPid.getP() == 0 && gyroPid.getI() == 0) {
            System.out.println("The GyroPid cannot be enabled until" 
        + " the PID Kp or Ki value is set.");
            return;
        }

        this.mode = Mode.DRIVE_ON_HEADING;

        this.speedSetpoint = speedSetpoint;

        enableGyroPid(heading);
    }

    /**
     * Enable the gyroPID with the specified heading as a setpoint
     * 
     * @param heading
     *            in degrees
     */
    private void enableGyroPid(double heading) {

        gyroPid.setSetpoint(heading);
        if (!gyroPid.isEnabled()) {
            gyroPid.enable();
            // Initialize the error
            gyroPid.calculate(gyro.getAngle());
        }

    }

    /**
     * Get the current gyro angle
     * <p>
     * NOTE: This routine will always return a positive angle >= 0 and < 360
     * degrees.
     * 
     * @return gyro angle in degrees.
     */
    public double getGryoAngle() {

        return gyro.getAngle();
    }

    /**
     * Get the heading error from the setpoint heading
     * <p>
     * NOTE: This routine will return zero if the gyro PID is not enabled.
     * <p>
     * The GyroPID is enabled using the {@link #rotateToHeading} routines.
     * 
     * @return error in degrees.
     */
    public double getGyroHeadingError() {

        if (!gyroPid.isEnabled()) {
            return 0;
        }
        return gyroPid.getError(gyro.getAngle());
    }

    /**
     * Get Gyro Rate
     * <p>
     * Returns the rate of rotation of the gyro in degrees per second
     * 
     * @return rate in degress/second
     */
    public double getGyroRate() {
        return gyro.getRate();
    }

    /**
     * Set the current gyro heading to zero.
     */
    public void resetGyroAngle() {
        setGyroAngle(0);
    }

    /**
     * Set the speeds on the motors using a gyroPID to rotate to the specified
     * heading at the specified speed.
     * <p>
     * This routine will cause the robot to pivot on the spot
     * 
     * @param heading
     *            to drive at 0 <= heading < 360
     */
    public void rotateToHeading(double heading) {

        rotateToHeading(heading, maxRotationOutput);
    }

    /**
     * Set the speeds on the motors using a gyroPID to rotate to the specified
     * heading at the specified speed.
     * <p>
     * This routine will cause the robot to pivot on the spot
     * 
     * @param heading
     *            to drive at 0 <= heading < 360
     * @param speedSetpoint
     *            0 < speed < 1.0 negative speeds are not allowed
     */
    public void rotateToHeading(double heading, double speedSetpoint) {

        // If the gain is set to zero, the pid cannot be enabled
        if (gyroPid.getP() == 0 && gyroPid.getI() == 0) {
            System.out.println(
                    "The GyroPid cannot be enabled until" 
            + " the PID Kp or Ki value is set.  Cannot rotateToHeading");
            return;
        }

        if (speedSetpoint <= 0 || speedSetpoint > maxRotationOutput) {
            System.out.println("Cannot rotate at speed " + speedSetpoint 
                    + " overriding to " + maxRotationOutput);
            speedSetpoint = maxRotationOutput;
        }

        this.mode = Mode.ROTATE_TO_HEADING;

        this.speedSetpoint = speedSetpoint;

        enableGyroPid(heading);
    }

    /**
     * Set the motor speeds to drive on the appropriate heading.
     * <p>
     * This routine requires the gyro PID to be enabled, and uses the output of the
     * gyro PID to steer the robot by reducing the speed on the appropriate side.
     * 
     * @return double representing the steering adjustment applied to the motors. A
     *         value of 1.0 or -1.0 indicates the robot is rotating on the spot to
     *         get as quickly as possible to the required heading.
     */
    private double setDriveOnHeadingSpeeds() {

        double angleError = gyroPid.getError(gyro.getAngle());

        double leftSpeed = speedSetpoint;
        double rightSpeed = speedSetpoint;

        double steering = 0;

        // If the angle is more than 30 degrees, then
        // rotate to the angle before starting the PID.
        if (Math.abs(angleError) > 30) {

            // Reset the integral error
            gyroPid.reset();

            // Limit the rotation to the max rotation speed
            if (leftSpeed > maxRotationOutput) {
                leftSpeed = maxRotationOutput;
            }

            // If the error is negative, then drive the left motor
            // in reverse.
            steering = 1.0;
            if (angleError < 0) {
                leftSpeed = -leftSpeed;
                steering = -1.0;
            }

            // Drive the motors in the opposite direction to get close
            // to the setpoint
            setSpeed(leftSpeed, -leftSpeed);

            return steering;
        }

        steering = gyroPid.get();

        // When steering with the gyroPid, one of the
        // wheels is slowed proportional to the steering
        if (steering > 0) {
            rightSpeed = leftSpeed * (1.0 - steering);
        }

        if (steering < 0) {
            leftSpeed = rightSpeed * (1.0 + steering);
        }

        setSpeed(leftSpeed, rightSpeed);

        return steering;
    }

    /**
     * Reset the gyro angle to a known heading angle.
     * <p>
     * This routine is useful when start autonomous to set the gyro angle to a known
     * configuration at the start of the match (ie pointed right = 90 degrees)
     * 
     * @param angle
     *            new angle reading for the gyro
     */
    public void setGyroAngle(double angle) {
        gyro.setGyroAngle(angle);
    }

    public void setGyroPidGain(double kP, double kI) {

        this.gyroPid.setP(kP);
        this.gyroPid.setI(kI);

        // If the gain is set to zero, the pid cannot be enabled
        if (kP == 0 && kI == 0) {
            disableGyroPid();
        }
    }

    /**
     * The maximum rotation speed to send to the motors
     * <p>
     * This motor output speed will be sent to both motors (in opposite polarity) to
     * rotate the robot to a desired angle.
     * <p>
     * NOTE: This value does not try to limit the rotational speed in degrees per
     * second, but provides a way to control the rotation by limiting the motor
     * output when under PID control <br>
     * NOTE: The maxRotationOutput is not used when driving the robot manually (via
     * the joysticks).
     *
     * @param maxRotationOutput
     *            to use on the output motors when rotating the robot a value of
     *            zero will be overridden to 0.5 (half output)
     */
    public void setMaxRotationOutput(double maxRotationOutput) {
        this.maxRotationOutput = maxRotationOutput;
    }

    private double setRotateToHeadingSpeeds() {

        double angleError = gyroPid.getError(gyro.getAngle());

        double leftSpeed = speedSetpoint;

        double steering = 0;

        // If the angle is more than 20 degrees, then
        // rotate to the angle before starting the PID.
        if (Math.abs(angleError) > 20) {

            // Reset the integral error
            gyroPid.reset();

            // If the error is negative, then drive the left motor
            // in reverse.
            steering = 1.0;
            if (angleError < 0) {
                leftSpeed = -leftSpeed;
                steering = -1.0;
            }

            // Drive the motors in the opposite direction to get close
            // to the setpoint
            setSpeed(leftSpeed, -leftSpeed);

            return steering;
        }

        // Since we are driving both motors with the rotation, it effectively
        // doubles the gain to the motors, so when rotating on the spot,
        // cut the steering to 1/2.
        steering = gyroPid.get() / 2;

        leftSpeed = steering;

        if (Math.abs(steering) > speedSetpoint) {
            leftSpeed = Math.signum(steering) * speedSetpoint;
        }

        // Drive the motors in the opposite direction to get
        // to the setpoint
        setSpeed(leftSpeed, -leftSpeed);

        return steering;
    }

    @Override
    public void updatePeriodic() {

        // Set the speed from the gyroPID before udpating the super
        double steering = 0;

        if (gyroPid.isEnabled()) {

            gyroPid.calculate(gyro.getAngle());

            if (mode == Mode.DRIVE_ON_HEADING) {
                steering = setDriveOnHeadingSpeeds();
            } else {
                steering = setRotateToHeadingSpeeds();
            }
        }

        SmartDashboard.putNumber("Gyro Steering", steering);

        super.updatePeriodic();

        // Update all SmartDashboard values
        SmartDashboard.putData("Gyro", gyro);
        SmartDashboard.putNumber("Gyro Angle", getGryoAngle());

        SmartDashboard.putData("Gyro PID", gyroPid);

        if (gyro.supportsPitch()) {
            SmartDashboard.putNumber("Gyro Pitch", gyro.getPitch());
        }

    }

}
