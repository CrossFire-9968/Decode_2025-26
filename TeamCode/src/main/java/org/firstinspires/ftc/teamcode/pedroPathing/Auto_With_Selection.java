package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.FuturePose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Yeeter;

@Configurable
@Autonomous(name = "Auto_With_Selection")
public class Auto_With_Selection extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState = 0;
    public Yeeter yeeter = new Yeeter();

    private Pose startPose = null;
    private Pose scorePose = null;
    private Pose beforePickup1Pose = null;
    private Pose pickup1Pose = null;
    private Pose beforepickup2Pose = null;
    private Pose pickup2Pose = null;
    private Pose beforepickup3Pose = null;
    private Pose pickup3Pose = null;
    private FuturePose Curve1 = null;

    private Path scorePreload;
    private PathChain scorePickup1, grabPickup2, scorePickup2, grabPickup3, scorePickup3, grabPickup4, scorePickup4;

    private double firstElementYeetPower = 0.75;
    private double secondElementYeetPower = 0.75;
    private int yeetPosition = 280;

    private enum Alliance {BLUE, RED, UNKNOWN}
    Alliance alliance = Alliance.UNKNOWN;
    private enum AutoStartLocation {GOAL, POINT, UNKNOWN}
    AutoStartLocation location = AutoStartLocation.UNKNOWN;
    boolean runBuild = false;
    String allianceSelected = "";
    String locationSelected = "";


    private void loadPreset(Alliance alliance, AutoStartLocation location) {
        if (alliance == Alliance.BLUE && location == AutoStartLocation.GOAL) {
            startPose = new Pose(26, 128.5, Math.toRadians(90));
            scorePose = new Pose(65, 79, Math.toRadians(125));
            beforePickup1Pose = new Pose(56, 89.3, Math.toRadians(180));
            pickup1Pose = new Pose(22, 88.3, Math.toRadians(180));
            beforepickup2Pose = new Pose(54.5, 57, Math.toRadians(180));
            pickup2Pose = new Pose(23, 58.5, Math.toRadians(180));
            beforepickup3Pose = new Pose(54.5, 36, Math.toRadians(180));
            pickup3Pose = new Pose(23, 36, Math.toRadians(180));
            Curve1 = new Pose(84, 46, Math.toRadians(180));
        }
        else if (alliance == Alliance.RED && location == AutoStartLocation.GOAL) {
            startPose = new Pose(110,120, Math.toRadians(90));
            scorePose = new Pose(77,77, Math.toRadians(48));
            beforePickup1Pose = new Pose(84,80.5, Math.toRadians(0));
            pickup1Pose = new Pose(118,80.5, Math.toRadians(0));
            beforepickup2Pose = new Pose(87.5,59, Math.toRadians(0));
            pickup2Pose = new Pose(119,57.5, Math.toRadians(0));
            beforepickup3Pose = new Pose(87.5,37.5, Math.toRadians(0));
            pickup3Pose = new Pose(119,36, Math.toRadians(0));
            Curve1 = new Pose(84,46, Math.toRadians(0));
        }
    }

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading(), 0.4)
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading(), 0.4)
                .build();

        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierCurve(scorePose, beforepickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), beforepickup2Pose.getHeading())
                .addPath(new BezierCurve(beforepickup2Pose, pickup2Pose))
                .setLinearHeadingInterpolation(beforepickup2Pose.getHeading(), pickup2Pose.getHeading())
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(pickup2Pose, beforepickup2Pose))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), beforepickup2Pose.getHeading())
                .addPath(new BezierLine(beforepickup2Pose, scorePose))
                .setLinearHeadingInterpolation(beforepickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup4 = follower.pathBuilder()
                .addPath(new BezierCurve(scorePose, beforepickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), beforepickup3Pose.getHeading())
                .addPath(new BezierCurve(beforepickup3Pose, pickup3Pose))
                .setLinearHeadingInterpolation(beforepickup3Pose.getHeading(), pickup3Pose.getHeading())
                .build();

        scorePickup4 = follower.pathBuilder()
                .addPath(new BezierLine(pickup3Pose, beforepickup3Pose))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), beforepickup3Pose.getHeading())
                .addPath(new BezierLine(beforepickup3Pose, scorePose))
                .setLinearHeadingInterpolation(beforepickup3Pose.getHeading(), scorePose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                telemetry.addLine("Case 1: Initiating move");
                follower.followPath(scorePickup1, true);
                setPathState(1);
                break;

            case 1:
                telemetry.addLine("Case 1: Moving to yeet position");

                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    telemetry.addLine("Case 1: Arrived at yeet position");
                    follower.pausePathFollowing();
                    setPathState(2);
                }
                break;

            case 2:
                telemetry.addLine("Case 2: Yeeting elements");
                yeeter.yeetAllElements(firstElementYeetPower, secondElementYeetPower, yeetPosition);


                // once Yeeter finished
                if (!yeeter.isLaunching()) {
                    telemetry.addLine("Case 2: Yeeting complete");
                    yeeter.resetPark(); // Reset park state machine so it runs only once
                    setPathState(3);
                }
                break;

            case 3:
                telemetry.addLine("Case 3: Parking yeeter");
                yeeter.park();
                if (yeeter.getYeeterParkState() == Yeeter.State.COMPLETE) {
                    telemetry.addLine("Case 3: Yeeter parked");
                    setPathState(4);
                }
                break;

            case 4:
                telemetry.addLine("Case 4: Initiating move to 1st pickup");
                yeeter.intake();
                follower.resumePathFollowing();
                follower.followPath(grabPickup2, true);
                setPathState(5);
                break;

            case 5:
                telemetry.addLine("Case 5: Arrived at 1st pickup");

                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    telemetry.addLine("Case 5: Initiating move to yeet");
                    follower.followPath(scorePickup2, true);
                    setPathState(6);
                }
                break;

            case 6:
                telemetry.addLine("Case 6: Moving to yeet position");

                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    telemetry.addLine("Case 6: Arrived at yeet position");
                    follower.pausePathFollowing();
                    setPathState(7);
                }
                break;

            case 7:
                telemetry.addLine("Case 7: Yeeting elements");
                yeeter.yeetAllElements(firstElementYeetPower, secondElementYeetPower, yeetPosition);

                // once Yeeter finished
                if (!yeeter.isLaunching()) {
                    telemetry.addLine("Case 7: Yeeting complete");
                    yeeter.resetPark(); // Reset park state machine so it runs only once
                    setPathState(8);
                }
                break;

            case 8:
                telemetry.addLine("Case 8: Parking yeeter");
                yeeter.park();
                if (yeeter.getYeeterParkState() == Yeeter.State.COMPLETE) {
                    telemetry.addLine("Case 8: Yeeter parked");
                    setPathState(9);
                }
                break;

            case 9:
                telemetry.addLine("Case 9: Initiating move to 2nd pickup");
