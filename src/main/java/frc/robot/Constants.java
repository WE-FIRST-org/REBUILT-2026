// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    // Motor controller IDs for drivetrain motors
    public static final int k_LEFT_LEADER_ID = 1;//Previously rightMotor1
    public static final int k_LEFT_FOLLOWER_ID = 3;//Previously rightMotor2
    public static final int k_RIGHT_LEADER_ID = 2; //Previously leftMotor1
    public static final int k_RIGHT_FOLLOWER_ID = 4; //Previously leftMotor2

    // Current limit for drivetrain motors. 60A is a reasonable maximum to reduce
    // likelihood of tripping breakers or damaging CIM motors
    public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;
  }

  public static final class FuelConstants {
    //Motor controller IDs for Fuel Mechanism motors
    public static final int k_INTAKE_MOTOR_ID = 9;
    public static final int k_INDEXER_MOTOR_ID = 5;
    public static final int k_SHOOTER_LEFT_MOTOR_ID = 61;
    public static final int k_SHOOTER_RIGHT_MOTOR_ID = 62;

    // Current limit and nominal voltage for fuel mechanism motors.
    public static final int INDEXER_MOTOR_CURRENT_LIMIT = 60;
    public static final int SHOOTER_MOTOR_CURRENT_LIMIT = 60;
    

    // Voltage values for various fuel operations. These values may need to be tuned
    // based on exact robot construction.
    // See the Software Guide for tuning information
    public static final double INTAKING_FEEDER_VOLTAGE = -12;
    public static final double INTAKING_INTAKE_VOLTAGE = 10;
    public static final double LAUNCHING_FEEDER_VOLTAGE = 9;
    public static final double LAUNCHING_LAUNCHER_VOLTAGE = 10.6;
    public static final double SPIN_UP_FEEDER_VOLTAGE = -6;
    public static final double SPIN_UP_TIMEOUT = 5;
  }

  public static final class VisionConstants {
    // VISION CONSTANTS
    //make sure to change it back into 46.17 after logic implementation, access raspi's static ip address via "sudo nmcui" (Network Managemer Command line User Interface)
    //router ip address 10.TE.AM.1 (eg 10.46.17.1)
    public static final String USB_CAMERA_NAME = "USB_Camera-10.48.14.5"; //VisionSubsystem.java MIT License text there
    
  }

  public static final class OperatorConstants {
    // Port constants for driver and operator controllers. These should match the
    // values in the Joystick tab of the Driver Station software
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    // This value is multiplied by the joystick value when rotating the robot to
    // help avoid turning too fast and beign difficult to control
    public static final double DRIVE_SCALING = .7;
    public static final double ROTATION_SCALING = .8;
  }

  public static final class AimAndRangeConstants {
    // PID values for turning and driving forward
    public static final double TURN_P = 0.05;
    public static final double TURN_I = 0;
    public static final double TURN_D = 0;
    public static final double DRIVE_P = 0.1;
    public static final double DRIVE_I = 0;
    public static final double DRIVE_D = 0;

    // April tag heights of game pieces
    public static final double HUB_TARGET_HEIGHT_METERS = 1.124;
    public static final double TOWER_TARGET_HEIGHT_METERS = 0.5525;
    public static final double OUTPOST_TARGET_HEIGHT_METERS = 0.5525;
    public static final double TRENCH_TARGET_HEIGHT_METERS = 0.889;

    // Camera height and pitch
    public static final double CAMERA_HEIGHT_METERS = 0.45;
    public static final double CAMERA_PITCH_DEGREES = 9;
    public static final double CAMERA_PITCH_RADIANS = Math.toRadians(CAMERA_PITCH_DEGREES);

    public static final double DISTANCE_GOAL_METERS = 0.5;
  }
}
