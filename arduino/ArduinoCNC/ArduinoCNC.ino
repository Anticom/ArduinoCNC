/*
dynamic stack:
http://playground.arduino.cc//Main/LibraryList#DataStructures
Arduino cookbook:
http://my.safaribooksonline.com/book/hobbies/9781449399368/serial-communications/sending_formatted_text_and_numeric_da#X2ludGVybmFsX0h0bWxWaWV3P3htbGlkPTk3ODE0NDkzOTkzNjglMkZyZWNlaXZpbmdfc2VyaWFsX2RhdGFfaW5fYXJkdWlubyZxdWVyeT0=

required libs:
StepperMotor
(ArrayStack)


//TODO:
- unify constants to save space
- remove buffer concept and poll next data, when required
	- change protocol to MX1Y1Z1[DX1Y1Z1] (unify M and D)
*/

//--------------------------------------------------
//-                START OF PROGRAM                -
//--------------------------------------------------
#define VERSION					"1.0" //current version of this script

//protocol:
//--------------------------------------------------
//input:
#define INPUT_RESET				'Z' //run init method to reset the whole program
#define INPUT_PING				'I' //answer with pong
//#define INPUT_READY				'R' //send true|false depending on wether program is ready to recieve data
#define INPUT_VERSION			'V' //send version of client
#define INPUT_MILLING_START		'S' //jump into milling routine, recieving data (still able to pause|continue)

#define INPUT_MOVEMENT_DATA		'M' //example: MX40Y34Z394
#define INPUT_FEED_RATE			'F' //example: F2.5
#define INPUT_PAUSE				'P' //hold the program     (jumpt to internal loop, until continue flag is sent)
#define INPUT_CONTINUE			'C' //continue the program (jump out of internal loop)
#define INPUT_MILLING_STOP		'T' //stop milling routine and jump back to main loop

#define INPUT_X					'X' //input identifier for X Axis (in data package)
#define INPUT_Y					'Y' //input identifier for Y Axis (in data package)
#define INPUT_Z					'Z' //input identifier for Z Axis (in data package)

//may be implemented in the future:
//#define INPUT_SPINDLE_ON		'N' //turn spindle on
//#define INPUT_SPINDLE_OFF		'F' //turn spindle off
//--------------------------------------------------

//output
#define OUTPUT_I_WAS_INIT		'A' //flag that will be sent once, when the arduino is newly initialized

#define OUTPUT_PONG				'P' //response to ping
//#define OUTPUT_SUCCESS			'S' //general flag for successful operation
//#define OUTPUT_TRUE				OUTPUT_SUCCESS	//alias for success
//#define OUTPUT_FALIURE			'F' //general flag for failed operation
//#define OUTPUT_FALSE				OUTPUT_FALIURE	//alias for failure
#define OUTPUT_INPUT_ERROR		'I' //signalize the server, that input was invalid. This may be followed by >>>R<<<

//#define OUTPUT_REPEAT_DATA		'R' //ask server to send last data package again
#define OUTPUT_SENDING_WAIT		'W' //signalize the server to wait with sending data, until >>>C<<< is sent (in case internal buffer is full)
#define OUTPUT_SENDING_CONTINUE	'C' //signalize the server to continue sending data
#define OUTPUT_SEND_NEXT		'N'	//ALTERNATIVE TO W|C buffering concept.				<-- this might be an easier way to implement
//--------------------------------------------------

//internal:
#define X						0   //internal representation for X Axis (DO NOT CHANGE - also used as index)
#define Y						1   //internal representation for Y Axis (DO NOT CHANGE - also used as index)
#define Z						2   //internal representation for Z Axis (DO NOT CHANGE - also used as index)
//--------------------------------------------------

//config:
#define MOVEMENT_BUFFER_SIZE	3    //number of elements of movements stored in the buffer
//#define MOVEMENT_BUFFER_INDEX	MOVEMENT_BUFFER_SIZE - 1
#define CONNECTION_TIMEOUT_MS	5000 //connection timeout in milliseconds, untill program shutdowns itself
#define BAUD_RATE				9600
#define INPUT_BUFFER_SIZE		200

//defaults:
#define DEFAULT_FEED_RATE		2.5
//--------------------------------------------------
//--------------------------------------------------

//global variables:

int run_order[3];
int movements[MOVEMENT_BUFFER_SIZE][3];
	int buffer_pointer = 0;

String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

int current_feed_rate	= 2;

