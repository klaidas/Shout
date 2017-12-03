<?php
    $con = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
    
	$uid = $_POST["uid"];
	
    $statement = mysqli_prepare($con, "SELECT * FROM comments WHERE uid = ?");
	mysqli_stmt_bind_param($statement, "i", $uid);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $cidResult, $pidResult, $uidResult, $commentResult);
    
    $response = array();
    $response["success"] = true;  
	$response["rows"] = array();
	
	
    while(mysqli_stmt_fetch($statement)){  
		$temp = array();
		$temp["cid"] = (string) $cidResult;
        $temp["pid"] = (string) $pidResult;
        $temp["uid"] = (string) $uidResult;
		$temp["comment"] = (string) $commentResult;
		
		array_push($response["rows"], $temp);
    }
    echo json_encode($response);
?>