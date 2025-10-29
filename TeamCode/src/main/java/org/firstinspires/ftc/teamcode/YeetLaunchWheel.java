package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class YeetLaunchWheel
{

    public DcMotor yeetMotor;

    public void init(HardwareMap hwMap)
    {
        yeetMotor = hwMap.get(DcMotor.class, "Motor_Yeet");
        yeetMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        yeetMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.stop();
    }

    public void intakeSpeed() {
        double intakePower = -0.3;
        yeetMotor.setPower(intakePower);
    }

    public void launchSpeed() {
        double launchPower = 1.0;
        yeetMotor.setPower(launchPower);
    }

    public void stop() {
        double stopPower = 0.0;
        yeetMotor.setPower(stopPower);
    }
}