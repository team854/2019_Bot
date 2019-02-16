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
        // If align button is pressed, there's less than 30 secs in the current period,
        // and if the current period is manual not auto
        if (Robot.oi.getWedgeState() && Timer.getMatchTime() <= 30 && Timer.getFPGATimestamp() > 15) {
            // Wedge will hit Cargo if it is down, move it up beforehand
            Robot.cargoSubsystem.setHeightState(true);; // XXX: Assumes true is up
            Robot.wedgeSubsystem.deployWedge();
            // XXX: Is the below needed?
            // Timer.delay(2);
            Robot.cargoSubsystem.setHeightState(false);
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