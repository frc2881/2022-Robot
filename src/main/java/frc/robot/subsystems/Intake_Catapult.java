package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake_Catapult extends SubsystemBase {

    private CANSparkMax intake_feeder;
    private CANSparkMax leftCatapult;
    private CANSparkMax rightCatapult;
    private Solenoid intakeSolenoid;

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

    }

    public void intake_feed(double speed, Feeder_Direction state){
        if(state == Feeder_Direction.INTAKE)
        intake_feeder.set(-speed);
        else{
            intake_feeder.set(speed);
        }
    }

    public void leftCatapult(double speed){
        leftCatapult.set(speed);
    }

    public void rightCatapult(double speed){
        rightCatapult.set(speed);
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
    }

}