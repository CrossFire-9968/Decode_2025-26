package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Mecanum
{
    public DcMotor motor_LR;
    public DcMotor motor_RR;
    public DcMotor motor_LF;
    public DcMotor motor_RF;
    double LFrontPower;
    double RFrontPower;
    double RRearPower;
    double LRearPower;

    public void init(HardwareMap hwMap)
    {
        motor_LF = hwMap.get(DcMotor.class, "Motor_LF");
        motor_LF.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor_RF = hwMap.get(DcMotor.class, "Motor_RF");
        motor_RF.setDirection(DcMotorSimple.Direction.REVERSE);
        motor_RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor_RR = hwMap.get(DcMotor.class, "Motor_RR");
        motor_RR.setDirection(DcMotorSimple.Direction.REVERSE);
        motor_RR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor_LR = hwMap.get(DcMotor.class, "Motor_LR");
        motor_LR.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_LR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.setAllMecanumPowers(0.0);
    }

    public void manualDrive(Gamepad gpad, Telemetry telemetry)
    {
        double turnSpeed = gpad.right_stick_x;
        double driveSpeed = gpad.left_stick_y;
        double strafeSpeed = gpad.right_trigger - gpad.left_trigger;

        telemetry.addData("rTrigger: ", gpad.right_trigger);
        telemetry.addData("lTrigger: ", gpad.left_trigger);

        //Motor powers labeled wrong
        // Raw drive power for each motor from joystick inputs
        LFrontPower = driveSpeed - turnSpeed - strafeSpeed;
        RFrontPower = driveSpeed + turnSpeed + strafeSpeed;
        RRearPower = driveSpeed + turnSpeed - strafeSpeed;
        LRearPower = driveSpeed - turnSpeed + strafeSpeed;

        // Find which motor power command is the greatest. If not motor
        // is greater than 1.0 (the max motor power possible) just set it by default
        // to 1.0 so the ratiometric calculation we do next does not
        // inadvertently increase motor powers.
        double max = 0.8;
        max = Math.max(max, Math.abs(LFrontPower));
        max = Math.max(max, Math.abs(RFrontPower));
        max = Math.max(max, Math.abs(RRearPower));
        max = Math.max(max, Math.abs(LRearPower));

        // Ratiometric calculation that proportionally reduces all powers in cases where on
        // motor input is greater than 1.0. This keeps the driving feel consistent to the driver.
        LFrontPower = (LFrontPower / max);
        RFrontPower = (RFrontPower / max);
        RRearPower = (RRearPower / max);
        LRearPower = (LRearPower / max);

        // Set motor speed
        setEachMecanumPower(LFrontPower, RFrontPower, RRearPower, LRearPower);
    }

    // Set all mecanum powers
    protected void setAllMecanumPowers(double power)
    {
        motor_LF.setPower(power);
        motor_RF.setPower(power);
        motor_RR.setPower(power);
        motor_LR.setPower(power);
    }

    protected void setEachMecanumPower(double LFpower, double RFpower, double RRpower, double LRpower)
    {
        motor_LF.setPower(LFpower);
        motor_RF.setPower(RFpower);
        motor_RR.setPower(RRpower);
        motor_LR.setPower(LRpower);
    }

    public void getMotorTelemetry(Telemetry telemetry)
    {
        telemetry.addData("LF Motor: ", LFrontPower);
        telemetry.addData("RF Motor: ", RFrontPower);
        telemetry.addData("RR Motor: ", RRearPower);
        telemetry.addData("LR Motor: ", LRearPower);
    }
}