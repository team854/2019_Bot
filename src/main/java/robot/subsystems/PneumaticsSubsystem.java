package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class PneumaticsSubsystem extends TSubsystem {

    // uncomment the compressor to enable pneumatics control
    Compressor compressor = null;

    @Override
    public void init() {
        if (compressor != null) {
            compressor.setClosedLoopControl(true);
        }
    };

    public void disableCompressor() {
        if (compressor != null) {
            compressor.setClosedLoopControl(false);
        }
    }

    public void enableCompressor() {
        if (compressor != null) {
            compressor.setClosedLoopControl(true);
        }
    }

    @Override
    protected void initDefaultCommand() {
     //   setDefaultCommand(new DefaultPneumaticsCommand());
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {

        if (compressor != null) {
            SmartDashboard.putBoolean("Compressor", compressor.enabled());
            SmartDashboard.putBoolean("Compressor Enabled", compressor.getClosedLoopControl());
        } else {
            SmartDashboard.putBoolean("Compressor", false);
            SmartDashboard.putBoolean("Compressor Enabled", false);
        }
    }

}
