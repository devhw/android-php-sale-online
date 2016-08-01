<?php
class DB_Functions{
	private $db;
	public function __construct(){
		include_once './db_connect.php';
		$this->db = new DB_Connect();
		$this->db->connect();
	}
	public function selectall($sql){
		$result = mysql_query($sql);
		while($row = mysql_fetch_assoc($result)){
			$result_all[] = $row;
		}
		mysql_free_result($result);
		return $result_all ;
	}
	public function selectone($sql){
		$result = mysql_query($sql);
		$resultone = mysql_fetch_assoc($result);
		mysql_free_result($result);
		return $resultone;
	}
	public function numrow($sql){
		$result = mysql_query($sql);
		$row = mysql_num_rows($result);
		mysql_free_result($result);
		return $row;
	}
	public function update($table,$data,$where){
		$sql = '';
		foreach($data as $key => $value){
			$sql .= ", $key = '".mysql_real_escape_string($value)."'";
		}
		$sql = 'UPDATE '.$table. ' SET '.trim($sql, ',').' WHERE ' .$where;
		return mysql_query($sql);
	}
	public function insert($table, $data){
		$field_list = '';
		$value_list = '';
		foreach($data as $key => $value){
			$field_list .= ",$key";
			$value_list .= ",'".mysql_real_escape_string($value)."'";
		}
		
		$sql= 'INSERT INTO '.$table.'('.trim($field_list, ',').') VALUES ('.trim($value_list, ',').')';
		
		return mysql_query($sql);
	}
	public function delete($table, $where){
		$sql = "DELETE FROM $table WHERE $where";
		return mysql_query($sql);
	}
}
?>