package robot.oi;

import com.torontocodingcollective.oi.TStick;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSelector {

    public SendableChooser<String> driveControlType;
    public SendableChooser<String> singleStickSide;

    public static final String     DRIVE_CONTROL_TYPE_ARCADE       = "Arcade";
    public static final String     DRIVE_CONTROL_TYPE_TANK         = "Tank";
    public static final String     DRIVE_CONTROL_TYPE_SINGLE_STICK = "Single Stick";

    public static final String     SINGLE_STICK_LEFT               = "Left";
    public static final String     SINGLE_STICK_RIGHT              = "Right";

    {
        // Drive Type
        driveControlType = new SendableChooser<>();
        driveControlType.addDefault(DRIVE_CONTROL_TYPE_ARCADE, DRIVE_CONTROL_TYPE_ARCADE);
        driveControlType.addObject(DRIVE_CONTROL_TYPE_TANK, DRIVE_CONTROL_TYPE_TANK);
        driveControlType.addObject(DRIVE_CONTROL_TYPE_SINGLE_STICK, DRIVE_CONTROL_TYPE_SINGLE_STICK);

        SmartDashboard.putData("Drive Type", driveControlType);

        // Single Stick Side
        singleStickSide = new SendableChooser<>();
        singleStickSide.addDefault(SINGLE_STICK_RIGHT, SINGLE_STICK_RIGHT);
        singleStickSide.addObject(SINGLE_STICK_LEFT, SINGLE_STICK_LEFT);

        SmartDashboard.putData("Single Stick Side", singleStickSide);
    }

    /**
     * Get the Drive Type
     */
    public DriveControlType getDriveControlType() {

        switch (driveControlType.getSelected()) {
        case DRIVE_CONTROL_TYPE_TANK:
            return DriveControlType.TANK;
        case DRIVE_CONTROL_TYPE_SINGLE_STICK:
            return DriveControlType.SINGLE_STICK;
        case DRIVE_CONTROL_TYPE_ARCADE:
        default:
            return DriveControlType.ARCADE;
        }
    }

    /**
     * Get the Single Stick side
     */
    public TStick getSingleStickSide() {

        switch (singleStickSide.getSelected()) {
        case SINGLE_STICK_LEFT:
            return TStick.LEFT;
        case SINGLE_STICK_RIGHT:
        default:
            return TStick.RIGHT;
        }
    }

    public void updatePeriodic() {
        SmartDashboard.putString("Drive Control", String.valueOf(getDriveControlType()));
        SmartDashboard.putString("Single Stick Side", String.valueOf(getSingleStickSide()));
    }
}
