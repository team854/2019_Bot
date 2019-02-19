package robot.oi;

import com.torontocodingcollective.oi.TButton;
import com.torontocodingcollective.oi.TGameController;
import com.torontocodingcollective.oi.TGameController_Logitech;
import com.torontocodingcollective.oi.TGameController_PS;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.oi.TRumbleManager;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.oi.TToggle;
import com.torontocodingcollective.oi.TTrigger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private final   TGameController driverController    = new TGameController_Logitech(0);
    private         TRumbleManager  driverRumble        = new TRumbleManager("Driver", driverController);

    private final   TGameController operatorController  = new TGameController_Logitech(1);
    private         TRumbleManager  operatorRumble      = new TRumbleManager("Operator", operatorController);

    private      TToggle            compressorToggle    = new TToggle(driverController, TStick.LEFT);
    private      TToggle            speedPidToggle      = new TToggle(driverController, TStick.RIGHT);

    // The following toggles are shared between Driver and Operator
    private TToggle         hatchGrabberToggle          = new TToggle();
    private TToggle         hatchDeployerToggle         = new TToggle();
    private TToggle         cargoGateToggle             = new TToggle();
    private boolean         cargoHeightOverride         = false;
    private boolean         cargoHeightOverrideState    = false;
    private TToggle         cameraToggle                = new TToggle();

    private DriveSelector   driveSelector               = new DriveSelector();

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
    	
    	int operatorControllerPOV = operatorController.getPOV();
    	int driverControllerPOV = driverController.getPOV();
    	
    	if (driverControllerPOV != -1) {
    		return driverControllerPOV;
    	}

    	if (operatorControllerPOV != -1) {
    		return operatorControllerPOV;
    	}
    	
        return -1;
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

    public void setGrabberState(boolean state) {
        hatchGrabberToggle.set(state);
    }

    public boolean getDeployerState() {
        return hatchDeployerToggle.get();
    }

    public void setDeployerState(boolean state) {
        hatchDeployerToggle.set(state);
    }


    /* ***************************************************************************************
     * Cargo Subsystem commands
     *****************************************************************************************/
    public boolean getHeightState() {
        // Press and hold from the controller
        
        // Allow software to override what the controller(s) says
        if (cargoHeightOverride) {
            return cargoHeightOverrideState;
        }
        return (driverController.getButton(TTrigger.RIGHT) || operatorController.getButton(TButton.Y));
    }

    public void overrideHeightState(boolean state) {
        // Overrides whatever the controllers are doing
        cargoHeightOverride = true;
        cargoHeightOverrideState = state;
    }

    public void releaseHeightState() {
        // Stops overriding the height state
        cargoHeightOverride = false;
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

    public boolean getAlignButton() {
        return (driverController.getButton(TButton.SQUARE) || operatorController.getButton(TStick.RIGHT));
    }
    

    public boolean isDriverActive() {
        return driverController.isUserActive();
    }

    public boolean isOperatorActive() {
        return operatorController.isUserActive();
    }

    public boolean getWedgeState() {
        return driverController.getButton(TButton.TRIANGLE);
    }

    public boolean getSlightLeft() {
        return operatorController.getButton(TTrigger.LEFT);
    }

    public boolean getSlightRight() {
        return operatorController.getButton(TTrigger.RIGHT);
    }    

    /* ***************************************************************************************
     * OI Init and Periodic 
     *****************************************************************************************/
    public void init() {
        compressorToggle.set(true);
        speedPidToggle.set(true);
        // Open is true
        hatchGrabberToggle.set(true);
        // Up is false
        hatchDeployerToggle.set(false);
        // False is closed
        cargoGateToggle.set(false);
        // True is front
        cameraToggle.set(true);
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
        cargoGateToggle.updatePeriodic(driverController.getButton(TTrigger.LEFT) 
                                            || operatorController.getButton(TButton.X));
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
        SmartDashboard.putBoolean("cargoHeightToggle", getDualToggle(TTrigger.RIGHT));
        SmartDashboard.putBoolean("cargoGateToggle", cargoGateToggle.get());
        SmartDashboard.putString("Camera", getCamera().toString());
    }
}
