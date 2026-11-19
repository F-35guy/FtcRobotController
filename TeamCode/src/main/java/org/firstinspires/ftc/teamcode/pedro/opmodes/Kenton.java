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
   // private static final double IDEAL_RPM = SHOOTER_POWER * 6000;
   // private static final double TICKS_PER_REVOLUTION = 28;
   // private static final double TPS = (IDEAL_RPM/60) * TICKS_PER_REVOLUTION;
   // double CurrentTPS = board.getShooterRPM();
   // double CurrentRPM = (CurrentTPS/TICKS_PER_REVOLUTION) * 60;



    // Button states
    boolean lastA = false;
    boolean shooterOn = false;
    boolean lastRightStickButton = false;
    boolean halfSpeed = false;



    @Override
    public void init() {
        board.init(hardwareMap);


        shooterOn = false;
        halfSpeed = false;
        lastA = false;
        lastRightStickButton = false;
        transferTimer.reset();


        board.stopShooter();


        telemetry.addData("Status", "Robot Initialized");
        telemetry.update();
    }



    @Override
    public void loop() {



// ====================================
        // DRIVE CONTROLS
        // ====================================
        boolean currentRightStickButton = gamepad1.right_stick_button;



// Toggle half speed
        if (currentRightStickButton && !lastRightStickButton) {
            halfSpeed = !halfSpeed;
        }


        lastRightStickButton = currentRightStickButton;


        double driveScale = halfSpeed ? 0.5 : 1.0;
        double strafe = -gamepad1.right_stick_x * driveScale;



// Inverting turn and straight
        double turn = -gamepad1.left_stick_x * driveScale;
        double straight = gamepad1.right_stick_y * driveScale;

        VoltageSensor batteryVolts = hardwareMap.get(VoltageSensor.class, "Control Hub");
        double voltage = batteryVolts.getVoltage();

        /*if (voltage >= 12.8) {
            SHOOTER_POWER = 0.46;
        } else if (voltage <= 12) {
            SHOOTER_POWER = 0.475;
        } else {
            SHOOTER_POWER = 0.5;
        }*/


        board.move(straight, strafe, turn);
        if (straight == 0&&(strafe == 0 && turn == 0)) {
            board.move(0,0,0);
        }

        //if (CurrentRPM == IDEAL_RPM) {
          //  board.Light(1);
        //}



// ====================================
        // SHOOTER TOGGLE
        // ====================================
        boolean currentA = gamepad2.a;



// Press gamepad 2 A to toggle shooter ON/OFF
        if (currentA && !lastA) {
            shooterOn = !shooterOn;
        }


        lastA = currentA;


        if (shooterOn) {
            board.shootPower(SHOOTER_POWER);
        } else {
            board.stopShooter();
        }



// ====================================
        // INTAKE AND TRANSFER CONTROLS
        // ====================================
        double strength = gamepad2.right_trigger;
        double reverseIntake = gamepad2.left_trigger;


        boolean feed = gamepad2.right_bumper || gamepad2.x;


        double intakePower = 0.0;
        double transferPower = 0.0;
        double blockerPosition = 0.0;



        if (!feed) {
            transferTimer.reset();
        }

// Right bumper or X: continuously cycle transfer (RUN and PAUSE) as long as held.
        if (feed) {
            //intakePower = 0.6;


            double cycleDuration = CYCLE_RUN_TIME + CYCLE_PAUSE_TIME;
            double elapsed = transferTimer.seconds();


            if (shooterOn) {
                blockerPosition = 0.26;
                double phase = elapsed % cycleDuration;
                if (phase < CYCLE_RUN_TIME) {
                    transferPower = 0.25;
                    intakePower = .10;
                }
            }
        }

// Intake controls: transfer system stays off.
        else if (gamepad1.a || strength > 0.1) {
            intakePower = 1.0;



// Transfer intentionally remains off while intaking.
        }

// Reverse intake: transfer system stays off.
        else if (reverseIntake > 0.1) {
            intakePower = -0.6;



// Transfer intentionally remains off while intaking.
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
        telemetry.addData("Half Speed", halfSpeed);
        telemetry.addData("Motor Rotations", board.getMotorRotations());
        telemetry.addData("Shooter", shooterOn ? "ON" : "OFF");
        telemetry.addData(
                "Shooter Power",
                shooterOn ? SHOOTER_POWER * 100 + "%" :
                        "0%"
        );


        if (feed && shooterOn) {
            double elapsed = transferTimer.seconds();
            double cycleDuration = CYCLE_RUN_TIME + CYCLE_PAUSE_TIME;
            int currentCycle = (int) (elapsed / cycleDuration) + 1;
            boolean isRunning = (elapsed % cycleDuration) < CYCLE_RUN_TIME;
            telemetry.addData("Transfer State", "Cycle " + currentCycle + " (" + (isRunning ? "RUN" : "PAUSE") + ")");
        } else {
            telemetry.addData("Transfer State", "IDLE");
        }


        double actualRPM = board.getShooterRPM();
        telemetry.addData("Actual Shooter RPM", actualRPM);


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