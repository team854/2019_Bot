package com.torontocodingcollective.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * TSubsystem
 * <p>
 * Extends the CommandBased Subsystem to add init() and updatePeriodic
 */
public abstract class TSubsystem extends Subsystem {

    /**
     * Initialize the subsystem
     */
    public abstract void init();

    /**
     * Update Periodic
     * <p>
     * This routine should be used for updating elements (like PIDs) that need to
     * run on every control loop, and to update the SmartDashboard values.
     */
    public abstract void updatePeriodic();

}
