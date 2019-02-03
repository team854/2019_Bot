package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import robot.RobotMap;
import robot.commands.cargo.DefaultCargoCommand;

public class CargoSubsystem extends TSubsystem {

	FakeSolenoid height  = new FakeSolenoid(RobotMap.CARGO_HEIGHT);
    FakeSolenoid gate    = new FakeSolenoid(RobotMap.CARGO_GATE);

    @Override
    public void init() {
        //Empty for now
    };

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DefaultCargoCommand());
    }

    public void setHeightState(boolean state) {
        height.set(state);
    }

    public void setGateState(boolean state) {
        gate.set(state);
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
    	// RM:  Always put the states of all devices on the 
    	//      Smartdashboard.
    	//      Add the solenoids here!!
    	
    }

}
