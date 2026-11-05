package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.mechanisms.kentProgBoard;


@TeleOp
public class Kenton extends OpMode {


    kentProgBoard board = new kentProgBoard();



    // Shooter power: 52.5% (motor power range is -1.0 to 1.0)
    private static final double SHOOTER_POWER = 0.475;



    // Button states
    boolean lastA = false;
    boolean shooterOn = false;
    boolean lastRightStickButton = false;
    boolean halfSpeed = false;



    @Override
    public void init() {
        board.init(hardwareMap);


        shooterOn = false;
        halfSpeed = false;
        lastA = false;
        lastRightStickButton = false;


        board.stopShooter();


        telemetry.addData("Status", "Robot Initialized");
        telemetry.update();
    }



    @Override
    public void loop() {



// ====================================
        // DRIVE CONTROLS
        // ====================================
        boolean currentRightStickButton = gamepad1.right_stick_button;



// Toggle half speed
        if (currentRightStickButton && !lastRightStickButton) {
            halfSpeed = !halfSpeed;
        }


        lastRightStickButton = currentRightStickButton;


        double driveScale = halfSpeed ? 0.5 : 1.0;
        double strafe = gamepad1.right_stick_x * driveScale;



// Inverting turn and straight
        double turn = -gamepad1.left_stick_x * driveScale;
        double straight = gamepad1.right_stick_y * driveScale;


        board.move(straight, strafe, turn);



// ====================================
        // SHOOTER TOGGLE
        // ====================================
        boolean currentA = gamepad2.a;



// Press gamepad 2 A to toggle shooter ON/OFF
        if (currentA && !lastA) {
            shooterOn = !shooterOn;
        }


        lastA = currentA;


        if (shooterOn) {
            board.shootPower(SHOOTER_POWER);
        } else {
            board.stopShooter();
        }



// ====================================
        // INTAKE AND TRANSFER CONTROLS
        // ====================================
        double strength = gamepad2.right_trigger;
        double reverseIntake = gamepad2.left_trigger;


        boolean feed = gamepad2.right_bumper || gamepad2.x;


        double intakePower = 0.0;
        double transferPower = 0.0;
        double blockerPosition = 0.0;



// Right bumper or X: feed game pieces.
        // Transfer runs only when the shooter is on.
        if (feed) {
            intakePower = 0.6;


            if (shooterOn) {
                transferPower = 0.5;
                blockerPosition = 0.26;
            }
        }

// Intake controls: transfer system stays off.
        else if (gamepad1.a || strength > 0.1) {
            intakePower = 1.0;



// Transfer intentionally remains off while intaking.
        }

// Reverse intake: transfer system stays off.
        else if (reverseIntake > 0.1) {
            intakePower = -0.6;



// Transfer intentionally remains off while intaking.
        }


        board.intake(intakePower);
        board.transfer(transferPower);
        board.transferBlock(blockerPosition);



// ====================================
        // TELEMETRY
        // ====================================
        telemetry.addData("Strafe", strafe);
        telemetry.addData("Turn", turn);
        telemetry.addData("Straight", straight);
        telemetry.addData("Half Speed", halfSpeed);
        telemetry.addData("Motor Rotations", board.getMotorRotations());
        telemetry.addData("Shooter", shooterOn ? "ON" : "OFF");
        telemetry.addData(
                "Shooter Power",
                shooterOn ? SHOOTER_POWER * 100 + "%" :
                        "0%"
        );


        double actualRPM = board.getShooterRPM();
        telemetry.addData("Actual Shooter RPM", actualRPM);


        telemetry.update();
    }



    @Override
    public void stop() {

// Stop all motors and return the blocker to its base position.
        board.stopShooter();
        board.move(0, 0, 0);
        board.intake(0);
        board.transfer(0);
        board.transferBlock(0);
    }
}