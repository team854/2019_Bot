package robot.subsystems;

import com.torontocodingcollective.sensors.limitSwitch.TLimitSwitch;
import com.torontocodingcollective.speedcontroller.TPwmSpeedController;
import com.torontocodingcollective.speedcontroller.TPwmSpeedController.TPwmSpeedControllerType;
import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.RobotMap;
import robot.commands.cargo.DefaultCargoCommand;

public class CargoSubsystem extends TSubsystem {

	private TPwmSpeedController intake  = new TPwmSpeedController(
                                                TPwmSpeedControllerType.SPARK,
                                                RobotMap.CARGO_MOTOR_PORT,
                                                RobotMap.CARGO_MOTOR_ISINVERTED);
    private FakeSolenoid        height       = new FakeSolenoid(RobotMap.CARGO_HEIGHT, RobotMap.CARGO_HEIGHT_2);
    private TLimitSwitch        limitSwitch  = new TLimitSwitch(RobotMap.CARGO_SWITCH_PORT, TLimitSwitch.DefaultState.TRUE);
    
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
            height.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void setIntakeSpeed(double speed) {
        intake.set(speed);
    }
    
    public double getIntakeSpeed() {
        return intake.get();
    }

    public boolean ballIn() {
        return limitSwitch.atLimit();
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        
    	SmartDashboard.putString("Cargo Height", height.get().name());
    	SmartDashboard.putNumber("Cargo Intake Speed", getIntakeSpeed());
    	SmartDashboard.putBoolean("Ball Detect", ballIn());
    }

}
