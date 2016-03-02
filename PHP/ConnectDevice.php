<?php


	$db_table_big_member = "big_member";
	$db_table_big_device = "big_device";
	$db_table_big_member_device = "big_member_device";

	$big_member_pk = $_POST['member_pk'];
	$big_device_Number = $_POST['device_Number'];
	//$big_member_pk = 1;

	$conn = mysqli_connect($db_host, $db_user, $db_password, $db_name);
	if(mysqli_connect_errno($conn)){
		echo "데이터 베이스 연결 실패 : " . mysqli_connect_error();
	}
	else{
		//echo "데이터 베이스 연결 성공!!"."</br>";

		mysqli_query($conn, "SET NAMES UTF8");

		$sql = "SELECT * FROM ".$db_table_big_device." WHERE Number = '".$big_device_Number."'";
		$result = mysqli_query($conn, $sql);
		$total_record = mysqli_num_rows($result);
		
		// 찾는 device가 없다
		if($total_record == 0){
			echo "Device Number error"."\n";
		}
		// 찾는 device가 있다
		else{
			$row = mysqli_fetch_array($result);
			$device_pk = $row[pk];

			$sql = "SELECT * FROM ".$db_table_big_member_device." WHERE member_pk = '".$big_member_pk."'";
			$result = mysqli_query($conn, $sql);
			$total_record = mysqli_num_rows($result);

			// 신규 연결
			if($total_record == 0){
				$sql = "INSERT INTO ".$db_table_big_member_device." (member_pk, device_pk) VALUES ('$big_member_pk', '$device_pk')";
				mysqli_query($conn, $sql);

				echo "Connect Device Success"."\n";
			}
			// 기존에 연결 되어있던 사람은 device 변경
			else{
				$row = mysqli_fetch_array($result);
				$sql = "UPDATE ".$db_table_big_member_device." SET device_pk = ".$device_pk." WHERE member_pk = '".$big_member_pk."'";
				mysqli_query($conn, $sql);

				echo "Change Device Success"."\n";
			}
		}
		
		mysqli_close($conn);
	}

?>