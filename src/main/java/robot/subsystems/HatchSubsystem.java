package robot.subsystems;

import com.torontocodingcollective.subsystem.TSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import robot.RobotMap;
import robot.commands.hatch.DefaultHatchCommand;

/**
 *
 */
public class HatchSubsystem extends TSubsystem {

    //
    Solenoid grabber = null; //new Solenoid(RobotMap.HATCH_GRABBER_PORT); 
    Solenoid deployer = null; //new Solenoid(RobotMap.HATCH_DEPLOYER_PORT);

    @Override
    public void init() {
        //Empty for now
    };

    //Reads joysticks and acts on the subsystem
    @Override
    protected void initDefaultCommand() {
       setDefaultCommand(new DefaultHatchCommand());
    }

    // Periodically update the dashboard and any PIDs or sensors
    @Override
    public void updatePeriodic() {
        //Empty for now.
    }

    //Depending on the state that the grabber is in, the grabber will open or close.
    public void setGrabberState(boolean state) {
        grabber.set(state);
    }

    //Depending on the state that the deployer is in, the deployer will open or close.
    public void setDeployerState(boolean state) {
        deployer.set(state);
    }


    ///Unused Methods
    
    //The "grabber" will close itself (piston will expand) 
    // public void grabberClose() {
    //     grabber.set(false);
    // }
    
    //The "grabber" will open (piston will retract)
    // public void grabberOpen() {
    //     grabber.set(true);
    // }

    //  Causes "deployer" piston to push out
    //  public void deployerOut(){
    //     deployer.set(true);
    // }

    // Causes "deployer" piston to retract back.
    // public void deployerRetract() {
    //     deployer.set(false);
    // }

}
