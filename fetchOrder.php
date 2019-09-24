<?php 
$db_name = 'xeroxapp';
$db_user = 'root';
$db_pass = 'root';
$db_host = 'localhost'; 

$result = array();

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

$useremail=$input[0]['User_email'];

$conn = mysqli_connect($db_host, $db_user, $db_pass,$db_name);
	
if (!$conn ) 
{
	array_push($result, array('message'=>"Error while fetching details",'status'=>"1"));	
}
else
{
		$query = "SELECT * FROM  order_details WHERE user_email = '$useremail'";		
		$qresult1 = mysqli_query($conn,$query);
		$query2 = "SELECT txn_id,noOfDocs FROM payment_details WHERE user_email = '$useremail'";
		$qresult2 = mysqli_query($conn,$query2);
		
		if($qresult1)
		if(mysqli_num_rows($qresult1)<=0)
		{
			array_push($result, array('message'=>"No order details"));
		}
		else
		{
		    while($row1=mysqli_fetch_array($qresult1) and $row2=mysqli_fetch_array($qresult2)) 
			{ 
				array_push($result, array(
					'message'=>"Order details fetched",
					'useremail'=>$row1[0],
					'orderStatus'=>$row1[1],
					'orderId'=>$row1[2],
					'orderDate'=>$row1[3],
					'txn_Id'=>$row2[0],
					'noofdocs'=>$row2[1]));			
			}
			/*while($row2=mysqli_fetch_array($qresult2))
			{
				array_push($result, array(
					'txn_Id',$row2[0]));
			}*/
			
		}
		
			echo json_encode($result);			
				mysqli_close($conn);
}

?>