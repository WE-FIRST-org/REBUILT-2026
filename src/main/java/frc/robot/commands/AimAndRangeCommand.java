package frc.robot.commands;

import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

import static frc.robot.Constants.AimAndRangeConstants.*;

public class AimAndRangeCommand extends Command {
    private final CANDriveSubsystem m_drive;
    private final VisionSubsystem m_vision;
    
    private final PIDController turnPID = new PIDController(TURN_P, TURN_I,TURN_D);
    private final PIDController drivePID = new PIDController(DRIVE_P, DRIVE_I, DRIVE_D);

    public AimAndRangeCommand(CANDriveSubsystem drive, VisionSubsystem vision) {
        m_drive = drive;
        m_vision = vision;
        addRequirements(m_drive, m_vision); // Interrupts other drive commands
    }

    @Override
    public void execute() {
        
        var result = m_vision.getLatestResult();
        
        if (result.hasTargets()) {
            PhotonTrackedTarget target = result.getBestTarget();
            
            // Calculate PID outputs
            double rotationSpeed = turnPID.calculate(target.getYaw(), 0);

            // Use PhotonUtils to find distance based on your camera's physical mounting
            double range = PhotonUtils.calculateDistanceToTargetMeters(
                CAMERA_HEIGHT_METERS,
                HUB_TARGET_HEIGHT_METERS,
                CAMERA_PITCH_RADIANS,
                Math.toRadians(target.getPitch())
            );
            double forwardSpeed = drivePID.calculate(range, DISTANCE_GOAL_METERS);

            // Command the drivetrain (adjust signs based on your robot)
            m_drive.driveArcade(forwardSpeed, -rotationSpeed);
        } else {
            m_drive.stop(); // Don't move if we lose the target
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.stop();
    }
}