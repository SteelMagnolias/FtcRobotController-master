package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Flywheel" + "", group="Iterative Opmode")
public class Flywheel extends OpMode
{
    private DcMotor flywheel;
    private DcMotor carousel;
    private DcMotor intake;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        flywheel  = hardwareMap.get(DcMotor.class, "flywheel");
        //carousel = hardwareMap.get(DcMotor.class, "carousel");
        intake = hardwareMap.get(DcMotor.class, "intake");

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
        boolean lb = gamepad1.left_bumper;
        boolean rb = gamepad1.right_bumper;
        telemetry.addData("lefty1", lefty1);

        if (lb) {
            flywheel.setPower(-1);
        } else {
            flywheel.setPower(0);
        }
        /*
        // Carousel
        if (lb)
            carousel.setPower(0);
        else
            carousel.setPower(-1);
        */
        // Intake
        /*if (rb)
            intake.setPower(0);
        else
            intake.setPower(1);
        */
        // Ensures Data Updates
        telemetry.update();
    }

    @Override
    public void stop() {
    }

}