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
    	Robot.oi.setDeployerState(true);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        //Default Commands should never finish(end).
        return true;
    }

    @Override
    protected void end() {

    }
    
}
