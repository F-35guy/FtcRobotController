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

    @Override
    public void loop() {
        double strafe = gamepad1.right_stick_x;
        // Inverting turn and straight
        double turn = -gamepad1.left_stick_x;
        double straight = gamepad1.left_stick_y;

        board.move(straight, strafe, turn);

        telemetry.addData("Strafe", strafe);
        telemetry.addData("Turn", turn);
        telemetry.addData("Straight", straight);
        telemetry.addData("Motor rotations", board.getMotorRotations());
    }
}
