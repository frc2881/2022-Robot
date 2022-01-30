// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

  private final Solenoid climberSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 1);
  private CANSparkMax leadScrew;

  public Climber() {
    leadScrew = new CANSparkMax(18, MotorType.kBrushless);
          leadScrew.restoreFactoryDefaults();
          leadScrew.setInverted(false);
          leadScrew.setIdleMode(IdleMode.kBrake);          

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
    climberSolenoid.set(false);

  }

  /**
   * Brings the arm to upright position
   */
  public void armUp(){
    climberSolenoid.set(true);

  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
