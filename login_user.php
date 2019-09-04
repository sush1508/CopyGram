<?php 
$db_name = 'xeroxapp';
$db_user = 'root';
$db_pass = 'root';
$db_host = 'localhost'; 

$response = array();
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

$conn = mysqli_connect($db_host, $db_user, $db_pass,$db_name);
	//mysqli_select_db($conn,"pccoerelearning");
$user_email = $input['email'];
$user_password = $input['password'];


if (!$conn ) 
{
    $response["status"] = 3;
	$response["message"] = "Login Error";	
}
else
{
		$query = "SELECT * FROM  registration WHERE email = '$user_email' AND password ='$user_password'";		
		$retval = mysqli_query($conn,$query);
		
		
		if(mysqli_num_rows($retval)<=0)
		{
			$response["status"] = 0;
			$response["message"] = "Email or passWord is incorrect";
		}
		else
		{
		    while($row = mysqli_fetch_assoc($retval)) 
			{ 
			    $username=$row['username'];
			}
			$response["status"] = 1;
			$response["username"] = $username;
			$response["message"] = "Login successful";
			
		}				
				mysqli_close($conn);
}
echo json_encode($response);
?>
