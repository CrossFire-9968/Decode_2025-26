package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "Manual")
public class Manual extends OpMode
{
   public Mecanum mecanum = new Mecanum();
   public Yeeter yeeter = new Yeeter();
   public AprilTag_9968 aTag = new AprilTag_9968();
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
   double aprilYeet = ;

   //100% at 12 ft
   //90% at 10 ft
   //85% at 8 ft
   //80% at 6 ft
   //75% at 4 ft
   //70% at 2 ft

   public void init()
   {
      mecanum.init(hardwareMap);
      aTag.init(hardwareMap);
      yeeter.init(hardwareMap);

      aTag.startStreaming();
   }

   @Override
   public void loop()
   {
      double bearingError = 0.0;

      //      // Yeeter control for double yeet
      //position 1
      if (gamepad2.cross) {
         yeeter.yeetAllElements(0.60, 0.60, 330);
         yeeter.resetPark();
      }
      // position 2
      else if (gamepad2.circle) {
         yeeter.yeetAllElements(0.70, 0.70, 290);
         yeeter.resetPark();
      }
      //position 3
      else if (gamepad2.triangle) {
         yeeter.yeetAllElements(0.75, 0.78, 270);
         yeeter.resetPark();
      }
      // long shot
      else if (gamepad2.square) {
         yeeter.yeetAllElements(0.90, 0.90, 265);
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
         // Blinking purple
      }
      else {
         // Blinking green
      }

      // Driver initiated April Tag alignment for yeeting
      if (gamepad2.right_bumper) {
         // PID targeting control
         bearingError = desiredBearingAngle - bearingAngle;

         // Check deadband first - if within tolerance, skip PID calculations
         if (Math.abs(bearingError) < bearingDeadband) {
            bearingMotorPower = 0.0;
         }
         else {
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

      }
      else {
         // Reset integral and derivative when not targeting to prevent accumulated error
         bearingErrorRunningSum = 0.0;
         previousBearingError = 0.0;
         mecanum.manualDrive(gamepad1, telemetry);
      }

      telemetry.addData("bearingError: ", bearingError);
      telemetry.addData("bearingAngle: ", bearingAngle);
      telemetry.addData("bearingErrorRunningSum: ", bearingErrorRunningSum);
      telemetry.update();
   }

   @Override
   public void stop()
   {
      // Save more CPU resources when camera is no longer needed.
      aTag.visionPortal.close();
   }
}