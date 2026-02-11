package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.GoBildaPrism.PrismColor;


@TeleOp(name = "Manual")
public class Manual extends OpMode {
    public Mecanum mecanum = new Mecanum();
    public Yeeter yeeter = new Yeeter();
    public AprilTag_9968 aTag = new AprilTag_9968();
    public GoBildaPrism led = new GoBildaPrism();
    public ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    double kp = 0.05;
    double ki = 0.0005;
    double kd = 0;
    double maxIntegralSum = 30.0;
    double desiredBearingAngle = 0.0;
    double bearingAngle = 0.0;
    double bearingMotorPower = 0.0;
    double bearingDeadband = 0.3;
    double bearingErrorRunningSum = 0.0;
    double previousBearingError = 0.0;
    //double aprilYeet = ;
    boolean firstLoopDone = false;


    public void init() {
        mecanum.init(hardwareMap);
        aTag.init(hardwareMap);
        yeeter.init(hardwareMap);
        led.init(hardwareMap);
        aTag.startStreaming();
    }


    @Override
    public void loop() {
        if (!firstLoopDone) {
            timer.reset();
            led.setPrismColor(PrismColor.RED);
            firstLoopDone = true;
        }

        double bearingError = 0.0;

        // Yeeter control for double yeet
        //position 1
        if (gamepad2.cross) {
            yeeter.yeetAllElements(0.65, 0.65, 330);
            yeeter.resetPark();
        }
        // position 2
        else if (gamepad2.circle) {
            yeeter.yeetAllElements(0.75, 0.75, 290);
            yeeter.resetPark();
        }
        //position 3
        else if (gamepad2.triangle) {
            yeeter.yeetAllElements(0.80, 0.80, 270);
            yeeter.resetPark();
        }
        // long shot
        else if (gamepad2.square) {
            yeeter.yeetAllElements(0.95, 0.95, 265);
            yeeter.resetPark();
        }
        else if (gamepad2.dpad_down) {
            yeeter.launchOne(0.60, 330);
            yeeter.resetPark();
        }
        else if (gamepad2.dpad_right) {
            yeeter.launchOne(0.70, 290);
            yeeter.resetPark();
        }
        else if (gamepad2.dpad_up) {
            yeeter.launchOne(0.80, 270);
            yeeter.resetPark();
        }
        else if (gamepad2.dpad_left) {
            yeeter.launchOne(0.90, 265);
            yeeter.resetPark();
        }
        else if (gamepad2.left_bumper) {
            yeeter.intakeOn();
            yeeter.resetPark();
        }
        else {
            yeeter.resetLaunchSequence();
        }

        aTag.runAprilTag(telemetry);
        bearingAngle = aTag.getRobotBearing();

        if (bearingAngle != 0.0) {
            led.setPrismColor(PrismColor.GREEN);
        }
        else if (timer.seconds() < 100) {
            led.setPrismColor(PrismColor.BLUE);
        }
        else {
            led.setPrismColor(PrismColor.RED);
        }

        // Driver initiated April Tag alignment for yeeting
        if (gamepad2.right_bumper) {
            // PID targeting control
            bearingError = desiredBearingAngle - bearingAngle;

            // Check deadband first - if within tolerance, skip PID calculations
            if (Math.abs(bearingError) < bearingDeadband) {
                bearingMotorPower = 0.0;
            } else {
                // Accumulate error for integral term
                bearingErrorRunningSum += bearingError;

                // Anti-windup: clamp integral sum
                if (bearingErrorRunningSum > maxIntegralSum) {
                    bearingErrorRunningSum = maxIntegralSum;
                }
                else if (bearingErrorRunningSum < -maxIntegralSum) {
                    bearingErrorRunningSum = -maxIntegralSum;
                }

                // Calculate derivative term (rate of change of error)
                double derivative = bearingError - previousBearingError;

                // PID Controller: Y = kp * error + ki * sum(error) + kd * derivative(error)
                bearingMotorPower = kp * bearingError + ki * bearingErrorRunningSum + kd * derivative;

                // Store current error for next iteration
                previousBearingError = bearingError;

                // Clamp motor power between -1 and 1
                bearingMotorPower = Math.max(-1.0, Math.min(1.0, bearingMotorPower));
            }

            mecanum.setEachMecanumPower(-bearingMotorPower, bearingMotorPower, bearingMotorPower, -bearingMotorPower);

        } else {
            // Reset integral and derivative when not targeting to prevent accumulated error
            bearingErrorRunningSum = 0.0;
            previousBearingError = 0.0;
            mecanum.manualDrive(gamepad1, telemetry);
        }

        telemetry.addData("Bearing Angle: ", bearingAngle);
        telemetry.addData("Timer: ", timer);
        telemetry.update();
    }


    @Override
    public void stop() {
        // Save more CPU resources when camera is no longer needed.
        aTag.visionPortal.close();
        led.setPrismColor(PrismColor.WHITE);
    }
}