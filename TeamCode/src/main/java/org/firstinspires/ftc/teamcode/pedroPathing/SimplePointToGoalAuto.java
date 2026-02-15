package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Yeeter;


@Configurable
@Autonomous(name = "SimplePointToGoalAuto")
public class SimplePointToGoalAuto extends OpMode {
    // Initialize poses
    private Pose startPose = null;
    private Pose yeetPose = null;
    private Pose yeetPoseOF = null;
    private Pose beforeGPPpose = null;
    private Pose GPPpose = null;
    private Pose beforePGPpose = null;
    private Pose PGPpose = null;
    private Pose beforePPGpose = null;
    private Pose PPGpose = null;
    private Pose endPose = null;

    Pose grabGPPControlPoint = null;
    Pose grabPGPControlPoint = null;
    Pose grabPPGControlPoint = null;

    private enum Alliance {BLUE, RED, UNKNOWN}

    private enum AutoStartLocation {GOAL, POINT, UNKNOWN}

    Alliance alliance = Alliance.UNKNOWN;
    AutoStartLocation location = AutoStartLocation.UNKNOWN;
    boolean runBuild = false;
    String allianceSelected = "";
    String locationSelected = "";


//    private final Pose startPoseFrontLeft = new Pose(26, 128.5, Math.toRadians(90));
//    private final Pose yeetPoseFrontLeft = new Pose(65, 80, Math.toRadians(126));
//    private final Pose GPPposeFrontLeft = new Pose(22, 88.3, Math.toRadians(180));
//    private final Pose PGPposeFrontLeft = new Pose(22, 51, Math.toRadians(180));
//    private final Pose PPGposeFrontLeft = new Pose(22, 35, Math.toRadians(180));
//
//    private final Pose startPoseFrontRight = new Pose(110,120, Math.toRadians(90));
//    private final Pose yeetPoseFrontRight = new Pose(77,77, Math.toRadians(48));
//    private final Pose GPPposeFrontRight = new Pose(118,80.5, Math.toRadians(0));
//    private final Pose PGPposeFrontRight = new Pose(119,57.5, Math.toRadians(0));
//    private final Pose PPGposeFrontRight = new Pose(119,36, Math.toRadians(0));
//
//    private final Pose startPoseBackLeft = new Pose(56.5,10, Math.toRadians(90));
//    private final Pose yeetPoseBackLeft = new Pose(64,79, Math.toRadians(132));
//    private final Pose GPPposeBackLeft = new Pose(29,82, Math.toRadians(180));
//    private final Pose PGPposeBackLeft = new Pose(23,55.5, Math.toRadians(180));
//    private final Pose PPGposeBackLeft = new Pose(23,36, Math.toRadians(180));
//
//    private final Pose startPose = new Pose(84,1, Math.toRadians(90));
//    private final Pose yeetPose = new Pose(77,77, Math.toRadians(40));
//    private final Pose GPPpose = new Pose(118,82, Math.toRadians(0));
//    private final Pose PGPpose = new Pose(119,57.5, Math.toRadians(0));
//    private final Pose PPGpose = new Pose(119,36, Math.toRadians(0));

    // Other Variables
    public Yeeter yeeter = new Yeeter();

    // Initialize variables for paths
    private PathChain scorePreload;
    private PathChain grabGPP;
    private PathChain scoreGPP;
    private PathChain grabPGP;
    private PathChain scorePGP;
    private PathChain grabPPG;
    private PathChain scorePPG;

    private Follower follower;
    private int pathState = 0;

    private int yeetCount;

    private TelemetryManager panelsTelemetry; // Panels telemetry

    private enum masterStateEnum {PRELOAD, GPP, PGP, PPG, YEET, COMPLETE}

//    private enum masterSideState {BLUE, RED}

    private masterStateEnum masterState;
    private masterStateEnum MotifPose;

//    private masterSideState sideState;


