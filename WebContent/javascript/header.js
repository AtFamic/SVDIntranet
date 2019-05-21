//screenの幅を640pxを境にHTMLを切り替える
function changeHeader(){
	var screenWidth = screen.innerWidth;
	var header = document.getElementById("header");
	if(screenWidth > 640){
		header.innerHTML = "<nav>\r\n" +
		"      <ul>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/DefaultViewServlet\">トップページ</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/MonthViewServlet?isInitial=true\">スケジュール</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/TimeCardServlet\">タイムカード</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/ScheduleServlet\">スケジュール登録</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/InformationServlet\">メッセージ登録</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/LogoutServlet\">ログアウト</a></li>\r\n" +
		"      </ul>\r\n" +
		"</nav>";
	}else{
		header.innerHTML = "<header>\r\n" +
				"<div id=\"nav-drawer\">\r\n" +
				"    <input id=\"nav-input\" type=\"checkbox\" class=\"nav-unshown\">\r\n" +
				"    <label id=\"nav-open\" for=\"nav-input\"><span></span></label>\r\n" +
				"    <label class=\"nav-unshown\" id=\"nav-close\" for=\"nav-input\"></label>\r\n" +
				"    <div id=\"nav-content\"><nav>\r\n" +
		"      <ul>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/DefaultViewServlet\">トップページ</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/MonthViewServlet?isInitial=true\">スケジュール</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/TimeCardServlet\">タイムカード</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/ScheduleServlet\">スケジュール登録</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/InformationServlet\">メッセージ登録</a></li>\r\n" +
		"	<li><a href=\"/SVD_IntraNet/LogoutServlet\">ログアウト</a></li>" +
		"      </ul>\r\n" +
		"</nav>" +
		"</div>\r\n" +
				"</div>\r\n" +
				"</header>";
	}
}
