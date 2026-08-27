package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Practice", group = "TeleOp")
public class Practice extends OpMode{
    @Override
    public void init() {
        telemetry.addData("Hello", "World)");

    }

    @Override
    public void loop() {

    }
}
