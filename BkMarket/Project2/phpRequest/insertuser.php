<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
 
     //Getting post data 
     $name = $_POST['name'];
     $username = $_POST['user_name'];
     $password = $_POST['password'];
     $phone = $_POST['phone'];
     $address = $_POST['address'];
     $created = $_POST['created'];
	 $user_role = $_POST['user_role'];
 
     //checking if the received values are blank
     if($name == '' || $username == '' || $password == '' || $phone == '' || $address == ''){
     //giving a message to fill all values if the values are blank
        echo 'please fill all values';
     }else{
     //If the values are not blank
     //Connecting to our database by calling dbConnect script 
         //require_once('dbConnect.php');
		 define('HOST','mysql5015.HostBuddy.com');
         define('USER','9f5bb6_prj2');
         define('PASS','Adgjmpt1');
         define('DB','db_9f5bb6_prj2');
 
         $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
 
     //Creating an SQL Query to insert into database 
     //Here you may need to change the retrofit_users because it is the table I created
     //if you have a different table write your table's name
 
     //This query is to check whether the username or email is already registered or not 
         $sql = "SELECT * FROM user WHERE user_name='$username' OR phone='$phone'";
 
     //If variable check has some value from mysqlii fetch array 
     //That means username or email already exist 
         $check = mysqli_fetch_array(mysqli_query($con,$sql));
 
     //Checking check has some values or not 
         if(isset($check)){
     //If check has some value that means username already exist 
             echo 'username or phone already exist';
         }else{ 
         //If username is not already exist 
        //Creating insert query 
            $sql = "INSERT INTO user VALUES( 
			                                null,
			                                '$username',
			                                '$password',
			                                '$name',
			                                '$phone',
			                                '$address',
				                            '$created',
				                            '$user_role')";
 
    //Trying to insert the values to db 
              if(mysqli_query($con,$sql)){
    //If inserted successfully 
                  echo 'successfully registered';
              }else{
    //In case any error occured 
                   echo 'oops! Please try again!';
              }
         }
 //Closing the database connection 
         mysqli_close($con);
    }
}else{
echo 'error';
}
?>