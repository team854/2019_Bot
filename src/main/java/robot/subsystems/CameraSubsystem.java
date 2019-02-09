package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.commands.*;

/**
 *
 */
public class CameraSubsystem extends TSubsystem {

	public enum Camera { FRONT, REAR, NONE };
	
	private UsbCamera frontCamera;
	private UsbCamera rearCamera;
	private VideoSink cameraFeed;
	
	private Camera curCamera = Camera.NONE;
	
    public CameraSubsystem() {

        //Uncomment this line to start a USB camera feed
        frontCamera = CameraServer.getInstance().startAutomaticCapture(0);
        rearCamera  = CameraServer.getInstance().startAutomaticCapture(1);

        cameraFeed = CameraServer.getInstance().getServer();
        curCamera = Camera.FRONT;
    }

    @Override
    public void init() {
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
    	}
    	
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
