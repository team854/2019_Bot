package robot.commands.cargo;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.TSafeCommand;
import robot.Robot;

/**
 * 
 */
public class DefaultCargoCommand extends TSafeCommand {

    private static final    String  COMMAND_NAME        = DefaultCargoCommand.class.getSimpleName();
    private                 double  startTime           = -1;
    // Do we want a ball to go in or out of the intake?
    // By default we want one in - which is `true`, but the value gets reversed below, so it's `false` right here
    private                 boolean ballInDesired  = false;

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

        // If the cargo intake is on, then turn on the cargo intake, and automatically
        // turn it off when a cargo ball is detected.
        if (Robot.oi.getCargoIntake()) {
        	Robot.cargoSubsystem.setIntakeSpeed(0.5);
        	
        	// If a ball is detected, then turn off the intake
        	if (Robot.cargoSubsystem.ballIn()) {
        		Robot.cargoSubsystem.setIntakeSpeed(0);
        		Robot.oi.stopCargoIntake();
        	}
        }
        else if (Robot.oi.getCargoEject()) {
        	// Operator holds the eject button to eject
        	Robot.cargoSubsystem.setIntakeSpeed(1.0);

        } else if (Robot.oi.getCargoReverseEject()) {
            	// Operator holds the eject button to eject
            	Robot.cargoSubsystem.setIntakeSpeed(-.6);

        } else {
        	// If not intaking or ejecting, then stop the motor.s
        	Robot.cargoSubsystem.setIntakeSpeed(0);
        }
//        // If the button is pressed, and the motor isn't not running  - meaning nothing is already running
//        if (Robot.oi.getIntakeState() && Robot.cargoSubsystem.getIntakeSpeed() == 0) {
//            Robot.cargoSubsystem.setIntakeSpeed(0.5);  // XXX: Test speed
//            startTime = timeSinceInitialized();  // Keep track of time
//            // Reverse what it's like at the moment
//            ballInDesired  = !Robot.cargoSubsystem.ballIn();
//        }
//
//        // Otherwise it checks if the command should stop
//        else if (Robot.cargoSubsystem.getIntakeSpeed() > 0) {
//            // The motor is running already
//
//            // Check if we're done, and we wanted a ball in - not out
//            if (Robot.cargoSubsystem.ballIn() == ballInDesired && ballInDesired) {
//                // Ball intake only uses the switch to know when to stop
//                Robot.cargoSubsystem.setIntakeSpeed(0);
//                startTime = -1;
//            }
//            // If the switch says the ball out, and at least 1 secs has passed
//            // XXX: Test timer
//            else if (Robot.cargoSubsystem.ballIn() == ballInDesired && !ballInDesired && timeSinceInitialized()-startTime >= 1) {
//                // Different then the ball intake, because the switch may report that the ball is gone
//                // when it's still there, so there's also a timer
//                // The motor will run for a minimum amount of time, and after that will look at the switch in case
//                Robot.cargoSubsystem.setIntakeSpeed(0);
//                startTime = -1;
//            }
//        }
    }

    @Override
    protected boolean isFinished() {
        // Default Commands should never finish(end).
        return false;
    }

    @Override
    protected void end() {

    }
    
}
