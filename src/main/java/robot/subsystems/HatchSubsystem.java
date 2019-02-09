package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.RobotMap;
import robot.commands.hatch.DefaultHatchCommand;
import robot.subsystems.FakeSolenoid;

public class HatchSubsystem extends TSubsystem {

    // Changed to FakeSolenoids until the production robot is built
	private FakeSolenoid grabber = new FakeSolenoid(RobotMap.HATCH_GRABBER_PORT);
	private FakeSolenoid deployer = new FakeSolenoid(RobotMap.HATCH_DEPLOYER_PORT);

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
