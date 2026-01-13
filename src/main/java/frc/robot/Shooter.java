// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private PS4Controller driverController;
  private PS4Controller operatorController;

  private Drivetrain drivetrain;

  // Shooter
  private SparkFlex shooterMotorLeft;
  private SparkFlex shooterMotorRight;
  private SparkClosedLoopController shooterPID;
  private RelativeEncoder shooterEncoder;

  // Intake + Indexer
  private WPI_VictorSPX intakeMotor;
  private WPI_VictorSPX indexerMotor;

  private double throttle, steer;
  private double speedModifer = 1;

  private static final int kShooterLeftCanId = 13;
  private static final int kShooterRightCanId = 14;
  // Set these to actual CAN IDs
  private static final int kIntakeCanId = 20;   // CHANGE ME
  private static final int kIndexerCanId = 21;  // CHANGE ME

  // Motor outputs
  private static final double kIntakePercentOutput = 0.7;
  private static final double kIndexerPercentOutput = 0.7;

  // Shooter stick shaping
  private static final double kShooterDeadband = 0.05;

  // ---------------- Shooter Modes ----------------
  private enum ShooterMode { CURRENT, VOLTAGE, SPEED }
  private ShooterMode shooterMode = ShooterMode.CURRENT;

  // CURRENT MODE tuning
  private static final double kShooterCurrentTargetAmps = 35.0; // tune
  private static final double kShooterCurrentLimitAmps  = 55.0; // safety limit
  private static final double kShooterBaseDuty = 0.55;          // base duty (0..1)
  private static final double kShooterCurrentKp = 0.015;        // trim gain (small!)
  private static final double kShooterTrimMax = 0.35;           // clamp trim

  // VOLTAGE MODE tuning
  private static final double kShooterVoltageVolts = 9.0;       // tune

  // SPEED MODE tuning (RPM)
  private static final double kShooterTargetRPM = 4500.0;       // tune
  private static final double kShooterVelKp = 0.00020;          // tune
  private static final double kShooterVelKi = 0.0;
  private static final double kShooterVelKd = 0.0;
  private static final double kShooterVelKff = 0.00018;         // tune

  // Optional: ramp to reduce brownouts / wheel slip
  private static final double kShooterOpenLoopRamp = 0.20;      // seconds 0->100%

  // Current-mode trim state
  private double shooterTrim = 0.0;
  private double lastTimeSec = 0.0;

  /** This function is run when the robot is first started up and should be used for any initialization code. */
  public Robot() {
    driverController = new PS4Controller(0);
    operatorController = new PS4Controller(1);

    drivetrain = new Drivetrain();

    shooterMotorLeft = new SparkFlex(kShooterLeftCanId, MotorType.kBrushless);
    shooterMotorRight = new SparkFlex(kShooterRightCanId, MotorType.kBrushless);

    // Configure LEFT shooter
    SparkFlexConfig leftCfg = new SparkFlexConfig();
    leftCfg.idleMode(IdleMode.kCoast);
    leftCfg.openLoopRampRate(kShooterOpenLoopRamp);

    // Hard current limit (safety + helps current-based control)
    leftCfg.smartCurrentLimit((int) kShooterCurrentLimitAmps);

    // Closed-loop velocity PID for SPEED mode
    leftCfg.closedLoop
        .feedbackSensor(ClosedLoopConfig.FeedbackSensor.kPrimaryEncoder)
        .pid(kShooterVelKp, kShooterVelKi, kShooterVelKd)
        .velocityFF(kShooterVelKff);

    shooterMotorLeft.configure(leftCfg, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Configure RIGHT shooter to follow LEFT (inverted follower like your original)
    SparkFlexConfig rightCfg = new SparkFlexConfig();
    rightCfg.follow(shooterMotorLeft, true);
    rightCfg.idleMode(IdleMode.kCoast);
    rightCfg.smartCurrentLimit((int) kShooterCurrentLimitAmps);
    shooterMotorRight.configure(rightCfg, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    shooterPID = shooterMotorLeft.getClosedLoopController();
    shooterEncoder = shooterMotorLeft.getEncoder();

    intakeMotor = new WPI_VictorSPX(kIntakeCanId);
    indexerMotor = new WPI_VictorSPX(kIndexerCanId);

    intakeMotor.setNeutralMode(NeutralMode.Coast);
    indexerMotor.setNeutralMode(NeutralMode.Coast);

    intakeMotor.set(0.0);
    indexerMotor.set(0.0);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * Deadband a joystick input to remove small amounts of drift.
   * If the absolute value of the input is less than 0.001, return 0.
   * Otherwise, return the input unchanged.
   */
  private double joystickDeadband(double input) {
    if (input * input < 0.001) {
      return 0.0;
    }
    return input;
  }

  @Override
  public void teleopInit() {
    drivetrain.setMode(NeutralMode.Brake);
  }

  @Override
  public void teleopPeriodic() {
    // ---- Driver drive control ----
    double curLeftYVal = driverController.getLeftY();
    double curRightXVal = driverController.getRightX();
    throttle = speedModifer * joystickDeadband(-(curLeftYVal) * Math.abs(curLeftYVal));
    steer = speedModifer * joystickDeadband(curRightXVal * Math.abs(curRightXVal));
    drivetrain.drive(throttle, steer);

    // ---- Shooter control ----
    // Right stick = shooter enable (push up = on). Magnitude is ignored in VOLTAGE/SPEED by default.
    double rightY = operatorController.getRightY();
    double shooterCmd = -rightY;

    shooterCmd = MathUtil.applyDeadband(shooterCmd, kShooterDeadband);
    shooterCmd = MathUtil.clamp(shooterCmd, 0.0, 1.0);

    // Mode switching: D-pad Up=Current, Right=Voltage, Left=Speed
    if (operatorController.getPOV() == 0) shooterMode = ShooterMode.CURRENT;
    if (operatorController.getPOV() == 90) shooterMode = ShooterMode.VOLTAGE;
    if (operatorController.getPOV() == 270) shooterMode = ShooterMode.SPEED;

    if (shooterCmd <= 0.0) {
      shooterMotorLeft.set(0.0);
      shooterTrim = 0.0;
      lastTimeSec = 0.0;
    } else {
      switch (shooterMode) {
        case VOLTAGE: {
          shooterMotorLeft.setVoltage(kShooterVoltageVolts);
          break;
        }
        case SPEED: {
          shooterPID.setReference(kShooterTargetRPM, SparkBase.ControlType.kVelocity);
          break;
        }
        case CURRENT:
        default: {
          // Current-based behavior:
          // - smartCurrentLimit is the hard safety cap
          // - below is a simple trim loop to bias duty until current ~= target
          double now = Timer.getFPGATimestamp();
          double dt = (lastTimeSec == 0.0) ? 0.02 : (now - lastTimeSec);
          lastTimeSec = now;

          double currentA = shooterMotorLeft.getOutputCurrent();
          double errorA = kShooterCurrentTargetAmps - currentA;

          // Proportional trim (keep small)
          shooterTrim += kShooterCurrentKp * errorA * dt * 50.0; // dt-normalized
          shooterTrim = MathUtil.clamp(shooterTrim, -kShooterTrimMax, kShooterTrimMax);

          double duty = kShooterBaseDuty + shooterTrim;
          duty = MathUtil.clamp(duty, 0.0, 1.0);

          shooterMotorLeft.set(duty);
          break;
        }
      }
    }

    // Debug (recommended while tuning)
    SmartDashboard.putString("ShooterMode", shooterMode.toString());
    SmartDashboard.putNumber("ShooterCurrent(A)", shooterMotorLeft.getOutputCurrent());
    SmartDashboard.putNumber("ShooterRPM", shooterEncoder.getVelocity());
    SmartDashboard.putNumber("ShooterAppliedOut", shooterMotorLeft.getAppliedOutput());

    // ---- Intake + Indexer ----
    // Operator Controller: intake while L1 held
    if (operatorController.getL1Button()) {
      intakeMotor.set(kIntakePercentOutput);
    } else {
      intakeMotor.set(0.0);
    }

    // Operator Controller: indexer while R1 held
    if (operatorController.getR1Button()) {
      indexerMotor.set(kIndexerPercentOutput);
    } else {
      indexerMotor.set(0.0);
    }
  }

  @Override
  public void disabledInit() {
    drivetrain.setMode(NeutralMode.Coast);

    // Safety: stop motors when disabled
    shooterMotorLeft.set(0.0);
    intakeMotor.set(0.0);
    indexerMotor.set(0.0);
  }

  @Override public void disabledPeriodic() {}
  @Override public void testInit() {}
  @Override public void testPeriodic() {}
  @Override public void simulationInit() {}
  @Override public void simulationPeriodic() {}
}
