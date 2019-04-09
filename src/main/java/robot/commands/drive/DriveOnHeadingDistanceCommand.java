package robot.commands.drive;

import com.torontocodingcollective.commands.gyroDrive.TDriveOnHeadingDistanceCommand;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import robot.Robot;

/**
 * Drive on a specified heading and speed for a specified distance
 */
public class DriveOnHeadingDistanceCommand extends TDriveOnHeadingDistanceCommand {

    private static final String COMMAND_NAME = 
            DriveOnHeadingDistanceCommand.class.getSimpleName();
    
    public DriveOnHeadingDistanceCommand(double distanceInches, double heading, double speed, double timeout,
            boolean brakeWhenFinished, TOi oi, TGyroDriveSubsystem driveSubsystem) {
    	super(distanceInches, heading, speed, timeout, brakeWhenFinished, oi, driveSubsystem);
    }

    @Override
    protected boolean isFinished() {

        if (super.isFinished()) {
        	return true;
        }
        
        if (Robot.oi.isOperatorDriving()) {
            logMessage("Operator driving");
            return true;
        }

        if (Robot.oi.isDriverDriving()) {
            logMessage("Driver driving");
            return true;
        }

        return false;
    }
}
