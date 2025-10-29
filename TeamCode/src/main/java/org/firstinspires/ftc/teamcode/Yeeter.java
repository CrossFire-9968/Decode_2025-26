package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Yeeter
{
   private final AimYeeter aimYeeter = new AimYeeter();
   private final YeetFeederArm feederArm = new YeetFeederArm();
   private final YeetFeederWheel feederWheel = new YeetFeederWheel();
   public final YeetLaunchWheel yeetWheel = new YeetLaunchWheel();
   private final ElapsedTime feedTimer = new ElapsedTime();


   public void init(HardwareMap hwMap){
      aimYeeter.init(hwMap);
      feederArm.init(hwMap);
      feederWheel.init(hwMap);
      yeetWheel.init(hwMap);
      this.park();
   }


   public void launchAll() {
      final double firstElementTime = 2.0;
      final double secondElementTime = 3.0;

      // Start outtake and launch wheels
      yeetWheel.launchSpeed();
      feederWheel.start();

      // Move feeder wheel to first element and pause for launch
      feedTimer.reset();
      feederArm.toFirstElement();

      // Wait for launch then move to element 2
      if (feedTimer.seconds() >= firstElementTime){
         feederArm.toSecondElement();
      }

      // Reset timer for second element
      feedTimer.reset();

      // Wait for launch then return feeder to home
      if (feedTimer.seconds() >= secondElementTime){
         this.park();
      }
   }

   public void park(){
      yeetWheel.stop();
      feederWheel.stop();
      feederArm.toHome();
      aimYeeter.
   }
}
