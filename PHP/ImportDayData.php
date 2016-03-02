<?php


	$db_table_big_device_elec = "big_device_elec";
	$db_table_big_elec = "big_elec";

	$big_device_pk = $_POST['device_pk'];
	$big_Year = $_POST['Year'];
	$big_Month = $_POST['Month'];
	$big_Date = $_POST['Date'];

	// $big_device_pk = 2;
	// $big_Year = 2014;
	// $big_Month = 9;
	// $big_Date = 11;

	$big_day_data = 0;

	$conn = mysqli_connect($db_host, $db_user, $db_password, $db_name);
	if(mysqli_connect_errno($conn)){
		echo "데이터 베이스 연결 실패 : " . mysqli_connect_error();
	}
	else{
		//echo "데이터 베이스 연결 성공!!"."</br>";

		mysqli_query($conn, "SET NAMES UTF8");

		$sql = "SELECT * FROM ".$db_table_big_device_elec." WHERE device_pk = '".$big_device_pk."'";
		$result = mysqli_query($conn, $sql);
		$total_record = mysqli_num_rows($result);

		if($total_record == 0){

		}
		else{
			$row = mysqli_fetch_array($result);
			for($i=0; $i<$total_record; $i++){
				mysqli_data_seek($result, $i);
				$row = mysqli_fetch_array($result);

				$sql = "SELECT * FROM ".$db_table_big_elec." WHERE pk = '".$row[elec_pk]."' AND Year = '".$big_Year."' AND Month = '".$big_Month."' AND Date = '".$big_Date."'";
				$result_2 = mysqli_query($conn, $sql);
				$total_record_2 = mysqli_num_rows($result_2);
				if($total_record_2 == 1){
					$row_2 = mysqli_fetch_array($result_2);
					$big_day_data = $row_2[Data];
					break;
				}
			}
		}

		echo $big_day_data."\n";

		mysqli_close($conn);
	}

?>