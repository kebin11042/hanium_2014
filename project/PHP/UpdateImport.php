<?php


	$db_table_big_member = "big_member";

	$big_member_pk = $_POST['member_pk'];
	$big_member_Import = $_POST['Import'];
	//$big_member_pk = 1;

	$conn = mysqli_connect($db_host, $db_user, $db_password, $db_name);
	if(mysqli_connect_errno($conn)){
		echo "데이터 베이스 연결 실패 : " . mysqli_connect_error();
	}
	else{
		//echo "데이터 베이스 연결 성공!!"."</br>";

		mysqli_query($conn, "SET NAMES UTF8");

		$sql = "UPDATE ".$db_table_big_member." SET Import = ".$big_member_Import." WHERE pk = '".$big_member_pk."'";
		mysqli_query($conn, $sql);

		echo "Success"."\n";
		
		mysqli_close($conn);
	}

?>