<?php


	$db_table_big_member = "big_member";

	$big_member_ID = $_POST['ID'];
	$big_member_PW = $_POST['PW'];

	$conn = mysqli_connect($db_host, $db_user, $db_password, $db_name);
	if(mysqli_connect_errno($conn)){
		echo "데이터 베이스 연결 실패 : " . mysqli_connect_error();
	}
	else{
		//echo "데이터 베이스 연결 성공!!"."</br>";

		mysqli_query($conn, "SET NAMES UTF8");

		$sql = "SELECT * FROM ".$db_table_big_member." WHERE ID = '".$big_member_ID."'";
		$result = mysqli_query($conn, $sql);
		$total_record = mysqli_num_rows($result);
		
		if($total_record == 0){
			echo "ID error"."\n";
		}
		else{
			$sql = "SELECT * FROM ".$db_table_big_member." WHERE ID = '".$big_member_ID."' AND PW = '".$big_member_PW."'";

			$result = mysqli_query($conn, $sql);
			$total_record = mysqli_num_rows($result);

			if($total_record == 0){
				echo "PW error"."\n";
			}
			else if($total_record == 1){
				$row = mysqli_fetch_array($result);

				echo "Login Success"."\n";
				echo $row[pk]."\n";
				echo $row[Goal]."\n";
				echo $row[Import]."\n";
			}
		}
		
		mysqli_close($conn);
	}

?>