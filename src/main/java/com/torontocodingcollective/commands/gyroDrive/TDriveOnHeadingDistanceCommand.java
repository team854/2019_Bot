package com.torontocodingcollective.commands.gyroDrive;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

/**
 * Drive on a specified heading and speed for a specified distance
 */
public class TDriveOnHeadingDistanceCommand extends TDriveOnHeadingCommand {

    private static final String COMMAND_NAME = 
            TDriveOnHeadingDistanceCommand.class.getSimpleName();
    
    double                            distanceInches = 0; // in inches
    private final TGyroDriveSubsystem driveSubsystem;

    /**
     * Construct a new DriveOnHeadingDistanceCommand
     * 
     * @param distanceInches
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
     * @param oi
     *            that extend the TOi operator input class
     * @param driveSubsystem
     *            that extends the TGyroDriveSubsystem
     */
    public TDriveOnHeadingDistanceCommand(double distanceInches, double heading, double speed, double timeout,
            boolean brakeWhenFinished, TOi oi, TGyroDriveSubsystem driveSubsystem) {

        super(heading, speed, timeout, brakeWhenFinished, oi, driveSubsystem);

        this.driveSubsystem = driveSubsystem;
        this.distanceInches = distanceInches;
    }

    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return "dist " + this.distanceInches 
                + ", " + super.getParmDesc(); 
    }
    

    @Override
    protected void initialize() {

        // Only print the command start message
        // if this command was not subclassed
        if (getCommandName().equals(COMMAND_NAME)) {
            logMessage(getParmDesc() + " starting");
        }

        super.initialize();
        driveSubsystem.resetEncoders();
    }

    @Override
    protected boolean isFinished() {

        if (super.isFinished()) {
            logMessage("Command ending at distance " + 
                    driveSubsystem.getDistanceInches() + "inches");
            return true;
        }

        if (driveSubsystem.getDistanceInches() > distanceInches) {
            logMessage("Command ending at distance " + 
                    driveSubsystem.getDistanceInches() + "inches");
            return true;
        }

        return false;
    }
}
