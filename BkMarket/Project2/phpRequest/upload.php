<?php header('Content-Type: text/html; charset=utf-8'); ?>
<?php
  if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $user_name = $_POST['user_name'];
    $title = $_POST['title'];
    $content = $_POST['content'];
    $cate_name = $_POST['cate_name'];
    $price= $_POST['price'];
	$image_link = $_POST['image_link'];
	$image_list = $_POST['image_list'];
	$created = $_POST['created'];
	$status = $_POST['status'];
	$qty= $_POST['qty'];
    require_once('dbconnect.php');
 
    $sql ="SELECT id FROM items ORDER BY id ASC";
 
    $res = mysqli_query($con,$sql);
 
    $id = 0;
 
    while($row = mysqli_fetch_array($res)){
        $id = $row['id'];
    }
 
    $path = "uploads/$id.png";
 
    $actualpath = "http://quangpg95-001-site1.1tempurl.com/PhotoUploadWithText/$path";
 
    $sql = "INSERT INTO items (title, content, user_name, cate_name, price, image_link, image_list, created, status, qty)
            VALUES ('$title','$content', '$user_name', '$cate_name', '$price', '$image_link', '$image_list', '$created', '$status', '$qty')";
 
    if(mysqli_query($con,$sql)){
        file_put_contents($path,base64_decode($image_link));
        echo "Successfully Uploaded";
    }
 
    mysqli_close($con);
  } else{
        echo "Error";
    }
?>