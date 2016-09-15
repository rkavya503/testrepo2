<html>
	<f:view>
		<head>
			<tiles:insertAttribute name="head" />
		</head>
		<body id="standard">
    		<div id="page_wrapper">
				<div id="header">
					<tiles:insertAttribute name="header" />
				</div>
				<div id="content_wrapper">
					<tiles:insertAttribute name="content" />
				</div>
				<div id="page_footer">
					<tiles:insertAttribute name="footer" />
				</div>
			</div>
		</body>
	</f:view>
</html>
