package robot.commands;

import com.torontocodingcollective.commands.gyroDrive.TDriveOnHeadingDistanceCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Robot;
import robot.commands.camera.WaitForVisionTarget;
import robot.commands.hatch.DropHatchCommand;
import robot.oi.AutoSelector;

/**
 * AutonomousCommand
 * <p>
 * This class extends the CommandGroup class which allows for a string of
 * commands to be chained together to create complex auto patterns.
 */
public class AutonomousCommand extends CommandGroup {

    /**
     * Autonomous Command
     * <p>
     * Construct an Autonomous Command to perform the auto portion of the robot
     * game. This command will be built when the constructor is called and each
     * element of the command will execute in order.
     * <p>
     * When a parallel command is started, it will act at the same time as all other
     * parallel commands and the next serial command. Parallel commands can end
     * before the serial command, however, when the serial command is complete, all
     * parallel commands will be interrupted at that time if they have not already
     * finished.
     * <p>
     * Since the commands are all constructed at the same instant (when this
     * constructor is called), the commands should not read sensor information in
     * the constructor. All commands should read any relevant sensor information
     * (speed, heading, position) in the init() method of the command. The init()
     * method will be run when the command starts and so can get the robot
     * information at the start of the command, the constructor will be run
     * immediately when the Auto CommandGroup is constructed, and will not have the
     * sensor information relevant to when the command is run.
     */
    public AutonomousCommand() {

        boolean deliver = true;
        
        // getting info
        String robotStartPosition = AutoSelector.getRobotStartPosition();
        String pattern            = AutoSelector.getPattern();

        // Print out the user selection and Game config for debug later
        System.out.println("Auto Command Configuration");
        System.out.println("--------------------------");
        System.out.println("Robot Position : " + robotStartPosition);
        System.out.println("Pattern        : " + pattern);

        switch (pattern) {
        
        case AutoSelector.PATTERN_CARGO_HATCH: 
        		
        	switch (robotStartPosition) {
        	
        	case AutoSelector.ROBOT_RIGHT:
        		
            	addParallel(new DropHatchCommand());
                addSequential(new TDriveOnHeadingDistanceCommand(40, 0, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new TDriveOnHeadingDistanceCommand(30, 340, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new TDriveOnHeadingDistanceCommand(50, 355, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new WaitForVisionTarget(deliver));

                break;
                
            case AutoSelector.ROBOT_LEFT:
            
                addParallel(new DropHatchCommand());
                addSequential(new TDriveOnHeadingDistanceCommand(40, 0, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new TDriveOnHeadingDistanceCommand(30, 20, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new TDriveOnHeadingDistanceCommand(50, 5, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new WaitForVisionTarget(deliver));
            
        		break;
        		
        	case AutoSelector.ROBOT_CENTER:
        		
        		// TODO: Add auto code

        		break;
        	}
        		
        	break;
        		
        case AutoSelector.PATTERN_NONE:
        	break;
        }
    }
}
