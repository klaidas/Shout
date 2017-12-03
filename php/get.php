<?php
    $con = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
    
    $statement = mysqli_prepare($con, "SELECT posts.*, users.user FROM posts, users WHERE posts.uid = users.uid");
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $pidResult, $uidResult, $titleResult, $bodyResult, $username);
    
    $response = array();
    $response["success"] = true;  
	$response["rows"] = array();
	
    while(mysqli_stmt_fetch($statement)){  
		$temp = array();
		
		$temp["pid"] = (string) $pidResult;
        $temp["uid"] = (string) $uidResult;
        $temp["title"] = (string) $titleResult;
		$temp["body"] = (string) $bodyResult;
		$temp["user"] = (string) $username;
		
		$likes_statement = mysqli_prepare($con, "SELECT * FROM likes WHERE pid = ?");
		mysqli_stmt_bind_param($likes_statement, "i", $pidResult);
		mysqli_stmt_execute($likes_statement);
		mysqli_stmt_store_result($likes_statement);
		$count = mysqli_stmt_num_rows($likes_statement);
		
		$temp["likes"] = (string) $count;
		
		array_push($response["rows"], $temp);
    }
    echo json_encode($response);
?>