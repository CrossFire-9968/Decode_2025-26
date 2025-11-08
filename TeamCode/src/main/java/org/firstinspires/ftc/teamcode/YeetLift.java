package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.Yeeter.State.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class YeetLift {
    public DcMotor liftMotor;
    // for presets
//    private static final int wallElementPosition = ;
//    private static final int lowBucketPosition = ;
//    private static final int highBucketPosition = ;
    Yeeter.State state = Yeeter.State.POWERUP;

    // Motor PIDF coefficients, USE CAUTION. These values change how the motor
    // responds when commanded to an encoder position.
    public static final double NEW_P = 13.0;
    public static final double NEW_I = 0.6;
    public static final double NEW_D = 0.1;
    public static final double NEW_F = 1.0;
    PIDFCoefficients pidfNew;
    PIDFCoefficients pidCheck;

    public void init(HardwareMap hwMap){
        state = Yeeter.State.INIT;
        liftMotor = hwMap.get(DcMotor.class, "yeet_lift_motor");
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.setPIDF();
//
//        // Explicitly initialize with motor power off for safety
        this.toHome();
    }

    public void setPIDF()
    {
        // Get a reference to the motor controller and cast it as an extended functionality controller.
        // We assume it's a REV Robotics Expansion Hub, which supports the extended controller functions.
        DcMotorControllerEx motorControllerEx = (DcMotorControllerEx) liftMotor.getController();

        // Get the port number of our configured motor.
        int motorIndex = ((DcMotorEx) liftMotor).getPortNumber();

        // change coefficients
        pidfNew = new PIDFCoefficients(NEW_P, NEW_I, NEW_D, NEW_F);
        motorControllerEx.setPIDFCoefficients(motorIndex, DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

        // Read coefficients from memory to check that they took.
        pidCheck = motorControllerEx.getPIDFCoefficients(motorIndex, DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int getPosition() {
        return liftMotor.getCurrentPosition();
    }

    public void raiseToYeet(int yeetLiftPosition){
        double yeetLiftMotorPower = -0.5;

        state = Yeeter.State.RUNNING;
        liftMotor.setTargetPosition(yeetLiftPosition);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(yeetLiftMotorPower);
    }

    public void toHome() {
        double parkSpeed = 0.2;
        int parkPosition = 0;

        if (liftMotor.getCurrentPosition() < 20) {
            liftMotor.setPower(0.0);
            liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            state = Yeeter.State.COMPLETE;
        }
        else {
            state = Yeeter.State.PARKING;
            liftMotor.setTargetPosition(parkPosition);
            liftMotor.setPower(parkSpeed);
            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public Yeeter.State getYeetLiftState () {
        return state;
    }
}