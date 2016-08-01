<?php header('Content-Type: application/json; charset=utf-8');
header('Content-Type: bitmap; charset=utf-8'); ?>
<?php

     $METHOD_LOGIN = 1;
	 $METHOD_REGISTER = 2;
	 $METHOD_GET_ISTEMS = 3;
	 $METHOD_ADD_ITEM = 4;
	 $METHOD_GET_ALL_ISTEMS = 5;
	 $METHOD_GET_BOOK_ISTEMS = 6;
	 $METHOD_GET_TEST_ISTEMS = 7;
	 $METHOD_GET_OTHER_ISTEMS = 8;
	 $METHOD_GET_USER_INFO = 9;
	 $METHOD_HIDE_ITEM = 10;
	 $METHOD_MANAGE_ITEM = 11;
	 $METHOD_ADD_ITEM_IMG = 12;
	 include_once 'db_function.php';
	 
	 $db = new DB_Functions();
	 $user_name =(string)$_POST['user_name'];
	 $method = $_POST['method'];
	 
	if($method == $METHOD_LOGIN) {
        $sql = "SELECT * FROM user WHERE user_name = '$user_name'";
        $rs = $db->selectone($sql);
        if($rs['password'] ==(string)$_POST['password']){
			$result = $rs;
        }else{
            $result = 'Sai tài khoản hoặc mật khẩu';
        }
    }
	if($method == $METHOD_REGISTER) {
        $data = array();
        $data['user_name'] = $user_name;
        $data['password'] =(string) $_POST['password'];
		$data['name'] =(string) $_POST['name'];
		$data['phone'] =(string) $_POST['phone'];
		$data['address'] =(string) $_POST['address'];
		$data['created'] =(string) $_POST['created'];
         
        $rs = $db->insert('user', $data);
        if($rs){
            $result['register'] = true;
        }else{
            $result['register'] = false;
        }
    }
	if($method == $METHOD_HIDE_ITEM) {
		$data = array();
		$data['status'] =(int)$_POST['status'];
		$id = (int)$_POST['id'];
		$rs = $db->update('items', $data, "id = $id");
		if($rs){
			$result['update'] = true;
		}else {
			$result['update'] = false;
		}
	}
	if($method == $METHOD_GET_ISTEMS) {
        $sql = "SELECT * FROM items WHERE user_name = '$user_name' AND status = 1 ORDER BY id DESC";
        $result = $db->selectall($sql);
    }
	if($method == $METHOD_MANAGE_ITEM) {
        $sql = "SELECT * FROM items WHERE user_name = '$user_name' ORDER BY status DESC , id DESC";
        $result = $db->selectall($sql);
    }
	if($method == $METHOD_GET_ALL_ISTEMS) {
        $sql = "SELECT * FROM items WHERE status = 1 ORDER BY id DESC";
        $result = $db->selectall($sql);
    }
	if($method == $METHOD_GET_BOOK_ISTEMS) {
        $sql = "SELECT * FROM items WHERE cate_name = 'Sách' AND status = 1 ORDER BY id DESC";
        $result = $db->selectall($sql);
    }
	if($method == $METHOD_GET_TEST_ISTEMS) {
        $sql = "SELECT * FROM items WHERE cate_name = 'Đề thi' AND status = 1 ORDER BY id DESC";
        $result = $db->selectall($sql);
    }
	if($method == $METHOD_GET_OTHER_ISTEMS) {
        $sql = "SELECT * FROM items WHERE cate_name = 'Khác' AND status = 1 ORDER BY id DESC";
        $result = $db->selectall($sql);
    }
	if($method == $METHOD_GET_USER_INFO){
		$sql = "SELECT * FROM user WHERE user_name = '$user_name' ORDER BY id DESC";
		$result = $db->selectone($sql);
	}
	if($method == $METHOD_ADD_ITEM){
        $data = array();
        $data['user_name'] = $user_name;
        $data['title'] = (string)$_POST['title'];
        $data['content'] = (string)$_POST['content'];
        $data['cate_name'] =(string) $_POST['cate_name'];
        $data['user_name'] =(string) $_POST['user_name'];
		$data['price'] = (string)$_POST['price'];
		$data['image_link'] =(string) $_POST['image_link'];
		$data['image_list'] =(string) $_POST['image_list'];
		$data['created'] =(string) $_POST['created'];
		$data['status'] = (int)$_POST['status'];
		$data['qty'] = (int)$_POST['qty'];
 
        $rs = $db->insert('items', $data);
        if($rs){
            $result['add'] = true;
        }else{
            $result['add'] = false;
        }
    }
    if($method == $METHOD_ADD_ITEM_IMG){
    	$data = array();
        $data['user_name'] = $user_name;
        $data['title'] = (string)$_POST['title'];
        $data['content'] = (string)$_POST['content'];
        $data['cate_name'] =(string) $_POST['cate_name'];
        $data['user_name'] =(string) $_POST['user_name'];
		$data['price'] = (string)$_POST['price'];
		$data['image_list'] =(string) $_POST['image_list'];
		$data['created'] =(string) $_POST['created'];
		$data['status'] = (int)$_POST['status'];
		$data['qty'] = (int)$_POST['qty'];

		$sql ="SELECT id FROM items ORDER BY id ASC";
 
        $res = mysql_query($sql);
		$id = 0;
        while($row = mysql_fetch_array($res)){
            $id = $row['id'];
        }
        $path = "uploads/$id.png";
        $data['image_link'] = "http://quangpg95-001-site1.1tempurl.com/$path";
        $fp = fopen($path, 'w+');
        if(!$fp){
            $result['add'] = false;
        }else{
            $imsrc = base64_decode((string)$_POST['image_link']);
            fwrite($fp, $imsrc);
            fclose($fp);
            $rs = $db->insert('items', $data);
            if($rs){
                $result['add'] = true;
                // file_put_contents($path,base64_decode($image_link));
            }else{
                $result['add'] = false;
            }
        }
        
    }
	$json = json_encode($result, JSON_PRETTY_PRINT); 
    // $json = json_encode($result); // use on hostinger
    print_r($json);
?>