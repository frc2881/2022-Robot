package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake_Catapult extends SubsystemBase {

    private CANSparkMax intake_feeder;
    private CANSparkMax leftCatapult;
    private CANSparkMax rightCatapult;
    private Solenoid intakeSolenoid;
    private ColorMatchResult matchLeft;
    private ColorMatchResult matchRight;
    private ColorSensorV3 colorSensorLeft = new ColorSensorV3(Port.kMXP);
    private ColorSensorV3 colorSensorRight = new ColorSensorV3(Port.kOnboard);

    private final ColorMatch colorMatcher = new ColorMatch();
                                            //R       G       B
    private final Color blueCargo = new Color(.1436, .4070, .4499); 
    private final Color redCargo = new Color(.5720, .3222, .1062); 

    private final int distance = 600;

    public enum Feeder_Direction {INTAKE, EJECT}
    public enum Catapult_Direction {LAUNCH, RESET}
    public enum Intake_Arm_Direction {EXTEND, RETRACT}



    public Intake_Catapult(){
      
        intake_feeder = new CANSparkMax(15, MotorType.kBrushless);
            intake_feeder.restoreFactoryDefaults();
            intake_feeder.setInverted(false);
            intake_feeder.setIdleMode(IdleMode.kBrake);
            intake_feeder.setSmartCurrentLimit(16);

        leftCatapult = new CANSparkMax(16, MotorType.kBrushless);
            leftCatapult.restoreFactoryDefaults();
            leftCatapult.setInverted(false);
            leftCatapult.setIdleMode(IdleMode.kBrake);
            leftCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
            leftCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
            leftCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float)4.5);
            leftCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float)0);
            leftCatapult.getEncoder().setPosition(0);
            leftCatapult.setSmartCurrentLimit(80);
        
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
            

        intakeSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);

        colorMatcher.addColorMatch(blueCargo);
        colorMatcher.addColorMatch(redCargo);
        colorMatcher.setConfidenceThreshold(.95);





    }


    public void intake_feed(double speed, Feeder_Direction state){
        if(state == Feeder_Direction.INTAKE)
        intake_feeder.set(-speed);
        else{
            intake_feeder.set(speed);
        }
    }

    public void leftCatapult(double speed){
        Color detectedColorLeft = colorSensorLeft.getColor();
        matchLeft = colorMatcher.matchColor(detectedColorLeft);
        
        if(DriverStation.getAlliance() == Alliance.Blue && colorSensorLeft.getProximity() > distance
        && matchLeft.color == blueCargo){
        leftCatapult.set(speed);
        }
        else if(DriverStation.getAlliance() == Alliance.Red && colorSensorLeft.getProximity() > distance 
        && matchLeft.color == redCargo){
        leftCatapult.set(speed);
        }
        else{
        leftCatapult.set(speed/3);
        }
        
    }

    public void rightCatapult(double speed){
        Color detectedColorRight = colorSensorRight.getColor();
        matchRight = colorMatcher.matchColor(detectedColorRight);
        if(DriverStation.getAlliance() == Alliance.Blue && colorSensorRight.getProximity() > distance
        && matchRight.color == blueCargo)
        {
        rightCatapult.set(speed);
        }
        else if(DriverStation.getAlliance() == Alliance.Red && colorSensorRight.getProximity() > distance 
        && matchRight.color == redCargo){
        rightCatapult.set(speed);
        }
        else{
        rightCatapult.set(speed/3);   
        }

    }


    public boolean leftColorBlue(){
        Color detectedColorLeft = colorSensorLeft.getColor();
        matchLeft = colorMatcher.matchColor(detectedColorLeft);
        if(matchLeft != null && matchLeft.color == blueCargo && colorSensorLeft.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }

    public boolean leftColorRed(){
        Color detectedColorLeft = colorSensorLeft.getColor();
        matchLeft = colorMatcher.matchColor(detectedColorLeft);
        if(matchLeft != null && matchLeft.color == redCargo && colorSensorLeft.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }

    public boolean rightColorBlue(){
        Color detectedColorRight = colorSensorRight.getColor();
        matchRight = colorMatcher.matchColor(detectedColorRight);
        if(matchRight != null && matchRight.color == blueCargo && colorSensorRight.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }

    public boolean rightColorRed(){
        Color detectedColorRight = colorSensorRight.getColor();
        matchRight = colorMatcher.matchColor(detectedColorRight);
        if(matchRight != null && matchRight.color == redCargo && colorSensorRight.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }
 

    public void extend(){
        intakeSolenoid.set(true);
    }

    public void retract(){
        intakeSolenoid.set(false);
    }

    @Override
      public void initSendable(SendableBuilder builder) {  
        super.initSendable(builder);

        builder.addDoubleProperty("Current", () -> intake_feeder.getOutputCurrent(),  null);

        builder.addDoubleProperty("Left Color Sensor Distance", () -> colorSensorLeft.getProximity(),  null);
       // builder.addStringProperty("Left Color", () -> leftColor(),  null);
        builder.addBooleanProperty("Left Blue", () -> leftColorBlue(), null);
        builder.addBooleanProperty("Left Red", () -> leftColorRed(), null);

        builder.addDoubleProperty("Right Color Sensor Distance", () -> colorSensorRight.getProximity(),  null);   
        //builder.addStringProperty("Right Color", () -> rightColor(),  null);
        builder.addBooleanProperty("Right Blue", () -> rightColorBlue(), null);
        builder.addBooleanProperty("Right Red", () -> rightColorRed(), null);

    }

}