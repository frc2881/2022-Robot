package frc.robot.commands;

import java.util.function.DoubleSupplier;
import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveWithJoysticks extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final DoubleSupplier m_forward;
    private final DoubleSupplier m_rotation;

    private Drive m_drive;
    
    public DriveWithJoysticks(Drive drive, DoubleSupplier forward, DoubleSupplier rotation) {
        m_forward = forward;
        m_rotation = rotation;

        m_drive = drive;

        addRequirements(m_drive);
    }

    @Override
    public void execute() {

        m_drive.arcadeDrive(m_forward.getAsDouble(), m_rotation.getAsDouble());
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