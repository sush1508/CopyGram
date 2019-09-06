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
    //$result["status"] = 3;
	array_push($result, array('message'=>"Connection error"));	
}
else
{
		$query = "SELECT * FROM  documents WHERE useremail = '$useremail'";		
		$retval = mysqli_query($conn,$query);
		
		//$rows = mysqli_num_rows($retval);
		if(mysqli_num_rows($retval)<=0)
		{
			//$result["status"] = 0;
			//$result["message"] = "No documents";
			array_push($result, array('message'=>"No documents"));
		}
		else
		{
		    while($row=mysqli_fetch_array($retval)) 
			{ 
				
				array_push($result, array(
					'message'=>"Documents fetched",
					'useremail'=>$row[0],
					'filename'=>$row[1],
					'filetype'=>$row[2],
					'filelocation'=>$row[3]	));
				

			}
			//$result['status']=1;
			//$result['message']="Documents fetched";
			//echo json_encode($result);
		}
		
			echo json_encode($result);			
				mysqli_close($conn);
}

?>