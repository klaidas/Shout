<?php
    $connect = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
	
    $user = $_POST["user"];
    $pass = $_POST["pass"];
	$email = $_POST["email"];
	
	function insertNewUser(){
		global $connect, $user, $pass, $email;
		$hashedPass = password_hash($pass, PASSWORD_DEFAULT);
		$statement = mysqli_prepare($connect, "INSERT INTO users (user, pass, email) VALUES (?, ?, ?)");
		mysqli_stmt_bind_param($statement, "sss", $user, $hashedPass, $email);
		mysqli_stmt_execute($statement);
		mysqli_stmt_close($statement);
	}
	
	function isUsernameAvailable(){
		global $connect, $user;
		$stmt = mysqli_prepare($connect, "SELECT * FROM users WHERE user = ?");
		mysqli_stmt_bind_param($stmt, "s", $user);
		mysqli_stmt_execute($stmt);
		mysqli_stmt_store_result($stmt);
		
		$count = mysqli_stmt_num_rows($stmt);
		mysqli_stmt_free_result($stmt);
		mysqli_stmt_close($stmt);
		if($count < 1){
			return true;
		} else{
			return false;
		}
	}
    
    $response = array();
    $response["success"] = false;
	if(isUsernameAvailable()){
		insertNewUser();
		$response["success"] = true;
	}
    
    echo json_encode($response);
?>