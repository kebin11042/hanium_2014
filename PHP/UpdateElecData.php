<meta charset = "utf-8">
<?php


	$db_table_big_device = "big_device";
	$db_table_big_elec = "big_elec";
	$db_table_big_device_elec = "big_device_elec";

	//device_Number = Mac 주소 (고유한 번호)
	$big_device_Number = $_GET['device_Number'];
	$big_device_pk = 0;
	//big_elec_Data는 mV로 온다 곧 전력량으로 환산을 해줘야함
	$big_elec_Data = $_GET['elec_Data'];
	$big_elec_Data = 5 * $big_elec_Data * (1.0/3600.0) * (1.0/1000.0) * (1.0/1000.0);
	$big_elec_Data = $big_elec_Data * 1000000;	//1000000배 

	//현재 날짜를 따온다
	$Year = DATE("Y", time());
	$Month = DATE("m", time());
	$Date = DATE("d", time());

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
		//데이터가 삽입되기 전 기존에 장비가 있는지 검사부터 한다

		//기존 장비가 등록되어 있지 않다면 새로 등록해야 한다
		if($total_record == 0){
			$sql = "INSERT INTO ".$db_table_big_device." (Number) VALUES ('$big_device_Number')";
			mysqli_query($conn, $sql);
			//방금 삽입한 pk 따오기
			$big_device_pk = mysqli_insert_id($conn);
			echo "기존 장비가 없어서 새로 등록함";

			//기존 장비가 없으면 당연히 데이터도 없음

			$sql = "INSERT INTO ".$db_table_big_elec." (Data, Year, Month, Date) VALUES ('$big_elec_Data', '$Year', '$Month', '$Date')";
			mysqli_query($conn, $sql);
			$big_elec_pk = mysqli_insert_id($conn);

			$sql = "INSERT INTO ".$db_table_big_device_elec." (device_pk, elec_pk) VALUES ('$big_device_pk', '$big_elec_pk')";
			mysqli_query($conn, $sql);
		}
		//기존에 장비가 있음
		else{
			$row = mysqli_fetch_array($result);
			$big_device_pk = $row[pk];
			echo "기존 장비 있음";

			$sql = "SELECT * FROM ".$db_table_big_device_elec." WHERE device_pk = '".$big_device_pk."'";
			$result = mysqli_query($conn, $sql);
			$total_record = mysqli_num_rows($result);
			
			//장비는 있으나 등록된 데이터가 아예없음 사실 이건 말이 안댐
			if($total_record == 0){
				$sql = "INSERT INTO ".$db_table_big_elec." (Data, Year, Month, Date) VALUES ('$big_elec_Data', '$Year', '$Month', '$Date')";
				mysqli_query($conn, $sql);
				$big_elec_pk = mysqli_insert_id($conn);

				$sql = "INSERT INTO ".$db_table_big_device_elec." (device_pk, elec_pk) VALUES ('$big_device_pk', '$big_elec_pk')";
				mysqli_query($conn, $sql);
				echo "기존 장비는 있으나 데이터가 연결된게 아예없음";
			}
			//장비는 등록되어있고 데이터도 있지만 아직 어떤 날짜의 데이터가 있는지는 알수 없다
			else{

				$FLAG = 0;
				$big_elec_pk = 0;

				for($i=0; $i<$total_record; $i++){
					mysqli_data_seek($result, $i);
    				$row = mysqli_fetch_array($result);

    				//검색한것 중에 오늘 날짜의 데이터가 있는지 살펴봐야한다
    				$sql = "SELECT * FROM ".$db_table_big_elec." WHERE pk = '".$row[elec_pk]."' AND Year = '".$Year."' AND Month = '".$Month."' AND Date = '".$Date."'";
    				$result_2 = mysqli_query($conn, $sql);
    				$total_record_2 = mysqli_num_rows($result_2);

    				if($total_record_2 == 1){
    					$FLAG = 1;
    					$row_2 = mysqli_fetch_array($result_2);
    					$big_elec_pk = $row_2[pk];
    					break;
    				}
				}

				//기존에 있던 데이타가 있음
				if($FLAG == 1){
					$sql = "SELECT * FROM ".$db_table_big_elec." WHERE pk = '".$big_elec_pk."'";
					$result = mysqli_query($conn, $sql);
					$row = mysqli_fetch_array($result);

					$Update_Data = $row[Data] + $big_elec_Data;

					$sql = "UPDATE ".$db_table_big_elec." SET Data = ".$Update_Data." WHERE pk = '".$big_elec_pk."'";
					mysqli_query($conn, $sql);
					echo "기존데이터 업데이트 성공";
				}
				//데이터는 존재하지만 오늘 날짜에 해당하는 데이터는 없다
				else{
					$sql = "INSERT INTO ".$db_table_big_elec." (Data, Year, Month, Date) VALUES ('$big_elec_Data', '$Year', '$Month', '$Date')";
					mysqli_query($conn, $sql);
					$big_elec_pk = mysqli_insert_id($conn);

					$sql = "INSERT INTO ".$db_table_big_device_elec." (device_pk, elec_pk) VALUES ('$big_device_pk', '$big_elec_pk')";
					mysqli_query($conn, $sql);

					echo "데이터는 존재하지만 오늘 날짜에 해당하는 데이터가 없어서 insert";
				}
			}
		}
		
		mysqli_close($conn);
	}

?>