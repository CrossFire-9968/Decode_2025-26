package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intake {

    public DcMotor outputMotor;

    public void init(HardwareMap hwMap)
    {
        outputMotor = hwMap.get(DcMotor.class, "Motor_Yeet");
        outputMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        outputMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.setOutputMotorPowers(0.0);
    }

    protected void setOutputMotorPowers(double power)
    {
        outputMotor.setPower(power);
    }
}