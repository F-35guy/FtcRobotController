package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ProgBoardMotors {
    private DcMotor RightRear;
    private DcMotor LeftRear;
    private DcMotor LeftFront;
    private DcMotor RightFront;
    private DcMotor transfer;
    private DcMotor shooter;
    private DcMotor intake;
    private double ticksPerRotation;

    public void init(HardwareMap hwMap) {
        RightRear = hwMap.get(DcMotor.class, "rightRear");
        RightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        LeftRear = hwMap.get(DcMotor.class, "leftRear");
        LeftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LeftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        LeftFront = hwMap.get(DcMotor.class, "leftFront");
        LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        RightFront = hwMap.get(DcMotor.class, "rightFront");
        RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        transfer = hwMap.get(DcMotor.class, "transfer");
        transfer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter = hwMap.get(DcMotor.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intake = hwMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        ticksPerRotation = RightRear.getMotorType().getTicksPerRev();
    }
    public void move(double straight, double strafe, double turn) {
        // Combine the inputs based on your existing direction logic
        // Straight: LF+, LR+, RF-, RR-
        // Turn:    LF+, LR+, RF+, RR+
        // Strafe:  LF-, LR+, RF-, RR+
        
        LeftFront.setPower(straight + turn - strafe);
        LeftRear.setPower(straight + turn + strafe);
        RightFront.setPower(-straight + turn - strafe);
        RightRear.setPower(-straight + turn + strafe);
    }
    public void transfer(double strength) {
        transfer.setPower(strength);
    }

    public void shoot(double active) {
        shooter.setPower(-active);
    }

    public void intake(double power) {
        intake.setPower(-power);
    }

    public void setMotorSpeed(double speed) {
        RightRear.setPower(speed);
        LeftRear.setPower(speed);
        LeftFront.setPower(speed);
        RightFront.setPower(speed);
    }

    public double getMotorRotations() {
        return RightRear.getCurrentPosition() / ticksPerRotation;
    }
}
