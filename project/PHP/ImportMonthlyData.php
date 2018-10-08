<?php


	$db_table_big_device = "big_device";
	$db_table_big_elec = "big_elec";
	$db_table_big_device_elec = "big_device_elec";

	$big_device_pk = $_POST['device_pk'];
	$big_Year = $_POST['Year'];
	$big_Month = $_POST['Month'];
	$big_DayOfMonth = $_POST['DayOfMonth'];

	// $big_device_pk = 2;
	// $big_Year = 2014;
	// $big_Month = 9;
	// $big_DayOfWeek = 30;

	//선언하고 0으로 초기화
	$DayData[$big_DayOfMonth];
	for($i=0; $i<$big_DayOfMonth; $i++){
		$DayData[$i] = 0;
	}

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

		//조회결과가 아무것도 없으면 사용(등록)한게 없다는 뜻이다
		if($total_record == 0){
		}
		//조회결과가 하나 이상일 경우 이번달 날짜랑 비교를 해줘야함
		else{
			for($i=0; $i<$total_record; $i++){
				mysqli_data_seek($result, $i);
				$row = mysqli_fetch_array($result);

				$sql = "SELECT * FROM ".$db_table_big_elec." WHERE pk = '".$row[elec_pk]."' AND Year = '".$big_Year."' AND Month = '".$big_Month."'";
				$result_2 = mysqli_query($conn, $sql);
				$total_record_2 = mysqli_num_rows($result_2);

				if($total_record_2 != 0){
					$row_2 = mysqli_fetch_array($result_2);
					$DayData[$row_2[Date] - 1] = $row_2[Data];
				}
			}
		}
		
		for($i=0; $i<$big_DayOfMonth; $i++){
			echo $DayData[$i]."\n";
		}

		mysqli_close($conn);
	}

?>