package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Yeeter
{
   private final AimYeeter aimYeeter = new AimYeeter();
   private final YeetFeederArm feederArm = new YeetFeederArm();
   private final YeetFeederWheel feederWheel = new YeetFeederWheel();
   public final YeetLaunchWheel yeetWheel = new YeetLaunchWheel();
   public  ElapsedTime feedTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
   private boolean firstTime = true;

   public void init(HardwareMap hwMap){
      aimYeeter.init(hwMap);
      feederArm.init(hwMap);
      feederWheel.init(hwMap);
      yeetWheel.init(hwMap);
      this.park();
   }

   public void launchAll(Telemetry tm) {
      final double firstElementTime = 2.0;
      final double secondElementTime = 5.0;

      // Start outtake and launch wheels
      yeetWheel.launchSpeed();
      feederWheel.start();

      // Move feeder wheel to first element and pause for launch
      if (firstTime) {
         feedTimer.reset();
         firstTime = false;
      }
      feederArm.toFirstElement();


      // Wait for launch then move to element 2
      if (feedTimer.seconds() >= firstElementTime){
         feederArm.toSecondElement();
      }

      // Reset timer for second element
     // feedTimer.reset();

      // Wait for launch then return feeder to home
      if (feedTimer.seconds() >= secondElementTime){
         this.park();
      }

      tm.addData("Feeder timer: ", feedTimer.seconds());
   }

   public void park(){
      yeetWheel.stop();
      feederWheel.stop();
      feederArm.toHome();
     // firstTime = true;
   }

}
