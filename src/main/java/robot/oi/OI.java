package robot.oi;

import com.torontocodingcollective.oi.TButton;
import com.torontocodingcollective.oi.TGameController;
import com.torontocodingcollective.oi.TGameController_PS;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.oi.TRumbleManager;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.oi.TToggle;

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
    private TToggle         hatchDeployerToggle = new TToggle(driverController, TButton.CIRCLE);
    private TToggle         cameraToggle        = new TToggle(driverController, TButton.RIGHT_BUMPER);

    private DriveSelector   driveSelector       = new DriveSelector();

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

    public boolean getGrabberState() {
        return hatchGrabberToggle.get();
    }

    public boolean getDeployerState() {
        return hatchDeployerToggle.get();
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

    public boolean getTurboOn() {
        return driverController.getButton(TButton.LEFT_BUMPER);
    }

    public void init() {
        compressorToggle.set(true);
        speedPidToggle.set(false);
        //Subject to possible future modifications, as it's yet to be decided how the grabber
        //should start out.
        hatchGrabberToggle.set(false);
        hatchDeployerToggle.set(false);
    }

    public void setSpeedPidEnabled(boolean state) {
        speedPidToggle.set(state);
    }

    public Camera getCamera() {
    	if (cameraToggle.get()) {
    		return Camera.FRONT;
    	}
    	return Camera.REAR;
    }
    
    @Override
    public void updatePeriodic() {

        // Update all Toggles
        compressorToggle.updatePeriodic();
        speedPidToggle.updatePeriodic();
        driverRumble.updatePeriodic();
        hatchGrabberToggle.updatePeriodic();
        hatchDeployerToggle.updatePeriodic();
        cameraToggle.updatePeriodic();

        // Update all SmartDashboard values
        SmartDashboard.putBoolean("Speed PID Toggle", getSpeedPidEnabled());
        SmartDashboard.putBoolean("Compressor Toggle", getCompressorEnabled());
        SmartDashboard.putString("Driver Controller", driverController.toString());
        SmartDashboard.putString("Camera", getCamera().toString());
    }
}