//--------------------------------------------------
//--------------------------------------------------
//setup
void setup() {
	Serial.begin(BAUD_RATE);
	inputString.reserve(INPUT_BUFFER_SIZE);
}
//--------------------------------------------------
//loop
void loop() {
	//reset working variables
	init_fw();

	//jump to main dispatcher
	main_dispatcher();
}
//--------------------------------------------------

//reset all working variables and clear containers
void init_fw() {
	//reset working variables
	//TODO: implement
	//TODO: implement setting default feed rate
	
	memset(run_order, 0, sizeof 3);
        for(int i = 0; i < MOVEMENT_BUFFER_SIZE; i++) {
          memset(movements[i], 0, 3);
        }
	buffer_pointer = 0;
	
	inputString = "";
	stringComplete = false;
	
	Serial.print(OUTPUT_I_WAS_INIT);
	return;
}

//this is the main loop
void main_dispatcher() {
	while(true) {
		readString();
	  
		if(stringComplete) {
			char command = shift_char(inputString);
			switch(command) {
				case INPUT_RESET:
					inputString = "";
					stringComplete = false;
					
					return;
					//break;
				case INPUT_PING:
					Serial.print(OUTPUT_PONG);
					break;
				case INPUT_VERSION:
					Serial.print(VERSION);
					break;
				case INPUT_MILLING_START:
					inputString = "";
					stringComplete = false;
					
					milling_dispatcher();
					break;
			}
			
			inputString = "";
			stringComplete = false;
		}
	}
}

//milling mode
void milling_dispatcher() {
	while(true) {
		readString();
	  
		if(stringComplete) {
			char command = shift_char(inputString);
			switch(command) {
				case INPUT_MOVEMENT_DATA:
					//TODO: implement
					break;
				case INPUT_FEED_RATE:
					//TODO: implement
					break;
				case INPUT_PAUSE:
					pause();
					break;
				case INPUT_MILLING_STOP:
					inputString = "";
					stringComplete = false;
					
					return;
					//break;
				//just in case...
				case INPUT_PING:
					Serial.print(OUTPUT_PONG);
					break;
			}
			
			inputString = "";
			stringComplete = false;
		}
		
	}
}

void move() {
	//check, wether buffer capacity is full
	
	//stepperX.run();
	//stepperY.run();
	//stepperZ.run();
}

//buffer_pointer MUSS < MOVEMENT_BUFFER_SIZE sein!!!
int* push_cue(int movement[3]) {
	if(buffer_pointer < MOVEMENT_BUFFER_SIZE) {
		movements[buffer_pointer] = movement;
		buffer_pointer++;
	} else {
		Serial.print(OUTPUT_SENDING_WAIT);
	}
}

int* shift_cue() {
	int movement[3] = movements[0];
	int tmp[3];
	int pointer = 1;
	
	while(pointer < MOVEMENT_BUFFER_SIZE) {
		movements[pointer - 1] = movements[pointer];
	}
	
	//if buffer freed up, 
	if(buffer_pointer == (MOVEMENT_BUFFER_SIZE - 1)) {
		Serial.print(OUTPUT_SENDING_CONTINUE);
	}
	
	buffer_pointer--;
	
	return movement;
}

void pause() {
	while(true) {
		readString();
	  
		if(stringComplete) {
			char command = shift_char(inputString);
			
			if(command == INPUT_CONTINUE) {
				return;
			}
			
			inputString = "";
			stringComplete = false;
		}
	}
}

int parse_int(String& term) {
	int value = 0;
	int term_size = term.length();
	int end_factor = 1;
	
        int substr_index = 0;
        
	if(term_size > 0) {
		//negatives vorzeichen
		if(term.charAt(0) == '-') {
			end_factor = -1;
		//erste ziffer
		} else if(term.charAt(0) >= '0' && term.charAt(0) <= '9') {
			value = term.charAt(0) - '0';
		}
	}
	
	for(int i = 1; i < term_size; i++) {
		if(term.charAt(i) >= '0' && term.charAt(i) <= '9') {
			value = (value * 10) + (term.charAt(i) - '0');
		} else {
                        substr_index = i;
			break;
		}
	}
	
        //mod term:
        term = term.substring(substr_index);
        
	return (value * end_factor);
}

char shift_char(String& term) {
	char out = term.charAt(0);
	
	term = term.substring(1);
	
	return out;
}

void readString() {  
  while(Serial.available()) {
    char inChar = (char)Serial.read();
    
    if(inChar == '\n') {
      stringComplete = true;
      return;
    } else {
      inputString += inChar;
    }
  }
}

//--------------------------------------------------
//-                 END OF PROGRAM                 -
//--------------------------------------------------