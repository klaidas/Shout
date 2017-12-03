<?php
    $con = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
    
	$cid = $_POST["cid"];
	
    $statement = mysqli_prepare($con, "DELETE FROM comments WHERE cid = ?");
	mysqli_stmt_bind_param($statement, "i", $cid);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
	
    echo json_encode($response);
?>