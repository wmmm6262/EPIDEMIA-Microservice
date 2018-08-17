package com.example.demo.envDataTsf;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.io.BufferedReader;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.springframework.stereotype.Component;

@Component
public class EnvDataTsfService {
	
	public String retrieve() {		
		return transfer();
	}
	
	public String transfer() {
		try {
            /*
             * Create a new Jsch object This object will execute shell
             * commands or scripts on server
             */
            JSch jsch = new JSch();

            /*
             * Open a new session, with your username, host and port
             * Set the password and call connect.
             * session.connect() opens a new connection to remote SSH server.
             * Once the connection is established, you can initiate a new channel.
             * this channel is needed to connect to remotely execution program
             */
            jsch.setKnownHosts(System.getProperty("user.dir")+"\\known_hosts");
            //CHANGE TO YOUR HOST ADDRESS, USERNAME, PASSWORD, AND PORT
            String host = "your_remote_address"; // remote host address
            String user = "your_username";
            String password = "your_password";
            int port = 0000000; // your port
           
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");
            session.connect();
            
            String mode = "--dev ";
            String file = "/data/epidemia/dev/modeling/EPIDEMIA_Automation.sh";
            //create the excution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
            InputStream in = channelExec.getInputStream();
            // Set the command that you want to execute
            // In our case its the remote shell script
            channelExec.setCommand("sh " + file + " " + mode);
            // Execute the command
            channelExec.connect();
            // Read the output from the input stream we set above
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            String output = "";

            //Read each line from the buffered reader and add it to result list
            // You can also simple print the result here 
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                output += line + "\n";
            }

            //retrieve the exit status of the remote command corresponding to this channel
            //int exitStatus = channelExec.getExitStatus();
            //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
            channelExec.disconnect();
            session.disconnect();
            System.out.println("Environmental data tranferring terminated.");
            output += "Environmental data tranferring terminated.";
            try
            {
    	    	Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + ".\\envDataTsf.lock"));
                System.out.println("envDataTsf.lock has been deleted");
            }
            catch(NoSuchFileException e)
            {
                System.out.println("No such file/directory exists");
            }
            
            return output;

        } catch (Exception e) {
            System.err.println("Error: " + e);
            return "Error" + e;
        }
	}	
}
