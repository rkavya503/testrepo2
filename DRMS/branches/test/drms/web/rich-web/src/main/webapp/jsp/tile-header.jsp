<h1>
	Akuacom DRAS Facility Dashboard
</h1>

<ul id="nav">
	<c:choose>
		<c:when test="${header.status == true}">
			<li id="current">
				<a href="/pss2.website/uoProgram.do">true</a>		
			</li>
		</c:when>
		<c:otherwise>
			<li>
				<a href="/pss2.website/uoProgram.do">false</a>		
			</li>			
		</c:otherwise>
	</c:choose>
	<li>
		<a href="/pss2.website/uoProgram.do">Programs</a>		
	</li>

	<li>
		<a href="/pss2.website/uoProgram.do">Programs</a>		
	</li>

	<li>
		<a href="/pss2.website/uoProgram.do">Programs</a>		
	</li>

	<li>
		<a href="/pss2.website/uoProgram.do">Programs</a>		
	</li>
	
	<li>
		<a href="/pss2.website/uoProgram.do">Programs</a>		
	</li>

	<li>
		<a href="/pss2.website/uoProgram.do">Programs</a>		
	</li>
</ul>

<ul id="navright">
	<li>
		<a href="#" title="Logout of the DRAS Automation Server">Logout</a>
	</li>
	<li>
		<a href="#" title="Click to edit your settings">Operator Name</a>
	</li>
</ul>
