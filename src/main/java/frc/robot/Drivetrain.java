package frc.robot;

// teleop imports
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;


public class Drivetrain {
    //Gear ratio : 8.45
    //Wheel: 6"
    private WPI_VictorSPX rightMotor1, rightMotor2, leftMotor1, leftMotor2;


    public enum DriveTrainMode { // mode switch var
        QUAD,
        CUBIC,
        LINEAR
    }

    DriveTrainMode driveProfile = DriveTrainMode.QUAD;

    public Drivetrain() {
        // Right Motors
        rightMotor1 = new WPI_VictorSPX(2);
        rightMotor2 = new WPI_VictorSPX(4);
        // Left Motors
        leftMotor1 = new WPI_VictorSPX(1);
        leftMotor2 = new WPI_VictorSPX(3);

        // Configure inversion/followers
        rightMotor1.setInverted(true);
        rightMotor2.setInverted(true);
        rightMotor2.follow(rightMotor1);

        leftMotor1.setInverted(false);
        leftMotor2.setInverted(false);
        leftMotor2.follow(leftMotor1);
    }

    public void setMode(NeutralMode mode) {
        rightMotor1.setNeutralMode(mode);
        rightMotor2.setNeutralMode(mode);
        leftMotor1.setNeutralMode(mode);
        leftMotor2.setNeutralMode(mode);
    }

    public void drive(double throttle, double turn) {
        switch (this.driveProfile) {
            case LINEAR:
                rightMotor1.set((throttle - turn));
                leftMotor1.set((throttle + turn));
                break;
            case QUAD:
                rightMotor1.set(((throttle < 0 ? -1 : 1) * Math.pow(throttle, 2) - turn));
                leftMotor1.set(((throttle < 0 ? -1 : 1) * Math.pow(throttle, 2) + turn));
                break;
            case CUBIC:
                rightMotor1.set((Math.pow(throttle, 3) - turn));
                leftMotor1.set((Math.pow(throttle, 3) + turn));
                break;
        }
    }
}
