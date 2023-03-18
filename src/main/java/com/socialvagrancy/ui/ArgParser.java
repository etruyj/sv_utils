//===================================================================
// ArgParser.java
// 	Description:
// 		Handles argument parsing for CLI tasks. The parser
// 		can load command abbreviations from a help file to 
// 		simplify upkeep for the 
//===================================================================

package com.socialvagrancy.utils.ui;

import com.socialvagrancy.utils.io.FileManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgParser
{
	private HashMap<String, String> arg_map;
	private HashMap<String, String> short_cut_map;

	public ArgParser()
	{
		arg_map = new HashMap<String, String>();
		short_cut_map = new HashMap<String, String>();
	}

	public ArgParser(String help_file, String config_file)
	{
		arg_map = new HashMap<String, String>();
		short_cut_map = new HashMap<String, String>();
	
		buildShortCutMapFromHelpFile(help_file);
		buildFromConfig(config_file);
	}
	
	public void buildFromConfig(String file_path)
	{
		String[] file_parts;
		String key = null;
		String value = null;

		try
		{
			ArrayList<String> config_file = FileManager.readFileIntoArray(file_path);
			
			for(int i=0; i<config_file.size(); i++)
			{
				if(!config_file.get(i).substring(0, 1).equals("#"))
				{
					file_parts = config_file.get(i).split(":");

					if(file_parts.length == 2)
					{
						key = file_parts[0].trim();
						value = file_parts[1].trim();

						arg_map.put(key, value);
					}
				}
			}

		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	public void buildShortCutMapFromHelpFile(String file_path)
	{
		String[] file_parts;
		String key = null; // Set to null for parsing.
		String value = null;

		try
		{
			ArrayList<String> help_file = FileManager.readFileIntoArray(file_path);

			for(int i=0; i<help_file.size(); i++)
			{
				// Filter out for commands.
				if(help_file.get(i).contains("--"))
				{
					// Remove initial tab if present.
					if(help_file.get(i).substring(0,1).equals("\t"))
					{
						help_file.set(i, help_file.get(i).substring(1, help_file.get(i).length()));
					}

					// Remove command description
					file_parts = help_file.get(i).split("\t");

					// Split command and short cuts.
					file_parts = file_parts[0].split(", ");

					if(file_parts.length > 1) // there is a short-cut for the command.
					{
						// Parse for key-values
						// If it contains -- it's the primary command
						// If there are two values with -- starting the flag,
						// The first value with -- will be also designated the primary
						// command, e.g. --insecure, --http
						if(file_parts[0].contains("--"))
						{
							value = file_parts[0].substring(2, file_parts[0].length());
						}
						else
						{
							key = file_parts[0];
						}

						// Set the other parameter based on which place we put the first.
						if(key != null)
						{
							value = file_parts[1].substring(2, file_parts[1].length());
						}
						else
						{
							key = file_parts[1];
						}

						short_cut_map.put(key, value);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	public boolean getBoolean(String value) throws Exception
	{
		// If the key is in the map - true
		// if the key is not in the map - false
		// if the key is int he map, but there is a value - error

		if(arg_map.get(value) == null)
		{
			return false;
		}
		
		if(arg_map.get(value).equals(""))
		{
			return true;
		}
		
		throw new Exception("Unable to parse value " + arg_map.get(value));
	}

	public String get(String value) throws Exception
	{
		if(arg_map.get(value) == null)
		{
			throw new Exception("Required field [" + value + "] was not set. Please set with --" + value + " when executing the script.");
		}
		else if(arg_map.get(value).equals(""))
		{
			throw new Exception("Incorrect value selected for [" + value + "].");
		}
		else
		{
			return arg_map.get(value);
		}
	}

	public void parse(String[] args)
	{
		String flag;
		String value;

		for(int i=0; i<args.length; i++)
		{
			flag = null;
			value = "";

			// Check to see if this is the full flag name.
			// If only a "-" is used, an abbreviated flag
			// has been used.
			if(args[i].substring(0, 1).equals("-"))
			{
				if(args[i].length()==2)
				{
					flag = short_cut_map.get(args[i]);
				}
				else
				{
					flag = args[i].substring(2, args[i].length());
				}
			}

			// Check to see if the next index is a flag.
			// if not, store string as values.
			// a while loop instead of an if statement allows
			// parsing options with white space.
			while(((i+1) < args.length) && (!args[i+1].substring(0, 1).equals("-")))
			{
				// Increment i first to save typing.
				// i needs to be incremented to skip over the
				// value during flag parsing as well.
				i++;
				value += args[i];
			}
			
			if(flag != null)
			{
				arg_map.put(flag, value);
			}	
		}
	}
}
