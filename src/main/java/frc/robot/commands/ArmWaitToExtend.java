// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.NavX;

public class ArmWaitToExtend extends CommandBase {

  private final NavX m_navx;
  private final double a;
  private int counter;
  private ArrayList<Float> roll;
  private ArrayList<Double> lpfRoll;
  /** Creates a new ArmWaitToExtend. */
  public ArmWaitToExtend(NavX navx) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_navx = navx;
    roll = new ArrayList<Float>();
    lpfRoll = new ArrayList<Double>();
    a = 0.5;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    roll.clear();
    lpfRoll.clear();
    counter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    roll.add(m_navx.getRoll());

    if(roll.size() > 1){
      lpfRoll.add((roll.get(counter)*a) + (roll.get(counter-1)*(1 - a)));
    }
    else{
      lpfRoll.add((double) roll.get(0));
    }
    counter++;

    while(roll.size() > 5){
      roll.remove(0);
      lpfRoll.remove(0);
      counter--;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(roll.size() < 5){
        return false;
    }
    else if(lpfRoll.get(4) < lpfRoll.get(3) && roll.get(0) <= 0.2){
        return true;
    }
    else{
        return false;
    }
  }
}
