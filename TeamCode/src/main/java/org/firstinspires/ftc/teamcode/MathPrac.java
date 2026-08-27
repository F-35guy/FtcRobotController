package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class MathPrac extends OpMode {
    @Override
    public void init () {

    }

    @Override
    public void loop() {
        double speedForward = -gamepad1.left_stick_y/2.0;
        double differenceSticks = gamepad1.left_stick_y - gamepad1.right_stick_y;
        float triggerAmount = gamepad1.left_trigger + gamepad1.right_trigger;
        telemetry.addData("left stick y", -gamepad1.left_stick_y);
        telemetry.addData("speed", speedForward);
        telemetry.addData("Right stick", gamepad1.right_stick_x);
        telemetry.addData("Right stick y", gamepad1.right_stick_y);
        telemetry.addData("B(o)", gamepad1.b);
        telemetry.addData("Difference between sticks", differenceSticks);
        telemetry.addData("Trigger sum", triggerAmount);
    }
}
