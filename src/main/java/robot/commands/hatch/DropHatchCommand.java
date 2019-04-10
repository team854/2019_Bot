package robot.commands.hatch;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;

import robot.Robot;

/**
 * 
 */
public class DropHatchCommand extends TSafeCommand {

    private static final String COMMAND_NAME = 
    DropHatchCommand.class.getSimpleName();

    public DropHatchCommand() {
        super(TConst.NO_COMMAND_TIMEOUT, Robot.oi);

        requires(Robot.hatchSubsystem);
    }
    
    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return super.getParmDesc(); 
    }
    
    @Override
    protected void initialize() {
    	System.out.println("Auto Drop Hatch: " + System.currentTimeMillis());
    	// Switch the state of the OI Deployer toggle.
    	// This step is wrapped in a command so that it can 
    	// be called in a command group
    	Robot.oi.setDeployerState(true);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
    	// Always end after switching the state of the OI deployer toggle
        return true;
    }

    @Override
    protected void end() {
    	// Call the deploy action in the hatch subsystem.
    	Robot.hatchSubsystem.setDeployerState(true);
    }
    
}
