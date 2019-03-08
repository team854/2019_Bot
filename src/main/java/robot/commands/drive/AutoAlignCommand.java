package robot.commands.drive;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.gyroDrive.TRotateToHeadingCommand;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import robot.Robot;

/**
 * 
 */
public class AutoAlignCommand extends TRotateToHeadingCommand {

    private static final String COMMAND_NAME = 
    AutoAlignCommand.class.getSimpleName();

    public AutoAlignCommand(double heading) {
        super(heading, Robot.oi, Robot.driveSubsystem);

        requires(Robot.driveSubsystem);
    }
    
    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return super.getParmDesc(); 
    }
    
    @Override
    protected void initialize() {
    	super.initialize();
    	// Start the operator rumble
    	Robot.oi.setOperatorRumble(true);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
    	
    	if (super.isFinished()) {
    		return true;
    	}
    	
    	// Always interrupt if the driver or operator joysticks are active
    	if (Robot.oi.isDriverDriving() || Robot.oi.isOperatorDriving()) {
    		return true;
    	}
    	
        return true;
    }

    @Override
    protected void end() {
    	super.end();
    	Robot.oi.setOperatorRumble(false);
    	
    }
    
}
