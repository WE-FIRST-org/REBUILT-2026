package frc.robot;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Shooter {
    public static final int kGUN_LEFT = 8;
    public static final int kGUN_RIGHT = 9;
    public static final int kINDEXER = 5;  // CAN ID for the BAG motor

    private final SparkMax m_shooterLEFT;
    private final SparkMax m_shooterRIGHT;
    private final WPI_VictorSPX m_indexer;

    private static final double kIndexerPercentOutput = 0.7;  // set speed for BAG motor

    public Shooter() {
        m_shooterLEFT = new SparkMax(kGUN_LEFT, MotorType.kBrushless);
        m_shooterRIGHT = new SparkMax(kGUN_RIGHT, MotorType.kBrushless);

        //LEFT MOTOR CONFIG
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftConfig.smartCurrentLimit(80);
        m_shooterLEFT.configure(leftConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        m_shooterLEFT.set(0);

        //RIGHT MOTOR CONFIG
        // Invert ONE side so wheels spin opposite directions
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightConfig.smartCurrentLimit(80);
        //rightConfig.inverted(true);
        rightConfig.follow(m_shooterLEFT, true); // set false to not invert follower if needed
        m_shooterRIGHT.configure(rightConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        m_shooterRIGHT.set(0);

        // INDEXER MOTOR (BAG)
        m_indexer = new WPI_VictorSPX(kINDEXER);
        m_indexer.setNeutralMode(NeutralMode.Coast);
        m_indexer.set(0.0); // start stopped
    }

    //ON Shooter 
    public void shootOn() {
        m_shooterLEFT.set(1.0);   // 100%
        m_shooterRIGHT.set(1.0);
        m_indexer.set(0.7);
    }

    //OFF Shooter
    public void shootOff() {
        m_shooterLEFT.set(0.0);
        m_shooterRIGHT.set(0.0);
        m_indexer.set(0.0);
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

