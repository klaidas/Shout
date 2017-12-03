<?php
    $con = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
	
	$pid = $_POST["pid"];
	
	$query = "SELECT comments.*, users.user FROM comments, users WHERE comments.pid = ? AND comments.uid = users.uid";
    $statement = mysqli_prepare($con, $query);
	mysqli_stmt_bind_param($statement, "i", $pid);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $cidResult, $pidResult, $uidResult, $commentResult, $userResult);
    
    $response = array();
    $response["success"] = true;  
	$response["rows"] = array();
	
    while(mysqli_stmt_fetch($statement)){  
		$temp = array();
		
		$temp["cid"] = (string) $cidResult;
        $temp["pid"] = (string) $pidResult;
        $temp["uid"] = (string) $uidResult;
		$temp["comment"] = (string) $commentResult;
		$temp["user"] = (string) $userResult;
		
		array_push($response["rows"], $temp);
    }
    echo json_encode($response);
?>