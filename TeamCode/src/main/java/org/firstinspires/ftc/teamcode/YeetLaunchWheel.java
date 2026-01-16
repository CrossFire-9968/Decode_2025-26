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
        yeetMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        yeetMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.stop();
    }

    public void intakeSpeed(double power) {
        yeetMotor.setPower(power);
    }

    public void noPinchSpeed() {
        double pinchPower = -0.3;
        yeetMotor.setPower(pinchPower);
    }

    public void yeetPower(double power) {
        yeetMotor.setPower(power);
    }

    public void stop() {
        double stopPower = 0.0;
        yeetMotor.setPower(stopPower);
    }
}