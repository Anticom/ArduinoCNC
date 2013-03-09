//config
int baudRate = 9600;
int ledPin = 13;                 // LED connected to digital pin 13

//working variables
int inByte;

void setup() {
  Serial.begin(baudRate);
  
  pinMode(ledPin, OUTPUT);      // sets the digital pin as output
}

void loop() {
  inByte = Serial.read();
  
  //daten verarbeiten:
  if(inByte == 'h') {
    digitalWrite(ledPin, HIGH);
    Serial.write("set to high");
  } else if(inByte == 'l') {
    digitalWrite(ledPin, LOW);
    Serial.write("set to low");
  }
  
  //Serial.write('0');
  //delay(1000);
}
