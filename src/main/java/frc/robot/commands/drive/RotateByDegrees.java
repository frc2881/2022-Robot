// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.utils.NavX;

public class RotateByDegrees extends CommandBase {
  
  private final NavX m_navx;
  private final Drive m_drive;
  private final DoubleSupplier m_turn;

  private double target;
  private double kmax;
  private double kp;
  private double ki;
  private double kd;
  private double kturn;
  private double error;
  private double spin;
  private PIDController pid;


  public RotateByDegrees(NavX navx, Drive drive, DoubleSupplier turn) {
    m_navx = navx;
    m_turn = turn;
    m_drive = drive;
    SmartDashboard.putNumber("turn kp", 0.12);
    SmartDashboard.putNumber("turn ki", 0.0004);
    SmartDashboard.putNumber("turn kd", 0.005);
    SmartDashboard.putNumber("Turn Value", 5);
    kmax = 0.15;
    kp = kmax;
    ki = 0;
    kd = 0; 
    pid = new PIDController(kp, ki, kd);
    
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pid.setP(SmartDashboard.getNumber("turn kp", 0.09));
    pid.setI(SmartDashboard.getNumber("turn ki", 0.00005));
    pid.setD(SmartDashboard.getNumber("turn kd", 0.004));
    target = m_navx.getAngle() - m_turn.getAsDouble();
    pid.reset();
    pid.setSetpoint(target);
    pid.setTolerance(.5);
    SmartDashboard.putNumber("target", target);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    error = target - m_navx.getAngle();
    spin = pid.calculate(m_navx.getAngle());
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
    return false;
  }
}
