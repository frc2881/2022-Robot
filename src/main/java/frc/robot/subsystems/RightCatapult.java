package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RightCatapult extends SubsystemBase {
  private CANSparkMax rightCatapult;
  private ColorMatchResult matchRight;
  private ColorSensorV3 colorSensorRight = new ColorSensorV3(Port.kOnboard);

  private final RelativeEncoder rightCatapultEnc;

  private final ColorMatch colorMatcher = new ColorMatch();
                                            //R       G       B
  private final Color blueCargo = new Color(.1436, .4070, .4499); 
  private final Color redCargo = new Color(.5720, .3222, .1062); 

  private final int distance = 600;

  private boolean cargoIsRed;
  private boolean cargoIsBlue;

  public RightCatapult() {
    rightCatapult = new CANSparkMax(17, MotorType.kBrushless);
        rightCatapult.restoreFactoryDefaults();
        rightCatapult.setInverted(true);
        rightCatapult.setIdleMode(IdleMode.kBrake);
        rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
        rightCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float)4.5);
        rightCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float)0);
        rightCatapult.getEncoder().setPosition(0);
        rightCatapult.setSmartCurrentLimit(80);

    colorMatcher.addColorMatch(blueCargo);
    colorMatcher.addColorMatch(redCargo);
    colorMatcher.setConfidenceThreshold(.95);

    rightCatapultEnc = rightCatapult.getEncoder();
  }

  public void _run(double speed){
    rightCatapult.set(speed);
  }

  public void disableEncoderSoftLimit(){
    rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
    rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
  }

  public void enableEncoderSoftLimit(){
    rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
  }

  public void resetEncoder(){
    rightCatapultEnc.setPosition(0);
  }

  public boolean reachedUpperSoftLimit(){
    return Math.abs(rightCatapult.getSoftLimit(CANSparkMax.SoftLimitDirection.kForward) - rightCatapult.getEncoder().getPosition()) < 0.1;
  }

  public boolean reachedLowerSoftLimit(){
    return Math.abs(rightCatapult.getSoftLimit(CANSparkMax.SoftLimitDirection.kReverse) - rightCatapult.getEncoder().getPosition()) < 0.1;
  }

  public void run(double speed) {
    rightCatapult.set(speed);
  }

  public boolean isBlue() {
    return cargoIsBlue;
  }

  public boolean isRed() {
    return cargoIsRed;
  }

  @Override
  public void periodic(){
    Color detectedColorRight = colorSensorRight.getColor();
    matchRight = colorMatcher.matchColor(detectedColorRight);
    if((matchRight != null) && (matchRight.color == redCargo) &&
       (colorSensorRight.getProximity() > distance)) {
        cargoIsRed = true;
    } else {
      cargoIsRed = false;
    }
    if((matchRight != null) && (matchRight.color == blueCargo) &&
       (colorSensorRight.getProximity() > distance)) {
        cargoIsBlue = true;
    } else {
      cargoIsBlue = false;
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {  
    super.initSendable(builder);

    builder.addDoubleProperty("Right Distance", () -> colorSensorRight.getProximity(),  null);   
    builder.addBooleanProperty("Right Blue", () -> isBlue(), null);
    builder.addBooleanProperty("Right Red", () -> isRed(), null);
    builder.addDoubleProperty("Right Catapult position", () -> rightCatapultEnc.getPosition(), null);
  }
}
