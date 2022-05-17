package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//Verson 1 of Claw_Machine
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// We'll probably decide to do encoders
// We also might us a raspberry pie I think
// Also try to do another version of this code and we'll test it to see which one we should use

class Claw_Mac {
    public DcMotor LMY1 = null;
    public DcMotor LMY2 = null;
    public DcMotor RMY1 = null;
    public DcMotor RMY2 = null;
    public DcMotor Sliderx = null;
    public DcMotor Vertical = null;
    public Servo LServo = null;
    public Servo RServo = null;

    public static double Claw_Home = 0.0;
    public static double Claw_Max = 0.45;
    public static double Claw_Min = 0.0;

    HardwareMap map = null;

    public void init(HardwareMap maps){
        map = maps;
        LMY1 = maps.get(DcMotor.class, "lmy1");
        LMY2 = maps.dcMotor.get("lmy2");
        RMY1 = maps.dcMotor.get("rmy1");
        RMY2 = maps.dcMotor.get("rmy2");
        Sliderx = maps.dcMotor.get("slider");
        Vertical = maps.dcMotor.get("vertical");
        LServo = maps.servo.get("lservo");
        RServo = maps.servo.get("rservo");

        LMY1.setDirection(DcMotorSimple.Direction.FORWARD);
        LMY2.setDirection(DcMotorSimple.Direction.FORWARD);
        RMY1.setDirection(DcMotorSimple.Direction.REVERSE);
        RMY2.setDirection(DcMotorSimple.Direction.REVERSE);
        Sliderx.setDirection(DcMotorSimple.Direction.FORWARD);
        Vertical.setDirection(DcMotorSimple.Direction.FORWARD);

        LMY1.setPower(0.0);
        LMY2.setPower(0.0);
        RMY1.setPower(0.0);
        RMY2.setPower(0.0);
        Sliderx.setPower(0.0);
        Vertical.setPower(0.0);
        LServo.setPosition(Claw_Home);
        RServo.setPosition(Claw_Home);

        LMY1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LMY2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RMY1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RMY2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Sliderx.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Vertical.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
}

@TeleOp(name = "Claw_Machine: Tele", group = "Claw_Machine")

public class Claw_Machine extends LinearOpMode{
    Claw_Mac robot = new Claw_Mac();
    double y = gamepad1.left_stick_y;
    double x = gamepad1.left_stick_x;
    double ver_pos = 0.0;
    double claw_pos = robot.Claw_Home;
    final double motor_speed = 0.2;
    final double claw_speed = 0.05;


    @Override
    public void runOpMode(){
        robot.init(hardwareMap);
        telemetry.addData("Say", "Hello");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            claw_pos = Range.clip(claw_pos,0.0,0.45);

            if (gamepad1.a && claw_pos == 0.0){
                robot.LServo.setPosition(claw_pos += claw_speed);
                robot.RServo.setPosition(claw_pos += claw_speed);
            }
            else if (gamepad1.a && claw_pos == 0.45){
                robot.LServo.setPosition(claw_pos -= claw_speed);
                robot.RServo.setPosition(claw_pos -= claw_speed);
            }
            else if (gamepad1.right_bumper)
                robot.Vertical.setPower(ver_pos = motor_speed);
            else if (gamepad1.left_bumper)
                robot.Vertical.setPower(ver_pos = -motor_speed);
            else if (gamepad1.right_bumper && ver_pos != 0)
                robot.Vertical.setPower(0.0);
            else{
                robot.LMY1.setPower(y/5);
                robot.LMY2.setPower(y/5);
                robot.RMY1.setPower(-y/5);
                robot.RMY2.setPower(-y/5);
                robot.Sliderx.setPower(x/2);
            }
            telemetry.addData("y", "%.2f",y);
            telemetry.addData("x","%.2f",x);
            telemetry.addData("Vertical_position","%.2f",ver_pos);
            telemetry.addData("Claw_position","%.2f",claw_pos);
            telemetry.update();

            sleep(50);
        }
    }
}
