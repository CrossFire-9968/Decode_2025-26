package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class YeetFeederArm
{
   private Servo feederArm;

   public void init(HardwareMap hwMap){
      // Initialize your servo in hardware map
      feederArm = hwMap.get(Servo.class, "yeet_feed_arm");
      this.toHome();
   }

   // Start feeder servo
   public void toHome()
   {
      double homePosition = 0.5;
      feederArm.setPosition(homePosition);
   }

   // Stop feeder servo
   public void toFirstElement()
   {
      double ball1Position = 1.0;
      feederArm.setPosition(ball1Position);
   }

   // Stop feeder servo
   public void toSecondElement()
   {
      double ball2Position = 0.35;
      feederArm.setPosition(ball2Position);
   }

}