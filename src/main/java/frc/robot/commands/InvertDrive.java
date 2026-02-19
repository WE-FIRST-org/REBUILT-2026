package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class InvertDrive extends Command {
    //creates a class level(m) variable 
    private static boolean m_Inverted = false;

    public InvertDrive(){
    }
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        toggleInvert();
    }
    public static void toggleInvert(){
        if(m_Inverted){
            m_Inverted = false;
        }else{
            m_Inverted = true;
        }
    }


    @Override
    public boolean isFinished() {
        // The command is always finished
        return true;
    }

    public static boolean getInvertStatus(){
        return m_Inverted;
    }

}