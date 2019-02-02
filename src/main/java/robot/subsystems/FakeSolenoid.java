package robot.subsystems;

public class FakeSolenoid {

	boolean state = false;

	public FakeSolenoid() {}
	
	public FakeSolenoid(int port) {}
	
	public void set(boolean state) {
		this.state = state;
	}
	
	public boolean get() {
		return state;
	}
}
