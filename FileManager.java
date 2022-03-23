//=========================================================================
// FileManager
// 	Description: Handles reads to and writes from the file.
//=========================================================================

package com.socialvagrancy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager
{
	public boolean appendToFile(String path, String nextline)
	{
		String line = nextline + "\n";
		try
		{
			Files.write(Paths.get(path), line.getBytes(), StandardOpenOption.APPEND);
		}
		catch (NoSuchFileException nada)
		{
			System.out.println(nada);
			System.out.println("Creating file " + path);
			// File doesn't exist
			try
			{
				// Write headers and try again.
				Files.write(Paths.get(path), line.getBytes(), StandardOpenOption.CREATE);
			}
			catch(Exception f)
			{
				System.out.println(f.getMessage());
			}

		}
		catch (IOException e)
		{
			e.getMessage();
			e.printStackTrace();
			
			return false;
		}

		return true;
	}

	public boolean createFileDeleteOld(String path, boolean printToShell)
	{
		//===========================================
		// createFile
		// 	This code creates a new file. If the
		// 	file path already exists, it will delete
		// 	the existing file first.
		//===========================================

		// Using the response code to track error messages for this process.
		// code:
		//  -1: File delete failed.
		//  -2: File delete exception.
		//  -3: File create failed.

		int response_code = 0;
		String message = "none";

		// Delete old file.
		try
		{
			
			File existingFile = new File(path);
			if(existingFile.delete() && printToShell)
			{
				message = "File: " + path + " deleted.";
			}
			else if(printToShell)
			{
				response_code = -1;
				message = "Failed to delete " + path;
			}	
		}
		catch(Exception e)
		{
			response_code = -2;
			message = e.getMessage();
		}

		if(printToShell)
		{
			System.out.println(message);
		}

		// Create new file.
		try
		{
			File newFile = new File(path);

			if(newFile.createNewFile())
			{
				response_code = 1;
				message = "File: " + path + " created.";
			}
			else
			{
				response_code = -3;
				message = "Failed to create file: " + path;
			}
		}
		catch(Exception e)
		{
			response_code = -4;
			message = e.getMessage();
		}

		if(printToShell)
		{
			System.out.println(message);
		}

		if(response_code>0)
		{
			// Successfully created file.
			return true;
		}
		else
		{
			// File creation failed.
			return false;
		}

	}

	public String getLastLineOf(String path)
	{
		// Get just the last line of the file.
		String lastLine = "none";

		try
		{
			File inFile = new File(path);

			BufferedReader stdInput = new BufferedReader(new FileReader(inFile));

			String input = null;

			while((input = stdInput.readLine()) != null)
			{
				lastLine = input;
			}

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return lastLine;
	}
}
