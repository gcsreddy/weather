# weather
endpoint to get weather info using latitude and longitude
server using http4s, cats

cd into project folder

run `sbt run`

sample request:
`curl localhost:8080/weather/lat/28.81/lon/-97.01` 

sample response:

```Weather for latitude 28.81 and longitude -97.01

current temperature : 23.67 Celsius

feel like temperature : 24.49 Celsius

temp is below freezing

weather conditions: overcast clouds

alerts: The National Weather Service in Corpus Christi has extended the
* Flood Advisory for...
Central Victoria County in south central Texas...
* Until 500 PM CDT.
* At 348 PM CDT, Doppler radar indicated heavy rain due to showers
and thunderstorms. Minor flooding is likely occurring in the
advisory area. Up to 2 inches has fallen over a narrow swath in
western Victoria County, with generally 0.5 to 1.5 inches
elsewhere.
Some locations that will experience flooding include...
Victoria, Guadalupe, Saxet Lakes, Downtown Victoria, Victoria
Riverside Park, Victoria College, Victoria Detar Hospital North,
Ball Airport Area, Victoria Mall, Victoria Colony Creek Country
Club, Brentwood Subdivision, Victoria Regional Airport, Oak
Village, Nursery, Dacosta, Bloomington, Mission Valley and Placedo.
Additional rainfall up to 0.5 inch is expected over the area. This
additional rain will result in minor flooding.,...FLASH FLOOD WATCH REMAINS IN EFFECT THROUGH THIS EVENING...
The Flash Flood Watch continues for
* A portion of south Texas, including the following areas, Coastal
Calhoun, Goliad, Inland Calhoun and Victoria.
* Through this evening
* Scattered to numerous showers and thunderstorms are expected to
develop today. Training of showers and thunderstorms may result in
additional rainfall totals of 1 to 2 inches, with locally heavier
amounts possible. Additional rainfall over already saturated soils
may result in flash flooding.
* Low lying areas and places prone to flooding are likely to flood.
Flooding along area rivers, creeks, and streams will likely
continue with any additional heavy rainfall.,...The Flood Warning continues for the following rivers in Texas...
Guadalupe River At Victoria affecting Victoria County.
For the Guadalupe River...including Victoria, Bloomington...Moderate
flooding is forecast.
...The Flood Warning remains in effect...
The Flood Warning continues for
the Guadalupe River At Victoria.
* Until further notice.
* At 1:45 AM CDT Monday the stage was 25.29 feet.
* Flood stage is 21.0 feet.
* Minor flooding is occurring and moderate flooding is forecast.
* Forecast...The river is expected to reach the moderate flood stage
today, and will continue to rise to near 27.7 feet on Thursday
before beginning a slow fall. The river is expected to remain in
the moderate flooding category through Friday morning.
* Impact...At 27.5 feet, Water backs into the golf course next to
the Optimist Club and the duck pond. Water is also flowing between
the gate at Grover`s Bend and McCright Drive. The water is also
back up in the ditch along Memorial Drive in front of the
vollyball courts and special events area
&&
Below are the latest river stages and stage forecasts:
Fld   Observed                Forecasts (1 am)
Location        Stg   Stg   Day/Time    Tue   Wed   Thu   Fri   Sat
Guadalupe River
Victoria        21.0  22.0  Sun 8 pm    27.3  27.6  27.6  26.7  23.9
&&