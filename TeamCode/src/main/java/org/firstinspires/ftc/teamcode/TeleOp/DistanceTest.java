package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "DistanceTest", group = "Iterative OpMode")
public class DistanceTest extends OpMode {

    private DistanceSensor testSensor;

    @Override
    public void init() {
        testSensor = hardwareMap.get(DistanceSensor.class, "sensorTest");
        //Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)testSensor;
    }

    @Override
    public void loop() {
        double range = testSensor.getDistance(DistanceUnit.METER);
        telemetry.addData("range", range);
        telemetry.update();
    }

    @Override
    public void stop() {}
}

