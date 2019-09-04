<?php 
$db_name = 'xeroxapp';
$db_user = 'root';
$db_pass = 'root';
$db_host = 'localhost'; 

$response = array();
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

$conn = mysqli_connect($db_host, $db_user, $db_pass,$db_name);
$user_email = $input['email'];
$user_name = $input['username'];
$user_mobile = $input['mobile'];
$user_password = $input['password'];


if (!$conn ) 
{
    $response["status"] = 3;
	$response["message"] = "Login Error";	
}
else
{
		$query = "INSERT INTO registration VALUES ('$user_name','$user_email','$user_password','$user_mobile')";		
		$result = mysqli_query($conn,$query);
		
		
		if(!$result)
		{
			$response["status"] = 0;
			$response["message"] = "Error registering the user";
		}
		else
		{
			$response["status"] = 1;
			$response["message"] = "Registration successful";
			
		}				
				mysqli_close($conn);
}
echo json_encode($response);
?>
