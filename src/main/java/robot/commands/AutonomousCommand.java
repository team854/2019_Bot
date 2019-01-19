package robot.commands;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.drive.TDriveTimeCommand;
import com.torontocodingcollective.commands.gyroDrive.TDriveOnHeadingDistanceCommand;
import com.torontocodingcollective.commands.gyroDrive.TRotateToHeadingCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Robot;
import robot.oi.AutoSelector;

/**
 * AutonomousCommand
 * <p>
 * This class extends the CommandGroup class which allows for a string of
 * commands to be chained together to create complex auto patterns.
 */
public class AutonomousCommand extends CommandGroup {

    public static final char LEFT   = 'L';
    public static final char RIGHT  = 'R';
    public static final char CENTER = 'C';

    /**
     * Autonomous Command
     * <p>
     * Construct an Autonomous Command to perform the auto portion of the robot
     * game. This command will be built when the constructor is called and each
     * element of the command will execute in order.
     * <p>
     * When a parallel command is started, it will act at the same time as all other
     * parallel commands and the next serial command. Parallel commands can end
     * before the serial command, however, when the serial command is complete, all
     * parallel commands will be interrupted at that time if they have not already
     * finished.
     * <p>
     * Since the commands are all constructed at the same instant (when this
     * constructor is called), the commands should not read sensor information in
     * the constructor. All commands should read any relevant sensor information
     * (speed, heading, position) in the init() method of the command. The init()
     * method will be run when the command starts and so can get the robot
     * information at the start of the command, the constructor will be run
     * immediately when the Auto CommandGroup is constructed, and will not have the
     * sensor information relevant to when the command is run.
     */
    public AutonomousCommand() {

        // getting info
        String robotStartPosition = AutoSelector.getRobotStartPosition();
        String pattern            = AutoSelector.getPattern();

        // Print out the user selection and Game config for debug later
        System.out.println("Auto Command Configuration");
        System.out.println("--------------------------");
        System.out.println("Robot Position : " + robotStartPosition);
        System.out.println("Pattern        : " + pattern);

        /* ***********************************************************
        *  Drive Straight using GyroPID control
        *  ***********************************************************/
        if (pattern.equals(AutoSelector.PATTERN_STRAIGHT)) {
            // Go forward 2 ft
            this.addSequential(
                    new TDriveOnHeadingDistanceCommand(250, 0, .6, 15, TConst.BRAKE_WHEN_FINISHED, 
                            Robot.oi, Robot.driveSubsystem));
        }

        /* ***********************************************************
        *  Drive Straight with with no GyroPID control
        *  ***********************************************************/
        if (pattern.equals(AutoSelector.PATTERN_STR_NP)) {
            // Go forward 2 ft
            this.addSequential(
                    new TDriveTimeCommand(.95, 6, TConst.BRAKE_WHEN_FINISHED, 
                            Robot.oi, Robot.driveSubsystem));
        }


        /* ***********************************************************
        *  Drive forward 2ft and then drive a 3ft box pattern
        *  ***********************************************************/
        if (pattern.equals(AutoSelector.PATTERN_BOX)) {
            // Go forward 2 ft
            this.addSequential(
                    // 24 in, 0 deg, .5 speed, 5 sec, Brake
                    new TDriveOnHeadingDistanceCommand(24, 0, .5, 5, TConst.COAST_WHEN_FINISHED, 
                            Robot.oi, Robot.driveSubsystem));

            // Drive a 3 ft box
            this.addSequential(
                    new TDriveOnHeadingDistanceCommand(36, 0, .5, 5, TConst.BRAKE_WHEN_FINISHED, 
                            Robot.oi, Robot.driveSubsystem));

            this.addSequential(new TRotateToHeadingCommand(90, 
                            Robot.oi, Robot.driveSubsystem));

            this.addSequential(
                    new TDriveOnHeadingDistanceCommand(36, 90, .5, 5, TConst.BRAKE_WHEN_FINISHED,
                            Robot.oi, Robot.driveSubsystem));

            this.addSequential(new TRotateToHeadingCommand(180, 
                            Robot.oi, Robot.driveSubsystem));

            this.addSequential(
                    new TDriveOnHeadingDistanceCommand(36, 180, .5, 5, TConst.BRAKE_WHEN_FINISHED, 
                            Robot.oi, Robot.driveSubsystem));

            this.addSequential(new TRotateToHeadingCommand(270, 
                            Robot.oi, Robot.driveSubsystem));

            this.addSequential(
                    new TDriveOnHeadingDistanceCommand(36, 270, .5, 5, TConst.BRAKE_WHEN_FINISHED, 
                            Robot.oi, Robot.driveSubsystem));

            this.addSequential(new TRotateToHeadingCommand(0, 
                            Robot.oi, Robot.driveSubsystem));
        }
    }
}