    private void loadPreset(Alliance alliance, AutoStartLocation location) {
        if (alliance == Alliance.BLUE && location == AutoStartLocation.GOAL) {
            grabGPPControlPoint = new Pose(48, 85, Math.toRadians(180));
            grabPGPControlPoint = new Pose(90, 51, Math.toRadians(180));
            grabPPGControlPoint = new Pose(72, 57, Math.toRadians(180));

            beforeGPPpose = new Pose (37,85.3, Math.toRadians(180));
            beforePGPpose = new Pose(56,64.5, Math.toRadians(180));
            beforePPGpose = new Pose(37,35, Math.toRadians(180));

            startPose = new Pose(26, 128.5, Math.toRadians(90));
            yeetPose = new Pose(60, 84, Math.toRadians(128));
            GPPpose = new Pose(22, 85.3, Math.toRadians(180));
            PGPpose = new Pose(22, 64.5, Math.toRadians(180));
            PPGpose = new Pose(22, 32, Math.toRadians(180));
        }
        if (alliance == Alliance.RED && location == AutoStartLocation.GOAL) {
            grabGPPControlPoint = new Pose(90, 83, Math.toRadians(180));
            grabPGPControlPoint = new Pose(54, 51, Math.toRadians(180));
            grabPPGControlPoint = new Pose(72, 57, Math.toRadians(180));

            beforeGPPpose = new Pose (86,78, Math.toRadians(0));
            beforePGPpose = new Pose(81,54, Math.toRadians(0));
            beforePPGpose = new Pose(96,36, Math.toRadians(0));

            startPose = new Pose(108, 120, Math.toRadians(90));
            yeetPose = new Pose(77, 77, Math.toRadians(49));
            GPPpose = new Pose(118, 78, Math.toRadians(0));
            PGPpose = new Pose(115, 54, Math.toRadians(0));
            PPGpose = new Pose(119, 36, Math.toRadians(0));
        }

        if (alliance == Alliance.RED && location == AutoStartLocation.POINT) {
            grabGPPControlPoint = new Pose(96, 83, Math.toRadians(180));
            grabPGPControlPoint = new Pose(54, 51, Math.toRadians(180));
            grabPPGControlPoint = new Pose(72, 57, Math.toRadians(180));

            beforeGPPpose = new Pose (85,73.4, Math.toRadians(0));
            beforePGPpose = new Pose(89,50.25, Math.toRadians(0));
            beforePPGpose = new Pose(90,28, Math.toRadians(0));



            startPose = new Pose(84, 1, Math.toRadians(90));
            yeetPose = new Pose(83, 72.325, Math.toRadians(44));
            GPPpose = new Pose(120, 73.4, Math.toRadians(0));
            PGPpose = new Pose(123, 50.25, Math.toRadians(0));
            PPGpose = new Pose(119.25, 28, Math.toRadians(0));
            yeetPoseOF = new Pose(85, 73.325, Math.toRadians(47));
            endPose = new Pose(85, 66.325, Math.toRadians(46));
        }


        if (alliance == Alliance.BLUE && location == AutoStartLocation.POINT) {
            grabGPPControlPoint = new Pose(48, 85, Math.toRadians(180));
            grabPGPControlPoint = new Pose(90, 51, Math.toRadians(180));
            grabPPGControlPoint = new Pose(72, 57, Math.toRadians(180));

            beforeGPPpose = new Pose (59,83.5, Math.toRadians(180));
            beforePGPpose = new Pose(57,60, Math.toRadians(180));
            beforePPGpose = new Pose(50,37, Math.toRadians(180));

            startPose = new Pose(56,10, Math.toRadians(90));
            yeetPose = new Pose(60,83, Math.toRadians(132));
            GPPpose = new Pose(25,83.5, Math.toRadians(180));
            PGPpose = new Pose(23,60, Math.toRadians(180));
            PPGpose = new Pose(23,37, Math.toRadians(180));
            yeetPoseOF = new Pose(60,83, Math.toRadians(136));
            endPose = new Pose(60,73, Math.toRadians(132));
        }
    }


