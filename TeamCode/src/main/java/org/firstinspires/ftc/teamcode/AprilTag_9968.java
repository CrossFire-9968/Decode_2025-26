package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class AprilTag_9968 {
    private AprilTagProcessor aprilTag;                // The variable to store our instance of the AprilTag processor.
    public VisionPortal visionPortal;                 // The variable to store our instance of the vision portal.
    private double robotRange = 0.0;
    private double robotBearing = 0.0;
    private double robotElevation = 0.0;
    private boolean tagDetected = false;
    private boolean isStreaming = false;

    public void init(HardwareMap hwMap) {
        //aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        //aprilTag.setDecimation(3);
        //visionPortal = VisionPortal.easyCreateWithDefaults(hwMap.get(WebcamName.class, "Webcam 1"), aprilTag);

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));

        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        builder.enableLiveView(false);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        visionPortal.setProcessorEnabled(aprilTag, true);
        isStreaming = true;  // Tracking initial state

    }   // end method init()

    // Add telemetry about AprilTag detections.
    public void runAprilTag(Telemetry tm) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        // Reset detection flag and values at start of each loop
        tagDetected = false;

        // Process only the first valid detection (most efficient)
        if (!currentDetections.isEmpty()) {
            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null) {
                    robotRange = detection.ftcPose.range;
                    robotBearing = detection.ftcPose.bearing;
                    robotElevation = detection.ftcPose.elevation;
                    tagDetected = true;
                    tm.addData("apBearing: ", detection.ftcPose.bearing);
                    break;  // Only process first valid tag for efficiency
                }
            }
        }
        
        // Reset values if no tag detected to avoid stale data
        if (!tagDetected) {
            robotRange = 0.0;
            robotBearing = 0.0;
            robotElevation = 0.0;
        }
    }

    public double getRobotRange() {
        return robotRange;
    }

    public double getRobotBearing() {
        return robotBearing;
    }

    public double getRobotElevation() {
        return robotElevation;
    }

    public boolean isTagDetected() {
        return tagDetected;
    }

    // Efficiently start streaming only if not already streaming
    public void startStreaming() {
        if (!isStreaming) {
            visionPortal.resumeStreaming();
            isStreaming = true;
        }
    }

    // Efficiently stop streaming only if currently streaming
    public void stopStreaming() {
        if (isStreaming) {
            visionPortal.stopStreaming();
            isStreaming = false;
        }
    }

}   // end class