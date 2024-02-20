#include <Arduino.h>
#include<SoftwareSerial.h>//Header for GSM Module
#include <LiquidCrystal.h>//Header for LCD Display
#include "HardwareSerial.h"
#include "RTC.h"
#include "SD.h"

File file;
RTC_TIMETYPE t;


SoftwareSerial mySerial(0,1);//Object and pin declaration of GSM Module
LiquidCrystal lcd(7, 6, 5, 4, 3, 2);//Object and pin declarations of LCD
/*
 * LCD RS pin to digital pin 7
 * LCD Enable pin to digital pin 6
 * LCD D4 pin to digital pin 5
 * LCD D5 pin to digital pin 4
 * LCD D6 pin to digital pin 3
 * LCD D7 pin to digital pin 2
 * LCD R/W pin to ground
 * 10K resistor:
 * ends to +5V and ground
 * wiper to LCD VO pin (pin 3)
 */
                      /**********************/  
 
int sm = A0;//Asign input variable and pin for Soil Moisture Sensor
int ldr = A1;//Asign input variable and pin for Light Sensor
int hm = A2;//Asign input variable and pin for Humidity Sensor
int temp = A3;//Asign input variable and pin for Temperature Sensor
//Variables declaration
int smValue,ldrValue,hmValue,tempValue,lightValue,soilValue; 
float hmVoltage,relativeHm,tempVoltage,degreesC,degreesF;

                       /**********************/ 



