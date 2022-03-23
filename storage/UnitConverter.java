//===================================================================
// UnitConverter.java
//	Description: Handles unit conversions.
//===================================================================

package com.socialvagrancy.utils.storage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class UnitConverter
{
	public static String bytesToHumanReadable(BigInteger bytes)
	{
		BigDecimal val = new BigDecimal(bytes);
		String unit = "B";

		while(val.compareTo(new BigDecimal("1024"))==1)
		{
			val = val.divide(new BigDecimal("1024"));

			switch(unit)
			{
				case "B":
					unit = "KiB";
					break;
				case "KiB":
					unit = "MiB";
					break;
				case "MiB":
					unit = "GiB";
					break;
				case "GiB":
					unit = "TiB";
					break;
				case "TiB":
					unit = "PiB";
					break;
				case "PiB":
					unit = "EiB";
					break;
			}
		}
		
		// Round to 2 decimal places.
		val = val.setScale(2, RoundingMode.HALF_UP);

		return val + " " + unit;
	}

	public static BigInteger humanReadableToBytes(String value)
	{
		String[] input = value.split(" ");
		BigInteger bytes = new BigInteger(input[0]);

		while(!input[1].equals("B"))
		{
			switch(input[1])
			{
				case "EiB":
					input[1] = "PiB";
					bytes.multiply(new BigInteger("1024"));
					break;
				case "PiB":
					input[1] = "TiB";
					bytes.multiply(new BigInteger("1024"));
					break;
				case "TiB":
					input[1] = "GiB";
					bytes.multiply(new BigInteger("1024"));
					break;
				case "GiB":
					input[1] = "MiB";
					bytes.multiply(new BigInteger("1024"));
					break;
				case "MiB":
					input[1] = "KiB";
					bytes.multiply(new BigInteger("1024"));
					break;
				case "KiB":
					input[1] = "B";
					bytes.multiply(new BigInteger("1024"));
					break;
				// Standard Unites
				case "EB":
					input[1] = "PB";
					bytes.multiply(new BigInteger("1000"));
					break;
				case "PB":
					input[1] = "TB";
					bytes.multiply(new BigInteger("1000"));
					break;
				case "TB":
					input[1] = "GB";
					bytes.multiply(new BigInteger("1000"));
					break;
				case "GB":
					input[1] = "MB";
					bytes.multiply(new BigInteger("1000"));
					break;
				case "MB":
					input[1] = "KB";
					bytes.multiply(new BigInteger("1000"));
					break;
				case "KB":
					input[1] = "B";
					bytes.multiply(new BigInteger("1000"));
					break;
				default:
					input[1] = "B";
					bytes.multiply(new BigInteger("0"));
			}
		}

		return bytes;
	}
}
