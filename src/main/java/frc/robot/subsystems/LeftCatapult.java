package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LeftCatapult extends SubsystemBase {
  private final CANSparkMax catapult;

  private final RelativeEncoder encoder;

  private final ColorSensorV3 colorSensor;
  private final ColorMatch colorMatcher;

  private final Color blueCargo = new Color(.1436, .4070, .4499);
  private final Color redCargo = new Color(.5720, .3222, .1062);

  private final int distance = 600;

  private boolean cargoIsRed;
  private boolean cargoIsBlue;

  public LeftCatapult() {
    catapult = new CANSparkMax(16, MotorType.kBrushless);
        catapult.restoreFactoryDefaults();
        catapult.setInverted(false);
        catapult.setIdleMode(IdleMode.kBrake);
        catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
        catapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float)4.5);
        catapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float)0.0);
        catapult.getEncoder().setPosition(0);
        catapult.setSmartCurrentLimit(80);

    colorSensor = new ColorSensorV3(Port.kMXP);

    colorMatcher = new ColorMatch();
    colorMatcher.addColorMatch(blueCargo);
    colorMatcher.addColorMatch(redCargo);
    colorMatcher.setConfidenceThreshold(0.95);

    encoder = catapult.getEncoder();
  }

  public void disableEncoderSoftLimit() {
    catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
    catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
  }

  public void enableEncoderSoftLimit() {
    catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
  }

  public void resetEncoder() {
    encoder.setPosition(0);
  }

  public boolean reachedUpperSoftLimit() {
    return Math.abs(catapult.getSoftLimit(CANSparkMax.SoftLimitDirection.kForward) - encoder.getPosition()) < 0.1;
  }

  public boolean reachedLowerSoftLimit() {
    return Math.abs(catapult.getSoftLimit(CANSparkMax.SoftLimitDirection.kReverse) - encoder.getPosition()) < 0.1;
  }

  public void run(double speed) {
    catapult.set(speed);
  }

  public boolean isBlue() {
    return cargoIsBlue;
  }

  public boolean isRed() {
    return cargoIsRed;
  }

  @Override
  public void periodic() {
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = colorMatcher.matchColor(detectedColor);

    if((match != null) && (match.color == redCargo) &&
       (colorSensor.getProximity() > distance)) {
      cargoIsRed = true;
    } else {
      cargoIsRed = false;
    }

    if((match != null) && (match.color == blueCargo) &&
       (colorSensor.getProximity() > distance)) {
      cargoIsBlue = true;
    } else {
      cargoIsBlue = false;
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addDoubleProperty("Left Distance", () -> colorSensor.getProximity(),  null);
    builder.addBooleanProperty("Left Blue", () -> isBlue(), null);
    builder.addBooleanProperty("Left Red", () -> isRed(), null);
    builder.addDoubleProperty("Left Catapult position", () -> encoder.getPosition(), null);
  }
}
