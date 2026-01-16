package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Autonomous(name="SimpleAuto")
public class SimpleAuto extends LinearOpMode
{

    public MecanumAuto mecanumAuto = new MecanumAuto();
    private double autoDrivePower = 0.5;
    private ElapsedTime cameraTimer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException
    {
        //-----------------------------------------------------------------------
        // INITIALIZATIONS
        // Our initialization code should go here before calling "WaitForStart()"
        //-----------------------------------------------------------------------

        mecanumAuto.init(hardwareMap);
        boolean isAutoComplete = false;

        // Init robot
        mecanumAuto.setAllMecanumPowers(0.0);

        //blinkin.setColor(RevBlinkinLedDriver.BlinkinPattern.VIOLET)

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Run this code while Autonomous has not timed out
        while (opModeIsActive() && !isAutoComplete) {
            clipSpecimen();

            isAutoComplete = true;
        }
    }

    public void clipSpecimen() {
        int countsToDriveOneInch = -33; // Approximate encoder counts to drive 1 inch
        int autoDelay = 500;
        int driveOffWall = 12;

        mecanumAuto.drive(-autoDrivePower, driveOffWall * countsToDriveOneInch);
        waitForMotionToComplete();
        sleep(autoDelay);
    }

    // Drive until one of the 4 wheels has reached it's target position. We only wait for one
    // because it is not guaranteed all 4 wheels will reach their target at the same time due
    // to inconsistencies in alignment and resistance in the drivetrain. If we wait for all 4 wheels
    // They will start fighting one another in a tug-of-war type effect and we may not transition
    // to the next stage as we expect.
    public void waitForMotionToComplete() {

        // Only wait until one of the wheels reaches position. At that point, stop all the motors
        while (mecanumAuto.motor_LF.isBusy() && mecanumAuto.motor_RF.isBusy() && mecanumAuto.motor_RR.isBusy() && mecanumAuto.motor_LR.isBusy()) {
            this.idle();
        }

        mecanumAuto.stopAndResetEncoders();
    }
}