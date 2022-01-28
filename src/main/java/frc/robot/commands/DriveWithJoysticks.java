package frc.robot.commands;

import java.util.function.DoubleSupplier;
import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveWithJoysticks extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final DoubleSupplier m_leftForward;
    private final DoubleSupplier m_rightForward;
    private final DoubleSupplier m_rotation;
    

    private Drive m_drive;
     
    
    public DriveWithJoysticks(Drive drive, DoubleSupplier leftForward, DoubleSupplier rightForward, DoubleSupplier rotation) {
        m_leftForward = leftForward;
        m_rightForward = rightForward; 
        m_rotation = rotation;

        m_drive = drive;

        addRequirements(m_drive);
        SmartDashboard.setDefaultBoolean("Set Arcade Drive", true);
    }

    @Override
    public void execute() {

    if(SmartDashboard.getBoolean("Set Arcade Drive", true) == true) {

        m_drive.arcadeDrive(m_leftForward.getAsDouble(), m_rotation.getAsDouble());
    }
    else{

        m_drive.tankDrive(m_leftForward.getAsDouble(),m_rightForward.getAsDouble());

    }
        
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.arcadeDrive(0.0, 0.0);
    }

      @Override
    public boolean isFinished() {
        return false;
    }
}