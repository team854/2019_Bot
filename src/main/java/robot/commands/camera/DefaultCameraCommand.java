package robot.commands.camera;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;
import robot.Robot;

/**
 * 
 */
public class DefaultCameraCommand extends TSafeCommand {

    private static final String COMMAND_NAME = 
    DefaultCameraCommand.class.getSimpleName();

    public DefaultCameraCommand() {
        super(TConst.NO_COMMAND_TIMEOUT, Robot.oi);

        requires(Robot.cameraSubsystem);
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
    	Robot.cameraSubsystem.setCameraFeed(Robot.oi.getCamera());
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
