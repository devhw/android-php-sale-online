<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$user_name = $_POST['user_name'];
	$password = $_POST['password'];
	if($user_name == '' || $password == ''){
		echo 'please fill all values';
	}else{
		define('HOST','mysql5015.HostBuddy.com');
        define('USER','9f5bb6_prj2');
        define('PASS','Adgjmpt1');
        define('DB','db_9f5bb6_prj2');
 
        $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
		
		$sql = "SELECT * FROM user WHERE user_name = '$user_name'";
		
		$query = mysqli_query($con, $sql);
		if(mysqli_num_rows($query)==0){
			echo 'User_Name not exist';			
		}else{
			$sql = "SELECT * FROM user WHERE user_name = '$user_name' AND password = '$password'";
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			if(isset($check)){
				echo 'successed!';
			}else{
				echo 'wrong password';
			}	
		}
		mysqli_close($con);
	}
}
?>