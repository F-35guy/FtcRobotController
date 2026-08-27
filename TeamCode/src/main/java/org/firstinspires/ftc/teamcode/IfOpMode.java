package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class IfOpMode extends OpMode {
    @Override
    public void init() {

    }
    @Override
    public void loop() {
        double StickY = gamepad1.left_stick_y * 0.5;
        double StickX = gamepad1.left_stick_x * 0.5;
        if(gamepad1.left_stick_y < 0) {
            telemetry.addData("Left stick", "Is Negative");

        }
        else if (gamepad1.left_stick_y > 0){
            telemetry.addData("Left stick", "is positive");
        }
        else{
            telemetry.addData("Left stick", "Is at 0");

        }
        if(gamepad1.a) {
            telemetry.addData("X button", "pressed");
        }

        telemetry.addData("Left stick y" , gamepad1.left_stick_y);

        if(!gamepad1.a) {
            telemetry.addData("Current speed x", StickX);
            telemetry.addData("Current speed Y", StickY);


        }
        else {
            telemetry.addData("Current speed", "Forward speed");
        }
        if(gamepad1.a) {
            telemetry.addData("X", gamepad1.right_stick_y);
            telemetry.addData("Y", gamepad1.right_stick_x);
        }
        else {
            telemetry.addData("X", gamepad1.right_stick_x);
            telemetry.addData("Y", gamepad1.right_stick_y);
        }
    }

}
