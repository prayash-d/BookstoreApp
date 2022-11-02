package books;


import java.io.IOException;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class mainGUI extends Application {
 
 //objects
 private final Manager manager = new Manager();
 private Customer currentCustomer;
 private static final Files fileOb = new Files();

 //buttons
 Button btn_login = new Button("LOGIN");
 Button btn_logout = new Button("LOGOUT");
 
 Button btn_goBack = new Button("BACK");
 
 Button btn_books = new Button("Books");
 Button btn_customer = new Button("Customers");
 

 Button btn_buy = new Button("BUY");
 Button btn_redeemBuy = new Button("REDEEM & BUY");
 
 TextField textfield_username = new TextField();
 PasswordField textfield_password = new PasswordField();
 
 //HBox lays out its children in a single horizontal row
 //If the hbox has a border and/or padding set, then the contents will be layed out within those insets
 HBox hBox = new HBox(); 

 //TableView allows displaying table views in application
 TableView<BookItem> tableview_booksTable = new TableView<>(); //create table for books
 ObservableList<BookItem> list_books = FXCollections.observableArrayList();

 TableView<Customer> customersTable = new TableView<>(); //create table for customers list
 ObservableList<Customer> customers = FXCollections.observableArrayList();
 
 public ObservableList<BookItem> addBooks(){ //function for appending all books from arraylist in manager class to 'books' arraylist
  list_books.addAll(manager.arraylist_booksList); ////
  return list_books;
 }
 
 public ObservableList<Customer> addCustomers(){ //function for appending all customers to 'customers' arraylist
  customers.addAll(manager.getCustomers());
  return customers;
 }

 @Override
 public void start(Stage primaryStage) {
  
  primaryStage.setTitle("BOOKSTORE APPLICATION");
  primaryStage.setResizable(false); //does not allowe resizing window
  primaryStage.setScene(new Scene(main_login(false), 605, 550)); //login screen initially set as false --> get there through primary stage
  
  primaryStage.show();
  
  System.out.println("Opened Bookstore.");

  //this saves information in file after closing application
  try{
      manager.array_file_refresh();
      System.out.println("Information saved to files.");
  }
  catch (IOException e){
      System.out.println("Error occured while saving files.");
  }
  
  //login btn function
  btn_login.setOnAction(e -> {
   
   boolean flag = false;
   
   if(textfield_username.getText().equals(manager.getUsername()) && textfield_password.getText().equals(manager.getUsername())) {
    primaryStage.setScene(new Scene(manager_start(), 605, 550)); //if admin credentials correct, go to manager start screen
    flag = true;
   }
   
   for(Customer cust: manager.getCustomers()){ //search for if a customer in list matches credentials
    if (textfield_username.getText().equals(cust.getUsername()) && textfield_password.getText().equals(cust.getPassword())) {
     currentCustomer = cust;
     primaryStage.setScene(new Scene(customer_start(0), 605, 550)); //if customer exists, go to customer home screen
     flag = true;
    }
   }
   
   if(flag == false) { //keep user in the login screen if flag is false (ie. no account was verified as admin OR customer by system)
    primaryStage.setScene(new Scene(main_login(true), 605, 550));
   }
   
  }); //end login button function
  
  
  //logout btn function
  btn_logout.setOnAction(e -> {
   
   primaryStage.setScene(new Scene(main_login(false), 605, 550)); //logging out takes you to login screen by default
   
   for(BookItem book: Manager.arraylist_booksList){ //apply a checkbox for each book in book list
    book.setSelect(new CheckBox());
   }
   
   //clear username and password text fields
   textfield_username.clear();
   textfield_password.clear();
   
  }); //end logout button function
  
   
//back button function -> go to new scene: manager start screen
  btn_goBack.setOnAction(e -> primaryStage.setScene(new Scene(manager_start(), 605, 550)));
  
  
//books button function -> go to new scene: book table screen
  btn_books.setOnAction(e -> primaryStage.setScene(new Scene(manager_books(), 605, 550)));
  
  //customer button function (for manager) -> go to new scene: customer table screen (view all customers in system)
  btn_customer.setOnAction(e -> primaryStage.setScene(new Scene(manager_customer(), 605, 550)));
  
  
  //buy button function (will no work unless customer has chosen a book)
  btn_buy.setOnAction(e -> {
   
   boolean book_chosen = false;
   
   for(BookItem book: Manager.arraylist_booksList){ //iterate through book list
    if (book.getSelect().isSelected()){ //if a book from the list is CHECKED, switch book_chosen flag to TRUE
        book_chosen = true;
    }
   }
   
   if(book_chosen == false){ //if no book chosen, stay in default customer home screen
    primaryStage.setScene(new Scene(customer_start(1), 605, 550));
   } 
   else if(book_chosen == true){ //if at least 1 book is chosen, proceed to checkout screen
    primaryStage.setScene(new Scene(customer_pay(false), 605, 550));
   }
  }); //end buy button function
  
  //redeem-points-and-buy function (will not work unless customer has points)
  btn_redeemBuy.setOnAction(e -> {
   
   boolean book_chosen = false;
   
   for(BookItem b: Manager.arraylist_booksList){
    if (b.getSelect().isSelected()) {
        book_chosen = true;
    }
   }
   
   if(book_chosen == false){ //if no book chosen, stay in default customer home screen
    primaryStage.setScene(new Scene(customer_start(1), 605, 550));
   } 
   else if(currentCustomer.getPoints() == 0){ //if no points in account, stay in default customer home screen
    primaryStage.setScene(new Scene(customer_start(2), 605, 550));
   }
   else if(currentCustomer.getPoints() > 0){ //if points > 0, proceed to checkout screen
    primaryStage.setScene(new Scene(customer_pay(true), 605, 550));
   }
   
  }); //end redeem-points-and-buy function
  
  //exiting application (pressing [x])
  //when application closed, reset file and add all the current infomation
  //when application reopened, program appends arrays with the current information
  primaryStage.setOnCloseRequest(e -> {
   
  System.out.println("Book Application closed.");
  
  try{ //proceeds to refresh and update all files using Files method
   
   fileOb.resetBookFile();
   fileOb.resetCustFile();
   System.out.println("Files have been reset.");
   
   fileOb.bookWrite(Manager.arraylist_booksList);
   fileOb.customerWrite(manager.getCustomers());
   System.out.println("Files have been updated with current array data");
   
  }
  
  catch(IOException exception){ //output the exception if something goes wrong
   exception.printStackTrace();
   System.out.println("Exception thrown" + exception);
  }
  
  }); //end set-on-close request
 
 } //END primaryStage

 
 //SUPPLEMENTARY FUNCTIONS
 
 //LOGIN SCREEN
 public Group main_login(boolean loginError){
  
  /* GROUP COMPONENT: a container component which applies no special layout to its children.
  Acts as a container to put other things in.
  All child components (nodes) are positioned at 0,0.
  Typically used to apply some effect or transformation to a set of controls as a whole - as a group*/
  
  Group main_login = new Group(); //to be the return variable
  
  //parent header
  HBox header = new HBox();
  
  //text container for title: "Bookstore"
  Text title_bookstore = new Text();
  
  //VBOX: lays out its children in a single VERTICAL COLUMN 
  //If the vbox has a border and/or padding set, then the contents will be layed out within those insets
  //INSETS: A set of INSIDE OFFSETS for the 4 sides of a rectangular area
  VBox loginBox = new VBox();
  
  VBox combine_elements = new VBox();
  
  title_bookstore.setText("BOOKSTORE");
  title_bookstore.setFont(Font.font("times new roman", FontWeight.BOLD, 35));
  
  header.getChildren().addAll(title_bookstore); //add title to header component

  header.setAlignment(Pos.CENTER); //center header component
  
  loginBox.setPadding(new Insets(30,65,45,65));
  loginBox.setSpacing(15);
  
  Text username = new Text("USERNAME");
  Text password = new Text("PASSWORD");
  
  btn_login.setMinWidth(200); //setMinWidth: Property for overriding the region's computed minimum width
  
  //add username text and its text field, and password text and textfield to vbox 
  loginBox.getChildren().addAll(username, textfield_username, password, textfield_password, btn_login); //displayed in order it is added 

  if(loginError){ //if loginError parameter passed is TRUE
      Text errorMsg = new Text("Invalid Login.");
      loginBox.getChildren().add(errorMsg); //append text: error message to vbox
  }

  //add header(hBOX) and login box(vBOX) into one container, then append to overall base -> Group: loginScreen
  combine_elements.getChildren().addAll(header, loginBox);
  combine_elements.setPadding(new Insets(80, 280, 200, 150));
  combine_elements.setSpacing(80);

  main_login.getChildren().addAll(combine_elements);
  
  return main_login;

 } //end loginScreen

 
 //MANAGER START SCREEN
 public VBox manager_start(){
  
  VBox manager_start = new VBox(); //to return
  HBox buttons_book_customer = new HBox(); //horizontal box
  
  manager_start.setAlignment(Pos.CENTER);
  manager_start.setPadding(new Insets(200,200,200,200));
  manager_start.setSpacing(100);

  buttons_book_customer.setAlignment(Pos.CENTER);
  buttons_book_customer.setSpacing(10);
  
  //add books btn and customer btn
  buttons_book_customer.getChildren().addAll(btn_books, btn_customer);
  btn_books.setPrefSize(100,100);
  btn_customer.setPrefSize(100,100);
  
  //add buttons to main start screen
  manager_start.getChildren().addAll(buttons_book_customer, btn_logout);
  
  return manager_start;

 }//end manager_start
 
 
 //MANAGER BOOKS TABLE SCREEN
 public Group manager_books() {
  
  Group manager_books = new Group(); //to return
  
  Label label = new Label("BOOKS");
  label.setFont(new Font("times new roman", 30));
  
  TextField addBookTitle = new TextField();
  TextField addBookPrice = new TextField();
  
  //add button
  Button addButton = new Button("Add");
  //delete button
  Button deleteButton = new Button("Delete");

  Label bookAddErr = new Label("Invalid Input");
  
  HBox back_area = new HBox();
  
  VBox combine = new VBox();
  VBox combine_vbox = new VBox();

  //hBox.getChildren().clear(); 
  hBox.getChildren().clear(); //clears hBox so that the screen looks the same as before and previous screens do not tangle with the new screen
  tableview_booksTable.getItems().clear();
  tableview_booksTable.getColumns().clear();

  //book title col.
  TableColumn<BookItem, String> titleColumn = new TableColumn<>("Title");
  titleColumn.setMaxWidth(500);
  titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

  //book price col.
  TableColumn<BookItem, Double> priceColumn = new TableColumn<>("Price");
  priceColumn.setMinWidth(150);
  priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

  tableview_booksTable.setItems(addBooks()); //set items into book table
  tableview_booksTable.getColumns().addAll(titleColumn, priceColumn); //put title and price category subheaders into table as well

  //book title text field
  addBookTitle.setPromptText("Title");
  addBookTitle.setMaxWidth(titleColumn.getPrefWidth());
  
  //book price text field
  addBookPrice.setMaxWidth(priceColumn.getPrefWidth());
  addBookPrice.setPromptText("Price");

  addButton.setOnAction(e -> {
   
   try {
    
    double price = Math.round((Double.parseDouble(addBookPrice.getText()))*100);
    Manager.arraylist_booksList.add(new BookItem(addBookTitle.getText(), price/100));
    //makes new book and adds it to arraylist
    tableview_booksTable.getItems().clear(); //refresh page so new books can be accessed
    tableview_booksTable.setItems(addBooks());
    addBookTitle.clear(); //clears text fields
    addBookPrice.clear();
    combine.getChildren().remove(bookAddErr); //removes a previous Invalid Input error if there was one
    
   }
   catch (Exception exception){
    if(!combine.getChildren().contains(bookAddErr)){
        combine.getChildren().add(bookAddErr);
    }
   }
   
  }); //end add button function

  deleteButton.setOnAction(e -> {
   
   BookItem selectedItem = tableview_booksTable.getSelectionModel().getSelectedItem(); //selects row highlighted
   tableview_booksTable.getItems().remove(selectedItem); //removes from table view as soon as delete pressed
   Manager.arraylist_booksList.remove(selectedItem); //actually removes from the arraylist
   
  }); //end delete button

  hBox.getChildren().addAll(addBookTitle, addBookPrice, addButton, deleteButton);
  hBox.setSpacing(10);
  hBox.setAlignment(Pos.CENTER);

  back_area.setPadding(new Insets(5));
  back_area.getChildren().addAll(btn_goBack);

  combine.setAlignment(Pos.CENTER);
  combine.setSpacing(5);
  combine.setPadding(new Insets(0, 0, 0, 150));
  combine.getChildren().addAll(label, tableview_booksTable, hBox);

  combine_vbox.setPadding(new Insets(0, 200, 60, 0));
  combine_vbox.setAlignment(Pos.CENTER);
  combine_vbox.getChildren().addAll(back_area, combine);

  manager_books.getChildren().addAll(combine_vbox);

  return manager_books;

 } //end manager_booksTableScreen
 
 
 //CUSTOMER TABLE SCREEN (for MANAGER)
 public Group manager_customer() {
  
  Group manager_customer = new Group();
  
  Label label = new Label("CUSTOMER");
  label.setFont(new Font("times new roman", 30));

  TextField username_add = new TextField();
  TextField password_add = new TextField();
  
  //btn for adding customer
  Button btn_add_customer = new Button("Add");
  //btn for deleting customer
  Button btn_delete_customer = new Button("Delete");
  
  HBox back_area = new HBox();
  VBox vertical_container = new VBox();
  
  VBox contain_elements = new VBox();
  
  hBox.getChildren().clear(); //clears hBox for appending username and password text fields, and add/delete button
  customersTable.getItems().clear();
  customersTable.getColumns().clear();

  //Customer username column
  TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
  usernameCol.setMinWidth(140);
  usernameCol.setStyle("-fx-alignment: CENTER;"); //align text in textfield to center
  usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

  //Customer password column
  TableColumn<Customer, String> passwordCol = new TableColumn<>("Password");
  passwordCol.setMinWidth(140);
  passwordCol.setStyle("-fx-alignment: CENTER;"); //align text in textfield to center
  passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

  //Customer points column
  TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
  pointsCol.setMinWidth(100);
  pointsCol.setStyle("-fx-alignment: CENTER;"); //align text in textfield to center
  pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

  customersTable.setItems(addCustomers()); //set items (customers) into customers table
  customersTable.getColumns().addAll(usernameCol, passwordCol, pointsCol); //put username, password, and points subheaders into table as well

  username_add.setPromptText("USERNAME");
  username_add.setMaxWidth(usernameCol.getPrefWidth());

  password_add.setMaxWidth(passwordCol.getPrefWidth());
  password_add.setPromptText("PASSWORD");

  btn_add_customer.setOnAction(e -> { //add customer (for manager)

   if(!(username_add.getText().equals("") || password_add.getText().equals(""))){
    
    manager.addCustomer(new Customer(username_add.getText(), password_add.getText())); //create Customer object w/ username and password, append to customerList
    customersTable.getItems().clear(); //this refreshes the table
    customersTable.setItems(addCustomers()); //add customer to table
    
    //clear text fields
    password_add.clear(); 
    username_add.clear();
    
   }
   
  }); //end add button function
  
  btn_delete_customer.setOnAction(e -> { //delete customer (for manager)
   
   Customer item_selected = customersTable.getSelectionModel().getSelectedItem(); //selects item in table
   customersTable.getItems().remove(item_selected); //remove from table view

   manager.deleteCustomer(item_selected); //removes from the arraylist
   
  }); //end deleteButton (for manager)

  //add username textfield, password textfield, add btn, delete btn to horizontal box container
  hBox.getChildren().addAll(username_add, password_add, btn_add_customer, btn_delete_customer);
  
  hBox.setSpacing(35); //spacing of each child element in horizontal box (hbox)
  hBox.setAlignment(Pos.CENTER);
  
  back_area.setPadding(new Insets(5)); 
  back_area.getChildren().addAll(btn_goBack);

  vertical_container.setAlignment(Pos.CENTER);
  vertical_container.setSpacing(5);
  vertical_container.setPadding(new Insets(0,0,0,110));
  vertical_container.getChildren().addAll(label, customersTable, hBox);

  contain_elements.setPadding(new Insets(0, 150, 60, 0));
  contain_elements.getChildren().addAll(back_area, vertical_container);
  contain_elements.setAlignment(Pos.CENTER);

  manager_customer.getChildren().addAll(contain_elements);
  return manager_customer;

 }//end manager_customer
 

 //CUSTOMER HOME SCREEN
 public Group customer_start(int flag){

  Group customer_start = new Group();
  
  HBox cust_infobox = new HBox();
  
  BorderPane header_top = new BorderPane();
  
  //message gives error that customer cannot continue without selecting a book or obtaining points first
  String continue_error = "";
  
  VBox contain_elements = new VBox();
  HBox bottom_hArea = new HBox();
  
  //clear columns
  tableview_booksTable.getItems().clear();
  tableview_booksTable.getColumns().clear();
  //tableview_booksTable.setFocusModel(null);

  Text welcome_message = new Text("Welcome, " + currentCustomer.getUsername() + ". You have " + currentCustomer.getPoints() + " points. You status is " + currentCustomer.getStatus() + ".");

  //booktitle col
  TableColumn<BookItem, String> titleColumn = new TableColumn<>("Title");
  titleColumn.setMinWidth(200);
  titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

  //bookprice col
  TableColumn<BookItem, Double> priceColumn = new TableColumn<>("Price");
  priceColumn.setMinWidth(100);
  priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

  //checkbox col
  TableColumn<BookItem, String> selectColumn = new TableColumn<>("Select");
  selectColumn.setMinWidth(100);
  selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

  tableview_booksTable.setItems(addBooks()); //add books to booktable (it was initially cleared)
  tableview_booksTable.getColumns().addAll(titleColumn, priceColumn, selectColumn); //place tile, price, and select columns into table (becomes visible)
  
  header_top.setLeft(welcome_message);

  bottom_hArea.setAlignment(Pos.BOTTOM_CENTER);
  bottom_hArea.setSpacing(30);
  
  bottom_hArea.getChildren().addAll(btn_buy, btn_redeemBuy, btn_logout);

  if(flag == 1){ continue_error = "You must choose a book first!"; }
  else if(flag == 2){ continue_error =  "You have no points to redeem!"; }

  Text display_errorMsg = new Text(continue_error);
  
  contain_elements.setSpacing(10);
  contain_elements.setAlignment(Pos.CENTER);
  contain_elements.setPadding(new Insets(40, 200, 30, 100));
  contain_elements.getChildren().addAll(header_top, tableview_booksTable, bottom_hArea, display_errorMsg);

  customer_start.getChildren().addAll(contain_elements);

  return customer_start;

 }//end customer_start

 
 //CUSTOMER CHECKOUT SCREEN
 public Group customer_pay(boolean points){
  
  Group customer_pay = new Group();
  
  HBox headline_receiptbox = new HBox();
  VBox list_receipt = new VBox();
  VBox bottom_infoSummary = new VBox();
  
  Text title_receipt = new Text();
  
  VBox contain_elements = new VBox();
  
  int bookCollection = 15;
  
  String[][] booksPurchased = new String[bookCollection][2]; //create 2d array to store book title and price
  
  int earnedPoints;
  int bookNum = 0;
  
  double num_subtotal = 0;
  double num_discount;
  double num_total;
  
  for(BookItem booklist: Manager.arraylist_booksList){
   
   if(booklist.getSelect().isSelected()){
    num_subtotal = num_subtotal + booklist.getPrice();
       
     //store book title and price from actual arraylist to local booksPurchased 2d array
     booksPurchased[bookNum][0] = booklist.getTitle();
     booksPurchased[bookNum][1] = String.valueOf(booklist.getPrice());

     bookNum++; //go to next book in list
       
   }
   
  }

  if(points == true){ //if points were used
   
   //if points money value is greater than or equal to subtotal, just use the points
   if((double)currentCustomer.getPoints() / 100 >= num_subtotal){
    
    num_discount = num_subtotal;
    
    //add the negative amount of points from converting from subtotal
    currentCustomer.setPoints(-(int)num_subtotal * 100);
    
   }
   else{ //else discount is number of points/100 -> $
    
    num_discount = ((double)currentCustomer.getPoints() / 100);
    
    //add the negative amount of points to customer's current points
    currentCustomer.setPoints(-currentCustomer.getPoints());
    
   }
   
  }
  else{ num_discount = 0; }

  num_total = num_subtotal - num_discount; //subtract discount from subtotal to obtain total
  
  earnedPoints = (int)num_total * 10; //gain back points based on total paid (x10)
  
  currentCustomer.setPoints(earnedPoints); //set those points gained to customer's total point collection
  
  headline_receiptbox.setSpacing(15);
  headline_receiptbox.setAlignment(Pos.CENTER); 
  headline_receiptbox.setPadding(new Insets(25,25,25,25));

  title_receipt.setText("RECEIPT");
  title_receipt.setFont(Font.font("times new roman", FontWeight.BOLD, 35));
  
  headline_receiptbox.getChildren().addAll(title_receipt);

  Text text_subtotal = new Text("Price before points discount: $" + (Math.round(num_subtotal * 100.0)) / 100.0);
  
  Text text_discount = new Text("Points Discount: $" + (Math.round(num_discount * 100.0)) / 100.0);
  
  Text text_total = new Text("Total Price: $" + (Math.round(num_total * 100.0)) / 100.0);

  list_receipt.getChildren().addAll(text_subtotal, text_discount, text_total);

  list_receipt.setAlignment(Pos.CENTER);

  bottom_infoSummary.setSpacing(60);
  bottom_infoSummary.setAlignment(Pos.CENTER);
  
  Text purchase_summary = new Text("You earned " + earnedPoints + " points " + ". Your status is: " + currentCustomer.getStatus() + "." + "\nThank you, and we hope to see you again!");
  bottom_infoSummary.getChildren().addAll(purchase_summary, btn_logout);

  contain_elements.setPadding(new Insets(100,100,100,150));
  contain_elements.setAlignment(Pos.CENTER);
  contain_elements.setSpacing(10);
  contain_elements.getChildren().addAll(headline_receiptbox, list_receipt, bottom_infoSummary);

  customer_pay.getChildren().addAll(contain_elements);
  
  Manager.arraylist_booksList.removeIf(book -> book.getSelect().isSelected()); //remove book if book is chosen
  
  return customer_pay;

 }//end customer_pay


 //main function -> launches application protocal
 public static void main(String[] args) {
  launch(args);
 }
 
}

