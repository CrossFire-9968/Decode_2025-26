package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "Manual")
public class Manual extends OpMode {
    public Mecanum mecanum = new Mecanum();

    public void init() {
        mecanum.init(hardwareMap);
    }


    @Override
    public void loop() {
        mecanum.manualDrive(gamepad1, telemetry);
        mecanum.getMotorTelemetry(telemetry);
        telemetry.update();
    }
}