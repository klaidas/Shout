<?php
    $connect = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
	
    $pid = $_POST["pid"];
    $uid = $_POST["uid"];
	$comment = $_POST["comment"];
	
	
	$statement = mysqli_prepare($connect, "INSERT INTO comments (pid, uid, comment) VALUES (?, ?, ?)");
	mysqli_stmt_bind_param($statement, "iis", $pid, $uid, $comment);
	mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
    
    $response = array();
	$response["success"] = true;
    
    echo json_encode($response);
?>