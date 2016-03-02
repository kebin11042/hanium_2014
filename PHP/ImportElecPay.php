<?php

	function get_curl($url){
		$agent = 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; MASMJS; rv:11.0)';
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_HEADER, 0);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_POST, 0);
		curl_setopt($ch, CURLOPT_USERAGENT, $agent);
		curl_setopt($ch, CURLOPT_REFERER, "");
		curl_setopt($ch, CURLOPT_TIMEOUT, 3);

		$buffer = curl_exec($ch);
		$cinfo = curl_getinfo($ch);
		curl_close($ch);

		if($cinfo['http_code'] != 200){
			return "";
		}

		return $buffer;
	}

	$url = "http://cyber.kepco.co.kr/ckepco/front/jsp/CY/E/E/CYEEHP00101.jsp";
	//echo $buffer = get_curl($url)."11111";

	preg_match_all("|<[^>]+>(.*)</[^>]+>|U", $buffer, $found, PREG_PATTERN_ORDER);
	echo $found[0][0];
?>