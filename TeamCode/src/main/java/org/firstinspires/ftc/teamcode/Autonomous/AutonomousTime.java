package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "AutonomousTime", group = "Iterative OpMode")
public class AutonomousTime extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    private DcMotor intake;
    private DcMotor flywheel;

    private Servo wobble;
    private Servo close;
    private Servo flick;

    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY =
            "Ae/tYNP/////AAABmWJ3jgvBrksYtYG8QcdbeqRWGQWezSnxje7FgEIzwTeFQ1hZ42y6YmaQ0h5p7aqN9x+q1QXf2zRRrh1Pxln3C2cR+ul6r9mHwHbTRgd3jyggk8tzc/ubgaPBdn1q+ufcYqCk6tqj7t8JNYM/UHLZjtpSQrr5RNVs227kQwBoOx6l4MLqWL7TCTnE2vUjgrHaEW1sP1hBsyf1D4SiyRl/Ab1Vksqkgv7hwR1c7J4+7+Nt3rDd16Fr2XToT87t0JlfOn6vszaPj10qvU7836U+/rx9cs1w53UPEdfF+AmDChhdW2TymZf+aS2QfnckyxdXKHjXUhdDw3f09BegsNdnVxXnvGkp0jhg9N7fjJa39k+8";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {

        initVuforia();
        initTfod();

        telemetry.update();
        //waitForStart();

        String zone = "";

        double fBack = 1;
        double fShoot = .78;
        double wUp = .5;
        double wDown = 0;
        double cClose = .85;
        double cOpen = 0;

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(2.5, 16.0/9.0);
        }

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        intake = hardwareMap.get(DcMotor.class, "intake");
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");

        wobble = hardwareMap.servo.get("wobble");
        close = hardwareMap.servo.get("close");
        flick = hardwareMap.servo.get("flick");

        AMT chad = new AMT(leftFront, rightFront, leftBack, rightBack);

        flick.setPosition(fBack);
        wobble.setPosition(wDown);
        close.setPosition(cClose);

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (!opModeIsActive() && !isStopRequested()) {
            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 0) {
                        zone = "A";
                        telemetry.addData("TFOD", "No items detected.");
                        telemetry.addData("Target Zone", "A");

                    } else {
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());

                            if (recognition.getLabel().equals("Single")) {
                                zone = "B";
                                telemetry.addData("Target Zone", "B");
                            } else if (recognition.getLabel().equals("Quad")) {
                                zone = "C";
                                telemetry.addData("Target Zone", "C");
                            } else {
                                telemetry.addData("Target Zone", "UNKNOWN");
                            }
                        }
                    }
                    telemetry.update();
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }

        waitForStart();
        tfod.shutdown();

        telemetry.clearAll();
        telemetry.addData("Zone", zone);
        telemetry.update();

        // drives to powershots (off)
        /*
        chad.driveTFB(.65, 160);
        sleep(50);
        chad.rotateT(-.65, 160);
        sleep(10);
        chad.driveTFB(.8, 2250);
        flywheel.setPower(-1);
        chad.rotateT(.65, 140);
         */

        // drives to shoot in the goals
        chad.driveTFB(.8, 2800);
        sleep(10);
        chad.driveTLR(.35, 625);
        flywheel.setPower(-1);
        sleep(500);
        flick.setPosition(fShoot);
        sleep( 450);
        flick.setPosition(fBack);
        sleep(650);
        flick.setPosition(fShoot);
        sleep( 450);
        flick.setPosition(fBack);
        sleep(650);
        flick.setPosition(fShoot);
        sleep( 450);
        flick.setPosition(fBack);
        sleep(650);
        chad.driveTFB(.65, 650);


        //chad.driveTFB(.8, 600);

        // determines the rest of the code based on the starting stack of rings
        /*
        switch (zone) {
            case "A":
                chad.rotateT(.8, 500);
                sleep(10);
                chad.driveTFB(.8, 850);
                sleep(10);
                break;
            case "B":
                chad.driveTFB(.8, 2000);
                sleep(10);
                break;
            case "C":
                chad.rotateT(.8, 150);
                sleep(10);
                chad.driveTFB(.8, 2800);
                sleep(10);
                chad.rotateT(.75, 300);
                break;
        }

        //wobble.setPosition(0);
        //sleep(500);
        close.setPosition(cOpen);
        sleep(500);

        if (zone.equals("A")) {
            chad.driveTime(.75, 0, .75, 0, 250);
            sleep(10);
            chad.rotateT(-.7, 250);
            chad.rotateT(.7, 275);
        }
        else if (zone.equals("B")) {
            chad.driveTLR(.65, 250);
        }

        if (zone.equals("A")) {
            chad.rotateT(-.8, 500);
            sleep(10);
            chad.driveTFB(-.8, 500);
            sleep(10);
        }
        else if (zone.equals("B")) {
            chad.driveTFB(-.8, 500);
            sleep(10);
        }
        else if (zone.equals("C")) {
            chad.rotateT(-.8, 250);
            sleep(10);
            chad.driveTFB(-.8, 1000);
            sleep(10);
        }

        chad.driveTFB(-.8, 3000);
        sleep(10);
        wobble.setPosition(wUp);
        sleep(10);
        close.setPosition(cOpen);
        sleep(10);
        chad.driveTFB(-.8, 50);
        sleep(10);
        wobble.setPosition(wDown);
        sleep(10);
        close.setPosition(cClose);
        sleep(10);
        chad.driveTFB(-.8, 3000 + 50);
        sleep(10);
        if (zone.equals("A")) {
            chad.rotateT(.8, 500);
            sleep(10);
            chad.driveTFB(.8, 500);
            sleep(10);
        }
        else if (zone.equals("B")) {
            chad.driveTFB(.8, 500);
            sleep(10);
        }
        else if (zone.equals("C")) {
            chad.rotateT(.8, 250);
            sleep(10);
            chad.driveTFB(.8, 1000);
            sleep(10);
        }
        wobble.setPosition(wDown);
        sleep(10);
        close.setPosition(cOpen);
        sleep(10);
        chad.driveTFB(.8, 50);
        sleep(10);
        wobble.setPosition(wUp);
        sleep(10);
        close.setPosition(cClose);
        sleep(10);

         */
    }

    private void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfodParameters.useObjectTracker = false;

        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
