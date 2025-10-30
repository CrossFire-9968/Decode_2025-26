package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "Manual")
public class Manual extends OpMode
{
   public Mecanum mecanum = new Mecanum();
   public Yeeter yeeter = new Yeeter();
   public AprilTag_9968 aTag = new AprilTag_9968();
   //   double kp = 0.1;
   //   double desiredBearingAngle = 0.0;
   //   double bearingAngle = 0.0;
   //   double bearingMotorPower = 0.0;
   //   double targetingDeadband = 0.05;

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
   }

   @Override
   public void loop()
   {
      mecanum.manualDrive(gamepad1, telemetry);
      mecanum.getMotorTelemetry(telemetry);

      // Yeeter control
      if (gamepad2.right_bumper) {
         yeeter.launchAll();
      }
      else {
         yeeter.resetLaunchSequence();
      }


      // Save CPU resources; can resume streaming when needed.
      //      if (gamepad2.dpad_down) {
      //         aTag.visionPortal.stopStreaming();
      //      }
      //      else if (gamepad2.dpad_up) {
      //         aTag.visionPortal.resumeStreaming();
      //      }

      //      aTag.runAprilTag(telemetry, gamepad2);
      //      bearingAngle = aTag.getRobotBearing();

      // Y = kp * (desired_angle - actual_angle)
      //      bearingMotorPower = kp * (desiredBearingAngle - bearingAngle);

      //      if (gamepad2.square) {
      //         // Limit range of targeting power between -1 and 1
      //         if (bearingMotorPower > 1) {
      //            bearingMotorPower = 1;
      //         }
      //         else if (bearingMotorPower < -1) {
      //            bearingMotorPower = -1;
      //         }

      // Add targeting power deadband
      //         if (Math.abs(bearingMotorPower) < targetingDeadband) {
      //            bearingMotorPower = 0.0;
      //         }
      //
      //         telemetry.addData("bearingPower: ", bearingMotorPower);
      //         mecanum.setEachMecanumPower(-bearingMotorPower, bearingMotorPower, bearingMotorPower, -bearingMotorPower);
   }

      telemetry.update();
}


@Override
public void stop()
{
   // Save more CPU resources when camera is no longer needed.
   //      aTag.visionPortal.close();
}

}