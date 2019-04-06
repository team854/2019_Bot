package robot.commands.drive;

import com.torontocodingcollective.commands.TDefaultDriveCommand;
import com.torontocodingcollective.commands.TDifferentialDrive;
import com.torontocodingcollective.commands.gyroDrive.TRotateToHeadingCommand;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.speedcontroller.TSpeeds;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Robot;
import robot.RobotConst;
import robot.oi.OI;
import robot.subsystems.CameraSubsystem;
import robot.subsystems.CameraSubsystem.Camera;

/**
 * Default drive command for a drive base
 */
public class DefaultDriveCommand extends TDefaultDriveCommand {

    private static final String COMMAND_NAME        = DefaultDriveCommand.class.getSimpleName();
    OI                          oi                  = Robot.oi;
    TGyroDriveSubsystem         driveSubsystem      = Robot.driveSubsystem;
    TDifferentialDrive          differentialDrive   = new TDifferentialDrive();
    CameraSubsystem             cameraSubsystem     = Robot.cameraSubsystem;
    boolean                     operatorControlling;
    TRotateToHeadingCommand     rotateToHeadingCommand = null;
    double                      driverControllingStartTime = -1;

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

        if(Robot.oi.getHopUp()){
            Scheduler.getInstance().add(new HopUpCommand());
        }

        
        // Check whether Driver is moving or not, otherwise the Operator can control
        TStickPosition leftStickPosition = oi.getOperatorDriveStickPosition(TStick.LEFT);
        TStickPosition rightStickPosition = oi.getOperatorDriveStickPosition(TStick.RIGHT);
        operatorControlling = true;
        if (oi.isDriverActive()) {
            leftStickPosition = oi.getDriverDriveStickPosition(TStick.LEFT);
            rightStickPosition = oi.getDriverDriveStickPosition(TStick.RIGHT);
            operatorControlling = false;
        }
        SmartDashboard.putBoolean("Driver Controlling", !operatorControlling);

        if (operatorControlling && DriverStation.getInstance().isAutonomous()) {
            leftStickPosition.y = leftStickPosition.y / 2; 
        }

        // Turn PIDs on/off
        /*if (operatorControlling) {
            oi.setSpeedPidEnabled(true);
        } else {
            oi.setSpeedPidEnabled(false);
        }*/

        // If the driver is controlling, reset the cargo state after one second
        if (!operatorControlling) {
        	
        	if (driverControllingStartTime < 0) {
        		driverControllingStartTime = timeSinceInitialized();
        	}
        	// Reset the gate height and flap if the driver has been
            // controlling for more than 2 seconds.
        	if (timeSinceInitialized() - driverControllingStartTime > .75) {
        		Robot.oi.setHeightState(false);
        		//Robot.oi.setDeployerState(false);
        	}
        }
        else {
        	// If the driver is not controlling reset the timer.
        	driverControllingStartTime = -1;
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
        
        
        if (operatorControlling && Robot.cameraSubsystem.getCurrentCamera() == Camera.REAR) {
            double temp = motorSpeeds.right;
            motorSpeeds.right = -motorSpeeds.left;
            motorSpeeds.left = -temp;
        }
        // Check if aligning needs to happen instead, and that two pieces of tape can be seen
        // It also makes sure that no one is driving at the time
        // XXX: Only check operator left stick because the right one might be moving from pressing in
        if (oi.getAutoAlignSelected() && cameraSubsystem.alignmentNeeded() && !(oi.isDriverDriving() || oi.isOperatorDriving())) {
            // Calculate required rotate heading and make use it's >=0 and < 360
            double heading = driveSubsystem.getGyroAngle() + cameraSubsystem.getDegreesOff();
            heading = heading % 360;
            if (heading < 0) {
                heading += 360;
            }
            // XXX: Has a default timeout of 5 secs, we'll see if we need to change it
            // Use the following to start a command through the scheduler
            System.out.println("*** Auto Align "+heading+" ***");
            Scheduler.getInstance().add(new AutoAlignCommand(heading));
        }

        // This will only run if the TRotateToHeadingCtommand isn't running
        if (operatorControlling && oi.getSlightLeft()) {
            motorSpeeds.left = 0;
            motorSpeeds.right = 0.5;  // XXX: Set this value
        }
        else if (operatorControlling && oi.getSlightRight()) {
            motorSpeeds.left = 0.5;  // XXX: Set this value
            motorSpeeds.right = 0;
        }
        driveSubsystem.setSpeed(motorSpeeds);
    }

    @Override
    protected boolean isFinished() {
        // The default command does not end
        return false;
    }

    @Override 
    protected void end() {
    	operatorControlling = true;
    	SmartDashboard.putBoolean("Driver Controlling", false);
    }
    
}
