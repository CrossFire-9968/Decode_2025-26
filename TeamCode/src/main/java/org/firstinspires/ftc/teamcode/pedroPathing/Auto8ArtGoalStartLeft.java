package org.firstinspires.ftc.teamcode.pedroPathing;

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

@Autonomous(name = "Auto8ArtGoalStartLeft")
public class Auto8ArtGoalStartLeft extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;
    public Yeeter yeeter = new Yeeter();

    private final Pose startPose = new Pose(30,128, Math.toRadians(90));
    private final Pose scorePose = new Pose(65,77, Math.toRadians(142));
    private final Pose pickup1Pose = new Pose(29,82, Math.toRadians(180));
    private final Pose beforepickup2Pose = new Pose(54.5,57, Math.toRadians(180));
    private final Pose pickup2Pose = new Pose(23,55.5, Math.toRadians(180));
    private final Pose beforepickup3Pose = new Pose(54.5,36, Math.toRadians(180));
    private final Pose pickup3Pose = new Pose(23,36, Math.toRadians(180));
    private final FuturePose Curve1 = new Pose(84,46, Math.toRadians(180));

    private Path scorePreload;
    private PathChain scorePickup1, grabPickup2, scorePickup2, grabPickup3, scorePickup3, grabPickup4, scorePickup4;

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .addParametricCallback(60, (yeeter.launchAllRunnable(0.8, 260)))
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading(), 0.4)
                .addParametricCallback(50, yeeter.intakeRunnable() )
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading(), 0.4)
                .addParametricCallback(50, yeeter.parkRunnable() )
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
                follower.followPath(scorePreload);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup1, true);
                    setPathState(2);
                }
                break;

            //  Add a 2-second delay before grabPickup2
            case 2:
                if (!follower.isBusy()) {
                    pathTimer.resetTimer();
                    setPathState(3);
                }
                break;

            case 3:
                if (pathTimer.getElapsedTimeSeconds() > 2.0) { // change time here
                    follower.followPath(grabPickup2, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup2, true);
                    setPathState(5);
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup3, true);
                    setPathState(6);
                }
                break;

            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup3, true);
                    setPathState(7);
                }
                break;

            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup4, true);
                    setPathState(8);
                }
                break;

            case 8:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup4, true);
                    setPathState(9);
                }
                break;

            case 9:
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
