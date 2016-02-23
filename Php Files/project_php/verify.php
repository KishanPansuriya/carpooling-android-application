<?php

	
	$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
	if($conn->connect_error)die($conn->connect_error);

	$response = array();
		
	$email = $_POST['email'];
	$password = $_POST['password'];

	$encrypted_password = hash('ripemd160',$password);

	$query1 = "SELECT password FROM users WHERE  email='$email'";

	$result = $conn->query($query1);
	$temp = $result->fetch_assoc()['password'];

	if($encrypted_password == $temp)
	{
		$response["success"] = 1;
	        echo json_encode($response);
	}
	else{
		$response["success"] = 0;
	        echo json_encode($response);
	}

	$conn->close();

?>