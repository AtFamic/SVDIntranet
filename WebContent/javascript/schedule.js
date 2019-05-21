//開始時刻と終了時刻を比較して、異常値であればfalseを返す。
function checkTime(){
	var startHour = document.forms[0].elements[1].value;
	var startMin = document.forms[0].elements[2].value;
	var endHour = document.forms[0].elements[3].value;
	var endMin = document.forms[0].elements[4].value;

	if(startHour == "" || startMin == "" || endHour == "" || endMin == ""){
		alert("開始時間および終了時間は入力必須項目です。")
		if(startHour == ""){
			document.forms[0].elements[1].focus();
		}else if(startMin == ""){
			document.forms[0].elements[2].focus();
		}else if(endHour == ""){
			document.forms[0].elements[3].focus();
		}else{
			document.forms[0].elements[4].focus();
		}
		return false;
	}

	var startTime = startHour * 60 + startMin;
	var endTime = endHour * 60 + endMin;
	if(startTime > endTime){
		alert("終了時刻は開始時刻よりも早くできません。");
		document.forms[0].elements[1].focus();
		return false;
	}else{
		return true;
	}
}
