package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;


/**
 *
 */
public class CargoSubsystem extends TSubsystem {

    @Override
    public void init() {
        //Empty for now
    };

    @Override
    protected void initDefaultCommand() {
     //   setDefaultCommand(new DefaultPneumaticsCommand());
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        //Empty for now.
    }

}
