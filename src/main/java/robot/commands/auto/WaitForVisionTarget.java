package robot.commands.auto;

import com.torontocodingcollective.commands.TSafeCommand;

import edu.wpi.first.wpilibj.command.Scheduler;
import robot.Robot;
import robot.commands.drive.AutoAlignCommand;
import robot.subsystems.CanDriveSubsystem;
import robot.subsystems.PwmDriveSubsystem;

/**
 * 
 */
public class WaitForVisionTarget extends TSafeCommand {

    private static final String COMMAND_NAME = 
    WaitForVisionTarget.class.getSimpleName();

    private final boolean deliver;
    
    public WaitForVisionTarget(boolean deliver) {
        super(Robot.oi);
        this.deliver = deliver;
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
    	
    	// This command is finished if a vision target is found
    	if (Robot.cameraSubsystem.targetsFound()) {
    	    if (deliver) {
    	    	
    	        // Calculate required rotate heading and make use it's >=0 and < 360
                double heading = Robot.driveSubsystem.getGyroAngle() + 
                        Robot.cameraSubsystem.getDegreesOff();
                System.out.println("Gyro Angle: "+Robot.driveSubsystem.getGyroAngle()+" Degrees Off: "+Robot.cameraSubsystem.getDegreesOff());
                heading = heading % 360;
                if (heading < 0) {
                    heading += 360;
                }
                
                // Get the distance from the ultrasonic sensor
                double distanceInches = 0;
                
                if (Robot.driveSubsystem instanceof CanDriveSubsystem) {
                    distanceInches = ((CanDriveSubsystem) Robot.driveSubsystem).getUltrasonicDistance();
                }
                if (Robot.driveSubsystem instanceof PwmDriveSubsystem) {
                    distanceInches = ((PwmDriveSubsystem) Robot.driveSubsystem).getUltrasonicDistance();
                }
                
                if (distanceInches < 0) {
                	logMessage("Invalid distance " + distanceInches + " - overriding to zero");
                	distanceInches = 0;
                }
                
                // XXX: Has a default timeout of 5 secs, we'll see if we need to change it
                // Use the following to start a command through the scheduler
                logMessage("Schedule auto align to heading " + heading
                		+ " and drive distance " + distanceInches);

                double visionTimeout = 2; // Allow 2 seconds for vision targeting
                double driveTimeout = 3; // Allow 3 seconds for distance timeout
                
    	        Scheduler.getInstance().add(new AutoAlignAndDeliver(
    	                heading, distanceInches, 0.5, visionTimeout, driveTimeout));
    	    }
    	    return true;
    	}
    	
        return false;
    }

}
