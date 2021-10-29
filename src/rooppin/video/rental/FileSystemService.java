package rooppin.video.rental;

import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

/**
 * Static class for all file system operations
 * @author VADIM&ORI&MATAN
 *
 */
class FileSystemService {

	/**
	 * Save film poster image in local storage
	 * @param image
	 * @param name
	 * @return
	 */
	static boolean saveImage(BufferedImage image,String name)
	{
		final String dir ="G:/Posters";
		File f = new File(dir + "/" + name + ".jpg");
		if(f.exists()) return true;
		try {
			ImageIO.write(image, "jpg", f);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	 }

	/**
	 * Gets poster image from local storage if exists
	 * @param fileName
	 * @return
	 */
	static BufferedImage getImageFromFile(String fileName) {
		final String dir ="G:/Posters";
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(dir + "/" + fileName + ".jpg"));
		    return img;
		} catch (IOException e) {}
		return null;
	}
	
	/**
	 * Make 32 bit array from ciclic xor user email and password
	 * @param s1
	 * @param s2
	 * @return
	 */
	static byte[] encode(String s1, String s2) {
		if(s1.length()==s2.length()) s1=s1 + "w";
		int length = 32, idx=0;
		byte[] a = s1.getBytes();
		byte[] b = s2.getBytes();
		byte[] str = new byte[length];
	   while(idx<length) {
		   str[idx]=(byte) (a[idx%a.length]^b[(idx++)%b.length]);
	   }
	   return str;
	}
	
	/**
	 * Find given string position in passwords file if exists
	 * @param input
	 * @return
	 */
	static long getPassFromFile(byte[] input) {
		File f = new File("pass");
		long position;
		String s = new String(input);
		if(!f.exists()) return -1;
		try(RandomAccessFile raf = new RandomAccessFile(f, "r");) {	
			long len = raf.length();
			byte[] buffer = new byte[32];
			do{
				position = raf.getFilePointer();
				raf.read(buffer, 0, 32);
				if(s.equals(new String(buffer))) return position;
			}while(position+buffer.length<len);
		} catch (EOFException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Store encoded string in passwords file
	 * 
	 * @param encodedPass
	 * @param place
	 */
	static void storePassToFile(byte[] encodedPass, long place) {
		
		try(RandomAccessFile raf = new RandomAccessFile(new File("pass"), "rw");) {
			if(place<0) place = raf.length();
			raf.seek(place);
			raf.write(encodedPass);
			System.out.println(place);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change existing password encoded string
	 * @param s
	 * @return
	 */
	static boolean updatePasswordInFile(String[]s) {
		long place = getPassFromFile(encode(s[0],s[1]));
		if(place!=-1) {
			storePassToFile(encode(s[0],s[2]),place);
			return true;
		}
		return false;
	}
	
	/**
	 * Update existing encoded string on email change
	 * @param oldEmail
	 * @param newEmail
	 * @param pass
	 * @return
	 */
	static boolean updateEmailInFile(String oldEmail, String newEmail, String pass) {
		long place = getPassFromFile(encode(oldEmail,pass));
		if(place!=-1) {
			storePassToFile(encode(newEmail,pass),place);
			return true;
		}
		return false;
	}
}
