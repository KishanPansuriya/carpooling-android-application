<?php

	
		$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
		if($conn->connect_error)die($conn->connect_error);
		$response = array();

		if (isset($_POST['loc1']) && isset($_POST['loc2']))
		{
			$loc1 = $_POST['loc1'];
			$loc2 = $_POST['loc2'];
			$date = $_POST['date'];
			
			$query1 = "SELECT * FROM temp WHERE  startPlace='$loc1' AND endPlace='$loc2' AND newJourneydate='$date'";

			$result = $conn->query($query1);
			$num = mysqli_num_rows($result);
			
			while($num>0)
			{
				$row = $result->fetch_row();
				$d1 = $row[0];
				$d2 = $row[9];
				//$temp1 = $result->fetch_assoc()['JID'];
				//$temp = $result->fetch_assoc()['email'];
				$var = $var."&".$d1.",".$d2;
				$num--;
			}
		}
		else{
				$response = "Nothing to show!";	
		}
		$response = $var;
		echo json_encode($response);

		$conn->close();

?>