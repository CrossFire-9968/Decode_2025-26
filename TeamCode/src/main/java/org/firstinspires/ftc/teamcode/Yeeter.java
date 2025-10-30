package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Yeeter
{
   private final AimYeeter aimYeeter = new AimYeeter();
   private final YeetFeederArm feederArm = new YeetFeederArm();
   private final YeetFeederWheel feederWheel = new YeetFeederWheel();
   public final YeetLaunchWheel yeetWheel = new YeetLaunchWheel();
   public ElapsedTime feedTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
   private boolean sequenceActive = false;

   public void init(HardwareMap hwMap)
   {
      aimYeeter.init(hwMap);
      feederArm.init(hwMap);
      feederWheel.init(hwMap);
      yeetWheel.init(hwMap);
      this.park();
   }

   public void launchAll()
   {
      final double timeAllottedForElement1 = 2.0;
      final double timeAllottedForElement2 = 3.0;

      if (!sequenceActive) {
         feedTimer.reset();
         yeetWheel.launchSpeed();
         feederWheel.start();
         sequenceActive = false;
      }

      if (sequenceActive) {
         double elapsedTime = feedTimer.seconds();

         // Move feeder wheel to first element and pause for launch
         if (elapsedTime <= timeAllottedForElement1) {
            feederArm.toFirstElement();
         }

         // Move feeder wheel to second element and pause for launch
         else if (elapsedTime <= timeAllottedForElement2) {
            feederArm.toSecondElement();
         }

         // All done, so wait for next button press
         else {
            this.park();
            sequenceActive = false;
         }
      }
   }

   public void park()
   {
      yeetWheel.stop();
      feederWheel.stop();
      feederArm.toHome();
   }
}
