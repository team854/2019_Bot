package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.RobotMap;
import robot.commands.cargo.DefaultCargoCommand;

public class CargoSubsystem extends TSubsystem {

	DoubleSolenoid height  = new DoubleSolenoid(RobotMap.CARGO_HEIGHT, RobotMap.CARGO_HEIGHT_2);
    DoubleSolenoid gate    = new DoubleSolenoid(RobotMap.CARGO_GATE, RobotMap.CARGO_GATE_2);

    @Override
    public void init() {
        //Empty for now
    };

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DefaultCargoCommand());
    }

    public void setHeightState(boolean state) {
        if (state) {  // XXX: Assumes true is up
            height.set(DoubleSolenoid.Value.kForward);
        }
        else {
            // kReverse will pull on the metal - just relax it instead
            height.set(DoubleSolenoid.Value.kOff);
        }
    }

    public void setGateState(boolean state) {
        if (state) { // XXX: Assumes true is an open gate
            gate.set(DoubleSolenoid.Value.kForward);
        }
        else {
            gate.set(DoubleSolenoid.Value.kReverse);
        }
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        SmartDashboard.putString("cargoHeight", height.get().name());
        SmartDashboard.putString("cargoGate", gate.get().name());
    }

}
