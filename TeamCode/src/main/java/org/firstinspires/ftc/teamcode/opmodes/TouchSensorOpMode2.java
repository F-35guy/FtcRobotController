package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.ProgBoard1;

public class TouchSensorOpMode2 extends OpMode {
    ProgBoard1 board = new ProgBoard1();
    @Override
    public void init() {
        board.init(hardwareMap);

    }
    @Override
    public void loop() {

        telemetry.addData("Touch sensor", board.getTouchSensorState());
        telemetry.addData("Touch Sensor released", board.getTouchSensorState());
    }
}
