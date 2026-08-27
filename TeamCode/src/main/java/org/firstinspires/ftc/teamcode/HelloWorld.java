package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp()
public class HelloWorld extends OpMode  {
    @Override
    public void init() {
        telemetry.addData("Message" , "Hello World");
    }

    @Override public void loop() {

    }
}
