package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.ProgBoardMotors;

public class MotorOpMode extends OpMode {
    ProgBoardMotors board = new ProgBoardMotors();
    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        board.setMotorSpeed(0.5);
        telemetry.addData("Motor rotations", board.getMotorRotations());
    }
}
