package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Quick9ball extends LinearOpMode {
    public int pathState = 0;
    public Pose InitPose = new Pose(57, 8, Math.toRadians(90));
    public Pose ShootingPose = new Pose(57, 8, Math.toRadians(180)); //with turret we should have heading ready to intake next balls
    //ALL VALUES FOR FIRST INTAKE
    public Pose ControlForPickup1 = new Pose(60, 40);
    public Pose Pickup1 = new Pose(10,36, Math.toRadians(180));
    public PathChain IntakeFirstBalls;
    public PathChain ShootFirstBalls;
    //ALL VALUES FOR SECOND INTAKE
    public Pose Pickup2 = new Pose(8,8, Math.toRadians(180));
    public PathChain IntakeSecondBalls;
    public PathChain ShootSecondBalls;
    private Follower follower; // add files for when we actually tune
    @Override
    public void runOpMode() throws InterruptedException {
        buildPaths(follower);
        waitForStart();
        while(opModeIsActive()){
            follower.update();
            runPaths(follower);
        }
    }
    public void buildPaths(Follower follower){
        IntakeFirstBalls = follower.pathBuilder()
                .addPath(new BezierCurve(InitPose, ControlForPickup1, Pickup1))
                .setTangentHeadingInterpolation()
                .build();
        ShootFirstBalls = follower.pathBuilder()
                .addPath(new BezierLine(Pickup1, ShootingPose))
                .setLinearHeadingInterpolation(Pickup1.getHeading(), ShootingPose.getHeading())
                .build();
        IntakeSecondBalls = follower.pathBuilder()
                .addPath(new BezierLine(ShootingPose, Pickup2))
                .setConstantHeadingInterpolation(180)
                .build();
        ShootSecondBalls = follower.pathBuilder()
                .addPath(new BezierLine(Pickup2, ShootingPose))
                .setConstantHeadingInterpolation(180)
                .build();
    }
    public void runPaths(Follower follwer){
        switch(pathState){
            case 0:
                follwer.followPath(IntakeFirstBalls);
                if(!follwer.isBusy()){
                    pathState +=1;
                    break;
                }
            case 1:
                follwer.followPath(ShootFirstBalls);
                if(!follwer.isBusy()){
                    pathState +=1;
                    break;
                }
            case 2:
                follwer.followPath(IntakeSecondBalls);
                if(!follwer.isBusy()){
                    pathState +=1;
                    break;
                }
            case 3:
                follwer.followPath(ShootSecondBalls);
                if(!follwer.isBusy()){
                    pathState = -1;
                    break;
                }
        }
    }
}
