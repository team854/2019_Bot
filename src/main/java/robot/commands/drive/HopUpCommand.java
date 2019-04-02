package robot.commands.drive;

import com.torontocodingcollective.commands.TSafeCommand;
import robot.Robot;
import robot.oi.OI;
import robot.subsystems.CanDriveSubsystem;

import com.torontocodingcollective.TConst;


public class HopUpCommand extends TSafeCommand{
    
    private static final String COMMAND_NAME    = HopUpCommand.class.getSimpleName();
    OI                          oi                  = Robot.oi;
    private enum          State  {BACK, UP, FINISH};
    private State currentState = State.BACK;        

    
    public HopUpCommand(){
        super(TConst.DEFAULT_COMMAND_TIMEOUT, Robot.oi);

        
        requires(Robot.driveSubsystem);
    }
    
    @Override
    protected String getCommandName() {
         return COMMAND_NAME; 
    }

    @Override
    protected String getParmDesc() {
        return super.getParmDesc();
    }

    @Override
    protected void initialize() {
        // Do nothing
        
        
    }

    @Override
    protected boolean isFinished() {
        if (super.isFinished()) {
    	    logMessage("command cancelled");
    		return true;
        }
        return true;
    }


    @Override
    protected void execute(){
           switch(currentState){

            case BACK:
            robot.subsystems.CanDriveSubsystem


           }
        

    }

}