//                yeeter.intake();
                follower.resumePathFollowing();
                follower.followPath(grabPickup3, true);
                setPathState(10);
                break;

            case 10:
                telemetry.addLine("Case 10: Moving to 2nd pickup");

                // Wait until yeet position reached
                if (!follower.isBusy()) {
                    telemetry.addLine("Case 10: Arrived at 2nd pickup");
                    setPathState(999);
                }
                break;

            case 999:
                telemetry.addLine("Case 999: Autonomous complete");
                if (!follower.isBusy()) {
                    setPathState(-1); // finished
                }
                break;
        }
    }


    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }


    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }


    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        yeeter.init(hardwareMap);
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
            }
            else if (gamepad1.dpad_down) {
                alliance = Alliance.RED;
                allianceSelected = "RED";
            }
        }
        else if (alliance != Alliance.UNKNOWN && location == AutoStartLocation.UNKNOWN) {
            telemetry.addLine("Alliance Selected: " + allianceSelected);
            telemetry.addLine("Select Start Location");
            telemetry.addLine("   Dpad Left: Goal");
            telemetry.addLine("   Dpad Right: Point");
            telemetry.addLine("   Circle: Start Over");

            if (gamepad1.dpad_left) {
                location = AutoStartLocation.GOAL;
                locationSelected = "GOAL";
            }
            else if (gamepad1.dpad_right) {
                location = AutoStartLocation.POINT;
                locationSelected = "POINT";
            }
            else if (gamepad1.circle) {
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
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
    }
}
