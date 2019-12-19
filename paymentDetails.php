<?php 
$db_name = 'xeroxapp';
$db_user = 'root';
$db_pass = 'root';
$db_host = 'localhost'; 
$response = array();
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array
$conn = mysqli_connect($db_host, $db_user, $db_pass,$db_name);
$email = $input['EMAIL'];
$count = $input['Docs'];
$status = $input['STATUS'];
$amount = $input['AMOUNT'];
$txn_id = $input['TXN_ID'];
$order_id = $input['ORDER_ID'];
if (!$conn ) 
{
    $response["status"] = 3;
	$response["message"] = "Error";	
}
else
{
		
		$query = "INSERT INTO payment_details VALUES ('$email','$count','$status','$amount','$txn_id','$order_id')";
		$query1 = "INSERT INTO order_details VALUES ('$email','Pending','$order_id',NOW())";		
		$result = mysqli_query($conn,$query);
		$result1 = mysqli_query($conn,$query1);
		
		
		if(!$result)
		{
			$response["status"] = 0;
			$response["message"] = "Error updating payment details";
		}
		else
		{
			$response["status"] = 1;
			$response["message"] = "Payment details updated";
			
		}				
				mysqli_close($conn);
}
echo json_encode($response);
?>
