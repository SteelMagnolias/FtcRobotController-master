package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "DistanceTest", group = "IterativeOpMode")
public class DistanceTest extends LinearOpMode {

    private DistanceSensor testSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        testSensor = hardwareMap.get(DistanceSensor.class, "sensor_range");
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)testSensor;

        telemetry.addData("range", String.format("%.01f mm", testSensor.getDistance(DistanceUnit.MM)));
        telemetry.update();
    }
}

