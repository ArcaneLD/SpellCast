package fr.arcane.spellcast.utils;

import org.bukkit.util.Vector;
import org.joml.Quaternionf;

public class Quaternion {
    double w, x, y, z;

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Quaternion fromEuler(double roll, double pitch, double yaw) {

        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);

        double w = cy * cp * cr + sy * sp * sr;
        double x = cy * cp * sr - sy * sp * cr;
        double y = sy * cp * sr + cy * sp * cr;
        double z = sy * cp * cr - cy * sp * sr;

        return new Quaternion(w, x, y, z);
    }

    public static Quaternion fromAxisAngle(double angle, double x, double y, double z) {

        double halfAngle = angle / 2;
        double s = Math.sin(halfAngle);
        return new Quaternion(Math.cos(halfAngle), x * s, y * s, z * s);
    }

    public static Quaternionf fromVector(Vector vector) {
        Vector yAxis = new Vector(0, 1, 0);
        // Calculer l'angle entre le vecteur et l'axe des y
        double angle = Math.acos(vector.dot(yAxis) / (vector.length() * yAxis.length()));

        return new Quaternionf().rotationAxis((float) angle, (float) vector.getX(), (float) vector.getY(), (float) vector.getZ());
    }

    public Quaternion multiply(Quaternion q) {
        return new Quaternion(
                w * q.w - x * q.x - y * q.y - z * q.z,
                w * q.x + x * q.w + y * q.z - z * q.y,
                w * q.y - x * q.z + y * q.w + z * q.x,
                w * q.z + x * q.y - y * q.x + z * q.w
        );
    }

    public static Quaternionf rotateX(Quaternionf existingQuaternion, double angleInDegrees) {

        float angleInRadians = (float) Math.toRadians(angleInDegrees);

        Quaternionf rotationQuaternion = new Quaternionf().rotationX(angleInRadians);
        return new Quaternionf(existingQuaternion).mul(rotationQuaternion);
    }

    public static Quaternionf rotateY(Quaternionf existingQuaternion, double angleInDegrees) {

        float angleInRadians = (float) Math.toRadians(angleInDegrees);

        Quaternionf rotationQuaternion = new Quaternionf().rotationY(angleInRadians);

        return new Quaternionf(existingQuaternion).mul(rotationQuaternion);
    }

    public static Quaternionf rotateZ(Quaternionf existingQuaternion, double angleInDegrees) {

        float angleInRadians = (float) Math.toRadians(angleInDegrees);

        Quaternionf rotationQuaternion = new Quaternionf().rotationZ(angleInRadians);

        return new Quaternionf(existingQuaternion).mul(rotationQuaternion);
    }

}
