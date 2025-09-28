package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intake {

    public DcMotor outputMotor_L;
    public DcMotor outputMotor_R;

    public void init(HardwareMap hwMap)
    {
        outputMotor_L = hwMap.get(DcMotor.class, "Motor_LF");
        outputMotor_L.setDirection(DcMotorSimple.Direction.FORWARD);
        outputMotor_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        outputMotor_R = hwMap.get(DcMotor.class, "Motor_RF");
        outputMotor_R.setDirection(DcMotorSimple.Direction.REVERSE);
        outputMotor_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.setOutputMotorPowers(0.0);
    }

    protected void setOutputMotorPowers(double power)
    {
        outputMotor_L.setPower(power);
        outputMotor_R.setPower(power);
    }
}
