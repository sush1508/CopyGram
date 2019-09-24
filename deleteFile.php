<?php 
$db_name = 'xeroxapp';
$db_user = 'root';
$db_pass = 'root';
$db_host = 'localhost'; 

$result = array();

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

$fileName = $input['deleteFile'];

$conn = mysqli_connect($db_host, $db_user, $db_pass,$db_name);
	
if (!$conn ) 
{
	$result['message'] = "Error connecting";
	$result['status'] = 0;
}
else
{
		$query = "DELETE FROM documents WHERE filename = '$fileName'";
		$query1 = "DELETE FROM printing_details WHERE file_name = '$fileName'";		
		$qresult = mysqli_query($conn,$query);
		$qresult1 = mysqli_query($conn,$query1);
		if($qresult and $qresult1)
		{
			$result['message'] = "File deleted";
			$result['status'] = 1;
		}
		else
		{
			$result['message'] = "File not deleted";
			$result['status'] = 2;
		}
		
		echo json_encode($result);			
		mysqli_close($conn);
}

?>




<!-- UPDATE `order_details` SET `order_status`="Pending" WHERE user_email = "sushsaid@gmail.com" -->