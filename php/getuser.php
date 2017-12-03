<?php
    $con = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
    
	$uid = $_POST["uid"];
	
    $statement = mysqli_prepare($con, "SELECT * FROM posts WHERE uid = ?");
	mysqli_stmt_bind_param($statement, "i", $uid);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $pidResult, $uidResult, $titleResult, $bodyResult);
    
    $response = array();
    $response["success"] = true;  
	$response["rows"] = array();
	
	$count = mysqli_stmt_num_rows($statement);
	$response["numrows"] = $count;
	
    while(mysqli_stmt_fetch($statement)){  
		$temp = array();
		$temp["pid"] = (string) $pidResult;
        $temp["uid"] = (string) $uidResult;
        $temp["title"] = (string) $titleResult;
		$temp["body"] = (string) $bodyResult;
		
		array_push($response["rows"], $temp);
    }
    echo json_encode($response);
?>