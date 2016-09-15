package com.akuacom.jbk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JbossKiller {

	public static void main(String[] args) throws Exception {
		new JbossKiller();
	}

	public JbossKiller() throws IOException, InterruptedException {
		// linux, unix, or mac os x system.
		List<String> commands = new ArrayList<String>();
		
	
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("ps -eax | grep 'run.sh'");

		SystemCommandExecutor commandExecutor = new SystemCommandExecutor(
				commands);
		int result = commandExecutor.executeCommand();

		StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
		StringBuilder procIds = new StringBuilder();
		StringReader sr = new StringReader(stdout.toString());
		BufferedReader br = new BufferedReader(sr);
		
		String line;
		while( (line = br.readLine()) != null) {
			line = line.trim();
			if(line.indexOf("grep") != -1) {
				continue;
			}
			String[] toks = line.split(" ");
			String procId =toks[0]; 
			procIds.append(procId);
			procIds.append(" ");
		}
		File bin = new File("./bin");
		File killem = null;
		if(bin.exists()) {
		    killem = new File( "./bin/killem");
		}
        if(killem == null || !killem.exists()) {
            killem = new File("./jbk/bin/killem");
            if(!killem.exists()) {
                System.out.println("couldn't find killem executable in ./bin or ./jbk/bin");
                System.exit(-1);
            }
        }
		String command = killem.getAbsolutePath() + " " + procIds.toString();
		System.out.println("exec-ing " + command);
		Runtime.getRuntime().exec(command);

	}
}
