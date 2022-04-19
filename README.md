# CryptocurrencyTrackerApp
My project is a computer application to track an individuals cryptocurrency portfolio. The user will be able to enter in which coins they’ve bought and for how much. Once the user has logged in, that users portfolio will be displayed in a table. The table will have each coin, the total spent on that coin, the total amount of coins they own, the average buy price for the coin, their profit/loss amount and their ROI. The application will also show their total portfolios value. 

User Login 

  Create Account

•	The user will be able to create an account. They will choose a unique username, enter their name, their email, and create a password. This will be stored in a MySQL database. The database will then auto-create a CSV filename based on their username. 
•	If the username already exists, a window will pop-up and notify the user to choose a different username.

  Logging In
  
•	The user will enter their username and password. If correct, the application will receive the associated CSV filename from the database and that users’ information will be displayed.

  After Login
  
Once the user has made an account and logged in, they will be able to add/edit their portfolio. This portfolio will be saved in a CSV file under the name that was created by the database. 
  
Actions Available
 
  The user will be able to:

•	Edit portfolio - a new window will pop-up and the user will be able to select from a dropdown menu which cryptocurrency (that is currently in their portfolio) to edit. They will then be able to edit the total amount spent and the total amount of coins purchased. The new information will replace the information in the CSV file and be saved for later use. Once complete, the table data will refresh, showing the updated data.
•	Add New Coin - a new window will pop-up and the user will be able to select from a list (that is provided by the API call) of which cryptocurrency to add to their portfolio. Then they will enter the total amount spent and the total amount of coins purchased. The new coin will be written to the CSV file and the table will be refreshed to show the newly added coins’ data.
•	Remove Coin - a new window will pop-up and the user will be able to select from a list of coins currently in their portfolio and remove that coin from their portfolio. A warning/confirm window will pop-up to confirm their decision to remove the coin. The coin to be deleted will be removed from the CSV file, and the table will refresh with the changes made.

  Displaying Information
  
•	The portfolio information will be read from the CSV file and displayed in a table. Together with the CSV file and the API call, the program will calculate the profit/loss, the ROI, and the total portfolio value. 
•	The CSV file will store the coin name, amount owned, average purchase price, and the total spent
•	When the CSV file is read, it will make the call to the API and map the information from the CSV file and get the data from the API.
•	Once the price information has been received from the API, the program will then calculate the values using current price information and save that in a list to be displayed in the table.

  API Call
  
•	I will use an API from CoinMarketCap. This will receive a JSON object containing the top 100 cryptocurrencies and their current prices, among other data. The program will parse the JSON object.

After Logging Out

Once the user wished to log out, the CSV file will close and the application will return to the log in screen.
