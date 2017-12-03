<?php
    $connect = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
	
    $uid = $_POST["uid"];
    $title = $_POST["title"];
	$body = $_POST["body"];
	
	
	$statement = mysqli_prepare($connect, "INSERT INTO posts (uid, title, body) VALUES (?, ?, ?)");
	mysqli_stmt_bind_param($statement, "iss", $uid, $title, $body);
	mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
    
    $response = array();
	$response["success"] = true;
    
    echo json_encode($response);
?>