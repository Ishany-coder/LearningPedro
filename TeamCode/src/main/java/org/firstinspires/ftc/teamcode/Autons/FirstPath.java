package org.firstinspires.ftc.teamcode.Autons;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//ALL HEADING VALUES ARE IN RADIANS
public class FirstPath extends LinearOpMode {
    private int pathState = 0;
    public static Pose InitPose = new Pose(56, 8, Math.toRadians(90));
    public static Pose control1 = new Pose(50,40);
    public static Pose PICKUP1 = new Pose(10, 36, Math.toRadians(180));
    public static Pose SHOOTPOSE = new Pose(77, 8, Math.toRadians(90));
    public static Pose control2 = new Pose(60, 65);
    public static Pose PICKUP2 = new Pose(10, 60, Math.toRadians(180));
    public PathChain intakeFirstSpike;
    public PathChain shootFirstSpike;
    public PathChain intakeSecondSpike;
    public PathChain shootSecondSpike;
    public Follower follower;
    @Override
    public void runOpMode() throws InterruptedException {
        follower.setStartingPose(InitPose);
        BuildPaths(follower);
        waitForStart();
        while(opModeIsActive()){
            follower.update();
            followPaths(follower);
        }
    }
    public void BuildPaths(Follower follwer){
//        intakeFirstSpike = follwer.pathBuilder()
//                .addPath(new BezierLine(InitPose, TURN_TO_PICKUP1))
//                .setLinearHeadingInterpolation(InitPose.getHeading(), TURN_TO_PICKUP1.getHeading(), 0.8) //might have to tune the end time value
//                .addPath(new BezierLine(TURN_TO_PICKUP1, PICKUP1))
//                .setConstantHeadingInterpolation(Math.toRadians(180))
//                .build();
        intakeFirstSpike = follwer.pathBuilder()
                .addPath(new BezierCurve(InitPose, control1, PICKUP1))
                .setTangentHeadingInterpolation() // by default paths use tangent heading just in case added this
                .build();
        shootFirstSpike = follwer.pathBuilder()
                .addPath(new BezierLine(PICKUP1, SHOOTPOSE))
                .setLinearHeadingInterpolation(PICKUP1.getHeading(), SHOOTPOSE.getHeading(), 0.8) // end time by default and recomended value in pedro is 0.8
                .build();
        intakeSecondSpike = follwer.pathBuilder()
                .addPath(new BezierCurve(SHOOTPOSE, control2, PICKUP2))
                .setLinearHeadingInterpolation(SHOOTPOSE.getHeading(), PICKUP2.getHeading())
                .build();
        shootSecondSpike = follwer.pathBuilder()
                .addPath(new BezierLine(PICKUP2, SHOOTPOSE))
                .setLinearHeadingInterpolation(PICKUP2.getHeading(), SHOOTPOSE.getHeading(), 0.8) // end time by default and recomended value in pedro is 0.8
                .build();
    }
    public void followPaths(Follower follower){
        switch(pathState){
            case(0):
                follower.followPath(intakeFirstSpike);
                if(!follower.isBusy()){
                    pathState += 1;
                    break;
                }
            case(1):
                follower.followPath(shootFirstSpike);
                if(!follower.isBusy()){
                    pathState += 1;
                    break;
                }
            case(2):
                follower.followPath(intakeSecondSpike);
                if(!follower.isBusy()){
                    pathState += 1;
                    break;
                }
            case(3):
                follower.followPath(shootSecondSpike);
                if(!follower.isBusy()){
                    pathState = -1;
                    break;
                }
        }
    }
}
