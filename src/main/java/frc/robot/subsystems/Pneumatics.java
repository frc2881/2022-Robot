// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
  /** Creates a new Pnuematics. */
  public Pneumatics() {}
  Compressor m_compressor = new Compressor(0, PneumaticsModuleType.REVPH);

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Compressor Current", () -> m_compressor.getCurrent(), null);


  }
}
