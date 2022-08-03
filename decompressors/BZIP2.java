//==================================================================
// BZIP2.java
// 	Description:
// 		Decompresses the .bz log files and saves the 
// 		rehydrated file with the same extension minus the
// 		.bz extension
//==================================================================

package com.socialvagrancy.blackpearl.logs.utils.decompressors;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import com.socialvagrancy.utils.Logger;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class BZIP2
{
	public static boolean decompress(String filepath, Logger log)
	{
		System.err.println(filepath.substring(filepath.length()-3, filepath.length()));

		if(filepath.substring(filepath.length()-3, filepath.length()).equals("bz2"))
		{
			try
			{
				int buffersize = 5024;
				InputStream fin = Files.newInputStream(Paths.get(filepath));
				BufferedInputStream in = new BufferedInputStream(fin);
				OutputStream out = Files.newOutputStream(Paths.get(filepath.substring(0, filepath.length()-4)));
				BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);

				final byte[] buffer = new byte[buffersize];
				int n =0;

				while(-1 != (n = bzIn.read(buffer)))
				{
					out.write(buffer, 0, n);
			
				}
				
				out.close();
				bzIn.close();
				
				log.INFO("File [" + filepath + "] decompressed successfully.");
				return true;
			}
			catch(IOException e)
			{
				log.WARN(e.getMessage());
				return false;
			}
		}
		else
		{
			log.WARN("File [" + filepath + "] is not a bzip2 file. Unable to decompress.");
			return false;
		}
	}

	public static void main(String[] args)
	{
		decompress(args[0], null);
	}
}
