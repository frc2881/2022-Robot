// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

  private final RelativeEncoder climbenc;

  private final Solenoid climberSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 1);
  private CANSparkMax leadScrew;

  public Climber() {
    leadScrew = new CANSparkMax(18, MotorType.kBrushless);
          leadScrew.restoreFactoryDefaults();
          leadScrew.setInverted(false);
          leadScrew.setIdleMode(IdleMode.kBrake);   
          leadScrew.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
          leadScrew.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float)23.8);
          leadScrew.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
          leadScrew.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float)0.5);
          leadScrew.setOpenLoopRampRate(0.08);

      climbenc = leadScrew.getEncoder();

      climbenc.setPositionConversionFactor((1.0/2.0) / 3.0);

  }
  public void _moveArm(double speed){
    leadScrew.set(speed);
  }

  /**
   * Extends or retracts the arm
   * 
   * @param speed positive value extends
   */
  public void moveArm(double speed){
    if(DriverStation.getMatchTime() < 35){
      _moveArm(speed);
    }
  }

public void _armBack(){
  climberSolenoid.set(true);
}
  /**
   * Brings the arm to back position
   */
  public void armBack(){
    if(DriverStation.getMatchTime() < 35){
    _armBack();
    }
  }

  public void _armUp(){
    climberSolenoid.set(false);
  }
  /**
   * Brings the arm to upright position
   */
  public void armUp(){
    if(DriverStation.getMatchTime() < 35){
    _armUp();
    }
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Double getClimbEncoderPostion(){
    return climbenc.getPosition();
  }


  public void unenableEncoderSoftLimit(){
    leadScrew.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
    leadScrew.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
  }

  public void enableEncoderSoftLimit(){
    leadScrew.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    leadScrew.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
  }

  public void resetEncoder(){
    climbenc.setPosition(0);
  }

  @Override
    public void initSendable(SendableBuilder builder) {  
      super.initSendable(builder);
      builder.addDoubleProperty("Climber Postion", () -> climbenc.getPosition(),  null);
      builder.addDoubleProperty("Current", () -> leadScrew.getOutputCurrent(),  null);
    }

}
