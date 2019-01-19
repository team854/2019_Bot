package com.torontocodingcollective.commands;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.commands.gyroDrive.TRotateToHeadingCommand;
import com.torontocodingcollective.oi.TOi;
import com.torontocodingcollective.subsystem.TDriveSubsystem;
import com.torontocodingcollective.subsystem.TGyroDriveSubsystem;

import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * Default Drive Command for Game Controllers
 * <p>
 * Implements the following basic controls for the driver. 
 * <ls>
 * <li>Tank, Arcade or Single Stick drive 
 * <li>Back Button to cancel a command 
 * <li>Start Button to reset the gyro and encoders 
 * <li>POV to rotate to angle 
 * </ls>
 */
public class TDefaultDriveCommand extends TSafeCommand {

    private final static String COMMAND_NAME = 
            TDefaultDriveCommand.class.getSimpleName();
    
    private final TOi                 oi;
    private final TDriveSubsystem     driveSubsystem;
    private final TGyroDriveSubsystem gyroDriveSubsystem;

    public TDefaultDriveCommand(TOi oi, TDriveSubsystem driveSubsystem) {

        super(TConst.NO_COMMAND_TIMEOUT, oi);
        
        requires(driveSubsystem);

        this.driveSubsystem = driveSubsystem;
        this.oi = oi;

        if (driveSubsystem instanceof TGyroDriveSubsystem) {
            gyroDriveSubsystem = (TGyroDriveSubsystem) driveSubsystem;
        } else {
            gyroDriveSubsystem = null;
        }
    }

    @Override
    protected String getCommandName() { return COMMAND_NAME; }
    
    @Override
    protected String getParmDesc() { 
        return super.getParmDesc(); 
    }
    
    @Override
    protected void initialize() {
        
        // Only print the command start message
        // if this command was not subclassed
        if (getCommandName().equals(COMMAND_NAME)) {
            logMessage(getParmDesc() + " starting");
        }
    }

    @Override
    protected void execute() {

        // Process all standard driver buttons before
        // driving the robot.

        // Reset encoders
        if (oi.getReset()) {

            driveSubsystem.resetEncoders();

            if (gyroDriveSubsystem != null) {
                gyroDriveSubsystem.resetGyroAngle();
            }
        }

        // Enable or disable PID controllers on the
        // drive motors
        if (oi.getSpeedPidEnabled()) {
            driveSubsystem.enableSpeedPids();
        } else {
            driveSubsystem.disableSpeedPids();
        }

        // If this is a gyro subsystem,
        // then rotate to the heading
        if (gyroDriveSubsystem != null) {
            int heading = oi.getRotateToHeading();
            if (heading != -1) {
                Scheduler.getInstance().add(new TRotateToHeadingCommand(heading, oi, gyroDriveSubsystem));
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }
}
