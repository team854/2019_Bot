package robot.commands.auto;

import com.torontocodingcollective.commands.TSafeCommand;

import robot.Robot;

/**
 * 
 */
public class AutoDelay extends TSafeCommand {

    private static final String COMMAND_NAME = 
    AutoDelay.class.getSimpleName();

    public AutoDelay(double delayTime) {
        super(delayTime, Robot.oi);
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
    		logMessage("Delay finished");
    		return true;
    	}
    	return false;
    }

}
