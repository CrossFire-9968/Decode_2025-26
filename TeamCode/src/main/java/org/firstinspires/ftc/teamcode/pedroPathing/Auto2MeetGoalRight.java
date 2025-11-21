package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.FuturePose;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Yeeter;

@Configurable
@Autonomous(name = "Auto2MeetGoalRight")
public class Auto2MeetGoalRight extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState = 0;
    public Yeeter yeeter = new Yeeter();

    private final Pose startPose = new Pose(110,120, Math.toRadians(90)); // Start Pose of our robot.
    private final Pose scorePose = new Pose(77,77, Math.toRadians(48)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose beforePickup1Pose = new Pose(84,80.5, Math.toRadians(0));
    private final Pose pickup1Pose = new Pose(118,80.5, Math.toRadians(0)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose beforepickup2Pose = new Pose(87.5,59, Math.toRadians(0));
    private final Pose pickup2Pose = new Pose(119,57.5, Math.toRadians(0)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose beforepickup3Pose = new Pose(87.5,37.5, Math.toRadians(0));
    private final Pose pickup3Pose = new Pose(119,36, Math.toRadians(0)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final FuturePose Curve1 = new Pose(84,46, Math.toRadians(0));


    private Path scorePreload;
    private PathChain scorePickup1, grabPickup2, scorePickup2, grabPickup3, scorePickup3, grabPickup4, scorePickup4;


    public void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .addPath(new BezierLine(scorePose, beforePickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), beforePickup1Pose.getHeading(), 0.4)
                //.addParametricCallback(0.2, yeeter::launchAllAuto)
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading(), 0.4)
                //.addParametricCallback(0.3, yeeter::intake)
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading(), 0.4)
                //.addParametricCallback(0.3, yeeter::park)
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
            // Move to first yeet position
            case 0:
                follower.followPath(scorePreload);
                setPathState(1);
                break;

            case 1:
                telemetry.addLine("Case1");
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup1, true);
                    pathTimer.resetTimer();
                    telemetry.addLine("Case1Busy");
                    setPathState(2);
                }
                break;


            //  Add a 2-second delay before grabPickup2
            case 2:
                telemetry.addLine("Case2");
                if (follower.isBusy()) //&& pathTimer.getElapsedTimeSeconds() > 0.5)
                {
                    follower.pausePathFollowing(); // Pause mid-path
                    //yeeter.launchAll(0.82, 280);
                    telemetry.addLine("Case2Busy");
                    setPathState(21);
                }
                break;

            case 21: // launching: keep calling launchAllAuto every loop until it finishes
                telemetry.addLine("Case 21");
                yeeter.launchAll(0.74, 280); // call every loop so Yeeter's timers progress

                // optional telemetry
                telemetry.addData("yeeterLaunching", yeeter.isLaunching());

                // once Yeeter finished, reset the timer and resume following
                if (!yeeter.isLaunching()) {
                    telemetry.addLine("LaunchComplete");
                    pathTimer.resetTimer();
//                    yeeter.intake();
                    follower.resumePathFollowing();
                    follower.followPath(grabPickup2, true);
                    if (!yeeter.isParking()) {
                        yeeter.park();
                        setPathState(4);
                    }
                }
                break;

//            case 3:
//                telemetry.addLine("Case3");
//                if (pathTimer.getElapsedTimeSeconds() > 2.0) { // change time here
//                    follower.resumePathFollowing();
//                    follower.followPath(grabPickup2, true);
//                    telemetry.addLine("Case3Busy");
//                    setPathState(4);
//                }
//                break;

            case 4:
                telemetry.addLine("Case 4");
                yeeter.intake();
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup2, true);
                    setPathState(41);
                }
                break;

            //  Add a 2-second delay before grabPickup2
            case 41:
                telemetry.addLine("Case41");
                if (!follower.isBusy()) //&& pathTimer.getElapsedTimeSeconds() > 0.5)
                {
                    follower.pausePathFollowing(); // Pause mid-path
                    //yeeter.launchAll(0.82, 280);
                    telemetry.addLine("Case41Busy");
                    setPathState(42);
                }
                break;

            case 42: // launching: keep calling launchAllAuto every loop until it finishes
                telemetry.addLine("Case 42");
                yeeter.launchAll(0.74, 280); // call every loop so Yeeter's timers progress

                // optional telemetry
                telemetry.addData("yeeterLaunching", yeeter.isLaunching());

                // once Yeeter finished, reset the timer and resume following
                if (!yeeter.isLaunching()) {
                    telemetry.addLine("LaunchComplete");
                    pathTimer.resetTimer();
//                    yeeter.intake();
                    follower.resumePathFollowing();
                    follower.followPath(grabPickup3, true);
                    if (!yeeter.isParking()) {
                        yeeter.park();
                        setPathState(9);
                    }
                }
                break;

            case 5:
                telemetry.addLine("Case 5");
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup3, true);
                    setPathState(51);
                }
                break;

            case 6:
                telemetry.addLine("Case 6");
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup3, true);
                    setPathState(7);
                }
                break;

            case 7:
                telemetry.addLine("Case 7");
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup4, true);
                    setPathState(8);
                }
                break;

            case 8:
                telemetry.addLine("Case 8");
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup4, true);
                    setPathState(9);
                }
                break;

            case 9:
                yeeter.park();
                telemetry.addLine("Case 9");
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



        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);
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
