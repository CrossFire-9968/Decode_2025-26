package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Yeeter
{
   private final YeetLift yeetLift = new YeetLift();
   private final YeetFeederArm feederArm = new YeetFeederArm();
   private final YeetFeederWheel feederWheel = new YeetFeederWheel();
   public final YeetLaunchWheel yeetWheel = new YeetLaunchWheel();
   public ElapsedTime feedTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
   private boolean sequenceActive = false;

   public void init(HardwareMap hwMap)
   {
      yeetLift.init(hwMap);
      feederArm.init(hwMap);
      feederWheel.init(hwMap);
      yeetWheel.init(hwMap);
      this.park();
   }

   public void launchAll(double launchPower, int yeetLiftPosition)
   {
      final double yeetDelay = 2.0;
      final double timeAllottedForElement1 = yeetDelay + 1.0;
      final double timeAllottedForElement2 = timeAllottedForElement1 + 1.5;

      if (!sequenceActive) {
         feedTimer.reset();
         sequenceActive = true;
      }

      if (sequenceActive) {
         double elapsedTime = feedTimer.seconds();
         double yeetMotorStartPosition = 130;

         if (yeetLift.getPosition() >= yeetMotorStartPosition){
            yeetWheel.launchSpeed(launchPower);
            feederWheel.yeetStart();
         }
         else{
            yeetWheel.noPinchSpeed();
         }

         if (elapsedTime < yeetDelay){
            yeetLift.raiseToYeet(yeetLiftPosition);
         }

         // Move feeder wheel to first element and pause for launch
         else if (elapsedTime >= yeetDelay && elapsedTime <= timeAllottedForElement1) {
            feederArm.toFirstElement();
         }

         // Move feeder wheel to second element and pause for launch
         else if (elapsedTime <= timeAllottedForElement2) {
            feederArm.toSecondElement();
         }

         // All done, so wait for next button press
         else {
            this.park();
         }
      }
   }

   public void intake(){
      yeetWheel.intakeSpeed();
   }

   // Only reset the sequence after the button is released.
   // We don't want to run it over and over again if the button is held.
   public void resetLaunchSequence() {
      sequenceActive = false;
      this.park();
   }

   public void park()
   {
      feederArm.toHome();
      yeetWheel.stop();
      feederWheel.stop();
      feederArm.toHome();
      yeetLift.toHome();
   }
}
