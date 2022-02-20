// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
  PneumaticHub m_ph = new PneumaticHub(1);

  /** Creates a new Pnuematics. */
  public Pneumatics() {
    m_ph.enableCompressorDigital();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Compressor Current", () -> m_ph.getCompressorCurrent(), null);
  }
}
