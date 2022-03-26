// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.NavX;

public class ClimbInsurance extends CommandBase {

  private final NavX m_navx;
  private int counter;
  private int counter1;
  private ArrayList<Float> roll;
  private ArrayList<Float> highRoll;
  /** Creates a new ArmWaitToExtend. */
  public ClimbInsurance(NavX navx) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_navx = navx;
    roll = new ArrayList<Float>();
    highRoll = new ArrayList<Float>();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    roll.clear();
    highRoll.clear();
    counter = 0;
    counter1 = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    roll.add(m_navx.getRoll());

    if(roll.size() > 1)
    {
      if(roll.get(counter) <= (roll.get(counter - 1) - 3) && counter1 == 0){
        highRoll.add(roll.get(counter - 1));
        counter1++;
      }
      if(roll.get(counter) <= -2){
        counter1 = 0;
     }
    }
    counter++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(highRoll.get(highRoll.size()) < 15){
        return true;
    }
    else{
        return false;
    }
  }
}
