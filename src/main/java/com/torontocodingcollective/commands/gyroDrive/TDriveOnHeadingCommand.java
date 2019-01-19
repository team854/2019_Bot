package com.torontocodingcollective.commands.gyroDrive;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

/**
 * Drive On Heading Command
 * <p>
 * This command is used to drive on a specified heading for a specified amount
 * of time.
 * <p>
 * The command can be extended to provide additional stopping variations
 * (distance, sensor, etc).
 */
public class TDriveOnHeadingCommand extends TSafeCommand {

    private static final String COMMAND_NAME = 
            TDriveOnHeadingCommand.class.getSimpleName();
    
    private double                    heading;
    private double                    speed;
    private final boolean             brakeWhenFinished;
    private boolean                   error = false;

    private final TGyroDriveSubsystem driveSubsystem;

    /**
     * Construct a new DriveOnHeadingCommand
     * 
     * @param heading
     *            in the range 0 <= heading < 360. If the heading is not in this
     *            range, then the command will end immediately and print an error to
     *            the DriverStation
     * @param speed
     *            at which to drive in the range 0 <= speed <= 1.0. if the speed is
     *            set to a very small value, the robot will not drive and the
     *            command will end on the timeout.
     * @param timeout
     *            the time after which this command will end automatically a value
     *            of {@link TConst#NO_COMMAND_TIMEOUT} will be used as an infinite
     *            timeout.
     * @param oi
     *            that extend the TOi operator input class
     * @param driveSubsystem
     *            that extends the TGyroDriveSubsystem
     */
    public TDriveOnHeadingCommand(double heading, double speed, double timeout, TOi oi,
            TGyroDriveSubsystem driveSubsystem) {
        this(heading, speed, timeout, TConst.BRAKE_WHEN_FINISHED, oi, driveSubsystem);
    }

    /**
     * Construct a new DriveOnHeadingCommand
     * 
     * @param heading
     *            in the range 0 <= heading < 360. If the heading is not in this
     *            range, then the command will end immediately and print an error to
     *            the DriverStation
     * @param speed
     *            at which to drive in the range 0 <= speed <= 1.0. if the speed is
     *            set to a very small value, the robot will not drive and the
     *            command will end on the timeout.
     * @param timeout
     *            the time after which this command will end automatically. A value
     *            of {@link TConst#NO_COMMAND_TIMEOUT} will be used as an infinite
     *            timeout.
     * @param brakeWhenFinished
     *            {@code true} to brake when the command finishes {@code false} to
     *            coast into the next command.
     * @param oi
     *            that extend the TOi operator input class
     * @param driveSubsystem
     *            that extends the TGyroDriveSubsystem
     */
    public TDriveOnHeadingCommand(double heading, double speed, double timeout, 
            boolean brakeWhenFinished, TOi oi,
            TGyroDriveSubsystem driveSubsystem) {

        super(timeout, oi);

        this.driveSubsystem = driveSubsystem;

        requires(driveSubsystem);

        if (heading < 0 || heading >= 360) {
            System.out.println(
                    "Heading on " + COMMAND_NAME 
                    + " must be >= 0 or < 360 degrees. " + heading
                    + " is invalid.  Command ending immediately");
            error = true;
            this.brakeWhenFinished = true;
            return;
        }

        setSpeed(speed);

        this.heading = heading;
        this.brakeWhenFinished = brakeWhenFinished;
    }

    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return "heading " + this.heading 
                + ", speed " + this.speed 
                + ", brake " + this.brakeWhenFinished 
                + ", " + super.getParmDesc(); 
    }

    @Override
    protected void initialize() {
        
        // Only print the command start message
        // if this command was not subclassed
        if (getCommandName().equals(COMMAND_NAME)) {
            logMessage(getParmDesc() + " starting");
        }

        if (!error) {
            driveSubsystem.driveOnHeading(speed, heading);
        }
    }

    @Override
    protected void execute() {

        // If there is an error, then do nothing
        if (error) {
            return;
        }

        // Update the speed and direction each loop.
        // If the values have not changed, this call will
        // have no effect. If the speed is changing for
        // acceleration or deceleration purposes, then
        // this call will adjust the speed setpoint.
        driveSubsystem.driveOnHeading(speed, heading);
    }

    /**
     * Adjust the speed on the driveOnHeading command
     * <p>
     * This routine is used to adjust the speed in the drive on heading command
     * without changing the heading.
     * <p>
     * This routine could be used to support acceleration and deceleration when
     * driving on a heading.
     * 
     * @param speed
     *            the speed to drive at when tracking the heading. The speed should
     *            be between 0 and 1.0. Negative speeds should not be used. If a
     *            value is given outside this range, then the value will be
     *            normalized to be within the range
     */
    public void setSpeed(double speed) {
        this.speed = Math.min(1.0, Math.max(speed, 0));
    }

    @Override
    protected boolean isFinished() {

        if (error) {
            logMessage("Ended with error - see previous message for details");
            return true;
        }

        // Check for a timeout or cancel
        if (super.isFinished()) {
            return true;
        }

        return false;
    }

    @Override
    protected void end() {
        if (brakeWhenFinished) {
            driveSubsystem.setSpeed(0, 0);
            driveSubsystem.disableGyroPid();
        }
    }
}
