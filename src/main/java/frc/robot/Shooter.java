package frc.robot;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Shooter extends SubsystemBase {
    public static final int kGUN = 13;
    public static final int kGUN1 = 14;

    private final SparkMax m_shooterLEFT;
    private final SparkMax m_shooterRIGHT;
    public Shooter() {
        m_shooterLEFT = new SparkMax(kGUN, MotorType.kBrushed);
        m_shooterRIGHT = new SparkMax(kGUN1, MotorType.kBrushed);
    }
    public void setSpeed(double speed) {
        m_shooterLEFT.set(speed);
        m_shooterRIGHT.set(-speed);
    }

}