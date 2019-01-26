package robot;

import com.torontocodingcollective.TConst;
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

    public static final int                     LEFT_DRIVE_DIO_ENCODER_PORT1;
    public static final boolean                 LEFT_DRIVE_DIO_ENCODER_ISINVERTED;
    public static final int                     RIGHT_DRIVE_DIO_ENCODER_PORT1;
    public static final boolean                 RIGHT_DRIVE_DIO_ENCODER_ISINVERTED;

    // ******************************************
    // Gyro Ports
    // ******************************************
    public static final int                     GYRO_PORT;
    public static final boolean                 GYRO_ISINVERTED;

    // ******************************************
    // Pneumatics Ports
    // ******************************************
    public static final int                     HATCH_GRABBER_PORT;
    public static final int                     HATCH_DEPLOYER_PORT;

    public static final int                     CARGO_HEIGHT_CHANGER;

    // Initializers if this code will be deployed to more than one
    // robot with different mappings
    static {

        switch (RobotConst.robot) {

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
            LEFT_DRIVE_DIO_ENCODER_ISINVERTED                 = TConst.INVERTED;

            RIGHT_DRIVE_PWM_SPEED_CONTROLLER_ADDRESS          = 2;
            RIGHT_DRIVE_PWM_SPEED_CONTROLLER_TYPE             = TPwmSpeedControllerType.TALON_SR;
            RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_ADDRESS = 3;
            RIGHT_DRIVE_PWM_FOLLOWER_SPEED_CONTROLLER_TYPE    = TPwmSpeedControllerType.VICTOR_SPX;
            RIGHT_DRIVE_PWM_MOTOR_ISINVERTED                  = TConst.INVERTED;
            RIGHT_DRIVE_DIO_ENCODER_PORT1                     = 2;
            RIGHT_DRIVE_DIO_ENCODER_ISINVERTED                = TConst.NOT_INVERTED;

            // Not used
            GYRO_PORT       = 0;
            GYRO_ISINVERTED = TConst.NOT_INVERTED;

            // Unset values below
            // **********************************
            HATCH_GRABBER_PORT = 0;
            HATCH_DEPLOYER_PORT = 0;

            CARGO_HEIGHT_CHANGER = 0;
            
            break;
        }
    }
}
