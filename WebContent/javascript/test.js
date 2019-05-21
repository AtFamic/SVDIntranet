//iphoneでレスポンシブ対応できないので画面サイズを表示させる
function test(){
	var iphoneWidth = window.innerWidth;
	var iphoneHeight = window.innerHeight;

	var test = document.getElementById("test");
	test.innerHTML = "Width:" + iphoneWidth + ", Height:" + iphoneHeight;

}

function changeIMG(){
	var screenWidth = screen.innerWidth;
	var header = document.getElementById("img1");
	if(screenWidth < 640){
		header.innerHTML = "";
	}else{
		header.innerHTML = "<img src=\"img/starViewData.png\" class=\"absolute\">";
	}
}