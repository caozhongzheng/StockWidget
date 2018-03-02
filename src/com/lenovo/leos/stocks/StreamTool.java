package com.lenovo.leos.stocks;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {

	public static byte[] readStream(InputStream inputStream) throws Exception{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0 ;
		while((len = inputStream.read(buffer)) != -1){
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}
}
