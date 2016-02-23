<?php
	$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
	if($conn->connect_error)die($conn->connect_error);
	$response = array();

	$email = $_POST['email'];

        $subject = "JOURNEYS new password request";
	$temp = hash('ripemd160',rand(1,99999));
	$pass = substr($temp,0,5);

	$encrypted_password = hash('ripemd160',$pass);

	$query1 = "SELECT name FROM users WHERE email='$email'";
	$result1 = $conn->query($query1);

	if($result1)
	{
         	$query = "UPDATE users SET password='$encrypted_password' WHERE email='$email'";
		$result = $conn->query($query);
		
		if($result)
		{
			ini_set('display_errors',1);

		        $message = "<b>New password for $email is $pass</b>";

	        	$retval = mail ($email,$subject,$message);
         
        	 	if($retval)
			{
				$response["success"] = 1;
				echo json_encode($response);
			}
		        else
			{
				$response["success"] = 0;
				echo json_encode($response);
			}
		}
	}
	$conn->close();
?>