package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class kentProgBoard {



    // ====================================
    // MOTOR DECLARATIONS
    // ====================================
    private DcMotor RightRear;
    private DcMotor LeftRear;
    private DcMotor LeftFront;
    private DcMotor RightFront;


    private DcMotor transfer;
    private DcMotorEx shooter;
    private DcMotor intake;


    private Servo transferStop;


    private double ticksPerRotation;



    // goBILDA 6000 RPM 1:1 motor
    // Encoder resolution: 28 ticks per revolution
    private static final double SHOOTER_TICKS_PER_REV = 28.0;





    // ====================================
    // HARDWARE INITIALIZATION
    // ====================================
    public void init(HardwareMap hwMap) {



// Right rear motor
        RightRear = hwMap.get(DcMotor.class, "rightRear");

// Restore the original motor directions on every OpMode init. Pedro's follower
        // configures drive motor directions for its own mecanum drive while autonomous runs.
        RightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        RightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightRear.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.
                        BRAKE
        );



// Left rear motor
        LeftRear = hwMap.get(DcMotor.class, "leftRear");
        LeftRear.setDirection(DcMotorSimple.Direction.FORWARD);
        LeftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LeftRear.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.
                        BRAKE
        );



// Left front motor
        LeftFront = hwMap.get(DcMotor.class, "leftFront");
        LeftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LeftFront.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.
                        BRAKE
        );



// Right front motor
        RightFront = hwMap.get(DcMotor.class, "rightFront");
        RightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightFront.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.
                        BRAKE
        );



// Transfer motor
        transfer = hwMap.get(DcMotor.class, "transfer");
        transfer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        transfer.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.
                        BRAKE
        );



// Shooter motor - encoder RPM control
        shooter = hwMap.get(DcMotorEx.class, "shooter");


        shooter.setMode(
                DcMotor.RunMode.
                        STOP_AND_RESET_ENCODER
        );


        shooter.setMode(
                DcMotor.RunMode.
                        RUN_USING_ENCODER
        );


        shooter.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.
                        FLOAT
        );


        shooter.setVelocity(0);



// Intake motor
        intake = hwMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.
                        BRAKE
        );



// Blocker servo
        transferStop = hwMap.get(Servo.class, "Blocker");
        transferStop.setPosition(0);



// Drive encoder configuration
        ticksPerRotation =
                RightRear.getMotorType().getTicksPerRev();
    }





    // ====================================
    // DRIVE CONTROLS
    // ====================================
    public void move(double straight, double strafe, double turn) {



// Original motor direction configuration
        double lf = straight + turn - strafe;
        double lr = straight + turn + strafe;
        double rf = -straight + turn - strafe;
        double rr = -straight + turn + strafe;



// Normalize motor powers to a maximum of 1.0
        double maxPower = Math.max(
                1.0,
                Math.max(
                        Math.max(Math.abs(lf), Math.abs(lr)),
                        Math.max(Math.abs(rf), Math.abs(rr))
                )
        );


        LeftFront.setPower(lf / maxPower);
        LeftRear.setPower(lr / maxPower);
        RightFront.setPower(rf / maxPower);
        RightRear.setPower(rr / maxPower);
    }





    // ====================================
    // TRANSFER MOTOR
    // ====================================
    public void transfer(double strength) {
        transfer.setPower(strength);
    }





    // ====================================
    // ENCODER-BASED SHOOTER RPM CONTROL
    // ====================================
    // Set shooter target speed in RPM
    public void shootRPM(double rpm) {


        double ticksPerSecond =
                rpm * SHOOTER_TICKS_PER_REV / 60.0;



// Negative direction matches original shooter
        shooter.setVelocity(-ticksPerSecond);
    }



    // Set shooter motor power as a percentage from 0.0 to 1.0.
    public void shootPower(double power) {
        shooter.setPower(-power);
    }



    // Get actual shooter RPM
    public double getShooterRPM() {


        double ticksPerSecond = shooter.getVelocity();


        double rpm =
                ticksPerSecond * 60.0 / SHOOTER_TICKS_PER_REV;


        return Math.abs(rpm);
    }



    // Stop the shooter
    public void stopShooter() {
        shooter.setVelocity(0);
    }



    // Raw shooter velocity in encoder ticks per second
    public double getVelocity() {
        return shooter.getVelocity();
    }





    // ====================================
    // INTAKE MOTOR
    // ====================================
    public void intake(double power) {
        intake.setPower(-power);
    }





    // ====================================
    // BLOCKER SERVO
    // ====================================
    public void transferBlock(double pos) {
        transferStop.setPosition(pos);
    }





    // ====================================
    // SET ALL DRIVE MOTOR POWERS
    // ====================================
    public void setMotorSpeed(double speed) {
        RightRear.setPower(speed);
        LeftRear.setPower(speed);
        LeftFront.setPower(speed);
        RightFront.setPower(speed);
    }





    // ====================================
    // DRIVE ENCODER ROTATIONS
    // ====================================
    public double getMotorRotations() {


        if (ticksPerRotation == 0) {
            return 0;
        }


        return RightRear.getCurrentPosition()
                / ticksPerRotation;
    }
}