    private void buildPaths() {
        // build paths
        buildPathsPreload();
        buildPathsGPP();
        buildPathsPGP();
        buildPathsPPG();
    }



    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
//        follower.setStartingPose(startPoseFrontLeft);

        // Reset master state machine
        masterState = masterStateEnum.PRELOAD;

        // Manually set mosaic until apriltag works
        MotifPose = masterStateEnum.GPP;

        // Hardware inits needed during autonomous
        yeeter.init(hardwareMap);

        // Reset state machine
        setPathState(0);

        // Reset yeetCount
        yeetCount = 0;

        // Initialize Panels telemetry
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        // Log completed initialization to Panels and driver station (custom log function)
        log("Status", "Initialized");
        telemetry.update(); // Update driver station after logging
    }


    @Override
    public void init_loop() {
        super.init_loop();

        if (alliance == Alliance.UNKNOWN) {
            telemetry.addLine("Select Alliance");
            telemetry.addLine("   Dpad Up: Blue");
            telemetry.addLine("   Dpad Down: Red");

            if (gamepad1.dpad_up) {
                alliance = Alliance.BLUE;
                allianceSelected = "BLUE";
            } else if (gamepad1.dpad_down) {
                alliance = Alliance.RED;
                allianceSelected = "RED";
            }
        } else if (alliance != Alliance.UNKNOWN && location == AutoStartLocation.UNKNOWN) {
            telemetry.addLine("Alliance Selected: " + allianceSelected);
            telemetry.addLine("Select Start Location");
            telemetry.addLine("   Dpad Left: Goal");
            telemetry.addLine("   Dpad Right: Point");
            telemetry.addLine("   Circle: Start Over");

            if (gamepad1.dpad_left) {
                location = AutoStartLocation.GOAL;
                locationSelected = "GOAL";
            } else if (gamepad1.dpad_right) {
                location = AutoStartLocation.POINT;
                locationSelected = "POINT";
            } else if (gamepad1.circle) {
                alliance = Alliance.UNKNOWN;
                location = AutoStartLocation.UNKNOWN;
            }

            // Force a rebuild when the selection has changed and all settings are known
            if (alliance != Alliance.UNKNOWN && location != AutoStartLocation.UNKNOWN) {
                runBuild = true;
            }
        }
        else if (alliance != Alliance.UNKNOWN && location != AutoStartLocation.UNKNOWN) {
            telemetry.addLine("Alliance Selected: " + allianceSelected);
            telemetry.addLine("Location Selected: " + locationSelected);
            telemetry.addLine("   Circle: Start Over");

            if (runBuild) {
                loadPreset(alliance, location);
                follower = Constants.createFollower(hardwareMap);
                buildPaths();
                follower.setStartingPose(startPose);
                runBuild = false;
            }

            if (gamepad1.circle) {
                alliance = Alliance.UNKNOWN;
                location = AutoStartLocation.UNKNOWN;
            }

            if (!runBuild) {
                telemetry.addLine("");
                telemetry.addLine("...ready for play");
            }
        }
    }


    @Override
    public void loop() {
        follower.update();

        // The master state machine. This allows for creating smaller reusable state
        // machines then stringing them together in the desired order during runtime.
        // For example, if you use AprilTags to determine the mosaic, the after the
        // preload yeet, the robot could be routed to the correct set of elements.
        switch (masterState) {
            case PRELOAD:
                updateStateMachinePreload();
                if (pathState == -1) {
                    masterState = masterStateEnum.YEET;
                    setPathState(0);
                }
                break;

            case YEET:
                updateStateMachineYeet();
                if (pathState == -1) {

                    // After first yeet, go to the Artifacts
                    if (yeetCount == 1) {
                        masterState = MotifPose;
                    }
                    // After yeeting Motif do ???
                    else if (yeetCount == 2) {
                        masterState = masterStateEnum.PGP;
                    }
                    else if (yeetCount == 3) {
                        masterState = masterStateEnum.PPG;
                    }
                    else if (yeetCount == 4) {
                        masterState = masterStateEnum.PGP;
                    }

                    setPathState(0);
                }
                break;

            case GPP:
                updateStateMachineGPP();
                if (pathState == -1) {
                    masterState = masterStateEnum.YEET;
                    setPathState(0);
                }
                break;

            case PGP:
                updateStateMachinePGP();
                if (pathState == -1) {
                    masterState = masterStateEnum.YEET;
                    setPathState(0);
                }
                break;

            case PPG:
                updateStateMachinePPG();
                if (pathState == -1) {
                    masterState = masterStateEnum.YEET;
                    setPathState(0);
                }
                break;

            case COMPLETE:
                log("State", "Autonomous Complete");
                break;
        }

        // Log to Panels and driver station (custom log function)
//        log("path state", pathState);
//        log("x", follower.getPose().getX());
//        log("y", follower.getPose().getY());
//        log("heading", follower.getPose().getHeading());
        log("Yeet count:", yeetCount);
        telemetry.update();
        panelsTelemetry.update();
    }


    public void buildPathsPreload() {
        // Move to yeet position from start pose
        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, yeetPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), yeetPose.getHeading())
                .build();
    }


    // Move to GPP pose, then back to scoring pose
    public void buildPathsGPP() {
        //final Pose grabGPPControlPoint1 = new Pose(48, 85, Math.toRadians(180));

        // Move from yeet pose to GPP pose
        grabPPG = follower.pathBuilder()
                .addPath(new BezierLine(yeetPose, beforeGPPpose))
                .setLinearHeadingInterpolation(yeetPose.getHeading(), beforeGPPpose.getHeading())
                .addPath(new BezierLine(beforeGPPpose, GPPpose))
                .setLinearHeadingInterpolation(beforeGPPpose.getHeading(), GPPpose.getHeading())
                .build();



        // Move from GPP pose to yeet pose
       scorePPG = follower.pathBuilder()
               .addPath(new BezierLine(GPPpose, yeetPoseOF))
              .setLinearHeadingInterpolation(GPPpose.getHeading(), yeetPoseOF.getHeading())
               .build();
    }


    // Move to PGP pose, then back to scoring pose
    public void buildPathsPGP() {
        //final Pose grabPGPControlPoint1 = new Pose(90, 51, Math.toRadians(180));

        // Move from yeet pose to PGP pose
        grabPGP = follower.pathBuilder()
                .addPath(new BezierLine(yeetPose,beforePGPpose))
                .setLinearHeadingInterpolation(yeetPose.getHeading(), beforePGPpose.getHeading())
                .addPath(new BezierLine(beforePGPpose,PGPpose))
                .setLinearHeadingInterpolation(beforePGPpose.getHeading(), PGPpose.getHeading())
                .build();

        // Move from PGP pose to yeet pose
        scorePGP = follower.pathBuilder()
                .addPath(new BezierLine(PGPpose, yeetPose))
                .setLinearHeadingInterpolation(PGPpose.getHeading(), yeetPose.getHeading())
                .build();
    }


    // Move to PPG pose, then back to scoring pose
    public void buildPathsPPG() {
        //final Pose grabPPGControlPoint1 = new Pose(72, 57, Math.toRadians(180));

        // Move from yeet pose to PPG pose
        grabGPP = follower.pathBuilder()
                .addPath(new BezierLine(yeetPose,beforePPGpose))
                .setLinearHeadingInterpolation(yeetPose.getHeading(), beforePPGpose.getHeading())
                .addPath(new BezierLine(beforePPGpose, PPGpose))
                .setLinearHeadingInterpolation(beforePPGpose.getHeading(), PPGpose.getHeading())
                .build();

        // Move from PPG pose to yeet pose
        scoreGPP = follower.pathBuilder()
                .addPath(new BezierLine(PPGpose,beforePGPpose))
                .setLinearHeadingInterpolation(PPGpose.getHeading(), beforePGPpose.getHeading())
                .addPath(new BezierLine(beforePGPpose, yeetPose))
                .setLinearHeadingInterpolation(beforePGPpose.getHeading(), yeetPose.getHeading())
                .build();
    }


    // State machine for yeeting preload upon start of autonomous
    public void updateStateMachinePreload() {
        switch (pathState) {
            case 0:
                // Move to the yeet pose from the start pose
                log("State", "Auto pathing started");
                follower.followPath(scorePreload, true);
                setPathState(1);
                break;

            case 1:
                log("State", "Moving to yeet pose");

                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    log("State", "Arrived at yeet pose");
                    setPathState(-1); // finished
                }
                break;
        }
    }


    public void updateStateMachineGPP() {
        switch (pathState) {
            case 0:
                log("State", "Moving to GPP pose");
                yeeter.intakeOn();
                follower.followPath(grabGPP, true);
                setPathState(1);
                break;

            case 1:
                log("State", "Arrived at GPP pose");
                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    log("State", "Moving to yeet");
                    yeeter.intakeOff();
                    follower.followPath(scoreGPP, true);
                    setPathState(2);
                }
                break;

            case 2:
                log("Moving to yeet position");

                if (!follower.isBusy()) {
                    log("State", "Arrived at yeet pose");
                    setPathState(-1);
                }
                break;
        }
    }


    public void updateStateMachinePGP() {
        switch (pathState) {
            case 0:
                log("State", "Moving to PGP pose");
                yeeter.intakeOn();
                follower.followPath(grabPGP, true);
                setPathState(1);
                break;

            case 1:
                log("State", "Arrived at PGP pose");

                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    log("State", "Moving to yeet");
                    yeeter.intakeOff();
                    follower.followPath(scorePGP, true);
                    setPathState(-1);
                }
                break;

            case 2:
                log("Moving to yeet position");

                if (!follower.isBusy()) {
                    log("State", "Arrived at yeet pose");
                    setPathState(-1);
                }
                break;
        }
    }


    public void updateStateMachinePPG() {
        switch (pathState) {
            case 0:
                log("State", "Moving to PPG pose");
                yeeter.intakeOn();
                follower.followPath(grabPPG, true);
                setPathState(1);
                break;

            case 1:
                log("Arrived at PPG pose");

                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    log("State", "Moving to yeet");
                    yeeter.intakeOff();
                    follower.followPath(scorePPG, true);
                    setPathState(-1);
                }
                break;

            case 2:
                log("Moving to yeet position");

                if (!follower.isBusy()) {
                    log("State", "Arrived at yeet pose");
                    setPathState(-1);
                }
                break;
        }
    }


    // State machine handling the yeeting of elements in autonomous
