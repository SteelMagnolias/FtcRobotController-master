// 19_10.26 Diagonal Driving TeleOp

package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="Drive_Code", group="Iterative Opmode")
public class Drive_Code extends OpMode
{
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor rightFront;

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

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        boolean a1 = gamepad1.a;
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

        // Rotational Motion
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

        // Ensures Data Updates
        telemetry.update();
    }
    @Override
    public void stop() {
    }

}