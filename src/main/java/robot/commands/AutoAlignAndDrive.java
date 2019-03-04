package robot.commands;

import com.torontocodingcollective.commands.gyroDrive.TDriveOnHeadingDistanceCommand;
import com.torontocodingcollective.commands.gyroDrive.TRotateToHeadingCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Robot;

/**
 * AutonomousCommand
 * <p>
 * This class extends the CommandGroup class which allows for a string of
 * commands to be chained together to create complex auto patterns.
 */
/**
 * Auto Align and Drive
 * <p>
 * This command group is used to rotate quickly to the desired direction, and then 
 * drive on the new heading until the target is reached
 * <p>
 * In order to drive as quickly as possible, the rotation is cut off after 1 second, and 
 * the drive is started on the target heading.
 */
public class AutoAlignAndDrive extends CommandGroup {

    public AutoAlignAndDrive(double heading, double distance, double speed) {

    	// Rotate to the required heading.  Allow only 0.5 seconds to get there.
    	// If this is used with vision targeting, it should not take more than 0.5 seconds
    	// to align.
    	addSequential(new TRotateToHeadingCommand(heading, 0.5,
                Robot.oi, Robot.driveSubsystem) );
    
    	// Drive on the heading for the requested distance at the requested speed
    	// The command shoud time out in about 2 seconds if the distance is not 
    	// reached.
    	addSequential(new TDriveOnHeadingDistanceCommand(distance, heading, speed, 2, true, 
                Robot.oi, Robot.driveSubsystem) );

    }
}
