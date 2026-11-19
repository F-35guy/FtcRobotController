package org.firstinspires.ftc.teamcode.pedro.opmodes;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;

public class Paths {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(82.5791, 7.8088, 90);
    private final Pose shoot4andleave = poseFactory.of(115.6595, 22.9973, 145);
    private final Pose shoot4andleaveControl1 = poseFactory.of(81.5682, 31.855, 0);
    private final Pose point2Start = poseFactory.of(115.6595, 22.9973, 145);
    private final Pose point2 = poseFactory.of(119.2014, 134.0637, 0);
    private final Pose point2Control1 = poseFactory.of(107.5117, 40.15, 0);
    private final Pose point2Control2 = poseFactory.of(107.0871, 96.7085, 0);
    private final Pose point3Start = poseFactory.of(119.2014, 134.0637, 0);
    private final Pose point3 = poseFactory.of(133.9017, 134.0105, 0);
    private final Pose point4Start = poseFactory.of(133.9017, 134.0105, 0);
    private final Pose point4 = poseFactory.of(115.5095, 22.143, 145);
    private final Pose point4Control1 = poseFactory.of(97.1143, 100.4853, 0);
    private final Pose point5Start = poseFactory.of(115.5095, 22.143, 145);
    private final Pose point5 = poseFactory.of(132.1616, 20.0481, 180);

    public Path shoot4andleave() {
        return curve(start, shoot4andleaveControl1, shoot4andleave).heading(Interpolator.linear(start, shoot4andleave));
    }

    public Path path2() {
        return curve(point2Start, point2Control1, point2Control2, point2).heading(Interpolator.linear(point2Start, point2));
    }

    public Path path3() {
        return line(point3Start, point3).heading(Interpolator.linear(point3Start, point3));
    }

    public Path path4() {
        return curve(point4Start, point4Control1, point4).heading(Interpolator.linear(point4Start, point4));
    }

    public Path path5() {
        return line(point5Start, point5).heading(Interpolator.linear(point5Start, point5));
    }
}
