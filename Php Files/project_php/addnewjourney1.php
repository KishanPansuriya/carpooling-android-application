<?php
	$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
	if($conn->connect_error)die($conn->connect_error);
	$response = array();

	$email = $_POST['email'];
	$startPlace = $_POST['startPlace'];
	$endPlace = $_POST['endPlace']; 
	$newJourneydate = $_POST['newJourneydate'];
	$vehicleNumber = $_POST['vehicleNumber'];
	$distance = $_POST['distance'];
	$vacantSeats= $_POST['vacantSeats'];
	$charges = $_POST['charges'];
	
	if($email != "")
{

	$query1 = "INSERT INTO temp(email, startPlace, endPlace,newJourneydate,vehicleNumber,distance,charges,vacantSeats,added_at)VALUES('$email', '$startPlace','$endPlace','$newJourneydate','$vehicleNumber','$distance','$charges','$vacantSeats',now())";
	$result = $conn->query($query1);
	
	$q = "SELECT * FROM temp WHERE email='$email'";
	$r = $conn->query($q);
	$jid = $r->fetch_assoc()['JID'];

	$query2 = "INSERT INTO Journey_Details(JID,OfferedBy) VALUES ('$jid','$email')";
	$result2 = $conn->query($query2);

	if($result){
		$response["success"] = 1;
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		echo json_encode($response);
	}
}
	$conn->close();
?>