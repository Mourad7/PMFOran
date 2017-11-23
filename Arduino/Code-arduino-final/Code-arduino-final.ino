#define ThermistancePIN A0 //Définition pin de la thermistance externe 
double resistance = 10000; // Résistance en ohm de la thermistance externe
double tension_entrante = 5; // Tension entrante d'arduino 

#include "DHT.h" // Librairie du capteur DHT
#define DHTPIN 2 // Pin de commande du capteur DTH interne
#define DHTTYPE DHT22  // Type de DHT : DHT22 (AM2302), AM2321

DHT dht(DHTPIN, DHTTYPE); // Chargement de la librairie du capteur DHT

#define FridgePIN 7 // Pin de contrôle de l'alimentation du frigo

// Variables de travail
int Value = 0;
bool test = false;
// Désigne la petite LED contrôlable sur la carte Arduino
int PIN_ONBOARD_LED = 13;
const int LVerte = 10; // broche 2 du micro-contrôleur se nomme maintenant : LVerte


void setup() {
  Serial.begin(9600);   // On ouvre la liaison série
  pinMode(FridgePIN, OUTPUT);   // On passe le pin du frigo en écriture
  // On passe la led de l'arduino en écriture
  pinMode(PIN_ONBOARD_LED, OUTPUT);
  pinMode(LVerte, OUTPUT); //Lverte est une broche de sortie
  dht.begin();   // On active la librairie du sensor DHT
}
 
void loop() {
//Declarations des variables 
  float lecture;
  float tension;
  float resistance_thermistance;
  float temperature;
  float point_de_rose; 
  float h = dht.readHumidity(); //fonction de lecture de l'humidité
  float t = dht.readTemperature(); //fonction de lecture de la température

  Serial.print(h); //Affichage du taux d'humidité interne
  Serial.print(",");

  Serial.print(t); //Affichage de la température interne
  Serial.print(",");

  lecture = analogRead(ThermistancePIN);   //Lecture de la valeur de thermistance par le port analogique 

  if (isnan(lecture)) {
    Serial.println("Impossible de lire la thermistance !");
    return;
  }
   //Calcul de la température externe grace à la relation de Steinhart-Hart
  tension = lecture / 1024 * tension_entrante;
  resistance_thermistance = (resistance * tension) / (tension_entrante - tension); 
  temperature = (1/(1.028013817*pow(10, -3) + 2.514094746*pow(10, -4)*log(resistance_thermistance) + 0.1335666539*pow(10, -7)*pow(log(resistance_thermistance), 3)))- 273.15;

  Serial.print(temperature); //Affichage de la température externe
  
  //Calcul du point de rosée grace à la formule de Gustav Magnus-Tetens
  
  point_de_rose = (237.7*(((17.27*temperature)/(237.7+temperature))+log(h/100)))/(17.27-(((17.27*temperature)/(237.7+temperature))+log(h/100)));
  Serial.print(",");
  Serial.println(point_de_rose); //Affichage du point de rosée 
  
  delay(500);
}

void serialEvent()
{
  
  while (Serial.available()) //Tant que des données sont disponibles
  {
      
    Value = Serial.parseInt();     // Lecture de la commande, sous forme d'entier

    // ON
    if (Value == 1)     // Allumage du réfrigérateur

    {
     
      digitalWrite(FridgePIN, HIGH);
      digitalWrite(PIN_ONBOARD_LED, HIGH); // On allume la LED de l'arduino pour indiquer l'activité
      digitalWrite(LVerte, HIGH); //allumer La led verte



    }
    // OFF
    else if (Value == 2) // Extinction du réfrigérateur
    {
      digitalWrite(FridgePIN, LOW);
      digitalWrite(PIN_ONBOARD_LED, LOW); // On eteint la LED de l'arduino pour indiquer la coupure
      digitalWrite(LVerte, LOW); //Eteinde la led verte


    }
    else {
      Value = 0;
    }

    while (test) {} // synchro
    test = true;
    Serial.print("R:");
    Serial.println(Value);
    test = false;
  }
}

