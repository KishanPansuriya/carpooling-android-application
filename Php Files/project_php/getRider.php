<?php

		// File that GET list of Rider Applied for the same.

		$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
		if($conn->connect_error)die($conn->connect_error);
		$response = array();

		if (isset($_POST['email']))
		{
			$email = $_POST['email'];
			
			$query1 = "SELECT * FROM Journey_Details WHERE  OfferedBy='$email'";

			$result = $conn->query($query1);
			$row = $result->fetch_row();

			$s1 = $row[2];
			$s2 = $row[3];
			$s3 = $row[4];
			$s4 = $row[5];	
			
			$response = $s1."&".$s2."&".$s3."&".$s4;
			
		}
		else{
				$response = "Nothing to show!";	
		}
		echo json_encode($response);

		$conn->close();

?>