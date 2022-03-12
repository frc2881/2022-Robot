// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import javax.print.attribute.standard.PrinterMessageFromOperator;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.utils.NavX;

public class RotateByDegrees extends CommandBase {
  
  private final NavX m_navx;
  private final Drive m_drive;
  private final DoubleSupplier m_turn;

  private double target;
  private double rotationspeed;
  private double p;
  private double i;
  private double d;
  private double error;
  private double spin;
  private PIDController pid;


  public RotateByDegrees(NavX navx, Drive drive, DoubleSupplier turn) {
    m_navx = navx;
    m_turn = turn;
    m_drive = drive;
    p = 0.025;
    i = 0;
    d = 0;
    pid = new PIDController(p, i, d);
    // Use addRequirements() here to declare subsystem dependencies.
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    target = m_navx.getAngle() - m_turn.getAsDouble();
    pid.reset();
    pid.setSetpoint(target);
    pid.setTolerance(1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    //rotationspeed = (target - m_navx.getAngle()) * 0.2;
    error = target - m_navx.getAngle();
    spin = pid.calculate(m_navx.getAngle());

    if (Math.abs(spin) < 0.3) {
      spin = Math.copySign(0.3, spin);
    } else if (Math.abs(spin) > 0.7) {
      spin = Math.copySign(0.7, spin);
    }
    
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
    if (pid.atSetpoint()) {
        return true; 
    } else {
        return false;
    }
  }
}
