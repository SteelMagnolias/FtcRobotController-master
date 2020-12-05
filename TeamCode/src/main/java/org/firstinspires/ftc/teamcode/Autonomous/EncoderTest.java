package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous (name = "EncoderTest", group = "Iterative OpMode")
public class EncoderTest extends LinearOpMode {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    @Override
    public void runOpMode() throws InterruptedException {

        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        AM test = new AM(leftFront, rightFront, leftBack, rightBack);

        waitForStart();

        //test.driveFB();

        /*rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition(2500);
        rightFront.setTargetPosition(5000);
        leftBack.setTargetPosition(5000);
        rightBack.setTargetPosition(5000);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        leftFront.setPower(0.25);
        rightFront.setPower(0.25);
        leftBack.setPower(0.25);
        rightBack.setPower(0.25);

        while (leftFront.isBusy() && opModeIsActive()) idle();

        leftFront.setPower(0.0);
        rightFront.setPower(0.0);
        leftBack.setPower(0.0);
        rightBack.setPower(0.0);*/

        /*leftFront.setTargetPosition(0);
        rightFront.setTargetPosition(0);
        leftBack.setTargetPosition(0);
        rightBack.setTargetPosition(0);

        leftFront.setPower(0.25);
        rightFront.setPower(0.25);
        leftBack.setPower(0.25);
        rightBack.setPower(0.25);

        while (leftFront.isBusy() && opModeIsActive()) idle();

        leftFront.setPower(0.0);
        rightFront.setPower(0.0);
        leftBack.setPower(0.0);
        rightBack.setPower(0.0);*/
    }
}

/*
        leftFront.
        rightFront.
        leftBack.
        rightBack.
*/
