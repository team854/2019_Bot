package robot.oi;

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

    private TToggle         compressorToggle    = new TToggle(driverController, TStick.LEFT);
    private TToggle         speedPidToggle      = new TToggle(driverController, TStick.RIGHT);
    private TToggle         hatchGrabberToggle  = new TToggle(driverController, TButton.TRIANGLE);
    private TToggle         hatchDeployerToggle = new TToggle(driverController, TButton.X_SYMBOL);
    private TToggle         cargoHeightToggle   = new TToggle(driverController, TTrigger.RIGHT);
    private TToggle         cargoGateToggle     = new TToggle(driverController, TTrigger.LEFT);
    private TToggle         cameraToggle        = new TToggle(driverController, TButton.CIRCLE);

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

    @Override
    public TStickPosition getDriveStickPosition(TStick stick) {
        return driverController.getStickPosition(stick);
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
    	
    	// RM: The hatch currently has two buttons in the controller map 
    	//     diagram - one for grab, and one for release.  
    	//     Having two buttons will hopefully help lower the accidental
    	//     release of the hatch (by double hitting the toggle)
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
    
    /* ***************************************************************************************
     * OI Init and Periodic 
     *****************************************************************************************/
    public void init() {
        compressorToggle.set(true);
        speedPidToggle.set(false);
        // Subject to possible future modifications, as it's yet to be decided how the grabber should start out.
        hatchGrabberToggle.set(false);
        hatchDeployerToggle.set(false);
        // Will change as well - height should start high, gate should start closed
        cargoHeightToggle.set(false);
        cargoGateToggle.set(false);
    }

    @Override
    public void updatePeriodic() {

    	// Update the Controller Rumbles
        driverRumble.updatePeriodic();

        // Update all Toggles
        compressorToggle.updatePeriodic();
        speedPidToggle.updatePeriodic();
        hatchGrabberToggle.updatePeriodic();
        hatchDeployerToggle.updatePeriodic();
        cargoHeightToggle.updatePeriodic();
        cargoGateToggle.updatePeriodic();
        cameraToggle.updatePeriodic();

        // Update all SmartDashboard values
        SmartDashboard.putString("Driver Controller", driverController.toString());
    }
}
