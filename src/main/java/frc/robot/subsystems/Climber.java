// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

  DoubleSolenoid leftClimber = new DoubleSolenoid(PneumaticsModuleType.REVPH, 1, 2);
  DoubleSolenoid rightClimber = new DoubleSolenoid(PneumaticsModuleType.REVPH, 3, 4);
  private CANSparkMax leadScrew;

  public enum ArmState {BUTTON, OUT, UP}


  
  public Climber() {
    leadScrew = new CANSparkMax(5, MotorType.kBrushless);
          leadScrew.setInverted(false);
          leadScrew.setIdleMode(IdleMode.kBrake);

  }

  public ArmState getLeftArmState(){
    if(leftClimber.get() != null)
        return ArmState.UP;
    else{
        return ArmState.OUT;
    }
  }

  /**
   * Extends or retracts the arm
   * 
   * @param speed positive value extends
   */
  public void moveArm(double speed){
    System.out.println(speed);
    leadScrew.set(speed);
  }

  /**
   * Brings the arm to back position
   */

  public void armBack(){
    leftClimber.set(Value.kReverse);
    rightClimber.set(Value.kReverse);
  }

  /**
   * Brings the arm to upright position
   */
  public void armUp(){
    leftClimber.set(Value.kForward);
    rightClimber.set(Value.kForward);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
