// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.utils.NavX;

public class RotateByDegrees extends CommandBase {
  private final NavX m_navx;
  private final Drive m_drive;
  private final DoubleSupplier m_turn;

  private PIDController pid;

  public RotateByDegrees(NavX navx, Drive drive, DoubleSupplier turn) {
    double kp, ki, kd;

    m_navx = navx;
    m_turn = turn;
    m_drive = drive;
    addRequirements(m_drive);
    kp = 0.12;
    //SmartDashboard.setDefaultNumber("RotateByDegrees kp", kp);
    ki = 0.0;
    //SmartDashboard.setDefaultNumber("RotateByDegrees ki", ki);
    kd = 0.007;
    //SmartDashboard.setDefaultNumber("RotateByDegrees kd", kd);
    pid = new PIDController(kp, ki, kd);

    //SmartDashboard.putNumber("RotateByDegrees Turn Amount", 0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double target;

    //target = m_navx.getAngle() - SmartDashboard.getNumber("RotateByDegrees Turn Amount", 0);
    target = m_navx.getAngle() - m_turn.getAsDouble();
    pid.reset();
    pid.setSetpoint(target);
    pid.setTolerance(1.0, 0.5);
    //pid.setP(SmartDashboard.getNumber("RotateByDegrees kp", 0.0));
    //pid.setI(SmartDashboard.getNumber("RotateByDegrees ki", 0.0));
    //pid.setD(SmartDashboard.getNumber("RotateByDegrees kd", 0.0));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double spin;

    spin = pid.calculate(m_navx.getAngle());
    if(Math.abs(spin) < 0.05) {
      spin = Math.copySign(0.05, spin);
    }
    spin = MathUtil.clamp(spin, -0.3, 0.3);
    m_drive.arcadeDrive(0, spin);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return pid.atSetpoint();
  }
}
