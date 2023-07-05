//===================================================================
// Print.java
// 	Description:
// 		Prints the output to the shell. Output must be converted
// 		to the OutputFormat structure for processing.
//===================================================================

package com.socialvagrancy.utils.ui.display;

import com.socialvagrancy.utils.ui.structures.OutputFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class Print
{
	//==============================================
	//	PRINT FUNCTIONS
	//==============================================

	public static void help(String file_name)
	{
		try
		{
			File ifile = new File(file_name);

			BufferedReader br = new BufferedReader(new FileReader(ifile));

			String line = null;

			while((line = br.readLine()) !=null)
			{
				System.out.println(line);
			}

			System.out.print("\n");
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void debug(ArrayList<OutputFormat> output)
	{
		for(int i=0; i<output.size(); i++)
		{
			System.err.println(output.get(i).key + ": " + output.get(i).value);
		}
	}

	public static void line(String header, String value, int indent, boolean includeHeaders)
	{
		// This is the final output, print to shell class.
		
		for(int i=0; i<indent; i++)
		{
			System.out.print("\t");
		}

		if(includeHeaders)
		{
			System.out.print(header + ": ");
		}

		System.out.println(value);
	}

	public static void shell(ArrayList<OutputFormat> output)
	{
		// formats output for standard shell format.

		int indent = 0;
		String current_heading = "none";
		String[] headers;

		for(int i=0; i<output.size(); i++)
		{
			headers = output.get(i).key.split(">");
		
		
			// Print categories.	
			if(output.get(i).value == null)
			{
		
				if(!headers[headers.length-1].equals(current_heading))
				{
					// Current header isn't the same as the reference.
					indent++;
					current_heading = headers[headers.length-1];

					line(headers[headers.length-1], "", indent-1, true);
				}
				else
				{
					indent--;
					// Make sure we're deeper than the root 
					// element of the tree.
					if(headers.length>1)
					{
						current_heading = headers[headers.length-2];
					}
					else
					{
						current_heading = "";
					}
				}
			}
			else
			{
				line(headers[headers.length-1], output.get(i).value, indent, true);
			}
		}
	}
}
