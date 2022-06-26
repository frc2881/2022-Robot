// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*
 * Addressable LEDs are connected to PWM channel 5 on the RoboRIO. There 146
 * LEDs, and they are connected in sequence as follows:
 *
 *   - 16 LEDs in a ring light around the targeting camera
 *
 *   - 20 LEDs running from the bottom to the top on the back left upright of
 *     the robot frame. These LEDs start at ~4 inches off the floor and run up
 *     to ~16.5 inches off the floor.
 *
 *   - 18 LEDs running from the bottom to the top on the back left diagonal of
 *     the robot frame. These LEDs start at ~17.5 inches off the floor and run
 *     up to ~26 inches off the floor.
 *
 *   - 27 LEDs running from the top to the bottom on the front of the left air
 *     tanks. These LEDs start at ~27.5 inches off the floor and run down to
 *     ~10 inches off the floor.
 *
 *   - 27 LEDs running from the bottom to the top on the front of the right air
 *     tanks. These LEDs start at ~10 inches off the floor and run up to ~27.5
 *     inches off the floor.
 *
 *   - 18 LEDs running from the top to the bottom on the back right diagonal of
 *     the robot frame. These LEDs start at ~26 inches off the floor and run
 *     down to ~17.5 inches off the floor.
 *
 *   - 20 LEDs running from the top to the bottom on the back right upright of
 *     the robot frame. These LEDs start at ~16.5 inches off the floor and run
 *     down to ~4 inches off the floor.
 */
public class PrettyLights extends SubsystemBase {
  private final String ENABLE_RING_LIGHT = "Enable Ring Light";
  private final int m_pink = 0xff349d;
  private final int m_green = 0x00e947;
  private final int m_blue = 0x01a0ff;
  private final int m_orange = 0xfea430;
  private final int m_purple = 0xaa00dc;
  private final int m_red = 0xff0000;
  private final int m_yellow = 0xffff00;
  private final int m_white = 0xffffff;
  private final int m_black = 0x000000;

  private final int[] m_pgbop =
  {
    m_pink,
    m_green,
    m_blue,
    m_orange,
    m_purple
  };

  private final int[] m_lgbtq =
  {
    0xd80504,
    0xf18600,
    0xf2e102,
    0x007925,
    0x0049f2,
    0x6f0880
  };

  private final int[] m_trans =
  {
    0x4092b2, // 0x56c3ee,
    0xae7783, // 0xe99faf,
    0xb4b5b5, // 0xf1f2f2,
    0xae7783, // 0xe99faf,
    0x4092b2  // 0x56c3ee
  };

  private final PowerDistribution m_powerHub;
  private final AddressableLED m_led;
  private final AddressableLEDBuffer m_ledBuffer;
  private final AddressableLEDBuffer m_ledBuffer2;
  private final DoubleLogEntry m_logCurrent;
  private int m_prevRing = m_black;
  private int m_prevColor = m_black;
  private int m_color = m_pink;
  private boolean m_ring = true;
  private boolean m_party = false;
  private int m_autoHue = 0;
  private int m_step = 0;
  private final int m_maxExplosions = 32;
  private final int m_explosionSpeed = 2;
  private final int m_explosionRate = 2;
  private int m_explosionCount = 0;
  private int[] m_explosionPos = new int[m_maxExplosions];
  private int[] m_explosionHue = new int[m_maxExplosions];
  private int[] m_explosionAge = new int[m_maxExplosions];
  private final int[][] m_explosion =
  {
    { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00,
      0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xbf, 0xff, 0x00, 0x00, 0x00, 0x00,
      0x00 },
    { 0x00, 0x00, 0x00, 0x00, 0xff, 0xbf, 0x7f, 0xbf, 0xff, 0x00, 0x00, 0x00,
      0x00 },
    { 0x00, 0x00, 0x00, 0xdf, 0xbf, 0x7f, 0x3f, 0x7f, 0xbf, 0xdf, 0x00, 0x00,
      0x00 },
    { 0x00, 0x00, 0xbf, 0x9f, 0x7f, 0x3f, 0x00, 0x3f, 0x7f, 0x9f, 0xbf, 0x00,
      0x00 },
    { 0x00, 0x9f, 0x7f, 0x5f, 0x3f, 0x00, 0x00, 0x00, 0x3f, 0x5f, 0x7f, 0x9f,
      0x00 },
    { 0x7f, 0x5f, 0x3f, 0x1f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1f, 0x3f, 0x5f,
      0x7f },
    { 0x3f, 0x1f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1f,
      0x3f }
  };

