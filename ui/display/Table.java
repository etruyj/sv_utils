//===================================================================
// Table.java
// 	Description:
// 		Outputs an ArrayList<OutputFormat> in either CSV or
// 		table format by converting the variable to a table format.
// 		Failed conversions are output to the shell.
//===================================================================

package com.socialvagrancy.utils.ui.display;

import com.socialvagrancy.utils.ui.structures.OutputFormat;

import java.util.ArrayList;

public class Table
{
	public static void format(ArrayList<OutputFormat> output, String output_format)
	{
		int line = 0;
		int columns = 0;
		int i = 0;
	//	int indent = 0; commented out and as part of the original script.
		boolean building_table = true;
		boolean build_successful = true;
		String line_start = "";

		String current_heading = "none";
		String[] headers;

		ArrayList<String> headings = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();

		// Build the table;
		while(building_table)
		{	
			headers = output.get(i).key.split(">");
			
			// Store the first key to mark the start of the line
			// When that key is hit again, we'll start a new table
			// row.
			if(i == 0)
			{
				line_start = output.get(i).key;
			}
			else if(output.get(i).key.equals(line_start))
			{
				// Increment to the next line.
				// If this is the first time the line is incremented
				// store the number of columns to add to the table.
				if(line==0)
				{
					columns = i;
				}

				line++;
			}
			
			// Store the keys as column headings
			// Allowing for tiered data and document formats such
			// as XML, blank headings will need to be ignored.
			if(line == 0)
			{
				headings.add(headers[headers.length-1]);
			}

			values.add(output.get(i).value);

			i++;

			if(i >= output.size())
			{
				building_table=false;

				// Catch single line inputs.
				if(line == 0)
				{
					columns = headings.size();
				}
			}
		}

		if(build_successful)
		{
			// headings.subList(columns, headings.size()).clear(); // mark for deletion

			if(output_format.equals("table"))
			{
				printTable(headings, values, columns);
			}
			else if(output_format.equalsIgnoreCase("csv"))
			{
				printCSV(headings, values, columns);
			}
		}
		else
		{
			// Print in shell format if table doesn't fit.
			//Print.shell(output);
			System.err.println("ERROR: Unable to format table for display.");
		}

	}

	//=======================================
	// Private Functions
	//=======================================

	private static ArrayList<Integer> calculateColumnWidths(ArrayList<String> headers, ArrayList<String> values)
	{
		ArrayList<Integer> column_widths = new ArrayList<Integer>();
		int itr = 0;

		int column_count = headers.size();


		// Get initial values
		for(int i=0; i<column_count; i++)
		{
			column_widths.add(headers.get(i).length());
		}

		// Check value width
		for(int i=0; i<values.size(); i++)
		{
			if(column_widths.get(itr)<values.get(i).length())
			{
				column_widths.set(itr, values.get(i).length());
			}

			itr++;
			
			// Reset when we get to the end of the table.
			if(itr==column_count)
			{
				itr = 0;
			}
		}


		return column_widths;
	}

	private static void printCSV(ArrayList<String> headers, ArrayList<String> values, int columns)
	{
		int column = 0;
		
		// Print headers
		for(int i = 0; i < columns; i++)
		{
			System.out.print(headers.get(i));
			
			if(i<columns-1)
			{
				System.out.print(",");
			}
		}
		System.out.print("\n");
		
		// Print Values
		for(int i = 0; i < values.size(); i++)
		{
			System.out.print(values.get(i));
			column++;

			if(column == columns)
			{
				System.out.print("\n");
				column = 0;
			}
			else
			{
				System.out.print(",");
			}
		}
	}
	private static void printDeliminator(int columns, ArrayList<Integer> column_width, int margin)
	{
		// Iterate through the columns
		for(int i=0; i<column_width.size(); i++)
		{
			for(int j=0; j<(column_width.get(i) + (2*margin)); j++)
			{
				if(j==0)
				{
					System.out.print("+");
				}
				else
				{
					System.out.print("-");
				}
			}
		}

		// Line end
		System.out.println("+");

/*		//Print top
		for(int i = 0; i<(column_width*columns); i++)
		{
			if(i%column_width==0)
			{
				System.out.print("+");
			}
			else
			{
				System.out.print("-");
			}
		}
		System.out.println("+");
*/
	}

	private static void printTable(ArrayList<String> headers, ArrayList<String> values, int columns)
	{
		int column = 0;
		//int column_width = 30;
		int margin = 2;
		ArrayList<Integer> column_widths = calculateColumnWidths(headers, values);

		// Print Top Bar
		printDeliminator(columns, column_widths, margin);

		// Print headers
		for(int i = 0; i < columns; i++)
		{
			int padding = (column_widths.get(i) + (2 * margin)) - (margin + headers.get(i).length() + 1); // Extra space is column size minus consumed.
			System.out.print("|");
		
			for(int j=0; j<margin; j++)
			{
				System.out.print(" ");
			}

			System.out.print(headers.get(i));

			for(int k=0; k<padding; k++)
			{
				System.out.print(" ");
			}
		}
		System.out.println("|");
		
		// Print Bar
		printDeliminator(columns, column_widths, margin);
		
		// Print Values
		for(int i = 0; i < values.size(); i++)
		{
			int padding = (column_widths.get(column) + (2 * margin)) - (margin + values.get(i).length() + 1);
			System.out.print("|");
		
			for(int j=0; j<margin; j++)
			{
				System.out.print(" ");
			}

			System.out.print(values.get(i));

			for(int k=0; k<padding; k++)
			{
				System.out.print(" ");
			}

			column++;

			if(column == columns)
			{
				System.out.println("|");

				column = 0;
				// Print Deliminator
				printDeliminator(columns, column_widths, margin);
			}
		
		}
	}
}
