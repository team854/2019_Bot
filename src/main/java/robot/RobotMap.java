package robot;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.speedcontroller.TCanSpeedController.TCanSpeedControllerType;
import com.torontocodingcollective.speedcontroller.TPwmSpeedController.TPwmSpeedControllerType;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * <p>
 * This map is intended to define the wiring only. Robot constants should be put
 * in {@link RobotConst}
 */
public class RobotMap {

	// ******************************************
	// Speed Controllers and encoders
	// PWM addresses
	// ******************************************
	public static final int                     LEFT_DRIVE_PWM_SPEED_CONTROLLER_ADDRESS;
	public static final TPwmSpeedControllerType LEFT_DRIVE_PWM_SPEED_CONTROLLER_TYPE;
	public static final int                     LEFT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS;
	public static final TPwmSpeedControllerType LEFT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE;
	public static final boolean                 LEFT_DRIVE_PWM_MOTOR_ISINVERTED;

	public static final int                     RIGHT_DRIVE_PWM_SPEED_CONTROLLER_ADDRESS;
	public static final TPwmSpeedControllerType RIGHT_DRIVE_PWM_SPEED_CONTROLLER_TYPE;
	public static final int                     RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS;
	public static final TPwmSpeedControllerType RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE;
	public static final boolean                 RIGHT_DRIVE_PWM_MOTOR_ISINVERTED;


	public static final int                     LEFT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS;
	public static final TCanSpeedControllerType LEFT_DRIVE_CAN_SPEED_CONTROLLER_TYPE;
	public static final int                     LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS;
	public static final TCanSpeedControllerType LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE;
	public static final boolean                 LEFT_DRIVE_CAN_MOTOR_ISINVERTED;

	public static final int                     RIGHT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS;
	public static final TCanSpeedControllerType RIGHT_DRIVE_CAN_SPEED_CONTROLLER_TYPE;
	public static final int                     RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS;
	public static final TCanSpeedControllerType RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE;
	public static final boolean                 RIGHT_DRIVE_CAN_MOTOR_ISINVERTED;

	public static final int                     LEFT_DRIVE_DIO_ENCODER_PORT1;
	public static final boolean                 LEFT_DRIVE_ENCODER_ISINVERTED;
	public static final int                     RIGHT_DRIVE_DIO_ENCODER_PORT1;
	public static final boolean                 RIGHT_DRIVE_ENCODER_ISINVERTED;
	// PWM still
	public static final int						WEDGE_MOTOR_PORT;
	public static final boolean					WEDGE_MOTOR_ISINVERTED;

	// Cargo PWM
	public static final int						CARGO_MOTOR_PORT;
	public static final boolean					CARGO_MOTOR_ISINVERTED;

	// ******************************************
	// Gyro Ports
	// ******************************************
	public static final int                     GYRO_PORT;
	public static final boolean                 GYRO_ISINVERTED;

	// ******************************************
	// Ultrasonic Ports
	// ******************************************
	public static final int                     ULTRASONIC_ANALOG_PORT;

	// ******************************************
	// Pneumatics Ports
	// ******************************************
	public static final int                     HATCH_GRABBER_PORT;
	public static final int						HATCH_GRABBER_PORT_2;
	public static final int                     HATCH_DEPLOYER_PORT;
	public static final int                     HATCH_DEPLOYER_PORT_2;

	public static final int                     CARGO_HEIGHT;
	public static final int                     CARGO_HEIGHT_2;

	// Limit switch port
	public static final int						CARGO_SWITCH_PORT;

