package robot;

public class RobotConst {

    public static final String  TEST_ROBOT                    = "TestRobot";
    public static final String  PROD_ROBOT                    = "ProdRobot";

    // *********************************************************
    // Drive Constants
    // *********************************************************
    // Forward for the elevator is counter-clockwise when looking
    // from the back of the robot towards the front
    public static final double  MAX_LOW_GEAR_SPEED;
    //public static final double  MAX_HIGH_GEAR_SPEED;

    public static final double  DRIVE_GYRO_PID_KP;
    public static final double  DRIVE_GYRO_PID_KI;
    public static final double  DRIVE_MAX_ROTATION_OUTPUT     = 0.35;

    public static final double  DRIVE_SPEED_PID_KP;
    public static final double  DRIVE_SPEED_PID_KI;
    
    public static final double  ENCODER_COUNTS_PER_INCH;

    // *********************************************************
    // For Ultrasonic Calibration
    // *********************************************************
    public static final double  ULTRASONIC_VOLTAGE_20IN       = 0.191;
    public static final double  ULTRASONIC_VOLTAGE_40IN       = 0.383;
    public static final double  ULTRASONIC_VOLTAGE_80IN       = 0.764;

    // For CargoSubsystem
    public static final double  CARGO_GATE_ENCODER_COUNTS_PER_INCH;  // Unused
    public static final double  CARGO_GATE_SPEED_PID_KP;
    public static final double  CARGO_GATE_SPEED_PID_KI;
    public static final double  CARGO_GATE_MAX_LOW_GEAR_SPEED;
    // Speed to turn the motors when opening the gate
    public static final double  CARGO_GATE_MOTOR_SPEED;
    // Encoder distance for the motors to turn enough to open/close the gate
    public static final double  CARGO_GATE_TOGGLE_ENCODER_DISTANCE;

    // Operator speed limitation - as a division of the motor speed
    // XXX: Needs to be changed after testing
    public static final double OPERATOR_SPEED_DIVISOR = 2;

    public static enum Direction {
        FORWARD, BACKWARD
    };

    // The TorontoCodingCollective framework was developed to run on different
    // robots through the use of multiple mappings and constants.
    public static final String robot = TEST_ROBOT;

    static {

        switch (robot) {

        case TEST_ROBOT:
        default:

            // The low gear speed should be set just below the 
            // maximum loaded speed of the robot
            MAX_LOW_GEAR_SPEED = 2900.0; // Encoder counts/sec
            //MAX_HIGH_GEAR_SPEED = 900.0;

            // Typically set the integral gain at 1/20 of the 
            // proportional gain.  The gain can often be increased
            // above this value, but typically gives good 
            // stability and acceptable performance
            DRIVE_GYRO_PID_KP = .01;
            DRIVE_GYRO_PID_KI = DRIVE_GYRO_PID_KP / 20.0;

            DRIVE_SPEED_PID_KP = 1;
            DRIVE_SPEED_PID_KI = DRIVE_SPEED_PID_KP / 20.0;
 
            ENCODER_COUNTS_PER_INCH = 28.52;

            // CargoSubsystem
            CARGO_GATE_ENCODER_COUNTS_PER_INCH = 0;  // Unused
            CARGO_GATE_SPEED_PID_KP = 0;
            CARGO_GATE_SPEED_PID_KI = 0;
            CARGO_GATE_MAX_LOW_GEAR_SPEED = 0;  // XXX: Needs to be set
            CARGO_GATE_MOTOR_SPEED = 0;
            CARGO_GATE_TOGGLE_ENCODER_DISTANCE = 0;

            break;
        }

    }
}
