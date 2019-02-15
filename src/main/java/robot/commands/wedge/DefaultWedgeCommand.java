package robot.commands.wedge;

import com.torontocodingcollective.commands.TSafeCommand;
import robot.Robot;
import com.torontocodingcollective.TConst;
import edu.wpi.first.wpilibj.Timer;

public class DefaultWedgeCommand extends TSafeCommand {

    private static final String COMMAND_NAME = 
    DefaultWedgeCommand.class.getSimpleName();

    public DefaultWedgeCommand() {
        super(TConst.NO_COMMAND_TIMEOUT, Robot.oi);

        requires(Robot.wedgeSubsystem);
    }
    
    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return super.getParmDesc(); 
    }
    
    @Override
    protected void initialize() {
        // Do nothing
    }

    @Override
    protected void execute() {
        System.out.println("FPGATimestampp: " + Timer.getFPGATimestamp());
        if (Robot.oi.getWedgeState() && Timer.getMatchTime() < 30) {
            Robot.wedgeSubsystem.deployWedge();
        }
    }

    @Override
    protected boolean isFinished() {
        // Default Commands should never finish(end).
        return false;
    }

    @Override
    protected void end() {}
}