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
  private double kp;
  private double ki;
  private double kd;
  //private double kTurn;
  //private double error;
  private double spin;
  private PIDController pid;

  private boolean pidOn = false;


  public RotateByDegrees(NavX navx, Drive drive, DoubleSupplier turn) {
    m_navx = navx;
    m_turn = turn;
    m_drive = drive;
    kp = 0.07;
    //SmartDashboard.putNumber("RotateByDegrees kp", kp);
    ki = 0.0017;
    //SmartDashboard.putNumber("RotateByDegrees ki", ki);
    kd = 0.007;
    //SmartDashboard.putNumber("RotateByDegrees kd", kd);
    pid = new PIDController(kp, ki, kd);

    //SmartDashboard.putNumber("RotateByDegrees Turn Amount", 0);
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //target = m_navx.getAngle() - SmartDashboard.getNumber("RotateByDegrees Turn Amount", 0);
    target = m_navx.getAngle() - m_turn.getAsDouble();
    pid.reset();
    pid.setSetpoint(target);
    pid.setTolerance(.5);
    pidOn = false;
    //pid.setP(SmartDashboard.getNumber("RotateByDegrees kp", kp));
    //pid.setI(SmartDashboard.getNumber("RotateByDegrees ki", ki));
    //pid.setD(SmartDashboard.getNumber("RotateByDegrees kd", kd));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(pidOn == false && target - m_navx.getAngle() > 7){
      m_drive.arcadeDrive(0, .5);} 
    else if(pidOn == false && target - m_navx.getAngle() < -7){
      m_drive.arcadeDrive(0, -.5);}
    else{
    spin = pid.calculate(m_navx.getAngle());
    m_drive.arcadeDrive(0, spin);
    pidOn = true;}
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
