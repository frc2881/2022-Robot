// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.TWINKLES;

public class PrettyLights extends SubsystemBase {
  public static final double hotPink = 0.57;

  private final Spark lights;
  /** Creates a new PrettyLights. */
  public PrettyLights() {

    lights = new Spark(1);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void color(double Color, double flash){
    lights.set(Color);



  }
}
