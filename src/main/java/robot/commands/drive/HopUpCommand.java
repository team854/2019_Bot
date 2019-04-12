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
        Robot.driveSubsystem.resetEncoders();
        
    }

    @Override
    protected boolean isFinished() {
        if (super.isFinished()) {
    	    logMessage("command cancelled");
    		return false;
        }
        if(currentState == State.FINISH){
            
            return true;
        }
        return false;
    }

    @Override
    protected void end(){

        Robot.driveSubsystem.setSpeed(0.0 , 0.0);
    }


    @Override
    protected void execute(){
           switch(currentState){

            case BACK:
            Robot.driveSubsystem.setSpeed(0.5, 0.5);
            if(Robot.driveSubsystem.getDistanceInches() > 2){
                currentState = State.UP;
            }
            break;

            case UP:
            Robot.driveSubsystem.setSpeed(-1.0, -1.0);
            if(Robot.driveSubsystem.getDistanceInches() < -90){
                currentState = State.FINISH;
            }
            break;

            case FINISH:

            break;

           }
        

    }

}