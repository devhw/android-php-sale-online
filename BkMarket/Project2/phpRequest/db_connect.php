<?php
     class DB_Connect{
		function __contruct(){
		}
		function __decontruct(){
		}
		public function connect(){
			require_once 'config.php';
			$con = mysql_connect(DB_HOST, USER, PASS);
			mysql_set_charset('utf8',$con);
			mysql_select_db(DATABASE);
			return $con;
		}
		public function close(){
			mysql_close();
		} 
	}
?>