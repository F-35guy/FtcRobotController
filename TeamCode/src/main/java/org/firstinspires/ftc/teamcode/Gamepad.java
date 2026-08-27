package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class Gamepad  extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("left stick x", gamepad1.left_stick_x);
        telemetry.addData("left stick y", -gamepad1.left_stick_y);
        telemetry.addData("X button (A)", gamepad1.a);
    }
}
