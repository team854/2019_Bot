package robot.subsystems;

import com.torontocodingcollective.speedcontroller.TPwmSpeedController;
import com.torontocodingcollective.speedcontroller.TPwmSpeedController.TPwmSpeedControllerType;
import com.torontocodingcollective.subsystem.TSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import robot.RobotMap;
import robot.commands.wedge.DefaultWedgeCommand;

public class WedgeSubsystem extends TSubsystem {

    private TPwmSpeedController wedge = new TPwmSpeedController(
                                                TPwmSpeedControllerType.SPARK,
                                                RobotMap.WEDGE_MOTOR_PORT,
                                                RobotMap.WEDGE_MOTOR_ISINVERTED);

    @Override
    public void init() {
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DefaultWedgeCommand());
    }

    public void deployWedge() {
        // XXX: Test
        wedge.set(1);
        Timer.delay(2);
        wedge.set(0);
    }

    @Override
    public void updatePeriodic() {
        //SmartDashboard.putBoolean(key, value);
    }
}