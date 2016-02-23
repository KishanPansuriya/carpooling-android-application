<?php

	
		$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
		if($conn->connect_error)die($conn->connect_error);
		$response = array();
		
		$name = $_POST['name'];
		$email = $_POST['email'];
		$password = $_POST['pass'];
		$contact = $_POST['contact'];
		$gender = $_POST['gender'];

	if($name!=""&&$email!=""&&$password!=""&&$contact!=""&&$gender!="")
	{
		
			$encrypted_password = hash('ripemd160',$password);
			$query1 = "INSERT INTO users(name, email, password,contact, gender, created_at)VALUES('$name','$email','$encrypted_password','$contact','$gender',NOW())";
			$result = $conn->query($query1);

			if($result)
			{
				$response["success"] = 1;
		        	$response["message"] = "User successfully Registered.";
			        echo json_encode($response);
			}
			else
			{
				$response["success"] = 0;
		        	$response["message"] = "Failed to register!";
			        echo json_encode($response);
			}
	}
	else
	{
		$response["success"] = 0;
	        $response["message"] = "Failed to register!";
		echo json_encode($response);
	}
	$conn->close();
?>