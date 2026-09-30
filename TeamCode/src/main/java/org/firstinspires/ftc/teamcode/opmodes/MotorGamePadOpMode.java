package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ProgBoardMotors;
@TeleOp
public class MotorGamePadOpMode extends OpMode {
    ProgBoardMotors board = new ProgBoardMotors();

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    boolean lastA = false;
    boolean lastB = false;
    boolean shooterOn = false;

    @Override
    public void loop() {
        double strafe = gamepad1.right_stick_x;
        // Inverting turn and straight
        double turn = -gamepad1.left_stick_x;
        double straight = gamepad1.left_stick_y;

        board.move(straight, strafe, turn);

        double strength = gamepad2.right_trigger;


        board.transfer(strength);
        if(gamepad1.a||strength > 0.1) {
            board.intake(1.0);
        } else {
            board.intake(0.0);
        }


        boolean currentA = gamepad2.a;
        boolean currentB = gamepad2.b;

        // A = toggle full power
        if (currentA && !lastA) {
            shooterOn = !shooterOn;

            if (shooterOn) {
                board.shoot(1.0);
            } else {
                board.shoot(0);
            }
        }

        // B = toggle half power
        if (currentB && !lastB) {
            shooterOn = !shooterOn;

            if (shooterOn) {
                board.shoot(0.25);
            } else {
                board.shoot(0);
            }
        }

        lastA = currentA;
        lastB = currentB;


        telemetry.addData("Strafe", strafe);
        telemetry.addData("Turn", turn);
        telemetry.addData("Straight", straight);
        telemetry.addData("Motor rotations", board.getMotorRotations());
    }
}
