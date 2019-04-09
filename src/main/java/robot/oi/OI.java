package robot.oi;

import com.torontocodingcollective.oi.TButton;
import com.torontocodingcollective.oi.TGameController;
import com.torontocodingcollective.oi.TGameController_Logitech;
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
    private static final boolean GRABBER_OPEN           = true;
    private static final boolean GRABBER_CLOSED         = false;
    
    private TToggle         hatchDeployerToggle         = new TToggle();
    private TToggle         cameraToggle                = new TToggle();
    private TToggle         cargoHeightToggle           = new TToggle();
    private TToggle         cargoIntakeToggle           = new TToggle();

    private DriveSelector   driveSelector               = new DriveSelector();

    private TToggle         autoAlignToggle             = new TToggle();

    /* ***************************************************************************************
     * Rumble commands
     *****************************************************************************************/
    public void setOperatorRumble(boolean on) {
    	if (on) {
    		operatorRumble.rumbleOn();
    	}
    	else {
    		operatorRumble.rumbleOff();
    	}
    }
    
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

    
    public boolean getHopUp(){
     
        return driverController.getButton(TButton.B);
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

    // Look at Driver and Operator toggles
    // If both are pressed, only toggle once
    private boolean getDualToggle(TButton button) {
        return (driverController.getButton(button) || operatorController.getButton(button));
    }

    private boolean getDualToggle(TTrigger trigger) {
        return (driverController.getButton(trigger) || operatorController.getButton(trigger));
    }

    public boolean isDriverActive() {
        return driverController.isUserActive();
    }

    public boolean isDriverDriving() {
        return driverController.isStickActive(TStick.LEFT) || driverController.isStickActive(TStick.RIGHT);
    }

    public boolean isOperatorDriving() {
        return operatorController.isStickActive(TStick.LEFT) || operatorController.isStickActive(TStick.RIGHT);
    }

    public boolean isOperatorActive() {
        return operatorController.isUserActive();
    }

    public boolean getSlightLeft() {
        return operatorController.getButton(TTrigger.LEFT);
    }

    public boolean getSlightRight() {
        return operatorController.getButton(TTrigger.RIGHT);
    }    
    
    public boolean getAutoAlignSelected() {
    	return autoAlignToggle.get();
    }

    public void setAutoAlign(boolean align) {
    	autoAlignToggle.set(align);
    }

    public void disableAutoAlign() {
        autoAlignToggle.set(false);
    }

    public boolean getAutoPart2() {
    	return operatorController.getButton(TButton.BACK);
    }
    
    public boolean getDeployWedge() {
        return driverController.getButton(TButton.Y);
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
    	return cargoHeightToggle.get();
    }

    public void setHeightState(boolean state) {
    	cargoHeightToggle.set(state);
    }

//    public boolean getIntakeState() {
//    	return (driverController.getButton(TTrigger.LEFT) || operatorController.getButton(TButton.X));
//    }
    
    public void stopCargoIntake() {
    	cargoIntakeToggle.set(false);
    }

    public boolean getCargoIntake() {
    	return cargoIntakeToggle.get();
    }
    
    public boolean getCargoEject() {
    	return operatorController.getButton(TButton.X);
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

    /* ***************************************************************************************
     * OI Init and Periodic 
     *****************************************************************************************/
    public void init() {
        compressorToggle.set(true);
        speedPidToggle.set(true);
        // Open is true
        hatchGrabberToggle.set(GRABBER_OPEN);
        // Up is false
        hatchDeployerToggle.set(false);
        // True is front
        cameraToggle.set(true);
        // Cargo Height is down
        cargoHeightToggle.set(false);
        // Auto Align is set to false - the robot cannot move by default
        autoAlignToggle.set(false);
        // Cargo Intake set to false - not intaking
        cargoIntakeToggle.set(false);
    }

    @Override
    public void updatePeriodic() {

    	// Update the Controller Rumbles
        driverRumble.updatePeriodic();
        operatorRumble.updatePeriodic();

        // Update all Toggles
        compressorToggle.updatePeriodic();
        speedPidToggle.updatePeriodic();
        cargoIntakeToggle.updatePeriodic(
        		   operatorController.getButton(TButton.START)
        		|| driverController.getButton(TTrigger.LEFT));

        autoAlignToggle.updatePeriodic(operatorController.getButton(TStick.RIGHT));

        // ********************
        // Update dual toggles
        // ********************
        hatchDeployerToggle.updatePeriodic(getDualToggle(TButton.A));
        cameraToggle.updatePeriodic(operatorController.getButton(TButton.B));
        cargoHeightToggle.updatePeriodic(operatorController.getButton(TButton.Y));
        // Update hatch grabber toggle by looking at two buttons
        // Will not change if both buttons are pressed
        // Open (Grabbed) should always be checked first
        if (!getDualToggle(TButton.LEFT_BUMPER) && getDualToggle(TButton.RIGHT_BUMPER)) {
            hatchGrabberToggle.set(GRABBER_OPEN);  // XXX: Assumes true = opened/grabbed
        }
        else if (getDualToggle(TButton.LEFT_BUMPER) && !getDualToggle(TButton.RIGHT_BUMPER)) {
            hatchGrabberToggle.set(GRABBER_CLOSED);  // XXX: Assumes false = closed/released
        }

        // Update all SmartDashboard values
        SmartDashboard.putString("Driver Controller", driverController.toString());
        SmartDashboard.putString("Operator Controller", operatorController.toString());
        SmartDashboard.putBoolean("cargoHeightToggle", cargoHeightToggle.get());
        SmartDashboard.putString("CameraToggle", getCamera().toString());
        SmartDashboard.putBoolean("AutoAlignToggle", autoAlignToggle.get());
    }
}
