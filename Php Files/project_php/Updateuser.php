<?php

	$conn = new mysqli('mysql.hostinger.in','u343967842_darsh','shoplr513','u343967842_demo');
	if($conn->connect_error)die($conn->connect_error);

	$target_path = "uploads/";
	$response = array();
	
	$server_ip = gethostbyname(gethostname());

	// final file url that is being uploaded
	$file_upload_url = 'http://' . $server_ip . '/' . $target_path;

	if (isset($_FILES['image']['name'])) 
	{
		// reading other post parameters
	    	$email = $_POST['email'];
		$userDOB = $POST['userDOB'];
		$userAddress = $POST['userAddress'];

		$query1 = "INSERT INTO user_information(email, userDOB, userAddress)VALUES('$email', '$userDOB', '$userAddress)";
		$result = $conn->query($query1);		

		$pic_name = "pic_".$email."jpg";
		//$target_path = $target_path . basename($_FILES['image']['name']);
		$target_path = $target_path . $pic_name;

    		$response['file_name'] = basename($_FILES['image']['name']);
	    	$response['email'] = $email;
 
    		try {
        		// Throws exception incase file is not being moved
        		if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {

            		// make error flag true
            		$response['error'] = true;
            		$response['message'] = 'Could not move the file!';
        	}
 
        		// File successfully uploaded
        		$response['message'] = 'File uploaded successfully!';
        		$response['error'] = false;
        		$response['file_path'] = $file_upload_url . basename($_FILES['image']['name']);
    		}	 
		catch (Exception $e) {
        		// Exception occurred. Make error flag true
        		$response['error'] = true;
        		$response['message'] = $e->getMessage();
    		}
	} 
	else {
    		// File parameter is missing
    		$response['error'] = true;
    		$response['message'] = 'Not received any file!';
	}
 
	// Echo final json response to client
	echo json_encode($response);

	$conn->close();
?>