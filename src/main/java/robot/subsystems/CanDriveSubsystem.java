package robot.subsystems;

import com.torontocodingcollective.sensors.encoder.TEncoder;
import com.torontocodingcollective.sensors.gyro.TAnalogGyro;
import com.torontocodingcollective.speedcontroller.TCanSpeedController;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import robot.RobotConst;
import robot.RobotMap;
import robot.commands.drive.DefaultDriveCommand;

/**
 * Chassis Subsystem
 * <p>
 * This class is describes all of the components in a differential (left/right)
 * drive subsystem.
 **/

public class CanDriveSubsystem extends TGyroDriveSubsystem {

	public CanDriveSubsystem() {

		super(
				// Left Speed Controller
				new TCanSpeedController(
						RobotMap.LEFT_DRIVE_CAN_SPEED_CONTROLLER_TYPE,
						RobotMap.LEFT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS,
						RobotMap.LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE,
						RobotMap.LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS, 
						RobotMap.LEFT_DRIVE_CAN_MOTOR_ISINVERTED),

				// Right Speed Controller
				new TCanSpeedController(
						RobotMap.RIGHT_DRIVE_CAN_SPEED_CONTROLLER_TYPE,
						RobotMap.RIGHT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS,
						RobotMap.RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE,
						RobotMap.RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS, 
						RobotMap.RIGHT_DRIVE_CAN_MOTOR_ISINVERTED),

				// Gyro used for this subsystem
				new TAnalogGyro(RobotMap.GYRO_PORT, RobotMap.GYRO_ISINVERTED),

				// Gyro PID Constants
				RobotConst.DRIVE_GYRO_PID_KP, 
				RobotConst.DRIVE_GYRO_PID_KI, 
				RobotConst.DRIVE_MAX_ROTATION_OUTPUT);

		// Get the encoders attached to the CAN bus speed controllers
		TEncoder leftEncoder = getSpeedController(TSide.LEFT).getEncoder();
		TEncoder rightEncoder = getSpeedController(TSide.RIGHT).getEncoder();

		super.setEncoders(
				leftEncoder,  RobotMap.LEFT_DRIVE_CAN_MOTOR_ISINVERTED,
				rightEncoder, RobotMap.RIGHT_DRIVE_CAN_MOTOR_ISINVERTED, 
				RobotConst.ENCODER_COUNTS_PER_INCH, 
				RobotConst.DRIVE_SPEED_PID_KP,
				RobotConst.DRIVE_SPEED_PID_KI,
				RobotConst.MAX_LOW_GEAR_SPEED);
	}

	@Override
	public void init() {
	}

	// Initialize the default command for the Chassis subsystem.
	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DefaultDriveCommand());
	}
}