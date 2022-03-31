// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.PrettyLights;
import frc.robot.utils.NavX;

public class ClimbInsurance extends CommandBase {

  private PrettyLights m_prettyLights;
  private final NavX m_navx;
  private boolean safe;
  private boolean back = true;
  float largestVal = 0;
  /** Creates a new ArmWaitToExtend. */
  public ClimbInsurance(NavX navx, PrettyLights prettyLights) {

    // Use addRequirements() here to declare subsystem dependencies.
    m_prettyLights = prettyLights;
    m_navx = navx;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    float roll = m_navx.getRoll();

      if(back == true){
        if(roll > largestVal){
          largestVal = roll;
        }
        else if(roll < largestVal - 3){
          if(largestVal < 19){
            m_prettyLights.greenColor();
            safe = true;
          } else{
            m_prettyLights.redColor();
            safe = false;
          }
          back = false;
        }
      }
      else{
        if(roll < largestVal){
          largestVal = roll;
        }
        else if(roll > largestVal + 3){
          if(largestVal > -24){
            m_prettyLights.greenColor();
            safe = true;
          } else{
            m_prettyLights.redColor();
            safe = false;
          }
          back = true;
        }
      }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(safe == true){
      return true;
    } else {
      return false;
    }
  }
}