// Assumes yeet position is always the same throughout autonomous
    public void updateStateMachineYeet() {
        // Set yeeter powers and position
        final double firstElementYeetPower = 0.76;
        final double secondElementYeetPower = 0.705;
        final int yeetPosition = 295;

        switch (pathState) {
            case 0:
                if (!follower.isBusy()) {
                    follower.pausePathFollowing();
                    setPathState(1);
                }
                break;

            case 1:
                log("State", "Yeeting elements");
                yeeter.yeetAllElementsAuto(firstElementYeetPower, secondElementYeetPower, yeetPosition);
                if (!yeeter.isLaunching()) {
                    yeeter.resetPark(); // Reset park state machine so it runs only once
                    setPathState(2);
                }
                break;

            case 2:
                log("State", "Parking yeeter");
                yeeter.park();
                if (yeeter.getYeeterParkState() == Yeeter.State.COMPLETE) {
                    log("State", "Yeet complete");
                    follower.resumePathFollowing();

                    masterState = masterStateEnum.PGP;

                    // Increment yeet count
                    yeetCount += 1;

                    setPathState(-1); // finished
                }
                break;
        }
    }


    public void setPathState(int pState) {
        pathState = pState;
    }


    // Custom logging function to support telemetry and Panels
    private void log(String caption, Object... text) {
        if (text.length == 1) {
            telemetry.addData(caption, text[0]);
            panelsTelemetry.debug(caption + ": " + text[0]);
        } else if (text.length >= 2) {
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
}
