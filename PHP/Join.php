<?php


	$db_table_big_member = "big_member";

	$big_member_ID = $_POST['ID'];
	$big_member_Name = $_POST['Name'];
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
		
		//중복되는게 하나라도 있다면
		if($total_record != 0){
			echo "ID overlap"."\n";
		}
		else{
			$sql = "INSERT INTO ".$db_table_big_member." (ID, PW, Name) VALUES ('$big_member_ID', '$big_member_PW', '$big_member_Name')";
			mysqli_query($conn, $sql);
			echo "Join OK"."\n";
		}
		
		mysqli_close($conn);
	}

?>