<?php
		// file that stores name of users applying for ride.

		$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
		if($conn->connect_error)die($conn->connect_error);
		$response = array();

		$email = $_POST['email'];
		$user_mail = $_POST['myEmail'];
		$jid = $_POST['jid'];
		
		if($user_mail == $email){$response = "You can't apply to your own Ride!";echo json_encode($response);$conn->close();die();}
		
		$q = "SELECT * FROM temp WHERE email='$email' AND JID='$jid'";
		$r = $conn->query($q);
		$temp = $r->fetch_assoc()['vacantSeats'];
		
		if($temp == 0)
		{
			$response = "No seats Available!";
			echo json_encode($response);$conn->close();die();
		}
		// Query to fetch blank RIDE TAKER column and fill it with user who apply

		$query2 = "SELECT * FROM Journey_Details WHERE OfferedBy = '$email' and JID='$jid'";
		$result2 = $conn->query($query2);
		$row = $result2->fetch_row();

		$s1 = $row[2];
		$s2 = $row[3];
		$s3 = $row[4];
		$s4 = $row[5];

	if($s1 != $user_mail && $s2 != $user_mail && $s3 != $user_mail && $s4 != $user_mail)
	{
		if($s1 == null){$query_final = "UPDATE Journey_Details SET TakenBy1 = '$user_mail' WHERE OfferedBy = '$email'";}
		else if($s2 == null)
		{$query_final = "UPDATE Journey_Details SET TakenBy2 = '$user_mail' WHERE OfferedBy = '$email'";}
		else if($s3 == null)
		{$query_final = "UPDATE Journey_Details SET TakenBy3 = '$user_mail' WHERE OfferedBy = '$email'";}
		else if($s4 == null)
		{$query_final = "UPDATE Journey_Details SET TakenBy4 = '$user_mail' WHERE OfferedBy = '$email'";}
		else{echo "bye";}

		$result = $conn->query($query_final);
		$temp = $temp-1;
		$query1 = "UPDATE temp SET vacantSeats = '$temp' WHERE email = '$email'";	
		$result1 = $conn->query($query1);

		if($result && $result1)
		{$response = "Applied for Ride Successfully! You can Chat with Ride provider after his approval for ride!";}
		else {$response = "Couldn't make It!";}
	}
	else
	{
		$response = "You already Applied for the same!";
	}

	echo json_encode($response);
	$conn->close();
?>