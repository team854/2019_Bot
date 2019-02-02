package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.commands.hatch.DefaultHatchCommand;

/**
 *
 */
public class HatchSubsystem extends TSubsystem {

    // Changed to FakeSolenoids until the production robot is built
	
//    Solenoid grabber = new Solenoid(RobotMap.HATCH_GRABBER_PORT); 
//    Solenoid deployer = new Solenoid(RobotMap.HATCH_DEPLOYER_PORT);
	private FakeSolenoid grabber = new FakeSolenoid();
	private FakeSolenoid deployer = new FakeSolenoid();

    @Override
    public void init() {
        //Empty for now
    };

    //Reads joysticks and acts on the subsystem
    @Override
    protected void initDefaultCommand() {
       setDefaultCommand(new DefaultHatchCommand());
    }

    //Depending on the state that the grabber is in, the grabber will open or close.
    public void setGrabberState(boolean state) {
        grabber.set(state);
    }

    //Depending on the state that the deployer is in, the deployer will open or close.
    public void setDeployerState(boolean state) {
        deployer.set(state);
    }


    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
    	SmartDashboard.putBoolean("Grabber", grabber.get());
    	SmartDashboard.putBoolean("Deployer", deployer.get());
    }


}
