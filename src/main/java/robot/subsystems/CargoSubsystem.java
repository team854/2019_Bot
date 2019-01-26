package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;
import com.torontocodingcollective.subsystem.TDriveSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import com.torontocodingcollective.speedcontroller.TPwmSpeedController;
import robot.RobotConst;
import robot.RobotMap;
import robot.commands.cargo.DefaultCargoCommand;


/**
 *
 */
public class CargoSubsystem extends TSubsystem {

    Solenoid heightChanger = new Solenoid(RobotMap.CARGO_HEIGHT_CHANGER);

    @Override
    public void init() {
        //Empty for now
    };

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DefaultCargoCommand());
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        //Empty for now.
    }

    // Should start high
    public void setHeightState(boolean state) {
        heightChanger.set(state);
    }

    // Should start closed
    // Encoders should reset after that
    public void setGateState(boolean state) {
        if (state) {
        } else {
        }
    }

}
