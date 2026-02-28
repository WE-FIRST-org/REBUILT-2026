// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.FuelConstants.*;

public class CANFuelSubsystem extends SubsystemBase {
  //class member Fields needed for the subsystem
  private final WPI_VictorSPX m_Intake_Motor;
  private final WPI_VictorSPX m_Indexer_Motor;
  // private final SparkMax m_Left_Shooter_Motor;
  // private final SparkMax m_Right_Shooter_Motor;

  public CANFuelSubsystem() {
    // create brushed motors for each of the motors on the launcher mechanism
    // m_Left_Shooter_Motor = new SparkMax(k_SHOOTER_LEFT_MOTOR_ID,MotorType.kBrushless);
    // m_Right_Shooter_Motor = new SparkMax(k_SHOOTER_RIGHT_MOTOR_ID, MotorType.kBrushless);
    
    m_Indexer_Motor = new WPI_VictorSPX(k_INDEXER_MOTOR_ID);
    
    m_Intake_Motor = new WPI_VictorSPX(k_INTAKE_MOTOR_ID);

    //Configurating the Shooters
    SparkMaxConfig ShooterConfig = new SparkMaxConfig();
    ShooterConfig.smartCurrentLimit(SHOOTER_MOTOR_CURRENT_LIMIT);
    // m_Left_Shooter_Motor.configure(ShooterConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    // ShooterConfig.follow(m_Left_Shooter_Motor,true);
    // m_Right_Shooter_Motor.configure(ShooterConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    // m_Left_Shooter_Motor.set(0);
    // m_Right_Shooter_Motor.set(0);

    m_Indexer_Motor.setNeutralMode(NeutralMode.Coast);
    m_Indexer_Motor.set(0);

    m_Intake_Motor.setNeutralMode(NeutralMode.Coast);
    m_Intake_Motor.set(0);

    // put default values for various fuel operations onto the dashboard
    // all commands using this subsystem pull values from the dashbaord to allow
    // you to tune the values easily, and then replace the values in Constants.java
    // with your new values. For more information, see the Software Guide.
    SmartDashboard.putNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Intaking intake roller value", INTAKING_INTAKE_VOLTAGE);
    SmartDashboard.putNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Launching launcher roller value", LAUNCHING_LAUNCHER_VOLTAGE);
    SmartDashboard.putNumber("Spin-up feeder roller value", SPIN_UP_FEEDER_VOLTAGE);
  }

  // A method to set the voltage of the intake roller
  public void setShooter(double voltage) {
    // m_Left_Shooter_Motor.setVoltage(voltage);
  }

  public void setIndexer(double voltage){
    m_Indexer_Motor.setVoltage(voltage);
  }

  // A method to set the voltage of the intake roller
  public void setIntakeMotor(double voltage) {
    m_Intake_Motor.setVoltage(voltage);
  }

  /**
   * stop all action within the Fuel Subsystem
   */
  public void stop() {
    m_Intake_Motor.set(0);
    m_Indexer_Motor.set(0);
    // m_Left_Shooter_Motor.set(0);
  }
}
