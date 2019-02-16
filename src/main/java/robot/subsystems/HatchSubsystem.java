package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.RobotMap;
import robot.commands.hatch.DefaultHatchCommand;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchSubsystem extends TSubsystem {

    // Changed to FakeSolenoids until the production robot is built
	private DoubleSolenoid grabber = new DoubleSolenoid(RobotMap.HATCH_GRABBER_PORT_2, RobotMap.HATCH_GRABBER_PORT);
	private DoubleSolenoid deployer = new DoubleSolenoid(RobotMap.HATCH_DEPLOYER_PORT_2, RobotMap.HATCH_DEPLOYER_PORT);

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
        if (state) { // Assumes true means open
            grabber.set(DoubleSolenoid.Value.kForward);
        }
        else {
            grabber.set(DoubleSolenoid.Value.kReverse);
        }
    }

    //Depending on the state that the deployer is in, the deployer will open or close.
    public void setDeployerState(boolean state) {
        if (state) { // XXX: Assumes true is down
            deployer.set(DoubleSolenoid.Value.kForward);
        }
        else {
            deployer.set(DoubleSolenoid.Value.kReverse);
        }
    }


    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
    	SmartDashboard.putString("Grabber", grabber.get().name());
    	SmartDashboard.putString("Deployer", deployer.get().name());
    }

}
