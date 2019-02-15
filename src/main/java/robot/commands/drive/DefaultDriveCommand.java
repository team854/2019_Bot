package robot.commands.drive;

import com.torontocodingcollective.commands.TDefaultDriveCommand;
import com.torontocodingcollective.commands.TDifferentialDrive;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.speedcontroller.TSpeeds;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import robot.Robot;
import robot.RobotConst;
import robot.oi.OI;

/**
 * Default drive command for a drive base
 */
public class DefaultDriveCommand extends TDefaultDriveCommand {

    private static final String COMMAND_NAME        = DefaultDriveCommand.class.getSimpleName();
    OI                          oi                  = Robot.oi;
    TGyroDriveSubsystem         driveSubsystem      = Robot.driveSubsystem;
    TDifferentialDrive          differentialDrive   = new TDifferentialDrive();
    boolean                     operatorControlling;

    // NetworkTable stuff
    NetworkTableInstance    inst    = NetworkTableInstance.getDefault();
    NetworkTable            table   = inst.getTable("myContoursReport");
    NetworkTableEntry       centerX = table.getEntry("centerX");
    double                  avgX;
    double                  degreesOff;
    /*NetworkTableEntry       centerY = table.getEntry("centerY");
    NetworkTableEntry       area    = table.getEntry("area");
    NetworkTableEntry       width   = table.getEntry("width");
    NetworkTableEntry       height  = table.getEntry("height");*/

    public DefaultDriveCommand() {
        // The drive logic will be handled by the TDefaultDriveCommand
        // which also contains the requires(driveSubsystem) statement
        super(Robot.oi, Robot.driveSubsystem);
    }
    
    @Override
    protected String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    protected String getParmDesc() {
        return super.getParmDesc();
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        
        // Print the command parameters if this is the current
        // called command (it was not sub-classed)
        if (getCommandName().equals(COMMAND_NAME)) {
            logMessage(getParmDesc() + " starting");
        }
        
        super.initialize();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

        // Check the driver controller buttons
        super.execute();

        // Check whether Driver is moving or not, otherwise the Operator can control
        TStickPosition leftStickPosition = oi.getOperatorDriveStickPosition(TStick.LEFT);
        TStickPosition rightStickPosition = oi.getOperatorDriveStickPosition(TStick.RIGHT);
        operatorControlling = true;
        if (oi.isDriverActive()) {
            leftStickPosition = oi.getDriverDriveStickPosition(TStick.LEFT);
            rightStickPosition = oi.getDriverDriveStickPosition(TStick.RIGHT);
            operatorControlling = false;
        }

        // Drive according to the type of drive selected in the
        // operator input.
        TStick singleStickSide = oi.getSelectedSingleStickSide();

        TSpeeds motorSpeeds;

        switch (oi.getSelectedDriveType()) {

        case SINGLE_STICK:
            TStickPosition singleStickPosition = rightStickPosition;
            if (singleStickSide == TStick.LEFT) {
                singleStickPosition = leftStickPosition;
            }
            motorSpeeds = differentialDrive.arcadeDrive(singleStickPosition);
            break;

        case TANK:
            motorSpeeds = differentialDrive.tankDrive(leftStickPosition, rightStickPosition);
            break;

        case ARCADE:
        default:
            motorSpeeds = differentialDrive.arcadeDrive(leftStickPosition, rightStickPosition);
            break;
        }

        // Limit Operator speeds
        if (operatorControlling) {
            motorSpeeds.left /= RobotConst.OPERATOR_SPEED_DIVISOR;
            motorSpeeds.right /= RobotConst.OPERATOR_SPEED_DIVISOR;
        }
        
        // Check if aligning needs to happen instead, and that a contour can actually be found
        if (oi.getAlignmentState() && centerX.getNumberArray(null) != null) {
            // Average the centerX values of both contours - both pieces of tape
            avgX = (centerX.getNumberArray(null)[0].doubleValue() + centerX.getNumberArray(null)[1].doubleValue()) / 2;
            // XXX: Do math here based on desired avgX value
            //      Convert to a heading based on the current gyro angle
            //      Use RobotConst.VISION_AVG_X_ERROR_MARGIN
            // To better visualize the below math, paste the following into https://www.desmos.com/scientific :
            // \tan^{-1}\left(\frac{\frac{x-320}{320}\left(146.25\right)}{307}\right)
            // x represents the avgX variable

            // - or + is left or right
            // 146.25: half the camera view field in cm
            // 307 distance from camera to 146.25 cm viewfield line
            degreesOff = Math.toDegrees(Math.atan((((avgX-320)/320) * 146.25) / 307));
            
            // Calculate the absolute angle to rotate to by looking at the current angle
            driveSubsystem.rotateToHeading(driveSubsystem.getGyroAngle() + degreesOff);
        }
        else {
            driveSubsystem.setSpeed(motorSpeeds);
        }
    }

    @Override
    protected boolean isFinished() {
        // The default command does not end
        return false;
    }
}
