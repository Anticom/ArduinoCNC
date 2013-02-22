/*
Projekt: CNC
Klasse: Multi-Schrittmotoren
Yorrick S.
2013-02-22
*/

#include <AccelStepper.h>

//Stepper Definitionen
//mode - step - direction
AccelStepper xStepper(AccelStepper::DRIVER, 10, 11);
AccelStepper yStepper(AccelStepper::DRIVER, 8, 9);
AccelStepper zStepper(AccelStepper::DRIVER, 6, 7);

String cmd = "";

int xCount = 0;
int yCount = 0;

void setup() {
  Serial.begin(9600);
  
  xStepper.setMaxSpeed(750.0);
  yStepper.setMaxSpeed(750.0);
  
  xStepper.setSpeed(500.0);
  yStepper.setSpeed(500.0);
  
  xStepper.setAcceleration(50000.0);
  yStepper.setAcceleration(50000.0);
  
  xStepper.moveTo(333);
  yStepper.moveTo(666);
  
  xStepper.setEnablePin(3);
  yStepper.setEnablePin(4);
  xStepper.enableOutputs();
  xStepper.enableOutputs();
}

void loop() {
  if (xStepper.distanceToGo() == 0) {
      if(xCount <= 10) {
        xStepper.moveTo(-xStepper.currentPosition());
        xCount++;
      }
      else {
        xStepper.disableOutputs();
        xCount = 0;
      }
  }

  if (yStepper.distanceToGo() == 0) {
      if(yCount <= 10) {
        yStepper.moveTo(-yStepper.currentPosition());
        yCount++;
      }
      else {
        yStepper.disableOutputs();
        yCount = 0;
      }
  }
   //read();
   //Serial.println(cmd);
   xStepper.run();
   yStepper.run();
   /*
   xStepper.runSpeedToPosition();
   yStepper.runSpeedToPosition();
   */
}

//void read(){
 // switch(Serial.read()){
  //  case is == "go":
   //   cmd = "go";
    //  break;
  //  case is == "stop":
   //   cmd = "stop";
   //   break;
 // }
//}


