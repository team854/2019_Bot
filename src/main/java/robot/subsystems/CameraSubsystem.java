package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Robot;
import robot.RobotConst;
import robot.commands.camera.DefaultCameraCommand;

/**
 *
 */
public class CameraSubsystem extends TSubsystem {

	public enum Camera { FRONT, REAR, NONE };
	
	private UsbCamera frontCamera;
	private UsbCamera rearCamera;
	private VideoSink cameraFeed;
	
	private Camera curCamera = Camera.NONE;

	// NetworkTable stuff
	private NetworkTableInstance    inst    = NetworkTableInstance.getDefault();
	private NetworkTable            table   = inst.getTable("myContoursReport");
	private NetworkTableEntry       centerX; //= table.getEntry("centerX");
	/*NetworkTableEntry       centerY = table.getEntry("centerY");
	NetworkTableEntry       area    = table.getEntry("area");
	NetworkTableEntry       width   = table.getEntry("width");
	NetworkTableEntry       height  = table.getEntry("height");*/
	
    public CameraSubsystem() {

        //Uncomment this line to start a USB camera feed
        frontCamera = CameraServer.getInstance().startAutomaticCapture(1);
        rearCamera  = CameraServer.getInstance().startAutomaticCapture(0);

        cameraFeed = CameraServer.getInstance().getServer();
        curCamera = Camera.FRONT;
    }

    @Override
    public void init() {
		setCameraFeed(curCamera);
    }

    public void setCameraFeed(Camera camera) {
    	
    	switch(camera) {
    	case FRONT:
    		if (curCamera == Camera.REAR) {
    			if (frontCamera != null) {
    				cameraFeed.setSource(frontCamera);
    				curCamera = Camera.FRONT;
    			}
    		}
    		break;
    	case REAR:
    		if (curCamera == Camera.FRONT) {
    			if (rearCamera != null) {
	    			cameraFeed.setSource(rearCamera);
	    			curCamera = Camera.REAR;
    			}
    		}
    		break;
    	default:
    	    break;
    	}
    	
	}
	
	public Camera getCurrentCamera() {
		return curCamera;
	}
	


	public boolean targetsFound() {
	    
	    // NOTE: The targets cannot be found if the robot is moving.
	    //
        //       Since the camera lags behind the robot,   
        //       any targets found when the robot is moving will be 
        //       in the wrong position.  
        //
        //       The robot must be still for about 200ms to guarantee
        //       the targets.
        if (Robot.driveSubsystem.getStoppedTime() < 0.2) {
            return false;
        }
        
		// Use alignmentNeeded() to check whether alignment should happen or not
		
		centerX = table.getEntry("centerX");
		try {
			// Check if anything is working
			if (centerX != null) {
				if (centerX.getNumberArray(null) != null) {
					// Check if only two contours can be seen
				    // NOTE: The getNumberArray is not guaranteed to 
				    //       be null here.  The array is updated in 
				    //       another thread.  This code could throw
				    //       a null pointer exception.
					if (centerX.getNumberArray(null).length == 2) {
						return true;
					}
				}
			}
		} catch (NullPointerException e) {
			return false;
		}

		return false;
	}

	public double getTargetAveragesX() {
		// Check targetsFound() before using

	    // NOTE: This code can be problematic and can cause a null pointer exception.
	    //       The array that was used in targetsFound is updated in another thread
	    //       and could have changed by this point in time.  The length of the 
	    //       array can now be a different length and you are not guaranteed to have
	    //       exactly two elements.  You want to save the 
	    //       array and never get a new array until the next time you want to check.
	    //
	    //       Move the centerX.getNumberArray() to the updatePeriodic, and always 
	    //       reference the same array for this loop.
		return centerX.getNumberArray(null)[0].doubleValue() + centerX.getNumberArray(null)[1].doubleValue() / 2;
	}

	public double getRawDegreesOff() {
		// Calculates degrees off and doesn't correct for error margins
		// Check targetsFound() before using
		// But getDegreesOff() should be used instead

		/* To better visualize the below math, paste the following into https://www.desmos.com/scientific :
		\tan^{-1}\left(\frac{\frac{x-320}{320}\left(146.25\right)}{307}\right)
		x represents the avgX variable
		- or + is left or right
		146.25: half the camera view field in cm - when we measured
		307 distance from camera to 146.25 cm viewfield line
		Field of view is 51 degrees
		Microsoft Skype LifeCam HD 3000
		*/
		
		return Math.toDegrees(Math.atan((((getTargetAveragesX()-320)/320) * 146.25) / 307));
	}

	public double getDegreesOff() {
		// Calculates degrees off and considers error margins
		// Check targetsFound() before using
		// Make sure to add this to the current gyro angle

		if (Math.abs(getRawDegreesOff()) < RobotConst.VISION_AVG_X_ERROR_MARGIN) {
			return 0;
		}

		return getRawDegreesOff();
	}

	public boolean alignmentNeeded() {
		// Includes error margin correction

	    if (targetsFound() && getDegreesOff() != 0) {
			return true;
		}

		return false;
	}
	
    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        
    	SmartDashboard.putString("Camera", curCamera.toString());
    }

    @Override
    protected void initDefaultCommand() {
    	setDefaultCommand(new DefaultCameraCommand());
    }

}
