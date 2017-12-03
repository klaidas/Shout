<?php
    $con = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
    
	$pid = $_POST["pid"];
	
    $statement = mysqli_prepare($con, "DELETE FROM posts WHERE pid = ?");
	mysqli_stmt_bind_param($statement, "i", $pid);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
	
    echo json_encode($response);
?>