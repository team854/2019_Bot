package robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class FakeSolenoid {

	Value value = Value.kOff;

	public FakeSolenoid() {}
	
	public FakeSolenoid(int port) {}
	
	public FakeSolenoid(int port, int port2) {}

	public Value get() {
		return value;
	}

	public void set(Value solenoidValue) {	
		this.value = value;
	}
}
