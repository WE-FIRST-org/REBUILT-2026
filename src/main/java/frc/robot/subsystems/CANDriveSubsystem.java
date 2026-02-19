// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;

public class CANDriveSubsystem extends SubsystemBase {
  private final WPI_VictorSPX m_leftLeader,m_leftFollower,m_rightLeader,m_rightFollower;

  private final DifferentialDrive m_drive;

  public CANDriveSubsystem() {
    //class member motor controller field
    //template for Spark motorObject = new SparkMax(k_Motor_ID, MotorType.kBrushed); // or MotorType.kBrushless(if using Neo)
    m_leftLeader = new WPI_VictorSPX(k_LEFT_LEADER_ID);
    m_leftFollower = new WPI_VictorSPX(k_LEFT_FOLLOWER_ID);
    m_rightLeader = new WPI_VictorSPX(k_RIGHT_LEADER_ID);
    m_rightFollower = new WPI_VictorSPX(k_RIGHT_FOLLOWER_ID);

    // set up differential drive class
    m_drive = new DifferentialDrive(m_leftLeader, m_rightLeader);

    // Configure inversion/followers
    m_rightLeader.setInverted(true);
    m_rightFollower.setInverted(true);
    m_rightFollower.follow(m_rightLeader);

    m_leftLeader.setInverted(false);
    m_leftFollower.setInverted(false);
    m_leftFollower.follow(m_leftFollower);

    /*
     * //This is meant for SparkMax only
     * //make sure to use motorObject.setCANTimeout(250)
     * SparkMaxConfig config = new SparkMaxConfig();
     * config.voltageCompensation(12);
     * config.smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT);
     * 
     * //Configuration for each motor
     * Resetting in case a new controller is swapped in and persisting in case of a controller reset due to breaker trip
     * config.follow(leftLeader);
     * leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
     * config.follow(rightLeader);
     * rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
     * // Remove following, then apply config to right leader
     * config.disableFollowerMode();
     * rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
     * // Set config to inverted and then apply to left leader. Set Left side inverted
     * // so that postive values drive both sides forward
     * config.inverted(true);
     * leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
     */
    
  }

  @Override
  public void periodic() {

  }

  /**
   * Controls the robot in an arcadic behavior
   * @param xSpeed value range from [1,-1]
   * @param zRotation value range from [-1,1] Counterclockwise is positive.
   */
  public void driveArcade(double xSpeed, double zRotation) {
    m_drive.arcadeDrive(xSpeed, zRotation);
  }

  /**
   * 
   */
  public void stop() {
    m_drive.stopMotor();
  }

}
