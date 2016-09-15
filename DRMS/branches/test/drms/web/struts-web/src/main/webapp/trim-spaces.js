function trimSpaces() {
			var jUser = document.getElementsByName("j_username");
			var jPass = document.getElementsByName("j_password");
			jUser[0].value = (jUser[0].value).replace(/^\s+|\s+$/g,'');
			jPass[0].value = (jPass[0].value).replace(/^\s+|\s+$/g,'');
        } 