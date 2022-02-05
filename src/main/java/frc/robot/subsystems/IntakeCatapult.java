package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeCatapult extends SubsystemBase {

    private CANSparkMax intakeFeeder;
    private CANSparkMax leftCatapult;
    private CANSparkMax rightCatapult;
    private Solenoid intakeSolenoid;

    public enum Direction {INTAKE, EJECT}
    public enum Catapult_Direction {LAUNCH, EJECT}
    public enum Intake_Arm_Direction {EXTEND, RETRACT}

    public IntakeCatapult(){
      
        intakeFeeder = new CANSparkMax(15, MotorType.kBrushless);
            intakeFeeder.restoreFactoryDefaults();
            intakeFeeder.setInverted(false);
            intakeFeeder.setIdleMode(IdleMode.kBrake);

        leftCatapult = new CANSparkMax(16, MotorType.kBrushless);
            leftCatapult.restoreFactoryDefaults();
            leftCatapult.setInverted(false);
            leftCatapult.setIdleMode(IdleMode.kBrake);
        
        rightCatapult = new CANSparkMax(17, MotorType.kBrushless);
            rightCatapult.restoreFactoryDefaults();
            rightCatapult.setInverted(false);
            rightCatapult.setIdleMode(IdleMode.kBrake);

        intakeSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);
    }

    public void intake_feed(double speed, Direction state){
        if(state == Direction.INTAKE)
        intakeFeeder.set(speed);
        else{
            intakeFeeder.set(-speed);
        }
    }

    /**
     * Launches the Left Catapult.
     * @param speed - speed of the motor.
     */
    public void leftCatapult(double speed){
        leftCatapult.set(speed);
        
    }

    /**
     * Launches the Right Catapult.
     * @param speed - speed of the motor.
     */
    public void rightCatapult(double speed){
        rightCatapult.set(speed);
        
    }

    public void extend(){
        intakeSolenoid.set(true);
    }

    public void retract(){
        intakeSolenoid.set(false);
    }


}