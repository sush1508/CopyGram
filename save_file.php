<?php

if(isset($_FILES['uploaded_file']['name']))
{
	$file_path = "uploads/";
	$filename = basename($_FILES['uploaded_file']['name']);
	$file_path = $file_path . basename(($_FILES['uploaded_file']['name']));

	
	$filetype = $_FILES['uploaded_file']['type'];
	$filesize = $_POST['fsize'];
	$user_email = $_POST['useremail'];
	$status=0;
	
	//check file size
	if($filesize>5000000000)
	{
		$status = 1;
		echo "file too large";
	}


	if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'],$file_path))	
	{
		
				$db_name = 'xeroxapp';
				$db_user = 'root';
				$db_pass = 'root';
				$db_host = 'localhost'; 
			 
			
				$conn = mysqli_connect($db_host, $db_user, $db_pass,$db_name);
				//mysqli_select_db($conn,"pccoerelearning");
			

				if (!$conn ) 
				{
			    		die("Connection failed: " . $conn->connect_error);
			
				}
				else
				{
					$query = "insert into documents values('$user_email','$filename','$filetype','$file_path')";		
					//$retval = mysqli_query($conn,$query);
						
					if($conn->query($query) === TRUE  )
					{
						
						$result= "The file ". basename( $_FILES["uploaded_file"]["name"]). " has been uploaded.";
						//echo $result;	
					}
					
				}
				mysqli_close($conn);
				

	}
	


}

	
 ?>
