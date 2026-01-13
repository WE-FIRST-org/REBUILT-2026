package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Intake {
    
    private WPI_VictorSPX intakeMotor;

    //This runs once when you say "new Intake()"
    public Intake(int canId) {
        intakeMotor = new WPI_VictorSPX(canId);

        // Clear old settings 
        intakeMotor.configFactoryDefault();

        // Setting it to "Coast" so it doesn't jerk to a stop
        intakeMotor.setNeutralMode(NeutralMode.Coast);
    }

    // Run the intake
    // Positive speed = IN, Negative speed = OUT
    public void run(double speed) {
        intakeMotor.set(speed);
    }

    // Stop the intake
    public void stop() {
        intakeMotor.set(0.0);
    }
}