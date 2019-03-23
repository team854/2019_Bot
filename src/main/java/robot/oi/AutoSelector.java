package robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSelector {

    public static SendableChooser<String> robotStartPosition;

    public static final String            ROBOT_LEFT   = "Robot Left";
    public static final String            ROBOT_CENTER = "Robot Center";
    public static final String            ROBOT_RIGHT  = "Robot Right";

    public static SendableChooser<String> pattern;

    public static final String            PATTERN_CARGO_HATCH    = "Cargo Hatch";
    public static final String            PATTERN_CARGO_DRIVE_UP = "Cargo Drive Up";
    public static final String            PATTERN_CARGO_DELIVER_AND_GO 
    															 = "Cargo Deliver and Go";
    public static final String            PATTERN_NONE           = "None";

    public static SendableChooser<String> delayTime;
    
    public static final String            DELAY_TIME_0        = "0";
    public static final String            DELAY_TIME_3        = "3";
    public static final String            DELAY_TIME_5        = "5";
    public static final String            DELAY_TIME_9        = "9";

    static {

        // Robot Position Options
        robotStartPosition = new SendableChooser<String>();
        robotStartPosition.addOption(ROBOT_LEFT, ROBOT_LEFT);
        robotStartPosition.setDefaultOption(ROBOT_CENTER, ROBOT_CENTER);
        robotStartPosition.addOption(ROBOT_RIGHT, ROBOT_RIGHT);

        SmartDashboard.putData("Robot Start", robotStartPosition);

        // Robot Pattern Options
        pattern = new SendableChooser<String>();
        pattern.setDefaultOption(PATTERN_CARGO_HATCH, PATTERN_CARGO_HATCH);
        pattern.addOption(PATTERN_CARGO_DRIVE_UP, PATTERN_CARGO_DRIVE_UP);
        pattern.addOption(PATTERN_CARGO_DELIVER_AND_GO, PATTERN_CARGO_DELIVER_AND_GO);
        pattern.addOption(PATTERN_NONE, PATTERN_NONE);
        
        SmartDashboard.putData("Auto Pattern", pattern);

        // Delay time at beginning of match
        delayTime = new SendableChooser<String>();
        delayTime.setDefaultOption(DELAY_TIME_0, DELAY_TIME_0);
        delayTime.addOption(DELAY_TIME_3, DELAY_TIME_3);
        delayTime.addOption(DELAY_TIME_5, DELAY_TIME_5);
        delayTime.addOption(DELAY_TIME_9, DELAY_TIME_9);

        SmartDashboard.putData("Delay Time", delayTime);
    }

    /**
     * Get the auto pattern.
     * 
     * @return "Straight" or "Box"
     */
    public static String getPattern() {

    	if (pattern.getSelected() == null) {
    		System.out.println("Pattern not chosen - overriding to " + PATTERN_CARGO_HATCH);
    		return PATTERN_CARGO_HATCH;
    	}
        return pattern.getSelected();
    }
    
    /**
     * Get the robot starting position on the field.
     * 
     * @return 'L' for left, 'R' for right or 'C' for center
     */
    public static String getRobotStartPosition() {

    	if (robotStartPosition.getSelected() == null) {
    		System.out.println("Start position not chosen - overriding to " + ROBOT_RIGHT);
    		return ROBOT_RIGHT;
    	}
        return robotStartPosition.getSelected();
    }

    /**
     * Get the delay before moving
     * 
     * @return double delay time
     */
    public static double getDelayTime() {

    	if (delayTime.getSelected() == null) {
    		System.out.println("Delay Time not chosen - overriding to " + DELAY_TIME_0);
    		return 0;
    	}
        return Double.valueOf(delayTime.getSelected());
    }

    public static void init() {}
}
