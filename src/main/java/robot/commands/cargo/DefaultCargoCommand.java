package robot.commands.cargo;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;
import robot.Robot;

/**
 * 
 */
public class DefaultCargoCommand extends TSafeCommand {

    private static final String COMMAND_NAME = 
    DefaultCargoCommand.class.getSimpleName();

    public DefaultCargoCommand() {
        super(TConst.NO_COMMAND_TIMEOUT, Robot.oi);

        requires(Robot.cargoSubsystem);
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
        Robot.cargoSubsystem.setHeightState(Robot.oi.getHeightState());
        Robot.cargoSubsystem.setGateState(Robot.oi.getGateState());
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
