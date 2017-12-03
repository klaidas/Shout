<?php
    $con = mysqli_connect("localhost", "shout_root", "36yTqszVwHKFu4Vc", "db_shout");
    
    $user = $_POST["user"];
    $pass = $_POST["pass"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM users WHERE user = ?");
    mysqli_stmt_bind_param($statement, "s", $user);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $uidResult, $userResult, $passResult, $emailResult);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        if (password_verify($pass, $passResult)) {
            $response["success"] = true;  
            $response["uid"] = (string) $uidResult;
            $response["user"] = (string) $userResult;
			$response["email"] = (string) $emailResult;
        }
    }
    echo json_encode($response);
?>