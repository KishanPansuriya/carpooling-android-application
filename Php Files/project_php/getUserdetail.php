<?php

		// When user get Ride Offerer List after then he selects one.
		// This file provides details of selected user.
		
		$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
		if($conn->connect_error)die($conn->connect_error);
		$response = array();

		$email = $_POST['email']; // Ride Offerer's email
		$user_mail = $_POST['myEmail']; //App user email
		$jid = $_POST['jid'];
			
		//Check - Ride owner  can not apply to his ride.

		$q = "SELECT email FROM temp WHERE email='$email' AND JID='$jid'";
		$r = $conn->query($q);
		$temp = $r->fetch_assoc()['email'];

		if($user_mail == $email)
		{
			$response = "You can't Apply to your own ride!";
		}
		else
		{
			$query1 = "SELECT * FROM temp WHERE  email='$email' AND JID='$jid'";
			$query2 = "SELECT name FROM users WHERE email='$email'";

			$result1 = $conn->query($query1);
			$result2 = $conn->query($query2);

				if(result1 && result2){
					$row = $result1->fetch_row();
					$d1 = $row[3];
					$d2 = $row[4];
	
					$d3 = $result2->fetch_assoc()['name'];
				}
				else{
					$d1 = "Blank";
					$d2 = "Blank";
					$d3 = "Blank";
				}

			$response = "You selected Ride on Date: ".$d2.".Ride is Offered by: ".$d3.".Total Distance is: ".$d1;

		}
		echo json_encode($response);
		$conn->close();

?>