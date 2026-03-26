// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
// import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import static frc.robot.Constants.OperatorConstants.*;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

   // The robot's subsystems
  public final CANDriveSubsystem m_driveSubsystem = new CANDriveSubsystem();
  public final CANFuelSubsystem m_fuelSubsystem = new CANFuelSubsystem();
  public final VisionSubsystem m_visionSubsystem = new VisionSubsystem();
  public final IMUSubsystem m_imuSubsystem = new IMUSubsystem();

    // The driver's controller
  private final CommandXboxController m_driverController = new CommandXboxController(
      DRIVER_CONTROLLER_PORT);

  // The operator's controller
  private final CommandXboxController m_operatorController = new CommandXboxController(
      OPERATOR_CONTROLLER_PORT);

  // The autonomous chooser
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

 
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureBindings();

    // Set the options to show up in the Dashboard for selecting auto modes. If you
    // add additional auto modes you can add additional lines here with
    // autoChooser.addOption
    // autoChooser.setDefaultOption("Autonomous", new ExampleAuto(m_driveSubsystem, m_fuelSubsystem));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the {@link Trigger#Trigger(java.util.function.BooleanSupplier)}
   * constructor with an arbitrary predicate, or via the named factories in
   * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
   * for {@link CommandXboxController Xbox}/
   * {@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_driverController.leftBumper().onTrue(new InvertDrive());
    m_driverController.rightTrigger(0.1).whileTrue(new AimAndRangeCommand(m_driveSubsystem, m_visionSubsystem));

    // m_operatorController.rightBumper().whileTrue(new LaunchSequence(m_fuelSubsystem));
    m_operatorController.rightBumper().whileTrue(new Launch(m_fuelSubsystem));
    // m_operatorController.rightTrigger(0.1)
    // .whileTrue(new RunCommand(
    //     () -> m_fuelSubsystem.setShooter(m_operatorController.getRightTriggerAxis()), 
    //     m_fuelSubsystem
    // ));
    m_operatorController.x().whileTrue(new Intake(m_fuelSubsystem));
    m_operatorController.a().whileTrue(new Eject(m_fuelSubsystem));

    // Set the default command for the drive subsystem to the command provided by
    // factory with the values provided by the joystick axes on the driver
    // controller. The Y axis of the controller is inverted so that pushing the
    // stick away from you (a negative value) drives the robot forwards (a positive
    // value)
    m_driveSubsystem.setDefaultCommand(new Drive(m_driveSubsystem, m_driverController));
    m_fuelSubsystem.setDefaultCommand(m_fuelSubsystem.run(() -> m_fuelSubsystem.stop()));



    // finding feedforward constant
    m_driverController.y().whileTrue(m_driveSubsystem.sysIdQuasistaticLinear(Direction.kForward));
    m_driverController.a().whileTrue(m_driveSubsystem.sysIdQuasistaticLinear(Direction.kReverse));
    m_driverController.b().whileTrue(m_driveSubsystem.sysIdDynamicLinear(Direction.kForward));
    m_driverController.x().whileTrue(m_driveSubsystem.sysIdDynamicLinear(Direction.kReverse));

    m_driverController.povUp().whileTrue(m_driveSubsystem.sysIdQuasistaticAngular(Direction.kForward));
    m_driverController.povDown().whileTrue(m_driveSubsystem.sysIdQuasistaticAngular(Direction.kReverse));
    m_driverController.povRight().whileTrue(m_driveSubsystem.sysIdDynamicAngular(Direction.kForward));
    m_driverController.povLeft().whileTrue(m_driveSubsystem.sysIdDynamicAngular(Direction.kReverse));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return autoChooser.getSelected();
  }
}
