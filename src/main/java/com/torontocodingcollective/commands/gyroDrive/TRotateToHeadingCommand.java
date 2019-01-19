package com.torontocodingcollective.commands.gyroDrive;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.TUtil;
import com.torontocodingcollective.commands.TSafeCommand;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

/**
 * Rotate To Heading Command
 * <p>
 * This command is used to pivot the robot on the spot to align with the given
 * heading.
 * <p>
 * This command will end when the angle is reached and the rotational speed is
 * below the threshold
 */
public class TRotateToHeadingCommand extends TSafeCommand {

    private static final String COMMAND_NAME = 
            TRotateToHeadingCommand.class.getSimpleName();
    
    public static final double        DEFAULT_TIMEOUT = 5.0;

    private final double              heading;
    private final double              maxRotationOutput;

    private final TGyroDriveSubsystem driveSubsystem;

    private boolean                   error           = false;

    /**
     * Rotate to the specified heading
     * <p>
     * This command will use the maxRotation speed specified as long as it does not
     * exceed the speed set in the subsystem.
     * 
     * @param heading
     *            0 <= heading < 360
     * @param maxRotationOutput
     *            a speed of -1 will indicate to use the default set in the
     *            subsystem. The subsystem maxRotationOutput will be used to
     *            override this value if it is set higher than the value in the
     *            subsystem. See {@link TGyroDriveSubsystem#setMaxRotationOutput()}
     * @param timeout
     *            the time after which this command will end automatically. A value
     *            of {@link TConst#NO_COMMAND_TIMEOUT} will be used as an infinite
     *            timeout.
     * @param oi
     *            that extend the TOi operator input class
     * @param driveSubsystem
     *            that extends the TGyroDriveSubsystem
     */
    public TRotateToHeadingCommand(double heading, double maxRotationOutput, double timeout, 
            TOi oi, TGyroDriveSubsystem driveSubsystem) {

        super(timeout, oi);

        this.driveSubsystem = driveSubsystem;

        requires(driveSubsystem);

        if (heading < 0 || heading >= 360) {
            logMessage(
                    "Heading on DriveOnHeadingCommand must be >= 0 or < 360 degrees. " + heading
                            + " is invalid.  Command ending immediately");
            this.heading = 0;
            this.maxRotationOutput = -1;
            return;
        }

        this.heading = heading;
        this.maxRotationOutput = maxRotationOutput;
    }

    /**
     * Rotate to the specified heading
     * <p>
     * This command will use the maxRotation speed specified in the drive subsystem
     * 
     * @param heading
     *            0 <= heading < 360
     * @param timeout
     *            the time after which this command will end automatically. A value
     *            of {@link TConst#NO_COMMAND_TIMEOUT} will be used as an infinite
     *            timeout.
     * @param oi
     *            that extend the TOi operator input class
     * @param driveSubsystem
     *            that extends the TGyroDriveSubsystem
     */
    public TRotateToHeadingCommand(double heading, double timeout, 
            TOi oi, TGyroDriveSubsystem driveSubsystem) {

        this(heading, -1, timeout, oi, driveSubsystem);
    }

    /**
     * Rotate to the specified heading
     * <p>
     * This command will use the maxRotation speed specified in the drive subsystem
     * 
     * @param heading
     *            0 <= heading < 360
     * @param oi
     *            that extend the TOi operator input class
     * @param driveSubsystem
     *            that extends the TGyroDriveSubsystem
     */
    public TRotateToHeadingCommand(double heading, 
            TOi oi, TGyroDriveSubsystem driveSubsystem) {

        this(heading, -1, DEFAULT_TIMEOUT, oi, driveSubsystem);
    }

    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return "target heading " + this.heading 
                + ", maxRotation " + this.maxRotationOutput 
                + ", " + super.getParmDesc(); 
    }
    
    @Override
    protected void initialize() {

        // Only print the command start message
        // if this command was not subclassed
        if (getCommandName().equals(COMMAND_NAME)) {
            logMessage(getParmDesc() + " starting");
        }

        logMessage("current heading " + driveSubsystem.getGryoAngle());
        
        if (error) {
            return;
        }

        if (maxRotationOutput <= 0) {
            driveSubsystem.rotateToHeading(heading);
        } else {
            driveSubsystem.rotateToHeading(heading, maxRotationOutput);
        }
    }

    @Override
    protected boolean isFinished() {

        if (error) {
            logMessage("finished with errors");
            return true;
        }

        // If the angle is close to the required heading and the
        // rotational speed is low (not an overshoot), then end
        double rotationRate = driveSubsystem.getGyroRate();
        double headingError = driveSubsystem.getGyroHeadingError();

        if (super.isFinished()) {
            logMessage("ended at heading " + TUtil.round(driveSubsystem.getGryoAngle(), 1)
            + " with error " + TUtil.round(headingError, 2) 
            + ", rotation rate " + TUtil.round(rotationRate, 1));
            return true;
        }

        if (Math.abs(headingError) <= 1.5 && Math.abs(rotationRate) < 3) {
            logMessage("finished at heading " + TUtil.round(driveSubsystem.getGryoAngle(), 1)
            + " with error " + TUtil.round(headingError, 2) 
            + ", rotation rate " + TUtil.round(rotationRate, 1));
            return true;
        }

        return false;
    }

    @Override
    protected void end() {
        // Always brake at the end of a Rotate to Heading command
        driveSubsystem.setSpeed(0, 0);
        driveSubsystem.disableGyroPid();
    }

}
