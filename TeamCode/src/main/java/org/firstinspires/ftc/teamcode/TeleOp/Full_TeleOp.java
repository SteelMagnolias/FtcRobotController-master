package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="Full_TeleOp", group="Iterative Opmode")
public class Full_TeleOp extends OpMode
{
    public enum Alignment {
        START,
        ALIGNMENT_TRUE,
        ALIGNMENT_FALSE
    }
    public enum GrabberArm {
        START,
        GRAB,
        LIFT_INITIAL,
        RELEASE,
        LIFT_FINAL
    }

    Alignment alignment = Alignment.START;
    GrabberArm grabberArm = GrabberArm.START;

    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor intake;
    private DcMotorEx flywheel;
    private Servo flick;
    private Servo wobble;
    private Servo close;
    private CRServo push;

    private double wStart;
    private double wEnd;

    private double previousError;
    private double previousTime;
    private double flickTime;
    private double ticks;
    private double targetSpeed;

    /*
    private DistanceSensor disSensorFront;
    private DistanceSensor disSensorLeft;
    private DistanceSensor disSensorRight;
    private DistanceSensor disSensorBack;
    */
    double target;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftBack  = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        intake = hardwareMap.get(DcMotor.class, "intake");
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flick = hardwareMap.get(Servo.class, "flick");
        wobble = hardwareMap.get(Servo.class, "wobble");
        flick = hardwareMap.get(Servo.class, "flick");
        close = hardwareMap.get(Servo.class, "close");
        push = hardwareMap.get(CRServo.class, "push");
        /*
        disSensorFront = hardwareMap.get(DistanceSensor.class, "sensorFront");
        disSensorLeft = hardwareMap.get(DistanceSensor.class, "sensorLeft");
        disSensorRight = hardwareMap.get(DistanceSensor.class, "sensorRight");
        disSensorBack = hardwareMap.get(DistanceSensor.class, "sensorBack");
        */

        wStart = .5;
        wEnd = 0;

        previousError = 0;
        previousTime = 0;
        flickTime = 0;
        ticks = 0;

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        flywheel.setDirection(DcMotor.Direction.REVERSE);

        // Motor encoder setup
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        flick.setPosition(1);
        wobble.setPosition(wStart);
        close.setPosition(.85);

        push.setPower(0);

        flywheel.setPower(0);
        intake.setPower(0);

