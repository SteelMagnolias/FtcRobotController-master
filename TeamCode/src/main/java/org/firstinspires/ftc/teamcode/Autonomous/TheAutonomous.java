package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "TheAutonomous", group = "Iterative OpMode")
public class TheAutonomous extends LinearOpMode {

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

        //test

        telemetry.update();
        //waitForStart();

        String zone = "";

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

        AM chad = new AM(leftFront, rightFront, leftBack, rightBack);

        flick.setPosition(.9);
        wobble.setPosition(.55);
        close.setPosition(.86);

        //telemetry.addData("Test", "test");
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

        //drive to x

        // encoder test stuff
        chad.driveFB(+10, .8);

        telemetry.addData("leftFront Target", leftFront.getTargetPosition());
        telemetry.addData("rightFront Target", rightFront.getTargetPosition());
        telemetry.addData("leftBack Target", leftBack.getTargetPosition());
        telemetry.addData("rightBack Target", rightBack.getTargetPosition());

        sleep (1000);

        chad.stopAll();

        telemetry.addData("leftFront Position", leftFront.getCurrentPosition());
        telemetry.addData("rightFront Position", rightFront.getCurrentPosition());
        telemetry.addData("leftBack Position", leftBack.getCurrentPosition());
        telemetry.addData("rightBack Position", rightBack.getCurrentPosition());

        // strafe and shoot at power-shots
        for (int i = 0; i < 2; i++) {
            chad.driveLR(-.23, .25);
            //shoot
        }

        // rotate and drive to correct zone
        chad.rotate(+0, .5);
        chad.driveFB(+0, .8);

        // drop the wobble, move away and rest the arm
        wobble.setPosition(0);
        chad.driveLR(+0, .35);
        wobble.setPosition(1);

        // if zone == B it is easier to get to single ring
        if (zone.equals("B")) {
            // rotate to face the rings
            chad.rotate(-0, .35);

            // grab ring and park on line
            chad.driveFB(+0, .6);
            chad.driveFB(-0, .8);
        }

        // drive backwards to park on launch line
        else {
            chad.driveFB(-0, .8);
        }


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
