package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.ProgBoardMotors;

@Autonomous(name = "Simple Leave Auto", group = "Autonomous")
public class SimpleLeaveAuto extends LinearOpMode {
    ProgBoardMotors board = new ProgBoardMotors();

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the motors using the hardware map
        board.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Status", "Running - Moving Forward");
            telemetry.update();

            // Drive forward at 50% power using move(straight, strafe, turn)
            board.move(0.5, 0, 0);

            // Wait for 3 seconds
            sleep(3000);

            // Stop the robot
            board.move(0, 0, 0);

            telemetry.addData("Status", "Finished");
            telemetry.update();
        }
    }
}
