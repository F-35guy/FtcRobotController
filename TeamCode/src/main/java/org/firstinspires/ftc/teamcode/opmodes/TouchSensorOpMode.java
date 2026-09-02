package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.ProgBoard2;

public class TouchSensorOpMode extends OpMode {
    ProgBoard2 board = new ProgBoard2();
    @Override
    public void init() {
        board.init(hardwareMap);

    }
    @Override
    public void loop() {
        telemetry.addData("Touch sensor", board.getTouchSensorState());
    }
}
