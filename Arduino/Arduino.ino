#include <SPI.h>
#include <Ethernet.h>

//analog
int portIn = A0;
double v;

// Enter a MAC address for your controller below.
// Newer Ethernet shields have a MAC address printed on a sticker on the shield
byte mac[] = {  0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
char serverName[] = "http://kebin1104.dothome.co.kr";
IPAddress ip(112,175,184,33);
String device_number = "000000000000";

EthernetClient client;

void setup() {
 // Open serial communications and wait for port to open:
  Serial.begin(9600);
   while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  // start the Ethernet connection:
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    while(true);
  }
  // give the Ethernet shield a second to initialize:
  delay(100);
  Serial.println("connecting...");
   
}

void loop()
{
  v = analogRead(portIn);
  v = (v * 5.0 / 1023.0) * 1000.0 - 2448.0;
  if(v < 0)
  {
    v = 0;
  }
  Serial.println(v);

  if (client.connect(ip, 80)) {
    Serial.println("connected");
    // Make a HTTP request:
    char chValue[32];
    dtostrf(v,2,4,chValue);
    String strValue = String(chValue);
    String post = 
  "GET /BIG/UpdateElecData.php?device_Number=" + device_number + "&elec_Data=" + strValue + " HTTP/1.1";
    client.println(post);
    client.println("Host: kebin1104.dothome.co.kr");
    client.println();
    Serial.println(post);
  } 
  else {
    Serial.println("connection failed");
  }
  client.stop();
  delay(1000);
}
