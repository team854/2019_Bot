package robot.commands.hatch;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;
import robot.Robot;

/**
 * 
 */
public class DefaultHatchCommand extends TSafeCommand {

    private static final String COMMAND_NAME = 
    DefaultHatchCommand.class.getSimpleName();

    public DefaultHatchCommand() {
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
        //Do nothing
    }

    @Override
    protected void execute() {
        Robot.hatchSubsystem.setGrabberState(Robot.oi.getGrabberState());
        Robot.hatchSubsystem.setDeployerState(Robot.oi.getDeployerState());
    }

    @Override
    protected boolean isFinished() {
        //Default Commands should never finish(end).
        return false;
    }

    @Override
    protected void end() {

    }
    
}
