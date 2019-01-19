
package robot;

import java.util.ArrayList;
import java.util.List;

import com.torontocodingcollective.subsystem.TSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import robot.commands.AutonomousCommand;
import robot.oi.AutoSelector;
import robot.oi.OI;
import robot.subsystems.CameraSubsystem;
import robot.subsystems.CanDriveSubsystem;
import robot.subsystems.PneumaticsSubsystem;
import robot.subsystems.PowerSubsystem;
import robot.subsystems.PwmDriveSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    public static final List<TSubsystem>    subsystemLs         = new ArrayList<TSubsystem>();

    public static final PwmDriveSubsystem   driveSubsystem      = new PwmDriveSubsystem();
//    public static final PneumaticsSubsystem pneumaticsSubsystem = new PneumaticsSubsystem();
 //   public static final PowerSubsystem      powerSubsystem      = new PowerSubsystem();
    public static final CameraSubsystem     cameraSubsystem     = new CameraSubsystem();

    public static OI                        oi;

    private Command                         autoCommand;

    // Add all of the subsystems to the subsystem list
    static {
        subsystemLs.add(driveSubsystem);
    //    subsystemLs.add(pneumaticsSubsystem);
     //   subsystemLs.add(powerSubsystem);
        subsystemLs.add(cameraSubsystem);
    }

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {

        oi = new OI();
        oi.init();

        for (TSubsystem subsystem : subsystemLs) {
            subsystem.init();
        }
        
        AutoSelector.init();
    }

    /**
     * This function is called once each time the robot enters Disabled mode. You
     * can use it to reset any subsystem information you want to clear when the
     * robot is disabled.
     */
    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {

        oi.updatePeriodic();

        Scheduler.getInstance().run();
        updatePeriodic();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable chooser
     * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
     * remove all of the chooser code and uncomment the getString code to get the
     * auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons to
     * the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() {

        // Turn on the drive pids for auto
        Robot.oi.setSpeedPidEnabled(true);
        driveSubsystem.enableSpeedPids();

        // Reset the gyro and the encoders
        Robot.driveSubsystem.setGyroAngle(0);
        Robot.driveSubsystem.resetEncoders();

        // Initialize the robot command after initializing the game data
        // because the game data will be used in the auto command.
        autoCommand = new AutonomousCommand();
        autoCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {

        // Update the OI before running the commands
        oi.updatePeriodic();

        Scheduler.getInstance().run();

        // Update all subsystems after running commands
        updatePeriodic();
    }

    @Override
    public void teleopInit() {

        if (autoCommand != null) {
            autoCommand.cancel();
        }

        // Turn off the drive PIDs
        // Save the battery in teleop by using the
        // SpeedController built in braking.
        Robot.oi.setSpeedPidEnabled(false);
        driveSubsystem.disableSpeedPids();

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {

        // Update the OI before running the commands
        oi.updatePeriodic();

        Scheduler.getInstance().run();

        // Update all subsystems after running commands
        updatePeriodic();
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    }

    /**
     * Update periodic
     */
    private void updatePeriodic() {

        // Update all subsystems
        for (TSubsystem subsystem : subsystemLs) {
            subsystem.updatePeriodic();
        }
    }
}
