package robot.commands.wedge;

import com.torontocodingcollective.commands.TSafeCommand;

import edu.wpi.first.wpilibj.Timer;
import robot.Robot;
import com.torontocodingcollective.TConst;

public class DefaultWedgeCommand extends TSafeCommand {

    private static final String COMMAND_NAME    = DefaultWedgeCommand.class.getSimpleName();
    private double              startTime       = -1;


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
        // Button pressed, wedge is deployable, and we aren't already deploying
        if (Robot.oi.getWedgeState() && Robot.wedgeSubsystem.isDeployable() && Robot.wedgeSubsystem.getWedgeSpeed() == 0) {
            Robot.cargoSubsystem.setHeightState(true);  // XXX: Assumes true is up
            Robot.wedgeSubsystem.setWedgeSpeed(1);
            startTime = Timer.getFPGATimestamp();  // What time did we start doing this?
        }
        // XXX: Delay needs to be tested
        // Deploying is happening, and 0.5 secs have passed
        else if (Robot.wedgeSubsystem.getWedgeSpeed() == 1 && Timer.getFPGATimestamp()-startTime >= 0.5) {
            Robot.wedgeSubsystem.setWedgeSpeed(0);
            Robot.cargoSubsystem.setHeightState(false);
            startTime = -1;
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