void setup()  // this setup code here is to run once:
{
lcd.begin(20, 4);//Declaration of LCD size and begin the LCD
mySerial.begin(9600);//Start the Serial Communication between GSM and GR-Kaede
Serial.begin(9600);//Start the Serial Monitor

pinMode(sm, INPUT);//Assign Soil Moisture Sensor as Input
pinMode(ldr, INPUT);//Assign Light Sensor as Input
pinMode(hm, INPUT);//Assign Humidity Sensor as Input
pinMode(temp, INPUT);//Assign Temperature Sensor as Input
pinMode(PIN_LED0, OUTPUT); // for SD access
pinMode(PIN_LED1, OUTPUT); // for File access
pinMode(PIN_LED2, OUTPUT); // for File write access
pinMode(PIN_LED3, OUTPUT);
                     /**********************/ 
lcd.clear();//Clear the LCD
lcd.setCursor(0, 0);//Set the cursor to origin point
lcd.print(" SMART AGRICULTURE ");//To print SMART AGRICULTURE in LCD
lcd.setCursor(0, 1);//Set the cursor to next row
lcd.print("       SYSTEM ");//TO print SYSTEM in LCD
                     /**********************/ 
          rtc_init();
          t.year = 15;
          t.mon = 10;
          t.day = 25;
          t.weekday = RTC_WEEK_SUNDAY;
          t.hour = 16;
          t.min = 31;
          t.second = 0;
          rtc_set_time(&t);
                   /**********************/ 
if (!SD.begin()) {

       while (1)

           ; // error

} else {

       digitalWrite(PIN_LED0, HIGH); // Success to access SD.

   }
                  /**********************/ 
if (SD.exists("myData.csv")) {

       SD.remove("myData.csv");

   }
                 /**********************/ 
   File file = SD.open("myData.csv", FILE_WRITE);

   if (file) {

       //Write

         file.println(",,SMART, AGRICULTURE ,SYSTEM,,");      
       file.println("DATE, TIME, SOIL MOISTURE, LIGHT INTEN., HUMIDITY, TEMP.('C), TEMP.('F)");

       file.close();

       digitalWrite(PIN_LED1, HIGH); // Success to open file.

   } else {

       while (1)

           ;//Error in opening file

   }
             /**********************/ 
}
void loop() // put your main code here, to run repeatedly:
{
    static unsigned long currenttime, oldtime = 0;
    static unsigned long starttime = millis();

       currenttime = millis() - starttime;

if ((currenttime - oldtime) >= 2000) //@1sec
{
       digitalWrite(PIN_LED2, HIGH); // blink LED

       File file = SD.open("myData.csv", FILE_WRITE);

/**********************************************************************************************************/         
       
     rtc_get_time(&t);
     file.println();
     file.print(t.day);
     file.print('/');
     file.print(t.mon);
     file.print('/');
     file.print(t.year);

     file.print(',');

     file.print(t.hour);
     file.print(':');
     file.print(t.min);
     file.print(':');
     file.print(t.second);

     
       file.print(",");

       
/**********************************************************************************************************/       
       
soilValue = analogRead(sm);//Get analog values of Soil Moisture Sensor
smValue = soilValue-24;//Displaying reading within 1000 in LCD
ldrValue = analogRead(ldr);//Get analog values of Light Sensor
lightValue = (ldrValue+200)/10;//Displaying values within 100 in LCD
hmVoltage = (hmValue/1023.0)*5.0; // Convert hmValue to voltage (5V circuit)
relativeHm = ((hmVoltage+0.958)/0.0307); // Convert voltage to relative humidity
tempValue = analogRead(temp);//Get analog values of Temperature Sensor
degreesC = (tempValue / 10)+17;//Convert millivolts to Celsius
degreesF = degreesC * 1.8 + 32;//Convert Celsius to Fahrenheit
       
       
       
/**********************************************************************************************************/         
       
 //Soil Moisture Sensor Details
  file.print(smValue); 
  file.print(',');
//Serial.print("Soil Moisture : ");//Print "Soil Moisture : " in Serial Monitor
lcd.setCursor(0, 2);//Set the cursor in LCD to 2nd row 4th Column
lcd.print(" SM:");//To print "SM:" in LCD display
lcd.setCursor(5, 2);//Set the cursor in LCD to 2nd row 8th Column
lcd.print(smValue);//Display the Soil moisture level in LCD
//Serial.print(smValue);//Display the Soil moisture level in Serial Monitor 


  //Light Sensor Details
  file.print(lightValue);
  file.print(',');
//Serial.print("| Light : ");//Print "Light : " in Serial Monitor
lcd.setCursor(0, 3);//Set the cursor in LCD to 3rd row 0th Column
lcd.print(" L :");
lcd.setCursor(5, 3);//Set the cursor in LCD to 3rd row 2nd Column
lcd.print(lightValue);
//Serial.print(lightValue);//Display the Light Intensity level in Serial Monitor

  //Humidity Sensor Details 
   file.print(relativeHm);
   file.print(',');
//Serial.print("% | Humidity : ");//Print "Humidity : " in Serial Monitor
lcd.setCursor(9, 3);//Set the cursor in LCD to 3rd row 5th Column
lcd.print("HM:");
lcd.setCursor(13, 3);//Set the cursor in LCD to 3rd row 8th Column
lcd.print(relativeHm);
//Serial.print(relativeHm); //Display the Humidity level in Serial Monitor


  //Temperature Sensor Details 
  file.print(degreesC);
  file.print(',');
  file.print(degreesF);
  file.print(',');
//Serial.print("% | Temperature : ");//Print "Temperature : " in Serial Monitor
lcd.setCursor(9, 2);//Set the cursor in LCD to 2nd row 11th Column
lcd.print(" T:");
lcd.setCursor(13, 2);//Set the cursor in LCD to 2nd row 14th Column
lcd.print(degreesC);
//Serial.print(degreesC);//Display the Temperature - Celsius in Serial Monitor
//Serial.print("C ");

//Serial.print(degreesF);//Display the Temperature - Fahrenheit in Serial Monitor
//Serial.println("F "); 

/**********************************************************************************************************/  

      file.close();

       digitalWrite(PIN_LED2, LOW);

       oldtime = currenttime;

   }

}





void checkConditions()
{
  if(smValue < 300 && (int)relativeHm > 20)//Check soil and air moisture is below 70%
  {
    if((int)degreesC > 20 && lightValue > 25)//Do not required to feed at night time
    {
      sendMessage();
    }
  }
}

void sendMessage()
{
  mySerial.println("AT+CMGF=1"); //Sets the GSM Module in Text Mode
  delay(500); // Delay of 500 milli seconds or 1/2 second
  mySerial.println("AT+CMGS=\"+919688755885\"\r"); //Declaring Mobile number with Country code
  delay(500);
  mySerial.print(" Soil Moisture : ");
  mySerial.print(smValue);
  mySerial.print(" % ");
  mySerial.println(" Light Level : ");
  mySerial.print(ldrValue);
  mySerial.print(" % ");
  mySerial.println(" Humidity : ");
  mySerial.print(hmValue);
  mySerial.print(" % ");
  mySerial.println(" Temperature : ");
  mySerial.print(tempValue);
 delay(100);
 mySerial.println("1\x1A");// ASCII code of CTRL+Z
 delay(500); 
}

