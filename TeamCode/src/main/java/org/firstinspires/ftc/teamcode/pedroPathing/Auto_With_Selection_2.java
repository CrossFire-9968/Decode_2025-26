package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Yeeter;

import java.util.Arrays;

@Configurable
@Autonomous(name = "Auto_With_Selection")
public class Auto_With_Selection_2 extends OpMode
{
   // Initialize poses
   private final Pose startPose = new Pose(26, 128.5, Math.toRadians(90));
   private final Pose yeetPose = new Pose(65, 79, Math.toRadians(125));
   private final Pose GPPpose = new Pose(22, 88.3, Math.toRadians(180));

   // Initialize variables for paths
   private PathChain scorePreload;
   private PathChain grabGPP;
   private PathChain scoreGPP;

   // Other Variables
   private final double firstElementYeetPower = 0.75;
   private final double secondElementYeetPower = 0.75;
   private final int yeetPosition = 280;
   private Follower follower;
   private int pathStatePreload = 0;
   private int pathStateYeet = 0;
   private int pathStateGPP = 0;
   public Yeeter yeeter = new Yeeter();
   private TelemetryManager panelsTelemetry; // Panels telemetry
   private enum masterAutoState {PRELOAD, YEET_PRELOAD, GPP, YEET_GPP, PGP, YEET_PGP, PPG, YEET_PPG, COMPLETE}
   private masterAutoState masterState;


   // Custom logging function to support telemetry and Panels
   private void log(String caption, Object... text) {
      if (text.length == 1) {
         telemetry.addData(caption, text[0]);
         panelsTelemetry.debug(caption + ": " + text[0]);
      }
      else if (text.length >= 2) {
         StringBuilder message = new StringBuilder();
         for (int i = 0; i < text.length; i++) {
            message.append(text[i]);
            if (i < text.length - 1) {
               message.append(" ");
            }
         }
         telemetry.addData(caption, message.toString());
         panelsTelemetry.debug(caption + ": " + message);
      }
   }


   public void buildPathsPreload()
   {
      // Move to yeet position from start pose
      scorePreload = follower.pathBuilder()
            .addPath(new BezierLine(startPose, yeetPose))
            .setLinearHeadingInterpolation(startPose.getHeading(), yeetPose.getHeading())
            .build();
   }

   // Move to PPG pose, then back to scoring pose
   public void buildPathsGPP()
   {
      // Move from yeet pose to GPP pose
      grabGPP = follower.pathBuilder()
            .addPath(new BezierCurve(Arrays.asList(
                  yeetPose,                                // Start point
                  new Pose(44, 85, Math.toRadians(180)),    // Control point 1
                  GPPpose)))                                // End point
            .setLinearHeadingInterpolation(yeetPose.getHeading(), GPPpose.getHeading())
            .build();

      // Move from GPP pose to yeet pose
      scoreGPP = follower.pathBuilder()
            .addPath(new BezierCurve(Arrays.asList(
                  GPPpose,                                // Start point
                  new Pose(44, 85, Math.toRadians(180)),    // Control point 1
                  yeetPose)))                                // End point
            .setLinearHeadingInterpolation(GPPpose.getHeading(), yeetPose.getHeading())
            .build();
   }


   // State machine for yeeting preload upon start of autonomous
   public void updateStateMachinePreload() {
      switch (pathStatePreload) {
         case 0:
            // Move to the yeet pose from the start pose
            log("Auto pathing started");
            follower.followPath(scorePreload, true);
            setPathStatePreload(1);
            break;

         case 1:
            log("Moving to yeet pose");

            // Wait until yeet position reached
            if (!follower.isBusy()) {
               telemetry.addLine("Arrived at yeet pose");
               setPathStatePreload(-1); // finished
            }
            break;
      }
   }


