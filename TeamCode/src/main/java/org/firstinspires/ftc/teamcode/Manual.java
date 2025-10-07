package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "Manual")
public class Manual extends OpMode {
    public Mecanum mecanum = new Mecanum();
    public intake intake = new intake();
    private double outputPower = 1.0;
    private double intakePower = -0.3;

    public void init() {
        mecanum.init(hardwareMap);
        intake.init(hardwareMap);
    }
    
    @Override
    public void loop() {
        mecanum.manualDrive(gamepad1, telemetry);
        mecanum.getMotorTelemetry(telemetry);

        if (gamepad2.left_bumper){
            intake.setOutputMotorPowers(outputPower);
        }
        else if (gamepad2.right_bumper){
            intake.setOutputMotorPowers(intakePower);
        }
        else {
            intake.setOutputMotorPowers(0.0);
        }
        telemetry.update();
    }


}