package org.firstinspires.ftc.teamcode.pedro.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.mechanisms.ProgBoardMotors;

@Autonomous(name = "Pedro Pathing Auto", group = "Autonomous")
public class PedroAutonomous extends LinearOpMode {

    private Follower follower;
    private final ProgBoardMotors board = new ProgBoardMotors();
    private final Paths paths = new Paths();

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware mechanisms
        board.init(hardwareMap);

        // Initialize Pedro Pathing Follower via Constants
        follower = Constants.create(hardwareMap);

        if (follower == null) {
            telemetry.addData("Init Error", Constants.lastError);
            telemetry.addData("Check", "Verify motor & pinpoint names in Robot Config");
            telemetry.update();
            waitForStart();
            return;
        }

        // Set starting pose matching Paths.java (start pose: x=82.5791, y=7.8088, heading=90 degrees)
        follower.setPose(new Pose(82.5791, 7.8088, 90));

        telemetry.addData("Status", "Initialized and Ready");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // ==========================================
            // Step 1: Shoot 4 and Leave Path
            // ==========================================
            follower.follow(paths.shoot4andleave());
            board.shoot(0.465); // Spin up shooter

            while (opModeIsActive() && !follower.atParametricEnd()) {
                follower.update();
                telemetry.addData("Action", "Following shoot4andleave");
                telemetry.update();
            }

            // Initial transfer / score action
            board.transfer(0.25);
            board.transferBlock(0.26);
            sleep(800);
            board.transfer(0);

            // ==========================================
            // Step 2: Path 2 (Only moves, no intake)
            // ==========================================
            follower.follow(paths.path2());
            board.intake(0); // Ensure intake is off

            while (opModeIsActive() && !follower.atParametricEnd()) {
                follower.update();
                telemetry.addData("Action", "Following path2 (movement only)");
                telemetry.update();
            }

            // ==========================================
            // Step 3: Path 3 (Runs intake)
            // ==========================================
            follower.follow(paths.path3());
            board.intake(1.0); // Run intake

            while (opModeIsActive() && !follower.atParametricEnd()) {
                follower.update();
                telemetry.addData("Action", "Following path3 (intake running)");
                telemetry.update();
            }

            // Stop intake after path 3
            board.intake(0);

            // ==========================================
            // Step 4: Path 4
            // ==========================================
            follower.follow(paths.path4());

            while (opModeIsActive() && !follower.atParametricEnd()) {
                follower.update();
                telemetry.addData("Action", "Following path4");
                telemetry.update();
            }

            // ==========================================
            // After Step 4: Repeat Firing Sequence
            // ==========================================
            board.shoot(0.465); // Spin up shooter
            sleep(500); // Give shooter time to spin up
            board.transfer(0.25);
            board.transferBlock(0.26);
            sleep(800);
            board.transfer(0);

            // ==========================================
            // Stop all shooting & intake mechanisms
            // ==========================================
            board.shoot(0);
            board.transfer(0);
            board.transferBlock(0);
            board.intake(0);

            // ==========================================
            // Step 5: Path 5 (Proceed along path 5)
            // ==========================================
            follower.follow(paths.path5());

            while (opModeIsActive() && !follower.atParametricEnd()) {
                follower.update();
                telemetry.addData("Action", "Following path5");
                telemetry.update();
            }

            // Hold final position
            follower.hold(Pose.zero());
            while (opModeIsActive()) {
                follower.update();
            }
        }
    }
}
