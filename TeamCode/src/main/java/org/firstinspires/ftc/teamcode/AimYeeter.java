package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class AimYeeter {
    public DcMotor aim_Motor;
    // for presets
//    private static final int wallElementPosition = ;
//    private static final int lowBucketPosition = ;
//    private static final int highBucketPosition = ;

    // Motor PIDF coefficients, USE CAUTION. These values change how the motor
    // responds when commanded to an encoder position.
//    public static final double NEW_P = ;
//    public static final double NEW_I = ;
//    public static final double NEW_D = ;
//    public static final double NEW_F = ;
//    PIDFCoefficients pidfNew;
//    PIDFCoefficients pidCheck;

    public void init(HardwareMap hwMap){
        aim_Motor = hwMap.get(DcMotor.class, "aim_Motor");
        aim_Motor.setDirection(DcMotorSimple.Direction.REVERSE);
        aim_Motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aim_Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//        this.setPIDF();
//
//        // Explicitly initialize with motor power off for safety
//        this.stop();
    }

//    public void setPIDF()
//    {
//        // Get a reference to the motor controller and cast it as an extended functionality controller.
//        // We assume it's a REV Robotics Expansion Hub, which supports the extended controller functions.
//        DcMotorControllerEx motorControllerEx = (DcMotorControllerEx) aim_Motor.getController();
//
//        // Get the port number of our configured motor.
//        int motorIndex = ((DcMotorEx) aim_Motor).getPortNumber();
//
//        // change coefficients
//        pidfNew = new PIDFCoefficients(NEW_P, NEW_I, NEW_D, NEW_F);
//        motorControllerEx.setPIDFCoefficients(motorIndex, DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
//
//        // Read coefficients from memory to check that they took.
//        pidCheck = motorControllerEx.getPIDFCoefficients(motorIndex, DcMotor.RunMode.RUN_USING_ENCODER);
//    }

    public int getPosition()
    {
        return aim_Motor.getCurrentPosition();
    }

    public void raiseToYeet(Telemetry tm){
        double aimSpeed = -0.5;
        int aimPosition = 100;
        aim_Motor.setTargetPosition(aimPosition);
        aim_Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        aim_Motor.setPower(aimSpeed);
        tm.addData("aim position: ", aim_Motor.getCurrentPosition() );
    }

    public void park() {
        double parkSpeed = 0.5;
        int parkPosition = 0;
        aim_Motor.setTargetPosition(parkPosition);
        aim_Motor.setPower(parkSpeed);
        aim_Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if ((aim_Motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) || (aim_Motor.getTargetPosition() == parkPosition&& aim_Motor.getCurrentPosition() < 5)) {
            aim_Motor.setPower(0.0);
            aim_Motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            aim_Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
}
