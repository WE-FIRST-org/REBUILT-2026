// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  boolean inversed;

  private PS4Controller driverController;
  private PS4Controller operatorController;

  private Drivetrain drivetrain;
  private Shooter shooter;
  private Intake intake;  // Intake object

  // Shooter 
  //private SparkFlex shooterMotorLeft;
  //private SparkFlex shooterMotorRight;

  // Intake + Indexer 
  //private WPI_VictorSPX intakeMotor;
  //private WPI_VictorSPX indexerMotor;

  private double throttle, steer;
  private double speedModifer = 1;

  //private static final int kShooterLeftCanId = 13;
  //private static final int kShooterRightCanId = 14;
  // Set these to actual CAN IDs
  //private static final int kIntakeCanId = 9;   // CHANGE ME
  //private static final int kIndexerCanId = 21;  // CHANGE ME

  // Motor outputs
  private static final double kIntakePercentOutput = 0.7;
  //private static final double kIndexerPercentOutput = 0.7;

  // Shooter stick shaping
  //private static final double kShooterDeadband = 0.05;

  /** This function is run when the robot is first started up and should be used for any initialization code. */
  public Robot() {

    driverController = new PS4Controller(0);
    operatorController = new PS4Controller(1);

    drivetrain = new Drivetrain();
    shooter = new Shooter();
    intake = new Intake();

    //shooterMotorLeft = new SparkFlex(kShooterLeftCanId, MotorType.kBrushless);
    //shooterMotorRight = new SparkFlex(kShooterRightCanId, MotorType.kBrushless);

    SparkFlexConfig rightCfg = new SparkFlexConfig();
    //rightCfg.follow(shooterMotorLeft, true); // set false to not invert follower if needed
    //shooterMotorRight.configure(rightCfg, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    //intakeMotor = new WPI_VictorSPX(kIntakeCanId);
    //indexerMotor = new WPI_VictorSPX(kIndexerCanId);

    //intakeMotor.setNeutralMode(NeutralMode.Coast);
    //indexerMotor.setNeutralMode(NeutralMode.Coast);

    //intakeMotor.set( 0.0);
    //indexerMotor.set( 0.0);

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
   * @param input the joystick input to deadband
   * @return the deadbanded input
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
    inversed = false;
  }

  @Override
  public void teleopPeriodic() {
    //---- DRIVER DRIVE CONTROL ----
    double curLeftYVal = driverController.getLeftY();
    double curRightXVal = driverController.getRightX();
    throttle = speedModifer * joystickDeadband(-(curLeftYVal) * Math.abs(curLeftYVal));
    steer = speedModifer * joystickDeadband(curRightXVal * Math.abs(curRightXVal));

    // throttle = 0.4 * joystickDeadband(-(driverController.getLeftY())*Math.abs(driverController.getLeftY()));
    // steer = 0.20 * joystickDeadband(driverController.getRightX() * Math.abs(driverController.getRightX()));
    // if(inversed){
    //   throttle *= -1;
    //   steer *= -1;
    // }
    // if (driverController.getLeftBumperButtonPressed()){
    //   inversed = !inversed;
    // }


    drivetrain.drive(throttle, steer);


    //double rightY = operatorController.getRightY();
    //double shooterCmd = -rightY;

    // shooterCmd = MathUtil.applyDeadband(shooterCmd, kShooterDeadband);
    // shooterCmd = MathUtil.clamp(shooterCmd, 0.0, 1.0);

    // shooterMotorLeft.set(shooterCmd);

    // SHOOTER AND INDEXER CONTROL
    if (operatorController.getR2Button()) {   
        shooter.shootOn();   // Spins shooter and indexer together
    } else {
        shooter.shootOff();  // Stops shooter and indexer
    }

    // OPERATOR CONTROL: intake while LB held
    if (operatorController.getL1Button()) { 
      intake.run(kIntakePercentOutput);
    } else {
      intake.stop();
    }
    // if (operatorController.getL1Button()) { 
    //   intakeMotor.set( kIntakePercentOutput);
    // } else {
    //   intakeMotor.set( 0.0);
    // }

    // // Operator Controller: indexer while RB held 
    // if (operatorController.getR1Button()) {
    //   indexerMotor.set( kIndexerPercentOutput);
    // } else {
    //   indexerMotor.set( 0.0);
    // }
  }

  @Override
  public void disabledInit() {
    drivetrain.setMode(NeutralMode.Coast);

    // Safety: stop motors when disabled
    shooter.shootOff();
    intake.stop();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
