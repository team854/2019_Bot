package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;
import com.torontocodingcollective.subsystem.TDriveSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import com.torontocodingcollective.speedcontroller.TPwmSpeedController;
import robot.RobotMap;
import robot.commands.cargo.DefaultCargoCommand;


/**
 *
 */
public class CargoSubsystem extends TSubsystem {

    Solenoid heightChanger = new Solenoid(RobotMap.CARGO_HEIGHT_CHANGER);
    // Drive subsystem is used to work with encoders and motor controllers of
    // the cargo gate, without copying a bunch of code
    TDriveSubsystem gate = new TDriveSubsystem(
                                    new TPwmSpeedController(
                                            RobotMap.CARGO_GATE_LEFT_PWM_SPEED_CONTROLLER_ADDRESS,
                                            RobotMap.CARGO_GATE_LEFT_PWM_SPEED_CONTROLLER_TYPE,
                                            RobotMap.CARGO_GATE_LEFT_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS,
                                            RobotMap.CARGO_GATE_LEFT_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE,
                                            RobotMap.CARGO_GATE_LEFT_PWM_MOTOR_ISINVERTED),
                                    new TPwmSpeedController(
                                            RobotMap.CARGO_GATE_RIGHT_PWM_SPEED_CONTROLLER_ADDRESS,
                                            RobotMap.CARGO_GATE_RIGHT_PWM_SPEED_CONTROLLER_TYPE,
                                            RobotMap.CARGO_GATE_RIGHT_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS,
                                            RobotMap.CARGO_GATE_RIGHT_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE,
                                            RobotMap.CARGO_GATE_RIGHT_PWM_MOTOR_ISINVERTED),
                                    new TDioQuadEncoder(
                                            RobotMap.CARGO_GATE_LEFT_DIO_ENCODER_PORT1,
                                            RobotMap.CARGO_GATE_LEFT_DIO_ENCODER_PORT1 + 1,
                                            RobotMap.CARGO_GATE_LEFT_DIO_ENCODER_ISINVERTED),
                                    new TDioQuadEncoder(
                                            RobotMap.CARGO_GATE_RIGHT_DIO_ENCODER_PORT1,
                                            RobotMap.CARGO_GATE_RIGHT_DIO_ENCODER_PORT1 + 1,
                                            RobotMap.CARGO_GATE_RIGHT_DIO_ENCODER_ISINVERTED),
                                    
                                    // Encoder counts per inch - NOT NEEDED
                                    RobotConst.CARGO_GATE_ENCODER_COUNTS_PER_INCH, 
                                    // Speed PID Kp, Ki
                                    RobotConst.CARGO_GATE_SPEED_PID_KP,
                                    RobotConst.CARGO_GATE_SPEED_PID_KI,
                                    // Max Encoder Speed
                                    RobotConst.CARGO_GATE_MAX_LOW_GEAR_SPEED,
                                );

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
        if (state) {  // True is open, motors turn in one direction to open the gates
            gate.setSpeed(100, 100);  // Dummy numbers
            while (gate.getEncoderDistance() != 1000) // Dummy distance
        } else {
            // Motors turn in opposite direction to close the gates
        }
    }

}