        target = 0;
        targetSpeed = 1500;
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    @Override
    public void loop()
    {
        // Assigning & Data
        double lefty1 = -(gamepad1.left_stick_y);
        double leftx1 = gamepad1.left_stick_x;
        double rightx1 = gamepad1.right_stick_x;
        boolean buttonUp = gamepad1.dpad_up;
        boolean buttonDown = gamepad1.dpad_down;
        boolean buttonLeft = gamepad1.dpad_left;
        boolean buttonRight = gamepad1.dpad_right;
        boolean lb = gamepad1.left_bumper;
        boolean rb = gamepad1.right_bumper;
        boolean rb2 = gamepad2.right_bumper;
        boolean lb2 = gamepad2.left_bumper;
        boolean a1 = gamepad1.a;
        boolean a2 = gamepad2.a;
        boolean b2 = gamepad2.b;
        boolean x2 = gamepad2.x;
        boolean y2 = gamepad2.y;
        boolean buttonLeft2 = gamepad2.dpad_left;
        boolean buttonUp2 = gamepad2.dpad_up;
        boolean buttonDown2 = gamepad2.dpad_down;
        telemetry.addData("lefty1", lefty1);
        telemetry.addData("leftx1", leftx1);
        telemetry.addData("rightx1", rightx1);
        telemetry.addData("lefty1", lefty1);
        telemetry.addData("leftx1", leftx1);
        telemetry.addData("rightx1", rightx1);
        telemetry.addData("buttonUp", buttonUp);
        telemetry.addData("buttonDown", buttonDown);
        telemetry.addData("buttonRight", buttonRight);
        telemetry.addData("buttonLeft", buttonLeft);
        telemetry.addData("lb", lb);
        telemetry.addData("rb", rb);
        telemetry.addData("a1", a1);
        telemetry.addData("a2", a2);
        telemetry.addData("b2", b2);
        telemetry.addData("x2", x2);
        telemetry.addData("y2", y2);
        telemetry.addData("80% power", targetSpeed);
        telemetry.addData("buttonLeft2", buttonLeft2);
        /*
        double frontDistance = disSensorFront.getDistance(DistanceUnit.METER);
        double leftDistance = disSensorLeft.getDistance(DistanceUnit.METER);
        double rightDistance = disSensorRight.getDistance(DistanceUnit.METER);
        double backDistance = disSensorBack.getDistance(DistanceUnit.METER);

        telemetry.addData("front distance", frontDistance);
        telemetry.addData("left distance", leftDistance);
        telemetry.addData("right distance", rightDistance);
        telemetry.addData("back distance", backDistance);
        */

      /*  if (getRuntime() < 3000) flywheel.setPower(.8);
        else if (getRuntime() < 4000) ticks = flywheel.getVelocity();
        else if (getRuntime() < 4100) flywheel.setPower(0);
      */

        double rm = rightx1;
        if (rm > -.1 && rm < .1) rm = 0;
        double pow;
        if (a1) pow = 1; // turbo mode
        else pow = .8;
        double c = Math.hypot(leftx1,lefty1);
        double perct = pow * c;
        if (c <= .1) perct = 0;
        double theta;

        if (leftx1 <=0 && lefty1 >= 0 )  {
            theta = Math.atan(Math.abs(leftx1)/Math.abs(lefty1));
            theta += (Math.PI/2);
        } else
        if (leftx1 <0 && lefty1 <= 0 )  {
            theta = Math.atan(Math.abs(lefty1)/Math.abs(leftx1));
            theta += (Math.PI);
        } else
        if (leftx1 >= 0 && lefty1 < 0 )  {
            theta = Math.atan(Math.abs(leftx1)/Math.abs(lefty1));
            theta += (3*Math.PI/2);
        } else {
            theta = Math.atan(Math.abs(lefty1)/Math.abs(leftx1));
        }

        double dir = 1;
        if (theta >= Math.PI) {
            theta -= Math.PI;
            dir = -1;
        }
        //if (leftx1 <= 0 && lefty1 >= 0 || leftx1 >= 0 && lefty1 <= 0){
        //   theta += (Math.PI/2);
        //}

        telemetry.addData("pow", pow);
        telemetry.addData("rm", rm);
        telemetry.addData("dir", dir);
        telemetry.addData("c", c);
        telemetry.addData("theta", theta);

        double fr = dir*((theta-(Math.PI/4))/(Math.PI/4));
        if (fr > 1) fr = 1;
        if(fr < -1) fr = -1;
        fr = (perct * fr);
        if (leftx1 == 0 && lefty1 == 0) fr = 0;

        double bl = dir*((theta-(Math.PI/4))/(Math.PI/4));
        if (bl > 1) bl = 1;
        if (bl < -1) bl = -1;
        bl = (perct * bl);
        if (leftx1 < .1 && leftx1 > -.1 && lefty1 < .1 && lefty1 > -.1) bl = 0;

        double fl = -dir*((theta-(3*Math.PI/4))/(Math.PI/4));
        if (fl > 1) fl = 1;
        if (fl < -1) fl = -1;
        fl = (perct * fl);
        if (leftx1 < .1 && leftx1 > -.1 && lefty1 < .1 && lefty1 > -.1) fl = 0;

        double br = -dir*((theta-(3*Math.PI/4))/(Math.PI/4));
        if (br > 1) br = 1;
        if (br < -1) br = -1;
        br = (perct * br);
        if (leftx1 < .1 && leftx1 > -.1 && lefty1 < .1 && lefty1 > -.1) br = 0;

        telemetry.addData("fl", fl);
        telemetry.addData("fr", fr);
        telemetry.addData("bl", bl);
        telemetry.addData("br", br);

        telemetry.addData("rlf", -dir*((theta-(3*Math.PI/4))/(Math.PI/4)));
        telemetry.addData("rrf", dir*((theta-(3*Math.PI/4))/(Math.PI/4)));
        telemetry.addData("rbl", dir*((theta-(3*Math.PI/4))/(Math.PI/4)) );
        telemetry.addData("rbr", -dir*((theta-(3*Math.PI/4))/(Math.PI/4)));

        leftFront.setPower(fl + rightx1);
        leftBack.setPower(bl + rightx1);
        rightFront.setPower(fr - rightx1);
        rightBack.setPower(br - rightx1);

        // Below: precision (slower) movement
        pow *= 0.5;
        if (buttonUp){
            // slowly moves forwards
            leftFront.setPower(pow);
            leftBack.setPower(pow);
            rightFront.setPower(pow);
            rightBack.setPower(pow);
        }
        else if (buttonDown){
            // slowly moves backwards
            leftFront.setPower(-pow);
            leftBack.setPower(-pow);
            rightFront.setPower(-pow);
            rightBack.setPower(-pow);
        }
        else if (buttonRight){
            // slowly moves right
            leftFront.setPower(pow);
            leftBack.setPower (-pow);
            rightFront.setPower (-pow);
            rightBack.setPower(pow);
        }
        else if (buttonLeft){
            // slowly moves left
            leftFront.setPower(-pow);
            leftBack.setPower (pow);
            rightFront.setPower (pow);
            rightBack.setPower(-pow);
        }
        else {
            // stops movement
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
        }

        if (rb){
            // slowly moves clockwise
            leftFront.setPower(pow);
            leftBack.setPower (pow);
            rightFront.setPower (-pow);
            rightBack.setPower(-pow);
        }
        else if (lb) {
            // slowly moves counter-clockwise
            leftFront.setPower(-pow);
            leftBack.setPower(-pow);
            rightFront.setPower(pow);
            rightBack.setPower(pow);
        }
        else {
            // stops movement
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
        }

        if (rb2 && flywheel.getVelocity() > (targetSpeed - 10) && flywheel.getVelocity() < (targetSpeed + 10) && getRuntime() - flickTime <= 1000) {
            flick.setPosition(.78);
            flickTime = getRuntime();
        }
        else flick.setPosition(1);

        /*
        if (b2) wobble.setPosition(0);
        else wobble.setPosition(.55);

        if (rb2) close.setPosition(0);
        else close.setPosition(.85);
        */

        /* Flywheel
        if (lb2) flywheel.setPower(1);
        else flywheel.setPower(0);
        */

        if (buttonLeft2 && !lb2) {
            flywheel.setPower(.8);
            targetSpeed = flywheel.getVelocity();
        }
        else if (!lb2) flywheel.setPower(0);

        telemetry.addData("velocity", flywheel.getVelocity());

        double currentSpeed = flywheel.getVelocity();
        double currentTime = getRuntime();
        double currentError = targetSpeed - currentSpeed;
        final double kp = 1.2;
        final double ki = 1.6;
        final double kd = 0;
        double p = kp * currentError;
        double i = ki * (currentError * (currentTime - previousTime));
        double d = kd * (currentError - previousError) / (currentTime - previousTime);
        double output = p + i + d;
        previousError = currentError;
        previousTime = currentTime;
        if (lb2) flywheel.setVelocity(flywheel.getVelocity() + output);

        /*
        double flypow;
        if (flywheel.getPower() < .5) flypow = 0;
        else flypow = 1;
        if (lb2 && flypow == target) {
            if (flypow == 0) {
                flywheel.setPower(.8);
                target = 1;
            } else {
                flywheel.setPower(0);
                target = 0;
            }
        }
        */

        telemetry.addData("flywheel", flywheel.getPower());

        // Intake
        if (buttonUp2) {
            intake.setPower(1);
            push.setPower(1);
        }
        else if (buttonDown2) {
            intake.setPower(-1);
            push.setPower(-1);
        }
        else {
            intake.setPower(0);
            push.setPower(0);
        }

        // Ensures Data Updates
        telemetry.update();

        /*  SERVO POSITIONS KEY:
         *  wobble: up = .55, down = 0
         *  close: close = 0, open = .85
         */
        switch(grabberArm) {
            case START:
                if (x2) {
                    wobble.setPosition(wEnd);
                    close.setPosition(0);
                    grabberArm = GrabberArm.GRAB;
                }
                break;
            case GRAB:
                if (b2) {
                    close.setPosition(.85);
                    grabberArm = GrabberArm.LIFT_INITIAL;
                }
                break;
            case LIFT_INITIAL:
                if (x2) {
                    wobble.setPosition(wStart);
                    grabberArm = GrabberArm.RELEASE;
                }
                break;
            case RELEASE:
                if (b2) {
                    wobble.setPosition(wEnd);
                    close.setPosition(0);
                    grabberArm = GrabberArm.LIFT_FINAL;
                }
                break;
            case LIFT_FINAL:
                if (a2) {
                    close.setPosition(.85);
                    wobble.setPosition(wStart);
                    grabberArm = GrabberArm.START;
                }
                break;
            default:
                grabberArm = GrabberArm.START;
        }

        if (y2 && grabberArm != GrabberArm.START) {
            grabberArm = GrabberArm.START;
        }
        if (a2 && grabberArm != GrabberArm.START && grabberArm != GrabberArm.LIFT_FINAL) {
            grabberArm = GrabberArm.LIFT_FINAL;
        }
    }
    @Override
    public void stop() {
    }

}