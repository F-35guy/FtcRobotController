package org.firstinspires.ftc.teamcode.pedro.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.mechanisms.ProgBoardMotors;

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

            board.shoot(.45);

            sleep(1500);

            board.transfer(.35);
            board.transferBlock(.26);
            board.intake(.05);

            sleep(1500);

            board.transfer(0);

            sleep(2000);

            board.transfer(.35);

            sleep(3000);



            // Drive forward at 50% power
            board.intake(0);
            board.transfer(0);
            board.shoot(0);
            board.move(-0.5,0,0);

            // Wait for 2 seconds
            sleep(500);

            // Stop the robot
            board.move(0,0,0);

            sleep(500);
            board.move(0,-1,0);

            sleep(900);

            board.move(0,0,0);

            telemetry.addData("Status", "Finished");
            telemetry.update();
        }
    }
}
