<?php


	$db_table_big_member = "big_member";
	$db_table_big_device = "big_device";
	$db_table_big_member_device = "big_member_device";

	$big_member_pk = $_POST['pk'];
	//$big_member_pk = 1;

	$conn = mysqli_connect($db_host, $db_user, $db_password, $db_name);
	if(mysqli_connect_errno($conn)){
		echo "데이터 베이스 연결 실패 : " . mysqli_connect_error();
	}
	else{
		//echo "데이터 베이스 연결 성공!!"."</br>";

		mysqli_query($conn, "SET NAMES UTF8");

		$sql = "SELECT * FROM ".$db_table_big_member." WHERE pk = '".$big_member_pk."'";
		$result = mysqli_query($conn, $sql);
		$row = mysqli_fetch_array($result);

		echo $row[Name]."\n";
		echo $row[Goal]."\n";
		echo $row[Import]."\n";

		$sql = "SELECT * FROM ".$db_table_big_member_device." WHERE member_pk = '".$big_member_pk."'";
		$result = mysqli_query($conn, $sql);
		$total_record = mysqli_num_rows($result);

		if($total_record == 0){
			echo "Not Connect"."\n";
		}
		else{
			$row = mysqli_fetch_array($result);
			$device_pk = $row[device_pk];
			$sql = "SELECT * FROM ".$db_table_big_device." WHERE pk = '".$device_pk."'";
			$result = mysqli_query($conn, $sql);
			$row = mysqli_fetch_array($result);

			echo $row[Number]."\n";
		}
		
		mysqli_close($conn);
	}

?>