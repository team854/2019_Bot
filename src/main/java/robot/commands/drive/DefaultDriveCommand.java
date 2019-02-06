package robot.commands.drive;

import com.torontocodingcollective.commands.TDefaultDriveCommand;
import com.torontocodingcollective.commands.TDifferentialDrive;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.speedcontroller.TSpeeds;
import robot.Robot;
import robot.RobotConst;
import robot.oi.OI;
import robot.subsystems.PwmDriveSubsystem;

/**
 * Default drive command for a drive base
 */
public class DefaultDriveCommand extends TDefaultDriveCommand {

    private static final    String COMMAND_NAME = DefaultDriveCommand.class.getSimpleName();
    OI                      oi                  = Robot.oi;
    PwmDriveSubsystem       driveSubsystem      = Robot.driveSubsystem;
    TDifferentialDrive      differentialDrive   = new TDifferentialDrive();
    boolean                 operatorControlling = true;

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
        if (oi.isDriverMoving()) {
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
        
        driveSubsystem.setSpeed(motorSpeeds);
    }

    @Override
    protected boolean isFinished() {
        // The default command does not end
        return false;
    }
}
