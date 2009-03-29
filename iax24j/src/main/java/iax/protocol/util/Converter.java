package iax.protocol.util;


/**
 * Utility class to convert from bytes to HexString and vice versa.
 */
public class Converter {

	/**
	 * Converts the given byte array to a hexadecimal string
	 * @param in The byte array to be converted
	 * @return The returned hexadecimal string
	 */
	public static String byteArrayToHexString( byte in[] ) {

		byte ch = 0x00;
		int i = 0;

		if( in == null || in.length <= 0 ) {
			return null;
		}
		
		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer( in.length * 2 );

		while( i < in.length )	{
			ch = (byte)(in[i] & 0xF0);
			ch = (byte)(ch >>> 4);
			ch = (byte)(ch & 0x0F);
			out.append( pseudo[(int)ch] );
			ch = (byte)(in[i] & 0x0F);
			out.append( pseudo[(int)ch] );
			i++;
		}

		return new String( out );
	}

	/**
	 * Converts the given hexadecimal string to a byte array
	 * 
	 * @param str The string to be converted
	 * @return The returned byte array
	 */
	public static byte[] hexStringToByteArray( String str )	{
		String strHex = "";
		int iValue = 0;
		int logByteIndex = 0;
		byte bytes[] = new byte[str.length() / 2];
		while( str.length() != 0 )	{
			strHex = str.substring( 0, 2 );
			str = str.substring( 2 );
			iValue = Integer.parseInt(strHex, 16);
			if( iValue > 0x7f )	{
				iValue = (iValue - 1) ^ 0xff;
				iValue = -iValue;
			}
			bytes[logByteIndex] = Byte.parseByte( iValue + "" );
			logByteIndex++;
		}
		return bytes;
	}
}
