# PizzaDronz
Informatics Large Practical Project. Grade awarded: 81/100 (A2)

# Drone-based Pizza Delivery Service
It is a drone-based service that delivers pizzas from a range of restaurants in Edinburgh onto the top of Appleton Tower. Its behaviour is as follows:

• It can make 2000 moves at most. Each move is either a HOVER move or a straight-line move of length 0.00015 degrees in one of the 16 compass directions.
  The drone must hover for one move when collecting an order at the restaurant and when the order is being collected at Appleton Tower.

•	It receives a certain date between 2023-01-01 and 2023-05-31 and the base URL address of the REST server where to retrieve data from. 
  These data include the participating restaurants (their location and menu), some geographical data that determines how the drone can fly
  and the list of orders on the given date. 
  Each order contains its number, date, customer name, card number, card expiry date, CVV, total cost, and names of pizzas ordered.
  
•	The system must validate all orders on the given date, decide which valid orders are to be delivered and which not, in order to **maximise the number of deliveries**, 
  and compute the flightpath of the drone for the entire day taking into account the movement restrictions.
  
# Usage
To execute this program, please run the following command from the directory you have cloned the repository into:
 
    java -jar target/PizzaDronz-1.0-SNAPSHOT.jar 2023-01-25 https://ilp-rest.azurewebsites.net word

  • Instead of **2023-01-25**, you can use any date between 2023-01-01 and 2023-05-31 (in this particular format YYYY-MM-DD).
  
  • Instead of **word**, you can type in any random word.
  
  • Everything else in the command must remain exactly as it is.
  
# Results
The result of a single execution of the aforementioned command will produce 3 files:

  • deliveries-2023-01-25.json (contains a JSON array of orders on the specified date with their outcomes, e.g. delivered, valid but not delivered, invalid with reasons);
  
  • flightpath-2023-01-25.json (contains a JSON array describing the drone's movement move-by-move on the specified date); and
  
  • drone-2023-01-25.geojson   (contains a GeoJSON visual representation of the drone's movement which can be rendered at geojson.io).