   public void updateStateMachineYeet()
   {
      switch(pathStateYeet) {
         case 0:
            if (!follower.isBusy()) {
               log("Yeeting elements");
               follower.pausePathFollowing();
               setPathStateYeet(1);
            }
            break;

         case 1:
            yeeter.yeetAllElements(firstElementYeetPower, secondElementYeetPower, yeetPosition);

            if (!yeeter.isLaunching()) {
               yeeter.resetPark(); // Reset park state machine so it runs only once
               setPathStateYeet(2);
            }
            break;

         case 2:
            log("Parking yeeter");
            yeeter.park();
            if (yeeter.getYeeterParkState() == Yeeter.State.COMPLETE) {
               log("Yeet complete");
               follower.resumePathFollowing();
               setPathStateYeet(-1); // finished
            }
            break;
      }
   }


   public void updateStateMachineGPP()
   {
      switch(pathStateGPP) {
         case 0:
            log("Moving to GPP pose");
            yeeter.intake();
            follower.followPath(grabGPP, true);
            setPathStateGPP(1);
            break;

         case 1:
            log("Arrived at GPP pose");

            // Wait until yeet position reached
            if (!follower.isBusy()) {
               telemetry.addLine("Moving to yeet");
               follower.followPath(scoreGPP, true);
               setPathStateGPP(2);
            }
            break;

         case 2:
            log("Moving to yeet position");

            if (!follower.isBusy()) {
               telemetry.addLine("Arrived at yeet pose");
               setPathStateGPP(-1);
            }
            break;
      }
   }


   public void setPathStatePreload(int pState)
   {
      pathStatePreload = pState;
   }


   public void setPathStateYeet(int pState)
   {
      pathStateYeet = pState;
   }


   public void setPathStateGPP(int pState)
   {
      pathStateGPP = pState;
   }


   @Override
   public void loop()
   {
      follower.update();

      // The master state machine. This allows for creating smaller reusable state
      // machines then stringing them together in the desired order during runtime.
      // For example, if you use AprilTags to determine the mosaic, the after the
      // preload yeet, the robot could be routed to the correct set of elements.
      switch (masterState) {
         case PRELOAD:
            updateStateMachinePreload();
            if (pathStatePreload == -1) {
               masterState = masterAutoState.YEET_PRELOAD;
               setPathStateGPP(0);
            }
            break;

         case YEET_PRELOAD:
            updateStateMachineYeet();
            if (pathStateYeet == -1) {
               masterState = masterAutoState.GPP;
               setPathStateYeet(0);
            }
            break;

         case GPP:
            updateStateMachineGPP();
            if (pathStateGPP == -1) { // Finished GPP
               masterState = masterAutoState.YEET_GPP;
               setPathStateYeet(0); // Reset PGP state machine
            }
            break;

         case YEET_GPP:
            updateStateMachineYeet();
            if (pathStateYeet == -1) {
               masterState = masterAutoState.COMPLETE;
               setPathStateYeet(0);
            }
            break;

         case COMPLETE:
            log("Autonomous Complete");
            break;
      }

      // Log to Panels and driver station (custom log function)
      log("path state", pathStateGPP);
      log("x", follower.getPose().getX());
      log("y", follower.getPose().getY());
      log("heading", follower.getPose().getHeading());
      telemetry.update();
   }


   @Override
   public void init()
   {
      follower = Constants.createFollower(hardwareMap);
      follower.setStartingPose(startPose);

      // Reset master state machine
      masterState = masterAutoState.PRELOAD;

      // Hardware inits needed during autonomous
      yeeter.init(hardwareMap);

      // build paths
      buildPathsPreload();
      buildPathsGPP();

      // Reset all state machines
      setPathStatePreload(0);
      setPathStateGPP(0);
      setPathStateYeet(0);

      // Log completed initialization to Panels and driver station (custom log function)
      log("Status", "Initialized");
      telemetry.update(); // Update driver station after logging
   }


   @Override
   // Runs once on start then enters into loop
   public void start()
   {
   }


   @Override
   public void stop()
   {
   }
}
