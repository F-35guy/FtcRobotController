package org.firstinspires.ftc.teamcode.pedro.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.mechanisms.kentProgBoard;

@TeleOp
public class Kenton extends OpMode {

    kentProgBoard board = new kentProgBoard();
    private final ElapsedTime transferTimer = new ElapsedTime();

    // Transfer cycle timing (in seconds)
    private static final double CYCLE_RUN_TIME = 1;
    private static final double CYCLE_PAUSE_TIME = .25;

    // Shooter power: 52.5% (motor power range is -1.0 to 1.0)
    double SHOOTER_POWER = 0.465;

    // RPM shooter settings (goBILDA 6000 RPM 1:1 motor)
    private static final double MAX_MOTOR_RPM = 6000.0;
    private static final double RPM_TOLERANCE_PERCENT = 0.025; // 2.5% up or down (5% total window)

    // Button states
    boolean lastA = false;
    boolean shooterOn = false;
    boolean lastDpadDown = false;
    boolean rpmShooterOn = false;
    boolean lastRightStickButton = false;
    boolean halfSpeed = false;

    @Override
    public void init() {
        board.init(hardwareMap);

        shooterOn = false;
        rpmShooterOn = false;
        halfSpeed = false;
        lastA = false;
        lastDpadDown = false;
        lastRightStickButton = false;
        transferTimer.reset();

        board.stopShooter();

        telemetry.addData("Status", "Robot Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        // ====================================
        // SHOOTER CONTROLS
        // ====================================
        boolean currentA = gamepad2.a;
        boolean currentDpadDown = gamepad2.dpad_down;

        // Press gamepad 2 A to toggle Power Shooter ON/OFF
        if (currentA && !lastA) {
            shooterOn = !shooterOn;
            if (shooterOn) {
                rpmShooterOn = false; // Disable RPM mode if Power mode activated
            }
        }
        lastA = currentA;

        // Press gamepad 2 D-Pad Down to toggle RPM Target Shooter ON/OFF
        if (currentDpadDown && !lastDpadDown) {
            rpmShooterOn = !rpmShooterOn;
            if (rpmShooterOn) {
                shooterOn = false; // Disable Power mode if RPM mode activated
            }
        }
        lastDpadDown = currentDpadDown;

        // Target RPM calculated as the percentage of MAX_MOTOR_RPM specified by SHOOTER_POWER
        double targetRPM = SHOOTER_POWER * MAX_MOTOR_RPM;
        double actualRPM = board.getShooterRPM();

        if (rpmShooterOn) {
            board.shootRPM(targetRPM);
        } else if (shooterOn) {
            board.shootPower(SHOOTER_POWER);
        } else {
            board.stopShooter();
        }

        // Is either shooter mode currently ON?
        boolean isShooterActive = shooterOn || rpmShooterOn;

        // ====================================
        // DRIVE CONTROLS
        // ====================================
        boolean currentRightStickButton = gamepad1.right_stick_button;

        // Toggle manual half speed
        if (currentRightStickButton && !lastRightStickButton) {
            halfSpeed = !halfSpeed;
        }
        lastRightStickButton = currentRightStickButton;

        // Drive power drops to 50% (0.5) if manual half speed is toggled OR if shooter is active in either mode
        double driveScale = (halfSpeed || isShooterActive) ? 0.5 : 1.0;
        double strafe = -gamepad1.right_stick_x * driveScale;

        // Inverting turn and straight
        double turn = -gamepad1.left_stick_x * driveScale;
        double straight = gamepad1.right_stick_y * driveScale;

        VoltageSensor batteryVolts = hardwareMap.get(VoltageSensor.class, "Control Hub");
        double voltage = batteryVolts.getVoltage();

        board.move(straight, strafe, turn);
        if (straight == 0 && (strafe == 0 && turn == 0)) {
            board.move(0, 0, 0);
        }

        // ====================================
        // INTAKE AND TRANSFER CONTROLS
        // ====================================
        double strength = gamepad2.right_trigger;
        double reverseIntake = gamepad2.left_trigger;

        boolean feed = gamepad2.right_bumper || gamepad2.x;
        boolean rpmFeed = gamepad2.left_bumper;

        double intakePower = 0.0;
        double transferPower = 0.0;
        double blockerPosition = 0.0;

        if (!feed && !rpmFeed) {
            transferTimer.reset();
        }

        // RPM-Based Feeding: Left Bumper held
        // Only opens blocker and feeds if actual RPM is within ±2.5% of target RPM
        if (rpmFeed) {
            double lowerBound = targetRPM * (1.0 - RPM_TOLERANCE_PERCENT);
            double upperBound = targetRPM * (1.0 + RPM_TOLERANCE_PERCENT);
            boolean inRpmRange = (actualRPM >= lowerBound && actualRPM <= upperBound);

            if (rpmShooterOn && inRpmRange) {
                blockerPosition = 0.26;
                double cycleDuration = CYCLE_RUN_TIME + CYCLE_PAUSE_TIME;
                double elapsed = transferTimer.seconds();
                double phase = elapsed % cycleDuration;
                if (phase < CYCLE_RUN_TIME) {
                    transferPower = 0.25;
                    intakePower = 0.10;
                }
            }
        }
        // Power-Based Feeding: Right Bumper or X held
        else if (feed) {
            double cycleDuration = CYCLE_RUN_TIME + CYCLE_PAUSE_TIME;
            double elapsed = transferTimer.seconds();

            if (shooterOn) {
                blockerPosition = 0.26;
                double phase = elapsed % cycleDuration;
                if (phase < CYCLE_RUN_TIME) {
                    transferPower = 0.25;
                    intakePower = 0.10;
                }
            }
        }
        // Intake controls: transfer system stays off.
        else if (gamepad1.a || strength > 0.1) {
            intakePower = 1.0;
        }
        // Reverse intake: transfer system stays off.
        else if (reverseIntake > 0.1) {
            intakePower = -0.6;
        }

        board.intake(intakePower);
        board.transfer(transferPower);
        board.transferBlock(blockerPosition);

        // ====================================
        // TELEMETRY
        // ====================================
        telemetry.addData("Strafe", strafe);
        telemetry.addData("Turn", turn);
        telemetry.addData("Straight", straight);
        telemetry.addData("Drive Power Scale", (driveScale * 100) + "%" + (isShooterActive ? " (Shooter Active Auto-50%)" : ""));
        telemetry.addData("Manual Half Speed", halfSpeed);
        telemetry.addData("Motor Rotations", board.getMotorRotations());
        telemetry.addData("Power Shooter (A)", shooterOn ? "ON" : "OFF");
        telemetry.addData("RPM Shooter (D-Pad Down)", rpmShooterOn ? "ON" : "OFF");
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Actual Shooter RPM", actualRPM);
        
        double lowerBound = targetRPM * (1.0 - RPM_TOLERANCE_PERCENT);
        double upperBound = targetRPM * (1.0 + RPM_TOLERANCE_PERCENT);
        boolean inRange = (actualRPM >= lowerBound && actualRPM <= upperBound);
        telemetry.addData("RPM in Range (±2.5%)", inRange ? "YES" : "NO");

        if ((feed && shooterOn) || (rpmFeed && rpmShooterOn && inRange)) {
            double elapsed = transferTimer.seconds();
            double cycleDuration = CYCLE_RUN_TIME + CYCLE_PAUSE_TIME;
            int currentCycle = (int) (elapsed / cycleDuration) + 1;
            boolean isRunning = (elapsed % cycleDuration) < CYCLE_RUN_TIME;
            telemetry.addData("Transfer State", "Cycle " + currentCycle + " (" + (isRunning ? "RUN" : "PAUSE") + ")");
        } else {
            telemetry.addData("Transfer State", "IDLE");
        }

        telemetry.update();
    }

    @Override
    public void stop() {
        // Stop all motors and return the blocker to its base position.
        board.stopShooter();
        board.move(0, 0, 0);
        board.intake(0);
        board.transfer(0);
        board.transferBlock(0);
    }
}
