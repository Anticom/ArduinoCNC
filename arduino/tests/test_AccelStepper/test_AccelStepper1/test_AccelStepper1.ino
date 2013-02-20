#include <AccelStepper.h>

// Define some steppers and the pins the will use
//mode - step - direction
AccelStepper stepper1(AccelStepper::DRIVER, 11, 10);
AccelStepper stepper2(AccelStepper::DRIVER,  9,  8);
AccelStepper stepper3(AccelStepper::DRIVER,  7,  6);


void setup() {
  stepper1.setMaxSpeed(200.0);
  stepper2.setMaxSpeed(300.0);
  stepper3.setMaxSpeed(300.0);
  
  stepper1.setAcceleration(100.0);
  stepper2.setAcceleration(100.0);
  stepper3.setAcceleration(100.0);
  
  stepper1.moveTo(24);
  stepper2.moveTo(1000000);
  stepper3.moveTo(1000000);
}

void loop() {
  stepper1.run();
  stepper2.run();
  stepper3.run();
}
