package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Flywheel" + "", group="Iterative Opmode")
public class Flywheel extends OpMode
{
    private DcMotor flywheel;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        flywheel  = hardwareMap.get(DcMotor.class, "flywheel");

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
        telemetry.addData("lefty1", lefty1);

        if (Math.abs(lefty1) > .1) {
            flywheel.setPower(lefty1);
        } else {
            flywheel.setPower(0);
        }

        // Ensures Data Updates
        telemetry.update();
    }

    @Override
    public void stop() {
    }

}