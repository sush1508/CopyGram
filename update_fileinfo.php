<?php 
$db_name = 'xeroxapp';
$db_user = 'root';
$db_pass = 'root';
$db_host = 'localhost'; 

$response = array();
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

$conn = mysqli_connect($db_host, $db_user, $db_pass,$db_name);
	
$user = $input['usermail'];
$file_name = $input['filename'];
$no_of_copies = $input['copies'];
$no_of_pages = $input['pages'];
$print_sides = $input['sides'];
$print_color = $input['color'];


if (!$conn ) 
{
    //$response["status"] = 3;
	$response["message"] = "Connection Error";	
}
else
{
	$query = "insert into printing_details values('$user','$file_name','$no_of_copies','$no_of_pages','$print_sides','$print_color') ";		
		
		if($conn->query($query) === TRUE  )
					{
						//$response["status"] = 1;
						$response["message"] = "Details uploaded to server";
					}
		else{
			$response["message"]="PHP ERROR ".mysqli_error($conn);
			$response["status"]=0;
			//$response["message"] = "Error while updating details";
		}
		
		
	mysqli_close($conn);
}
echo json_encode($response);
?>