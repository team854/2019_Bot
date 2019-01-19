package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Power Subsystem
 * <p>
 * This subsystem contains only the Power Distribution Panel and is used to read
 * the current on a power port.
 */
public class PowerSubsystem extends TSubsystem {

    PowerDistributionPanel pdp = new PowerDistributionPanel();

    public double getMotorCurrent(int port) {
        return pdp.getCurrent(port);
    };

    @Override
    public void init() {
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        SmartDashboard.putData("PDP", pdp);
    }

    @Override
    protected void initDefaultCommand() {
        // There are no commands for a Power Subsystem. This subsystem is used to
        // access the current on the PDP ports.
    }

}
