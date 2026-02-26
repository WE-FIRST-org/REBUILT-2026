package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;


public class DisplayTarget extends Command {
    VisionSubsystem m_visionSubsystem;

    public DisplayTarget(VisionSubsystem visionSystem) {
        addRequirements(visionSystem);
        this.m_visionSubsystem = visionSystem;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        SmartDashboard.putBoolean("Has Target", m_visionSubsystem.getHasTarget());
        SmartDashboard.putNumber("Best Target", m_visionSubsystem.getBestTarget().getFiducialId());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
