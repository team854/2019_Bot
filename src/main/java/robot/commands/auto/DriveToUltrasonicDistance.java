package robot.commands.auto;

import com.torontocodingcollective.commands.TSafeCommand;
import com.torontocodingcollective.commands.gyroDrive.TDriveOnHeadingDistanceCommand;

import edu.wpi.first.wpilibj.command.Scheduler;
import robot.Robot;
import robot.commands.auto.AutoAlignAndDeliver;
import robot.commands.drive.AutoAlignCommand;
import robot.subsystems.CanDriveSubsystem;
import robot.subsystems.PwmDriveSubsystem;

/**
 * 
 */
public class DriveToUltrasonicDistance extends TSafeCommand {

    private static final String COMMAND_NAME = 
    DriveToUltrasonicDistance.class.getSimpleName();

    /*
     * This command sets up the drive to distance command
     * based on the current ultrasonic readings.
     */
    public DriveToUltrasonicDistance() {
        super(Robot.oi);
    }
    
    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return super.getParmDesc(); 
    }
    
    @Override
    protected void initialize() {
        // Only print the command start message
        // if this command was not subclassed
        if (getCommandName().equals(COMMAND_NAME)) {
            logMessage(getParmDesc() + " starting");
        }

    	super.initialize();
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
    	
    	if (super.isFinished()) {
    	    logMessage("command cancelled");
    		return true;
    	}
    	
    	// NOTE: This command only runs through once, and the purpose of the 
    	//       command is to read the ultrasonic sensor, and then drive
    	//       to the specified distance.
		double distanceInches = 0;
            
		if (Robot.driveSubsystem instanceof CanDriveSubsystem) {
			distanceInches = ((CanDriveSubsystem) Robot.driveSubsystem).getUltrasonicDistance();
		}
		if (Robot.driveSubsystem instanceof PwmDriveSubsystem) {
			distanceInches = ((PwmDriveSubsystem) Robot.driveSubsystem).getUltrasonicDistance();
		}
		
		
		Scheduler.getInstance().add(new TDriveOnHeadingDistanceCommand(distanceInches, 0, 
				0.2, 5, true, Robot.oi, Robot.driveSubsystem));

		return true;
    }

}
