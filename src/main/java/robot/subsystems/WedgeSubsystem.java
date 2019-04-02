package robot.subsystems;

import com.torontocodingcollective.speedcontroller.TPwmSpeedController;
import com.torontocodingcollective.speedcontroller.TPwmSpeedController.TPwmSpeedControllerType;
import com.torontocodingcollective.subsystem.TSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    /*public void deployWedge() {
        // XXX: Test
        wedge.set(1);
        Timer.delay(2);
        wedge.set(0);
    }*/

    public void setWedgeSpeed(double speed) {
        wedge.set(speed);
    }

    public double getWedgeSpeed() {
        return wedge.get();
    }

    public boolean isDeployable() {
        return true;
        // There's 30 secs or less left
        // and we're not in Sandstorm
//        return (Timer.getMatchTime() <= 30 && Timer.getFPGATimestamp() > 15);
    }

    @Override
    public void updatePeriodic() {
        SmartDashboard.putBoolean("WedgeIsDeployable", isDeployable());
    }
}