  /** Creates a new PrettyLights. */
  public PrettyLights(PowerDistribution powerHub) {
    m_powerHub = powerHub;

    m_led = new AddressableLED(5);
    m_ledBuffer = new AddressableLEDBuffer(146);
    m_ledBuffer2 = new AddressableLEDBuffer(m_ledBuffer.getLength());
    m_led.setLength(m_ledBuffer.getLength());
    m_led.setData(m_ledBuffer);
    m_led.start();

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logCurrent = new DoubleLogEntry(log, "/prettyLights/current");
    } else {
      m_logCurrent = null;
    }

    SmartDashboard.setDefaultBoolean(ENABLE_RING_LIGHT, false);
  }

  private void solid(AddressableLEDBuffer buffer, int offset, int length,
                     int c) {
    for(int i = 0; i < length; i++) {
      buffer.setRGB(offset + i, c >> 16, (c >> 8) & 255, c & 255);
    }
  }

  private void colorBar(AddressableLEDBuffer buffer, int[] colors, int offset,
                        int length, boolean reversed) {
    int i, step, idx, c, r, g, b;

    step = colors.length;

    for(i = 0, idx = step, c = 0; i < length; i++, idx += step) {
      r = colors[c] >> 16;
      g = (colors[c] >> 8) & 255;
      b = colors[c] & 255;

      if(idx > length) {
        c++;
        if(((idx - length) * 2) >= step) {
          r = colors[c] >> 16;
          g = (colors[c] >> 8) & 255;
          b = colors[c] & 255;
        }
        idx -= length;
      }

      if(reversed) {
        buffer.setRGB(offset + length - i - 1, r, g, b);
      } else {
        buffer.setRGB(offset + i, r, g, b);
      }
    }
  }

  private void auto() {
    int idx, hue;

    for(idx = 0; idx < 65; idx++) {
      hue = (m_autoHue + (idx * 180 / 65)) % 180;
      m_ledBuffer.setHSV(idx + 16, hue, 225, 255);
      m_ledBuffer.setHSV(145 - idx, hue, 225, 255);
    }

    m_autoHue += 3;
    m_autoHue %= 180;
  }

  private void party() {
    int idx, age, offset;

    if(m_explosionCount == 0) {
      m_step = 0;
    }

    if((m_explosionCount < m_maxExplosions) &&
       ((m_step % m_explosionRate) == 0)) {
      m_explosionPos[m_explosionCount] = (int)(Math.random() * 130);
      m_explosionHue[m_explosionCount] = (int)(Math.random() * 180);
      m_explosionAge[m_explosionCount] = 0;
      m_explosionCount++;
    }

    for(idx = 0; idx < 130; idx++) {
      m_ledBuffer.setRGB(idx + 16, 0, 0, 0);
    }

    for(idx = 0; idx < m_explosionCount; idx++) {
      age = m_explosionAge[idx] / m_explosionSpeed;

      for(offset = 0; offset < 13; offset++) {
        if(m_explosion[age][offset] != 0) {
          m_ledBuffer.setHSV((((m_explosionPos[idx] + 130 + offset - 6) %
                               130) + 16), m_explosionHue[idx], 255,
                               m_explosion[age][offset]);
        }
      }

      m_explosionAge[idx]++;
    }

    if(m_explosionAge[0] == (m_explosion.length * m_explosionSpeed)) {
      for(idx = 0; idx < (m_explosionCount - 1); idx++) {
        m_explosionPos[idx] = m_explosionPos[idx + 1];
        m_explosionHue[idx] = m_explosionHue[idx + 1];
        m_explosionAge[idx] = m_explosionAge[idx + 1];
      }
      m_explosionCount--;
    }

    m_step++;
  }

  private void disabledBase(AddressableLEDBuffer buffer, int[] one, int[] two,
                            int[] three) {
    colorBar(buffer, one, 16, 20, true);
    colorBar(buffer, two, 36, 18, true);
    colorBar(buffer, three, 54, 27, false);
    colorBar(buffer, three, 81, 27, true);
    colorBar(buffer, two, 108, 18, false);
    colorBar(buffer, one, 126, 20, false);
  }

  private void merge(AddressableLEDBuffer buffer1,
                     AddressableLEDBuffer buffer2) {
    int idx, split;

    split = (((m_step % (5 * 50)) * 2) * 20) / 37;
    for(idx = 0; idx < 20; idx++) {
      if(idx < split) {
        buffer1.setLED(idx + 16, buffer2.getLED(idx + 16));
        buffer1.setLED(145 - idx, buffer2.getLED(145 - idx));
      } else if(idx == split) {
        buffer1.setRGB(idx + 16, m_white >> 16, (m_white >> 8) & 255,
                       m_white & 255);
        buffer1.setRGB(145 - idx, m_white >> 16, (m_white >> 8) & 255,
                       m_white & 255);
      }
    }

    split = ((((m_step % (5 * 50)) * 2) - 40) * 18) / 26;
    for(idx = 0; idx < 18; idx++) {
      if(idx < split) {
        buffer1.setLED(idx + 36, buffer2.getLED(idx + 36));
        buffer1.setLED(125 - idx, buffer2.getLED(125 - idx));
      } else if(idx == split) {
        buffer1.setRGB(idx + 36, m_white >> 16, (m_white >> 8) & 255,
                       m_white & 255);
        buffer1.setRGB(125 - idx, m_white >> 16, (m_white >> 8) & 255,
                       m_white & 255);
      }
    }

    split = ((((m_step % (5 * 50)) * 2) - 18) * 27) / 53;
    for(idx = 0; idx < 27; idx++) {
      if(idx < split) {
        buffer1.setLED(idx + 81, buffer2.getLED(idx + 81));
        buffer1.setLED(80 - idx, buffer2.getLED(80 - idx));
      } else if(idx == split) {
        buffer1.setRGB(idx + 81, m_white >> 16, (m_white >> 8) & 255,
                       m_white & 255);
        buffer1.setRGB(80 - idx, m_white >> 16, (m_white >> 8) & 255,
                       m_white & 255);
      }
    }
  }

  private void disabled() {
    m_step++;
    if(m_step == (15 * 50)) {
      m_step = 0;
    }

    if(m_step < (5 * 50)) {
      disabledBase(m_ledBuffer, m_pgbop, m_trans, m_lgbtq);
      disabledBase(m_ledBuffer2, m_trans, m_lgbtq, m_pgbop);
    } else if(m_step < (10 * 50)) {
      disabledBase(m_ledBuffer, m_trans, m_lgbtq, m_pgbop);
      disabledBase(m_ledBuffer2, m_lgbtq, m_pgbop, m_trans);
    } else {
      disabledBase(m_ledBuffer, m_lgbtq, m_pgbop, m_trans);
      disabledBase(m_ledBuffer2, m_pgbop, m_trans, m_lgbtq);
    }

    merge(m_ledBuffer, m_ledBuffer2);
  }

  @Override
  public void periodic() {
    boolean bUpdate = false;

    // Set the ring light based on the robot enable state.
    if((DriverStation.isEnabled() && m_ring) ||
       SmartDashboard.getBoolean(ENABLE_RING_LIGHT, false)) {
      solid(m_ledBuffer, 0, 16, m_pink);
      if(m_prevRing != m_pink) {
        m_prevRing = m_pink;
        bUpdate = true;
      }
    } else {
      solid(m_ledBuffer, 0, 16, m_black);
      if(m_prevRing != m_black) {
        m_prevRing = m_black;
        bUpdate = true;
      }
    }

    // If party lights have been enabled (at the end of the climb sequence),
    // then display them regardless of any other robot state (so that they
    // continue after the match ends).
    if(m_party) {
      party();
      m_prevColor = m_black;
      bUpdate = true;
    }

    // Otherwise, see if the robot is enabled.
    else if(DriverStation.isEnabled()) {
      // See if the robot is in autonomous mode.
      if(!DriverStation.isTeleop()) {
        // Display the autonomous light pattern.
        auto();
        m_prevColor = m_black;
        bUpdate = true;
      } else {
        // If the default color is selected and it is near the end of the
        // match, display the end of match warning color.
        if((m_color == m_pink) && (DriverStation.getMatchTime() <= 35) &&
           (DriverStation.getMatchTime() > 25)) {
          solid(m_ledBuffer, 16, 130, m_yellow);
          if(m_prevColor != m_yellow) {
            m_prevColor = m_yellow;
            bUpdate = true;
          }
        } else {
          // Display the selected color.
          solid(m_ledBuffer, 16, 130, m_color);
          if(m_prevColor != m_color) {
            m_prevColor = m_color;
            bUpdate = true;
          }
        }
      }
    }

    // Otherwise, the robot is disabled.
    else {
      // Generate the disabled light pattern.
      disabled();
      m_prevColor = m_black;
      bUpdate = true;
    }

    // Write the generated color pattern to the LEDs.
    if(bUpdate) {
      m_led.setData(m_ledBuffer);
    }

    // Log the current used by the LEDs.
    if(kEnableDetailedLogging) {
      m_logCurrent.append(m_powerHub.getCurrent(11));
    }
  }

  public void defaultColor() {
    m_color = m_pink;
  }

  public void greenColor() {
    m_color = m_green;
  }

  public void redColor() {
    m_color = m_red;
  }

  public void partyColor() {
    m_party = true;
  }

  public void disableRing() {
    m_ring = false;
  }

  public void enableRing() {
    m_ring = true;
  }

  public void reset() {
    m_color = m_pink;
    m_ring = true;
    m_party = false;
  }
}
