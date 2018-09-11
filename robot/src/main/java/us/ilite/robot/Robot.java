package us.ilite.robot;

import com.flybotix.hfr.util.log.ELevel;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import us.ilite.lib.drivers.Clock;
import us.ilite.robot.commands.CommandQueue;
import us.ilite.robot.modules.ModuleList;

public class Robot extends IterativeRobot {
    
    private ILog mLogger = Logger.createLog(this.getClass());

    private CommandQueue mCommandQueue = new CommandQueue();
    private ModuleList mRunningModules = new ModuleList();

    private Clock mClock = new Clock();
    private Data mData = new Data().simulated();

    @Override
    public void robotInit() {
        Logger.setLevel(ELevel.DEBUG);
        mLogger.info("Robot Init");
        mLogger.info("Is Simulated: " + isSimulation());
        
        // Call power on init for each module
        mRunningModules.powerOnInit(mClock.getCurrentTime());
    }

    // This contains code run in ALL robot modes.
    // It's also important to note that this runs AFTER mode-specific code
    @Override
    public void robotPeriodic() {
        mLogger.info(this.toString());

        mClock.cycleEnded();
    }

    @Override
    public void autonomousInit() {
        mapInputsAndCachedSensors();

        mRunningModules.modeInit(mClock.getCurrentTime());
    }

    @Override
    public void autonomousPeriodic() {
        mapInputsAndCachedSensors();

        mCommandQueue.update(mClock.getCurrentTime());
        mRunningModules.update(mClock.getCurrentTime());
    }

    @Override
    public void teleopInit() {
        mapInputsAndCachedSensors();

        mRunningModules.modeInit(mClock.getCurrentTime());
    }

    @Override
    public void teleopPeriodic() {
        mapInputsAndCachedSensors();

        mRunningModules.update(mClock.getCurrentTime());
    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        // Temporary for debugging purposes
        mClock.getCurrentTime();
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

    public void mapInputsAndCachedSensors() {

    }

    public String toString() {

        String mRobotMode = "Unknown";
        String mRobotEnabledDisabled = "Unknown";
        double mNow = Timer.getFPGATimestamp();

        if(this.isAutonomous()) {
            mRobotMode = "Autonomous";
        }
        if(this.isOperatorControl()) {
            mRobotMode = "Operator Control";
        }
        if(this.isTest()) {
            mRobotEnabledDisabled = "Test";
        }

        if(this.isEnabled()) {
            mRobotEnabledDisabled = "Enabled";
        }
        if(this.isDisabled()) {
            mRobotEnabledDisabled = "Disabled";
        }

        return String.format("State: %s\tMode: %s\tTime: %s", mRobotEnabledDisabled, mRobotMode, mNow);

    }

}
