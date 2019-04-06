package robot.commands;

import com.torontocodingcollective.commands.gyroDrive.TDriveOnHeadingDistanceCommand;
import com.torontocodingcollective.commands.gyroDrive.TRotateToHeadingCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Robot;
import robot.commands.auto.AutoDelay;
import robot.commands.auto.DriveToUltrasonicDistance;
import robot.commands.hatch.DropHatchCommand;
import robot.oi.AutoSelector;
import robot.commands.auto.WaitForVisionTarget;

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
        double delayTime          = AutoSelector.getDelayTime();

        // Print out the user selection and Game config for debug later
        System.out.println("Auto Command Configuration");
        System.out.println("--------------------------");
        System.out.println("Robot Position : " + robotStartPosition);
        System.out.println("Pattern        : " + pattern);
        System.out.println("Delay Time     : " + delayTime);

        
        // Delay before pattern
		if (delayTime > 0) {
			addSequential(new AutoDelay(delayTime));
		}

        switch (pattern) {
        
        case AutoSelector.PATTERN_FRONT_HATCH: 
        /*case AutoSelector.PATTERN_CARGO_DRIVE_UP:
        case AutoSelector.PATTERN_CARGO_DELIVER_AND_GO:*/
        		
        	switch (robotStartPosition) {
        	
        	case AutoSelector.ROBOT_RIGHT:
        		
                addParallel(new DropHatchCommand());
                addSequential(new TDriveOnHeadingDistanceCommand(40, 0, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.1));
                addSequential(new TRotateToHeadingCommand(330, 3, Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.1));
                addSequential(new TDriveOnHeadingDistanceCommand(35, 330, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.1));
                addSequential(new TRotateToHeadingCommand(0, 3, Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.5));
                addSequential(new WaitForVisionTarget(deliver));

                break;
                
            case AutoSelector.ROBOT_LEFT:
            
                addParallel(new DropHatchCommand());
                addSequential(new TDriveOnHeadingDistanceCommand(40, 0, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.1));
                addSequential(new TRotateToHeadingCommand(30, 3, Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.1));
                addSequential(new TDriveOnHeadingDistanceCommand(35, 30, .5, 5, true, 
                Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.1));
                addSequential(new TRotateToHeadingCommand(0, 3, Robot.oi, Robot.driveSubsystem) );
                addSequential(new AutoDelay(0.5));
                addSequential(new WaitForVisionTarget(deliver));
            
        		break;
            
            // Robot center is not used    
        	/*case AutoSelector.ROBOT_CENTER:

        		addSequential(new DriveToUltrasonicDistance());
//                addSequential(new TDriveOnHeadingDistanceCommand(60, 0, .2, 5, true, 
//                Robot.oi, Robot.driveSubsystem) );

                break;*/
            }
            case AutoSelector.PATTERN_SIDE_HATCH:
                
            switch (robotStartPosition) {
        	
                case AutoSelector.ROBOT_RIGHT:
                    
                        
                    break;
                    
                case AutoSelector.ROBOT_LEFT:
                
                    addParallel(new DropHatchCommand());
                    addSequential(new TDriveOnHeadingDistanceCommand(150, 0, .8, 5, false, 
                    Robot.oi, Robot.driveSubsystem) );
                    addSequential(new TDriveOnHeadingDistanceCommand(100, 335, .8, 5, true, 
                    Robot.oi, Robot.driveSubsystem) );
                    addSequential(new TRotateToHeadingCommand(90, 3, Robot.oi, Robot.driveSubsystem) );

                   
                
                    break;
                
                // Robot center is not used    
                /*case AutoSelector.ROBOT_CENTER:
    
                    addSequential(new DriveToUltrasonicDistance());
    //                addSequential(new TDriveOnHeadingDistanceCommand(60, 0, .2, 5, true, 
    //                Robot.oi, Robot.driveSubsystem) );
    
                    break;*/
                }
                
        		
        	break;
                
            
        /*case AutoSelector.PATTERN_NONE:
        	break;*/
        }
    }
}

