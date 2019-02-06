package robot.oi;

import com.torontocodingcollective.oi.TAxis;
import com.torontocodingcollective.oi.TButton;
import com.torontocodingcollective.oi.TGameController;
import com.torontocodingcollective.oi.TGameController_PS;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.oi.TRumbleManager;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.oi.TToggle;
import com.torontocodingcollective.oi.TTrigger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.RobotConst;
import robot.subsystems.CameraSubsystem.Camera;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

/**
 * Driver Controller (inherited from TOi)
 * 
 * Sticks: Right Stick = Drive Stick Left Stick = Drive Stick Right Stick Press
 * = Toggle PIDs Left Stick Press = Toggle Compressor
 * 
 * Buttons: Start Button = Reset Encoders and Gyro Back Button = Cancel any
 * Command
 * 
 * Bumpers/Triggers: Left Bumper = Turbo shift
 * 
 * POV: Any Angle = Rotate to the Pressed Angle
 * 
 */
public class OI extends TOi {

    private TGameController driverController    = new TGameController_PS(0);
    private TRumbleManager  driverRumble        = new TRumbleManager("Driver", driverController);

    private TGameController operatorController  = new TGameController_PS(1);  // Possible wrong port?
    private TRumbleManager  operatorRumble       = new TRumbleManager("Operator", operatorController);

    private TToggle         compressorToggle    = new TToggle(driverController, TStick.LEFT);
    private TToggle         speedPidToggle      = new TToggle(driverController, TStick.RIGHT);

    // The following toggles are shared between Driver and Operator
    private TToggle         hatchGrabberToggle  = new TToggle();
    private TToggle         hatchDeployerToggle = new TToggle();
    private TToggle         cargoHeightToggle   = new TToggle();
    private TToggle         cargoGateToggle     = new TToggle();
    private TToggle         cameraToggle        = new TToggle();

    private DriveSelector   driveSelector       = new DriveSelector();

    /* ***************************************************************************************
     * Drive Subsystem commands
     *****************************************************************************************/
    @Override
    public boolean getCancelCommand() {
        return driverController.getButton(TButton.BACK);
    }

    public boolean getCompressorEnabled() {
        return compressorToggle.get();
    }

    // DO NOT USE - Use the Driver and Operator ones below
    @Override
    public TStickPosition getDriveStickPosition(TStick stick) {
        return null;
    }
    
    public TStickPosition getDriverDriveStickPosition(TStick stick) {
        return driverController.getStickPosition(stick);
    }

    public TStickPosition getOperatorDriveStickPosition(TStick stick) {
        return operatorController.getStickPosition(stick);
    }

    @Override
    public boolean getReset() {
        return driverController.getButton(TButton.START);
    }

    @Override
    public int getRotateToHeading() {
        return driverController.getPOV();
    }

    /**
     * Get the selected drive type
     * 
     * @return {@link DriveControlType} selected on the SmartDashboard. The default
     *         drive type is {@link DriveControlType#ARCADE}
     */
    public DriveControlType getSelectedDriveType() {
        return driveSelector.getDriveControlType();
    }

    /**
     * Get the selected single stick side
     * 
     * @return {@link TStick} selected on the SmartDashboard. The default single
     *         stick drive is {@link TStick#RIGHT}
     */
    public TStick getSelectedSingleStickSide() {
        return driveSelector.getSingleStickSide();
    }

    @Override
    public boolean getSpeedPidEnabled() {
        return speedPidToggle.get();
    }

    public void setSpeedPidEnabled(boolean state) {
        speedPidToggle.set(state);
    }

    /* ***************************************************************************************
     * Hatch Subsystem commands
     *****************************************************************************************/
    public boolean getGrabberState() {
        // Looks at two buttons to determine this - look at updatePeriodic()
        return hatchGrabberToggle.get();
    }

    public boolean getDeployerState() {
        return hatchDeployerToggle.get();
    }

    /* ***************************************************************************************
     * Cargo Subsystem commands
     *****************************************************************************************/
    public boolean getHeightState() {
    	
    	// RM: For the cargo height, do you want this as a 
    	//     toggle or a press and hold to put the height up?
    	//     Using a press and hold feature for the height
    	//     will ensure that the height is always set low
    	//     when driving.
        return cargoHeightToggle.get();
    }

