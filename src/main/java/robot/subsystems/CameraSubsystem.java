package robot.subsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
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
	private NetworkTable            table   = inst.getTable("GRIP/myContoursReport");
	private NetworkTableEntry       centerX	= table.getEntry("centerX");
	private NetworkTableEntry       centerY	= table.getEntry("centerY");
	private double[]				centerXArray;
	
    public CameraSubsystem() {

    	// The cameraFeed is the switchable camera.
    	// Since it is added to the CameraServer first, it gets the IP address:
    	// http://roborio-team-frc.local:1181/?action=stream
    	//
    	// Use the above IP address to display the switching feed in the 
    	// SmartDashboard by adding an MJPEG Stream viewer at that URL.
    	//
    	// In GRIP, the selected camera source can be used for vision tracking
    	// by using an IP camera at the above URL.
    	//
    	// NOTE: There is no way to display the switched view in the current
    	//       Shuffleboard because the "Selected Camera" does not appear
    	//       as a camera source in the Shuffleboard.
        cameraFeed = CameraServer.getInstance().addServer("Selected Camera");

        // Start the front camera.
        // The URL of the front camera will be 
    	// http://roborio-team-frc.local:1182/?action=stream
        //
        // NOTE:  If there are no listeners on the 1182 port, the Camera Server will
        //        shift the port number of the frontCamera to 1181 whenever it is selected
        //        as the source, and will return to port 1182 when the selection is set to 
        //        another camera.
        frontCamera = CameraServer.getInstance().startAutomaticCapture("Front Camera", 1);
        frontCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 30);
        frontCamera.setExposureManual(40);
        
        // Start the Rear camera.
        // The URL of the front camera will be 
    	// http://roborio-team-frc.local:1183/?action=stream
        rearCamera  = CameraServer.getInstance().startAutomaticCapture("Rear Camera", 0);
        rearCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 30);
        rearCamera.setExposureManual(40);

        // Set the starting feed to the front camera.
        cameraFeed.setSource(frontCamera);
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
	
	private boolean targetsFound() {
	    
	    // NOTE: The targets cannot be found if the robot is moving.
	    //
        //       Since the camera lags behind the robot,   
        //       any targets found when the robot is moving will be 
        //       in the wrong position.  
        //
        //       The robot must be still for about 200ms to guarantee
        //       the targets.
        if (Robot.driveSubsystem.getStoppedTime() < 0.15) {
            return false;
        }
        
		// Use alignmentNeeded() to check whether alignment should happen or not
		if (centerXArray != null && centerXArray.length == 2) {
			return true;
		}
		return false;
	}

	private double getTargetAveragesX() {
		
		if (!targetsFound()) {
			return 0;
		}
		
		// Check targetsFound() before using
		return (centerXArray[0] + centerXArray[1]) / 2.0;
	}

	private double getRawDegreesOff() {
		// Calculates degrees off and doesn't correct for error margins
		// Check targetsFound() before using
		// But getDegreesOff() should be used instead

		/* 
		To better visualize the below math, paste the following into https://www.desmos.com/scientific :
		\tan^{-1}\left(\frac{\frac{x-320}{320}\left(146.25\right)}{307}\right)
		x represents the avgX variable
		- or + is left or right
		146.25: half the camera view field in cm - when we measured
		307 distance from camera to 146.25 cm viewfield line
		Field of view is 51 degrees
		Microsoft Skype LifeCam HD 3000
		*/

		// Last multiplication part is a hack
		return Math.toDegrees(Math.atan((((getTargetAveragesX()-RobotConst.VISION_CENTER_X)/320) * 146.25) / 307));
	}

	public double getDegreesOff() {
		// Calculates degrees off and considers error margins
		// Check targetsFound() before using
		// Make sure to add this to the current gyro angle

		// Users should use alignmentNeeded before using - this is just in case
		if (!targetsFound()) {
			return 0;
		}

		return getRawDegreesOff();
	}

	public boolean alignmentNeeded() {
		// Includes error margin correction

	    if (targetsFound() && Math.abs(getDegreesOff()) > RobotConst.VISION_AVG_X_ERROR_MARGIN) {
			return true;
		}

		return false;
	}
	
    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        
		// Setup the vision targets array so we use the same values each loop
		if (centerX != null) {
			centerXArray = centerX.getDoubleArray(new double[] {});
			double[] centerYArray = centerY.getDoubleArray(new double[] {});
			if (centerXArray.length != centerYArray.length) {
				centerXArray = null;
			}
			else {
				filterCenterXArray(centerYArray);
				if (centerXArray != null) {
					if (centerXArray.length == 0) {
						centerXArray = null;
					}
				}
			}
		} else {
			centerXArray = null;
		}
		
    	SmartDashboard.putString("Camera", curCamera.toString());
    	SmartDashboard.putBoolean("Targets Found", targetsFound());
    	SmartDashboard.putNumber("Target Center", getTargetAveragesX());
    	SmartDashboard.putBoolean("On Target", targetsFound() && !alignmentNeeded());
        SmartDashboard.putNumber("Degrees Off", getDegreesOff());
    }

    @Override
    protected void initDefaultCommand() {
    	setDefaultCommand(new DefaultCameraCommand());
    }
    
    private void filterCenterXArray(double[] centerYArray) {

    	if (centerXArray == null) {
    		return;
    	}
    	
    	// Filter the array to only use the values that are closest to the middle
    	List<Double> xValues = new ArrayList<>();
    	for (int i=0; i<centerXArray.length; i++) {
    		if (centerYArray[i] > 200) {
    			xValues.add(centerXArray[i]);
    		}
    	}
    	
    	if (xValues.size() < 2) {
    		centerXArray = null;
    		return;
    	}
    	
    	Collections.sort(xValues);

    	// Get the closest index to center
    	double minDistance = 300;
    	int minIndex = -1;
    	
    	for (int i=0; i<xValues.size(); i++) {
    		
    		double distance = Math.abs(xValues.get(i) - RobotConst.VISION_CENTER_X);

    		if (distance < minDistance) {
    			minDistance = distance;
    			minIndex = i;
    		}
    	}
    	
    	// Once the minimum is found, find the minimum distance of the index
    	// over the min distance index, or the index under the min distance index.
    	
    	int nextClosestIndex = -1;
    	
    	if (minIndex == 0) {
    		nextClosestIndex = 1;
    	}
    	else if (minIndex == xValues.size()-1) {
    		nextClosestIndex = xValues.size()-2;
    	}
    	else {
    		double dist1 = Math.abs(xValues.get(minIndex-1) - RobotConst.VISION_CENTER_X);
    		double dist2 = Math.abs(xValues.get(minIndex+1) - RobotConst.VISION_CENTER_X);
    		if (dist1 < dist2) {
    			nextClosestIndex = minIndex - 1;
    		}
    		else {
    			nextClosestIndex = minIndex + 1;
    		}
    	}
    	
    	centerXArray = new double[] {
    			xValues.get(minIndex), xValues.get(nextClosestIndex) };
    }

}
