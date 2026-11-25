package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.RevHubIMU;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.pedropathing.revhub.localizers.TwoWheelLocalizer;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static String lastError = "";

    public static Follower create(HardwareMap h) {
        lastError = "";
        try {
            // Configure Mecanum Drivetrain motor names and required directions
            MecanumConfig mecanumConfig = new MecanumConfig(c -> {
                c.frontLeftName.set("leftFront");
                c.frontRightName.set("rightRight");
                c.frontRightName.set("rightFront");
                c.backLeftName.set("leftRear");
                c.backRightName.set("rightRear");

                c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
            });

            // Configure Two-Wheel Localizer using drive train motor encoders and Control Hub IMU
            TwoWheelConfig twoWheelConfig = new TwoWheelConfig(c -> {
                c.xPodName.set("leftRear");   // Drive motor encoder for forward tracking
                c.yPodName.set("rightRear");  // Drive motor encoder for strafe tracking
                c.imuName.set("imu");
                c.xPodOffset.set(0.0);
                c.yPodOffset.set(0.0);
                c.forwardTicksToInches.set(0.005); // Estimated ticks-to-inches (can be tuned later with TwoWheelTuner)
                c.strafeTicksToInches.set(0.005);  // Estimated ticks-to-inches (can be tuned later with TwoWheelTuner)
                c.xPodDirection.set(Encoder.FORWARD);
                c.yPodDirection.set(Encoder.FORWARD);
                c.imu.set(new RevHubIMU(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )));
            });

            // Foresight Algorithm Configuration
            ForesightConfig foresightConfig = new ForesightConfig(c -> {
                // Default foresight config
            });

            Mecanum drivetrain = new Mecanum(h, mecanumConfig);
            TwoWheelLocalizer localizer = new TwoWheelLocalizer(h, twoWheelConfig);
            Foresight foresight = new Foresight(foresightConfig);

            return new Follower(localizer, drivetrain, foresight);
        } catch (Exception e) {
            lastError = e.getClass().getSimpleName() + ": " + e.getMessage();
            return null;
        }
    }
}
