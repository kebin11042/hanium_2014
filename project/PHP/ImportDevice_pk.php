<?php


	$db_table_big_member_device = "big_member_device";
	$db_table_big_member = "big_member";

	$big_member_pk = $_POST['Logined_pk'];

	$big_device_pk = "Not Connect";

	$conn = mysqli_connect($db_host, $db_user, $db_password, $db_name);
	if(mysqli_connect_errno($conn)){
		echo "데이터 베이스 연결 실패 : " . mysqli_connect_error();
	}
	else{
		//echo "데이터 베이스 연결 성공!!"."</br>";

		mysqli_query($conn, "SET NAMES UTF8");

		$sql = "SELECT * FROM ".$db_table_big_member_device." WHERE member_pk = '".$big_member_pk."'";
		$result = mysqli_query($conn, $sql);
		$total_record = mysqli_num_rows($result);

		if($total_record != 0){
			$row = mysqli_fetch_array($result);
			$big_device_pk = $row[device_pk];
		}
		
		echo $big_device_pk."\n";
		
		$sql = "SELECT * FROM ".$db_table_big_member." WHERE pk = '".$big_member_pk."'";
		$result = mysqli_query($conn, $sql);
		$row = mysqli_fetch_array($result);

		echo $row[Goal]."\n";
		echo $row[Import]."\n";

		mysqli_close($conn);
	}

?>