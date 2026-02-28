// MIT License

// Copyright (c) 2024 Isopod00

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
// Author: UMN Robotics Ri3d

package frc.robot.subsystems; 

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.VisionConstants.*;

public class VisionSubsystem extends SubsystemBase {
    PhotonCamera camera; // Declare the name of the camera used in the pipeline
    boolean hasTarget = false; // Stores whether or not a target is detected
    PhotonPipelineResult result = null; // Stores all the data that Photonvision returns
    
    public VisionSubsystem() {
        camera = new PhotonCamera(USB_CAMERA_NAME);
    }
    
    @Override
    public void periodic() {
        this.result = camera.getLatestResult(); // Query the latest result from PhotonVision
        hasTarget = (result != null) && result.hasTargets(); // update boolean

        if (hasTarget && result.getBestTarget() != null) {
            PhotonTrackedTarget best = result.getBestTarget();
            SmartDashboard.putNumber("Best Target ID", best.getFiducialId());
            SmartDashboard.putNumber("t_area", best.getArea());
            SmartDashboard.putNumber("t_pitch", best.getPitch());
            SmartDashboard.putNumber("t_yaw", best.getYaw());
            SmartDashboard.putNumber("t_skew", best.getSkew());
            // If you want distance/InRange telemetry, compute and publish here
            SmartDashboard.putNumber("t_distance_est", getDistanceToTarget(best));
            SmartDashboard.putBoolean("InRange", InRange(0, 5, 0, 5));
        } else {
            SmartDashboard.putNumber("Best Target ID", -1);
            SmartDashboard.putNumber("t_area", 0);
            SmartDashboard.putNumber("t_pitch", 0);
            SmartDashboard.putNumber("t_yaw", 0);
            SmartDashboard.putNumber("t_skew", 0);
            SmartDashboard.putNumber("t_distance_est", 0);
            SmartDashboard.putBoolean("InRange", false);
        }

        InRange(0, 5, 0, 5); // Put to SmartDashboard whether or not the target is in range
    }


    public PhotonPipelineResult getLatestResult() {
        return this.result;
    }

    public PhotonTrackedTarget getTargetWithID(int id) { // Returns the apriltag target with the specified ID (if it exists)
        if (result == null) {
            return null;
        }

        List<PhotonTrackedTarget> targets = result.getTargets(); // Create a list of all currently tracked targets
        for (PhotonTrackedTarget i : targets) {
            if (i.getFiducialId() == id) { // Check the ID of each target in the list
                return i; // Found the target with the specified ID!
            }
        }
        return null; // Failed to find the target with the specified ID
    }
    
    public PhotonTrackedTarget getBestTarget() {
        if (hasTarget && result != null) {
            return result.getBestTarget(); // Returns the best (closest) target
        }
        else {
            return null; // Otherwise, returns null if no targets are currently found
        }
    }

    public boolean getHasTarget() {
        return hasTarget; // Returns whether or not a target was found
    }

    public double getDistanceToTarget(PhotonTrackedTarget target) {
        if (!hasTarget || target == null) {
            return Double.NaN;
        }

        double distance = PhotonUtils.calculateDistanceToTargetMeters(
                CAMERA_HEIGHT_METERS,
                HUB_TARGET_HEIGHT_METERS,
                CAMERA_PITCH_RADIANS,
                Math.toRadians(target.getPitch())
            );
        SmartDashboard.putNumber("t_area", target.getArea());
        SmartDashboard.putNumber("t_pitch", target.getPitch());
        return distance;
    }

    public boolean InRange(double distanceThreshold, double distanceThresholdRange,
    double angleThreshold, double angleThresholdRange) {
        if (!hasTarget) {
            return false;
        }
    
        PhotonTrackedTarget bestTarget = getBestTarget();
        if (bestTarget == null) {
            return false;
        }

        double distanceToTarget  = getDistanceToTarget(bestTarget);
        double angleToTarget = bestTarget.getYaw(); // Assuming yaw gives the angle

        // Check that the target is within the +/- range around the thresholds
        boolean distanceOk = Math.abs(distanceToTarget - distanceThreshold) <= distanceThresholdRange;
        boolean angleOk = Math.abs(angleToTarget - angleThreshold) <= angleThresholdRange;

        return distanceOk && angleOk;
    }
}

// I need to modify 'periodic()' to call a new function 'InRange()' that returns a boolean value if the target is within a distance and angle range 

