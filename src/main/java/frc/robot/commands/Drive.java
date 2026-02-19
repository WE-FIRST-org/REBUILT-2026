// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static frc.robot.Constants.OperatorConstants.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import frc.robot.subsystems.CANDriveSubsystem;


/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Drive extends Command {
  //class member subsystem fields
  CANDriveSubsystem m_driveSubsystem;
  CommandPS4Controller m_controller;

  public Drive(CANDriveSubsystem driveSystem, CommandPS4Controller driverController) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSystem);
    m_driveSubsystem = driveSystem;
    m_controller = driverController;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  /**
   * Command is executed based on the movement of the joy stick, it will respect the inverted status of the robot.
   */
  @Override
  public void execute() {
    boolean isInverted = InvertDrive.getInvertStatus();
    if(isInverted){
      m_driveSubsystem.driveArcade(m_controller.getLeftY() * DRIVE_SCALING, -m_controller.getLeftX() * ROTATION_SCALING);
    }else{
      m_driveSubsystem.driveArcade(-m_controller.getLeftY() * DRIVE_SCALING, m_controller.getLeftX() * ROTATION_SCALING);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.driveArcade(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
  
}
