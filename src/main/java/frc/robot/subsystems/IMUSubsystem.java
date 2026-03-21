package frc.robot.subsystems; 

// import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearAcceleration;

import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;

import com.studica.frc.Navx;

public class IMUSubsystem extends SubsystemBase {
    private Navx navx;
    // private SimDeviceSim device;

    // Required to get different readings
    private Quaternion quat = navx.getQuat6D();
    private AngularVelocity[] omega = navx.getAngularVel();
    private Quaternion quat9 = navx.getQuat9D();
    private LinearAcceleration[] accel = navx.getLinearAccel();
    private float[] mag = new float[4];
    // private int error = navx.getCompass(mag);
    
    public IMUSubsystem() {
        // Create NavX Object
        navx = new Navx(0, 100); // CAN
        // navx = new Navx(Navx.Port.kUSB1); // USB 
    
        // Create Sim Object
        // device = new SimDeviceSim("NavX3", 0);
        
        // You can enable which messages the navx sends to prevent bus saturation 
        navx.enableOptionalMessages(true,
                                    true,
                                    true,
                                    true, 
                                    true, 
                                    true, 
                                    true, 
                                    true, 
                                    true, 
                                    true);
    
        // UUID of the navx if thats something you want
        int[] uuids = new int[3];
        navx.getSensorUUID(uuids);
        SmartDashboard.putNumber("uuid0", uuids[0]);
        SmartDashboard.putNumber("uuid1", uuids[1]);
        SmartDashboard.putNumber("uuid2", uuids[2]);
    
        // Add reset yaw btn to dashboard
        SmartDashboard.putBoolean("Reset Yaw", false);
    }
    
    @Override
    public void periodic() {
        // Yaw, Pitch, Roll, Angle
        SmartDashboard.putNumber("Yaw:",  navx.getYaw().in(Degrees));
        SmartDashboard.putNumber("Pitch:", navx.getPitch().in(Degrees));
        SmartDashboard.putNumber("Roll:", navx.getRoll().in(Degrees));
        SmartDashboard.putNumber("Angle:", navx.getAngle().in(Degrees));
    
        // 6-axis quaternion
        SmartDashboard.putNumber("q6_w:", quat.getW());
        SmartDashboard.putNumber("q6_x:", quat.getX());
        SmartDashboard.putNumber("q6_y:", quat.getY());
        SmartDashboard.putNumber("q6_z:", quat.getZ());
    
        // Angular Velocity
        SmartDashboard.putNumber("w_x:", omega[0].in(DegreesPerSecond));
        SmartDashboard.putNumber("w_y:", omega[1].in(DegreesPerSecond));
        SmartDashboard.putNumber("w_z:", omega[2].in(DegreesPerSecond)); 
    
        // 9-axis quaternion 
        SmartDashboard.putNumber("q9_w:", quat9.getW());
        SmartDashboard.putNumber("q9_x:", quat9.getX());
        SmartDashboard.putNumber("q9_y:", quat9.getY());
        SmartDashboard.putNumber("q9_z:", quat9.getZ());
    
        // Linear Acceleration
        SmartDashboard.putNumber("v_x:", accel[0].in(MetersPerSecondPerSecond));
        SmartDashboard.putNumber("v_y:", accel[1].in(MetersPerSecondPerSecond));
        SmartDashboard.putNumber("v_z:", accel[2].in(MetersPerSecondPerSecond));
    
        // Get Compass reading from Mag
        SmartDashboard.putNumber("mag_1:", mag[0]); 
        SmartDashboard.putNumber("mag_2:", mag[1]);
        SmartDashboard.putNumber("mag_3:", mag[2]);
        SmartDashboard.putNumber("mag_4:", mag[3]);
      
        // Temperature reported by IMU
        SmartDashboard.putNumber("temp:", navx.getTemperature().in(Celsius));
    
        if (SmartDashboard.getBoolean("Reset Yaw", false))
        {
          navx.resetYaw();
          SmartDashboard.putBoolean("Reset Yaw", false);
        }
    }

    public double getYaw() {
        return navx.getYaw().in(Degrees);
    }

    public double getPitch() {
        return navx.getPitch().in(Degrees);
    }

    public double getRoll() {
        return navx.getRoll().in(Degrees);
    }

    public double getAngle() {
        return navx.getAngle().in(Degrees);
    }

    public double getQ6W() {
        return quat.getW();
    }

    public double getQ6X() {
        return quat.getX();
    }

    public double getQ6Y() {
        return quat.getY();
    }

    public double getQ6Z() {
        return quat.getZ();
    }

    public double getAngularVelX() {
        return omega[0].in(DegreesPerSecond);
    }

    public double getAngularVelY() {
        return omega[1].in(DegreesPerSecond);
    }

    public double getAngularVelZ() {
        return omega[2].in(DegreesPerSecond);
    }

    public double getQ9W() {
        return quat9.getW();
    }

    public double getQ9X() {
        return quat9.getX();
    }

    public double getQ9Y() {
        return quat9.getY();
    }

    public double getQ9Z() {
        return quat9.getZ();
    }

    public double getLinearAccelX() {
        return accel[0].in(MetersPerSecondPerSecond);
    }

    public double getLinearAccelY() {
        return accel[1].in(MetersPerSecondPerSecond);
    }

    public double getLinearAccelZ() {
        return accel[2].in(MetersPerSecondPerSecond);
    }

    public double getCompassMag1() {
        return mag[0];
    }

    public double getCompassMag2() {
        return mag[1];
    }

    public double getCompassMag3() {
        return mag[2];
    }

    public double getCompassMag4() {
        return mag[3];
    }

    public double getTemp() {
        return navx.getTemperature().in(Celsius);
    }
}