	// Initializers if this code will be deployed to more than one
	// robot with different mappings
	static {

		switch (RobotConst.robot) {

		case RobotConst.PROD_ROBOT:
			// CAN Constants
			// Speed Controllers connected through the CAN bus
			LEFT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS           = 10;
			LEFT_DRIVE_CAN_SPEED_CONTROLLER_TYPE              = TCanSpeedControllerType.TALON_SRX;
			LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS  = 11;
			LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE     = TCanSpeedControllerType.VICTOR_SPX;
			LEFT_DRIVE_CAN_MOTOR_ISINVERTED                   = TConst.NOT_INVERTED;
			LEFT_DRIVE_ENCODER_ISINVERTED                     = TConst.INVERTED;

			RIGHT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS          = 20;
			RIGHT_DRIVE_CAN_SPEED_CONTROLLER_TYPE             = TCanSpeedControllerType.TALON_SRX;
			RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS = 21;
			RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE    = TCanSpeedControllerType.VICTOR_SPX;
			RIGHT_DRIVE_CAN_MOTOR_ISINVERTED                  = TConst.INVERTED;
			RIGHT_DRIVE_ENCODER_ISINVERTED                    = TConst.NOT_INVERTED;

			// Not used
			GYRO_PORT       = 0;
			GYRO_ISINVERTED = TConst.NOT_INVERTED;

			// Pneumatic DoubleSolenoid ports
			HATCH_GRABBER_PORT      = 7;  // Putting air into this should open it
			HATCH_GRABBER_PORT_2	= 6;
			HATCH_DEPLOYER_PORT     = 4;  // Putting air into this should bring it down
			HATCH_DEPLOYER_PORT_2	= HATCH_DEPLOYER_PORT+1;

			CARGO_HEIGHT            = 0;  // Putting air into this one should bring it up
            CARGO_HEIGHT_2          = CARGO_HEIGHT+1;


			// PWM
			WEDGE_MOTOR_PORT		= 0;
			WEDGE_MOTOR_ISINVERTED	= false;
			
			CARGO_MOTOR_PORT = 1;
			CARGO_MOTOR_ISINVERTED = false;

			// PWM Values are Unused on Prod Robot
			// PWM Constants
			LEFT_DRIVE_PWM_SPEED_CONTROLLER_ADDRESS           = 0;
			LEFT_DRIVE_PWM_SPEED_CONTROLLER_TYPE              = TPwmSpeedControllerType.SPARK;
			LEFT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS  = 1;
			LEFT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE     = TPwmSpeedControllerType.TALON_SRX;
			LEFT_DRIVE_PWM_MOTOR_ISINVERTED                   = TConst.NOT_INVERTED;

			RIGHT_DRIVE_PWM_SPEED_CONTROLLER_ADDRESS          = 2;
			RIGHT_DRIVE_PWM_SPEED_CONTROLLER_TYPE             = TPwmSpeedControllerType.TALON_SR;
			RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS = 3;
			RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE    = TPwmSpeedControllerType.VICTOR_SPX;
			RIGHT_DRIVE_PWM_MOTOR_ISINVERTED                  = TConst.INVERTED;

			LEFT_DRIVE_DIO_ENCODER_PORT1                      = 0;
			RIGHT_DRIVE_DIO_ENCODER_PORT1                     = 1;

			ULTRASONIC_ANALOG_PORT                            = 0;

			// XXX: set this
			CARGO_SWITCH_PORT								  = 0;
			
			break;

		case RobotConst.TEST_ROBOT:
		default:
			// PWM Constants
			// Talon and Victors connected through Pwm
			LEFT_DRIVE_PWM_SPEED_CONTROLLER_ADDRESS           = 0;
			LEFT_DRIVE_PWM_SPEED_CONTROLLER_TYPE              = TPwmSpeedControllerType.SPARK;
			LEFT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS  = 1;
			LEFT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE     = TPwmSpeedControllerType.TALON_SRX;
			LEFT_DRIVE_PWM_MOTOR_ISINVERTED                   = TConst.NOT_INVERTED;
			LEFT_DRIVE_DIO_ENCODER_PORT1                      = 0;
			LEFT_DRIVE_ENCODER_ISINVERTED                     = TConst.INVERTED;

			RIGHT_DRIVE_PWM_SPEED_CONTROLLER_ADDRESS          = 2;
			RIGHT_DRIVE_PWM_SPEED_CONTROLLER_TYPE             = TPwmSpeedControllerType.TALON_SR;
			RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS = 3;
			RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE    = TPwmSpeedControllerType.VICTOR_SPX;
			RIGHT_DRIVE_PWM_MOTOR_ISINVERTED                  = TConst.INVERTED;
			RIGHT_DRIVE_DIO_ENCODER_PORT1                     = 2;
			RIGHT_DRIVE_ENCODER_ISINVERTED                    = TConst.NOT_INVERTED;

			// Not used
			GYRO_PORT       = 0;
			GYRO_ISINVERTED = TConst.NOT_INVERTED;

			// Pneumatics are not used on the test platform
			HATCH_GRABBER_PORT      = 0;
			HATCH_GRABBER_PORT_2	= HATCH_GRABBER_PORT+1;
			HATCH_DEPLOYER_PORT     = 1;
			HATCH_DEPLOYER_PORT_2	= HATCH_DEPLOYER_PORT+1;
			
			CARGO_HEIGHT			= 2;
			CARGO_HEIGHT_2			= CARGO_HEIGHT+1;

			// Not used
			WEDGE_MOTOR_PORT		= 4;
			WEDGE_MOTOR_ISINVERTED	= false;
			CARGO_MOTOR_PORT		= 5;
			CARGO_MOTOR_ISINVERTED	= true;


			// CAN Constants
			// CAN Constants are unused
			LEFT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS           = 0;
			LEFT_DRIVE_CAN_SPEED_CONTROLLER_TYPE              = TCanSpeedControllerType.TALON_SRX;
			LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS  = 1;
			LEFT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE     = TCanSpeedControllerType.VICTOR_SPX;
			LEFT_DRIVE_CAN_MOTOR_ISINVERTED                   = TConst.NOT_INVERTED;

			RIGHT_DRIVE_CAN_SPEED_CONTROLLER_ADDRESS          = 2;
			RIGHT_DRIVE_CAN_SPEED_CONTROLLER_TYPE             = TCanSpeedControllerType.TALON_SRX;
			RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_ADDRESS = 3;
			RIGHT_DRIVE_CAN_FOLLOWER_SPEED_CONTROLLER_TYPE    = TCanSpeedControllerType.VICTOR_SPX;
			RIGHT_DRIVE_CAN_MOTOR_ISINVERTED                  = TConst.INVERTED;

			ULTRASONIC_ANALOG_PORT                            = 0;

			// Not used
			CARGO_SWITCH_PORT								  = 4;
			
			break;
		}
	}
}
