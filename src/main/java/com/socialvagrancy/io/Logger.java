//===================================================================
// Logger.java
// 	This code handles logging of API calls and managing the log
// 	files. 
//
// Author: etruyj
//
// 	Log Levels:
// 		- NONE (0) 	- No logs are generated.
// 		- INFO (1) 	- All Activity
// 		- API-ONLY (2) 	- All API calls, all API results, all errors.
// 		- ERROR () 	- Only API calls that result in an error.
//===================================================================

package com.socialvagrancy.utils.io;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Logger
{
	// Vars
	private String file_path; // path to the log directory;
	private String file_name; // name of the log file.
	private int max_size; // maximum size on open before a new file is create.
	private int file_count; // the number of log files to keep.
	private int log_level;

	public Logger(String path, int size, int count, int level)
	{
		file_path = path;
		max_size = size;
		file_count = count;
		log_level = level;
	}

	//=================================================
	// Log calls to streamline the logging command.
	// DEBUG - 1
	// INFO - 2
	// WARN - 3
	// ERR - 4
	//=================================================

	// Version 1.7 logs
	public void debug(String message)
	{
		logWithSizedLogRotation("DEBUG: " + message, 1);
	}

	public void info(String message)
	{
		logWithSizedLogRotation("INFO: " + message, 2);
	}

	public void warn(String message)
	{
		logWithSizedLogRotation("WARN: " + message, 3);
	}

	public void error(String message)
	{
		logWithSizedLogRotation("ERR: " + message, 4);
	}

	// Version 1.0 logs
	public void INFO(String message)
	{
		logWithSizedLogRotation("INFO: " + message, 2);
	}

	public void WARN(String message)
	{
		logWithSizedLogRotation("WARN: " + message, 3);
	}

	public void ERR(String message)
	{
		logWithSizedLogRotation("ERR: " + message, 4);
	}

	public void checkLogs()
	{
		// Checks the file size of the log
		// file and increments down the list.
		
		try
		{
			// Check to see if the file path was 
			if(Files.size(Paths.get(file_path))>max_size)
			{
				rotateLogs();	
			}
		}
		catch(IOException e)
		{
			writeLog("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void log(String log, int msg_level)
	{
		if(log_level > 0 && msg_level>=log_level)
		{
			writeLog(log);
		}
	}

	public void logWithDatedLogRotation(String log, int msg_level)
	{
		// Checks the date of the last log BEFORE writing a line
		// if the new date doesn't match the last date, logs
		// are rotated before information is logged.
	}

	public void logWithSizedLogRotation(String log, int msg_level)
	{
		// Checks the size of the log after each line is written.
		// This allows for dynamic rolling of logs.

		if(log_level > 0 && msg_level>=log_level)
		{
			writeLog(log);
		}

		// See if the log is over the specified size
		// and if so, rotate.
		checkLogs(); 
	}

	public void rotateLogs()
	{
		// Log Rotation.
		// This script rotates the logs. Log rotation increments
		// the marker on each of the logs so older logs are saved
		// until the specified number of log files.
		
		try
		{
			String source = file_path;
	

			// Delete the max file first as move fails instead of overwriting.
			if(Files.exists(Paths.get(source + "." + file_count)))
			{
				Files.delete(Paths.get(source + "." + file_count));
			}

			// Rotate backwards, so the old file is copied before the new
			// is overwrites it.
			for(int i=file_count-1; i>0; i--)
			{
				if(Files.exists(Paths.get(source + "." + i)))
				{
					Files.move(Paths.get(source + "." + i), Paths.get(source + "." + (i+1)));
		
					if(log_level==1)
					{
						writeLog("Moving " + source + "." + i + " to destination " + source + "." + (i+1));
					}
				}
			}
	
			// Increment the current log
			if(file_count>1)
			{
				Files.move(Paths.get(source), Paths.get(source + ".1"));
			
				// Delete the old file to make way for the new.
				Files.delete(Paths.get(source));

				if(log_level==1)
				{
					writeLog("Moving " + source + " to " + source + ".1");
					writeLog("Deleting " + source);
				}
			}
			else
			{
				Files.delete(Paths.get(source));

				if(log_level==1)
				{
					writeLog("Deleting " + source);
				}
			}
		}
		catch(Exception e)
		{
			writeLog("Failed to rotate logs.");
			writeLog(e.getMessage());
		}
	}

	public void writeLog(String log)
	{
		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		String logline = dateformat.format(now) + " :: " + log + "\n";

		try
		{
			Files.write(Paths.get(file_path), logline.getBytes(), StandardOpenOption.APPEND);	
		}
		catch(NoSuchFileException nada)
		{
			logline = dateformat.format(now) + " :: " + "Creating log file...\n";
			try
			{
				Files.write(Paths.get(file_path), logline.getBytes(), StandardOpenOption.CREATE);
				writeLog(log);
			}
			catch(IOException n)
			{
				System.out.println(n.getMessage());
			}
		}
		catch(IOException e)
		{
			writeLog(e.getMessage());
		}
	}	
}
