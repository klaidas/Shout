<?php
    $connect = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
	
    $pid = $_POST["pid"];
    $uid = $_POST["uid"];
	
	$check_statement = mysqli_prepare($connect, "SELECT * FROM likes WHERE pid = ? AND uid = ?");
	mysqli_stmt_bind_param($check_statement, "ii", $pid, $uid);
	mysqli_stmt_execute($check_statement);
	mysqli_stmt_store_result($check_statement);
	mysqli_stmt_bind_result($check_statement, $likeidResult, $pidResult, $uidResult);

	$count = mysqli_stmt_num_rows($check_statement);
	
	$response = array();
	$response["success"] = false;
	
	if($count < 1){
	
		$insert_statement = mysqli_prepare($connect, "INSERT INTO likes (pid, uid) VALUES (?, ?)");
		mysqli_stmt_bind_param($insert_statement, "ii", $pid, $uid);
		mysqli_stmt_execute($insert_statement);
		mysqli_stmt_close($insert_statement);
		
		$response["success"] = true;
		$response["liked"] = true;
		
	} else if($count == 1){
	
		mysqli_stmt_fetch($check_statement);
		$lid = $likeidResult;
	
		$remove_statement = mysqli_prepare($connect, "DELETE FROM likes WHERE likeid = ?");
		mysqli_stmt_bind_param($remove_statement, "i", $lid);
		mysqli_stmt_execute($remove_statement);
		mysqli_stmt_close($remove_statement);
		
		$response["success"] = true;
		$response["liked"] = false;
	}
	
	echo json_encode($response);
?>