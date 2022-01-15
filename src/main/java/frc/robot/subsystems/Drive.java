package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.NavX;

public class Drive extends SubsystemBase {
    private final NavX m_navx;
    private final RelativeEncoder leftenc;
    private final RelativeEncoder rightenc;
    private final DifferentialDriveOdometry odometry;


    private CANSparkMax leftFront;
    private CANSparkMax leftRear;
    private CANSparkMax rightFront;
    private CANSparkMax rightRear;

    private DifferentialDrive driveTrain;

    public Drive(NavX navx){
      
        m_navx = navx; 

        leftFront = new CANSparkMax(1, MotorType.kBrushless);
            leftFront.setInverted(false);
            leftFront.setIdleMode(IdleMode.kBrake);

        leftRear = new CANSparkMax(2, MotorType.kBrushless);
            leftRear.setInverted(false);
            leftRear.setIdleMode(IdleMode.kBrake);
        
        rightFront = new CANSparkMax(3, MotorType.kBrushless);
            rightFront.setInverted(true);
            rightFront.setIdleMode(IdleMode.kBrake);

        rightRear = new CANSparkMax(4, MotorType.kBrushless);
            rightRear.setInverted(true);
            rightRear.setIdleMode(IdleMode.kBrake);
        
        driveTrain = new DifferentialDrive(leftFront, rightFront);
        
        leftenc = leftRear.getEncoder();
        rightenc = rightRear.getEncoder();

        leftenc.setPositionConversionFactor(Units.inchesToMeters(2.26194671058));
        rightenc.setPositionConversionFactor(Units.inchesToMeters(2.26194671058));
        
        leftRear.follow(leftFront);
        rightRear.follow(rightFront);

        resetEncoders();

        odometry = new DifferentialDriveOdometry(navx.getRotation2d());
    }

    public void arcadeDrive(double speed, double rotation){
        System.out.println(speed + " " + rotation);
        driveTrain.arcadeDrive(speed, rotation);
    }

    public void setMaxOutput(double maxOutput){
        driveTrain.setMaxOutput(maxOutput);
    }

    @Override
    public void periodic() {
      odometry.update(
          m_navx.getRotation2d(), leftenc.getPosition(), rightenc.getPosition());
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
      }

    public void resetEncoders()
    {
      leftenc.setPosition(0);
      rightenc.setPosition(0);
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        odometry.resetPosition(pose, m_navx.getRotation2d());
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftenc.getVelocity(),
        -rightenc.getVelocity());
      }

    public void driveTankVolts(double leftVolts, double rightVolts) {
        leftFront.setVoltage(leftVolts);
        rightFront.setVoltage(rightVolts);
        driveTrain.feed();
      }

      @Override
      public void initSendable(SendableBuilder builder) {  
        super.initSendable(builder);
        builder.addDoubleProperty("Left Encoder", () -> leftenc.getPosition(),  null);
        builder.addDoubleProperty("Right Encoder", () -> rightenc.getPosition(),  null);

      }
}