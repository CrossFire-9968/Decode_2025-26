package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Yeeter
{
   private final YeetLift yeetLift = new YeetLift();
   private final YeetFeederArm feederArm = new YeetFeederArm();
   private final YeetFeederWheel feederWheel = new YeetFeederWheel();
   public final YeetLaunchWheel yeetWheel = new YeetLaunchWheel();
   public ElapsedTime feedTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
   private boolean sequenceActive = false;
   private boolean parkActive = false;

   public enum State
   {POWERUP, INIT, RUNNING, IDLE, COMPLETE}

   public State parkState = State.IDLE;

   ;

   public void init(HardwareMap hwMap)
   {
      yeetLift.init(hwMap);
      feederArm.init(hwMap);
      feederWheel.init(hwMap);
      yeetWheel.init(hwMap);
      this.park();
   }

   public void yeetAllElements(double powerElement1, double powerElement2, int yeetLiftPosition)
   {
      final double yeetDelay = 1.0;
      final double timeAllottedForElement1 = yeetDelay + 0.7;
      final double timeAllottedForElement2 = timeAllottedForElement1 + 1.2;

      if (!sequenceActive) {
         feedTimer.reset();
         sequenceActive = true;
      }

      if (sequenceActive) {
         double elapsedTime = feedTimer.seconds();
         double yeetMotorStartPosition = 130;

         if (yeetLift.getPosition() >= yeetMotorStartPosition) {
            yeetWheel.yeetPower(powerElement1);
            feederWheel.yeetStart();
         }
         else {
            yeetWheel.noPinchSpeed();
         }

         if (elapsedTime < yeetDelay) {
            yeetLift.raiseToYeet(yeetLiftPosition);
         }

         // Move feeder wheel to first element and pause for launch
         else if (elapsedTime >= yeetDelay && elapsedTime <= timeAllottedForElement1) {
            feederArm.toFirstElement();
         }

         // Move feeder wheel to second element and pause for launch
         else if (elapsedTime <= timeAllottedForElement2) {
            yeetWheel.yeetPower(powerElement2);
            feederArm.toSecondElement();
         }

         // All done, so wait for next button press
         else {
            this.resetLaunchSequence();
         }
      }
   }

   public boolean isLaunching()
   {
      return sequenceActive;
   }

   public void intakeOn()
   {
      yeetWheel.intakeSpeed(-0.5);
   }

   public void intakeOff()
   {
      yeetWheel.intakeSpeed(0.0);
   }

   // Only reset the sequence after the button is released.
   // We don't want to run it over and over again if the button is held.
   public void resetLaunchSequence()
   {
      sequenceActive = false;
      this.park();
   }


   public State park()
   {
      switch (parkState) {
         case IDLE:
            parkState = State.RUNNING;
            break;

         case RUNNING:
            feederArm.toHome();
            yeetWheel.stop();
            feederWheel.stop();
            feederArm.toHome();
            yeetLift.toHome();

            if (yeetLift.getYeetLiftState() == State.COMPLETE) {
               parkState = State.COMPLETE;
            }
            break;
      }

      return yeetLift.getYeetLiftState();
   }


   public void resetPark()
   {
      if (parkState == State.COMPLETE) {
         parkState = State.IDLE;
      }
   }


   public State getYeeterParkState()
   {
      return parkState;
   }


   public boolean isParking()
   {
      return true;
   }

}