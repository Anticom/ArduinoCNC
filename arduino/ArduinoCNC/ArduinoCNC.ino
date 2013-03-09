#include <AccelStepper.h>

/*
enum serial {
  NOTHING,
  SEND_INIT,
  POLL_NEXT,
  MILLING_START,
  MILLING_STOP,
  POSITION_ZEROFY,
  POSITION_GOTOZERO
};
*/

/*
//definition for serial communication protocol
#define SERIAL_NULL            0
#define SERIAL_SEND_INIT       1
#define SERIAL_POLL_NEXT       2
#define SERIAL_MILLING_START   3
#define SERIAL_MILLING_STOP    4
#define SERIAL_RESET_POSITION  5
*/

// Define some steppers and the pins the will use
//mode - step - direction
AccelStepper stepper1(AccelStepper::DRIVER, 9, 8);
//AccelStepper stepper2(AccelStepper::DRIVER,  9,  8);
//AccelStepper stepper3(AccelStepper::DRIVER,  7,  6);

int i = 0;

void setup() {
  stepper1.setEnablePin(7);
  
  stepper1.setMaxSpeed(7500.0);
  //stepper1.setSpeed(7500.0);
  stepper1.setAcceleration(100.0);
  stepper1.move(1600);
}

void loop() {
  stepper1.run();
  if(stepper1.distanceToGo() == 0) {
    stepper1.disableOutputs();
  }
  /*
  if(areYouThere()) {
    int _whatToDo = whatToDo();
    switch(_whatToDo) {
      case SERIAL_NULL:
        //do nothing :)
        break;
      case SERIAL_SEND_INIT:
        serial_init();
        break;
      case SERIAL_POLL_NEXT:
        //float[] data = serial_poll_next();
        //process_next(data);
        break;
      case SERIAL_MILLING_START:
        break;
      case SERIAL_MILLING_STOP:
        break;
      case SERIAL_RESET_POSITION:
        break;
      default:
        break;
    }
  }
  */
}

/*
//hört, ob seriell eine antwort kommt
boolean areYouThere() {
  return false;
}

//Arduino fragt den Rechner, was er als nächstes tun soll:
int whatToDo() {
  //send init
  //send next command
  //start milling
  //stop milling
  //reset absolute positions
  
  //nothing
  return SERIAL_NULL;
}

//sendet seriell initial-daten, damit der Java Server weiß, wie er die Daten für pollNextCommands() aufbereiten soll
void serial_init() {
  boolean success = false;
  boolean response = true;
  while(!success) {
    //send data
    
    //was successful?!
    if(response) {
      success = true;
    }
  }
}

float* serial_poll_next() {
  return float[] foo = {1, 2, 3};
}

void process_next(float* data) {
  
}

//Arduino holt sich vom PC die nächsten Kommandos ab (Schritte mit beschleunigung der 3 Motoren):
int* pollNextCommands() {
  int foo[] = {1, 2};
  return foo;
}
*/
