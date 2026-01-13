package frc.robot;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Shooter {
    public static final int kGUN_LEFT = 13;
    public static final int kGUN_RIGHT = 14;

    private final SparkMax m_shooterLEFT;
    private final SparkMax m_shooterRIGHT;

    public Shooter() {
        m_shooterLEFT = new SparkMax(kGUN_LEFT, MotorType.kBrushless);
        m_shooterRIGHT = new SparkMax(kGUN_RIGHT, MotorType.kBrushless);

        //LEFT MOTOR CONFIG
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftConfig.smartCurrentLimit(80);
        m_shooterLEFT.configure(leftConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

        //RIGHT MOTOR CONFIG
        // Invert ONE side so wheels spin opposite directions
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightConfig.smartCurrentLimit(80);
        rightConfig.inverted(true);
        m_shooterRIGHT.configure(rightConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }

    //ON Shooter 
    public void shootOn() {
        m_shooterLEFT.set(1.0);   // 100%
        m_shooterRIGHT.set(1.0);
    }

    //OFF Shooter
    public void shootOff() {
        m_shooterLEFT.set(0.0);
        m_shooterRIGHT.set(0.0);
    }

    // public void setSpeed(double speed) {
    //     m_shooterLEFT.set(speed);
    //     m_shooterRIGHT.set(-speed);
    // }

}


//     // Optional: if you still want variable speed later
//     public void setSpeed(double speed) {
//         m_shooterLEFT.set(speed);
//         m_shooterRIGHT.set(speed);
//     }
// }