    public boolean getGateState() {
    	
    	// RM: For the cargo release gate, do you want this as a 
    	//     toggle or a press and hold to open the gate?
    	//     Opening the gate is more like a "shot", so I would
    	//     recommend not using a toggle.  The gate should
    	//     always be closed when not shooting.
        return cargoGateToggle.get();
    }

    /* ***************************************************************************************
     * Camera Subsystem 
     *****************************************************************************************/
    public Camera getCamera() {
    	if (cameraToggle.get()) {
    		return Camera.FRONT;
    	}
    	return Camera.REAR;
    }

    // Look at Driver and Operator toggles
    // If both are pressed, only toggle once
    private boolean getDualToggle(TButton button) {
        return (driverController.getButton(button) || operatorController.getButton(button));
    }

    private boolean getDualToggle(TTrigger trigger) {
        return (driverController.getButton(trigger) || operatorController.getButton(trigger));
    }
    
    public boolean isDriverMoving() {
        double[] axes  = new double[4];
        axes[0]        = driverController.getAxis(TStick.LEFT, TAxis.X);
        axes[1]        = driverController.getAxis(TStick.LEFT, TAxis.Y);
        axes[2]        = driverController.getAxis(TStick.RIGHT, TAxis.X);
        axes[3]        = driverController.getAxis(TStick.RIGHT, TAxis.Y);

        for (int i = 0; i < axes.length; i++) {
            if (axes[i] >= RobotConst.JOYSTICK_AXIS_MAX_MOVEMENT_ERROR) {
                return true;
            }
        }
        // No moving sticks were found
        return false;
    }

    public boolean isOperatorMoving() {
        double[] axes  = new double[4];
        axes[0]        = operatorController.getAxis(TStick.LEFT, TAxis.X);
        axes[1]        = operatorController.getAxis(TStick.LEFT, TAxis.Y);
        axes[2]        = operatorController.getAxis(TStick.RIGHT, TAxis.X);
        axes[3]        = operatorController.getAxis(TStick.RIGHT, TAxis.Y);

        for (int i = 0; i < axes.length; i++) {
            if (axes[i] >= RobotConst.JOYSTICK_AXIS_MAX_MOVEMENT_ERROR) {
                return true;
            }
        }
        // No moving sticks were found
        return false;
    }

    /* ***************************************************************************************
     * OI Init and Periodic 
     *****************************************************************************************/
    public void init() {
        compressorToggle.set(true);
        speedPidToggle.set(false);
        // XXX: Subject to possible future modifications, as it's yet to be decided how the grabber should start out.
        hatchGrabberToggle.set(false);
        hatchDeployerToggle.set(false);
        // XXX: Will change as well - height should start high, gate should start closed
        cargoHeightToggle.set(false);
        cargoGateToggle.set(false);
    }

    @Override
    public void updatePeriodic() {

    	// Update the Controller Rumbles
        driverRumble.updatePeriodic();
        operatorRumble.updatePeriodic();

        // Update all Toggles
        compressorToggle.updatePeriodic();
        speedPidToggle.updatePeriodic();

        // ********************
        // Update dual toggles
        // ********************
        hatchDeployerToggle.updatePeriodic(getDualToggle(TButton.X_SYMBOL));
        cargoHeightToggle.updatePeriodic(getDualToggle(TTrigger.RIGHT));
        cargoGateToggle.updatePeriodic(getDualToggle(TTrigger.LEFT));
        cameraToggle.updatePeriodic(getDualToggle(TButton.CIRCLE));
        // Update hatch grabber toggle by looking at two buttons
        // Will not change if both buttons are pressed
        // If only Close button pressed
        if (getDualToggle(TButton.LEFT_BUMPER) && !getDualToggle(TButton.RIGHT_BUMPER)) {
            hatchGrabberToggle.set(false);  // XXX: Assumes false = closed
        }
        else if (!getDualToggle(TButton.LEFT_BUMPER) && getDualToggle(TButton.RIGHT_BUMPER)) {
            hatchGrabberToggle.set(true);  // XXX: Assumes true = opened
        }

        
        // Update all SmartDashboard values
        SmartDashboard.putString("Driver Controller", driverController.toString());
        SmartDashboard.putString("Operator Controller", operatorController.toString());
    }
}
