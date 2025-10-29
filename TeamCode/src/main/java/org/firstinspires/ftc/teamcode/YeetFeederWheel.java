package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class YeetFeederWheel
{
   private CRServo yeetFeedLaunchServo;

   public void init(HardwareMap hwMap){
      // Initialize your servo in hardware map
      yeetFeedLaunchServo = hwMap.get(CRServo.class, "yeet_feed_wheel");
      this.stop();
   }

   // Start feeder servo
   public void start()
   {
      double launchPower = 0.0;
      yeetFeedLaunchServo.setPower(launchPower);
   }

   // Stop feeder servo
   public void stop()
   {
      double stopPower = 0.0;
      yeetFeedLaunchServo.setPower(stopPower);
   }
}