package frc.robot.commands;

import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

import static frc.robot.Constants.VisionConstants.*;
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
    public void initialize() {
        turnPID.reset();
        drivePID.reset();
    }

    @Override
    public void execute() {
        PhotonPipelineResult result = m_vision.getLatestResult();

        if (result == null || !result.hasTargets()) {
            m_drive.stop();
            SmartDashboard.putBoolean("AimAndRange/HasTarget", false);
            return;
        }
        
        PhotonTrackedTarget target = result.getBestTarget();
        if (target == null) {
            m_drive.stop();
            SmartDashboard.putBoolean("AimAndRange/HasTarget", false);
            return;
        }

        SmartDashboard.putBoolean("AimAndRange/HasTarget", true);

        double distance = m_vision.getDistanceToTarget(target); // meters (may be NaN)
        double angleDeg = target.getYaw(); // degrees

        // Defensive: ensure numeric distance
        if (Double.isNaN(distance)) {
            m_drive.stop();
            SmartDashboard.putString("AimAndRange/Distance", "NaN");
            return;
        }

        // PID: measurement, setpoint
        double rotationSpeed = turnPID.calculate(angleDeg, 0.0); // turn to yaw=0
        double forwardSpeed = drivePID.calculate(distance, DISTANCE_GOAL_METERS);

        // Clamp outputs to safe [-1,1] (or your motor input range)
        rotationSpeed = Math.max(-1.0, Math.min(1.0, rotationSpeed));
        forwardSpeed  = Math.max(-1.0, Math.min(1.0, forwardSpeed));

        SmartDashboard.putNumber("AimAndRange/DistanceMeters", distance);
        SmartDashboard.putNumber("AimAndRange/AngleDeg", angleDeg);
        SmartDashboard.putNumber("AimAndRange/ForwardSpeed", forwardSpeed);
        SmartDashboard.putNumber("AimAndRange/RotationSpeed", rotationSpeed);

        m_drive.driveArcade(forwardSpeed, rotationSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}