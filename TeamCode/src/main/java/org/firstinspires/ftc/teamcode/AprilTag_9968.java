package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class AprilTag_9968
{
   private AprilTagProcessor aprilTag;                // The variable to store our instance of the AprilTag processor.
   public VisionPortal visionPortal;                 // The variable to store our instance of the vision portal.
   private double robotRange;
   private double robotBearing;
   private double robotElevation;

   public void init(HardwareMap hwMap) {
      // Create the AprilTag processor the easy way.
      aprilTag = AprilTagProcessor.easyCreateWithDefaults();
      aprilTag.setDecimation(3);

      // Create the vision portal the easy way.
      visionPortal = VisionPortal.easyCreateWithDefaults(hwMap.get(WebcamName.class, "Webcam 1"), aprilTag);

   }   // end method initAprilTag()


   public void runAprilTag(Telemetry tm, Gamepad gPad) {
      tm.addData("DS preview on/off", "3 dots, Camera Stream");
      tm.addData(">", "Touch START to start OpMode");

      telemetryAprilTag(tm);

   }   // end method runOpMode()


   // Add telemetry about AprilTag detections.
   private void telemetryAprilTag(Telemetry tm) {

      List<AprilTagDetection> currentDetections = aprilTag.getDetections();
      tm.addData("# AprilTags Detected", currentDetections.size());

      // Step through the list of detections and display info for each one.
      for (AprilTagDetection detection : currentDetections) {
         if (detection.metadata != null) {
//            tm.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
//            tm.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
//            tm.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
            tm.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));

            robotRange = detection.ftcPose.range;
            robotBearing = detection.ftcPose.bearing;
            robotElevation = detection.ftcPose.elevation;
//         } else {
//            tm.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
//            tm.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
         }
      }   // end for() loop

      // Add "key" information to telemetry
//      tm.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
//      tm.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
//      tm.addLine("RBE = Range, Bearing & Elevation");

   }   // end method telemetryAprilTag()


   public double getRobotRange() {
      return robotRange;
   }

   public double getRobotBearing() {
      return robotBearing;
   }

   public double getRobotElevation() {
      return robotElevation;
   }



}   // end class
