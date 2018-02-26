/**
 * @email: zhujiawen@baidu.com
 */
package com.nais.kid.keeper.helper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhujaiwen
 *
 */
public class IOHelper {
	
	private static Log logger = LogFactory.getLog(IOHelper.class);

	/**
	 * 创建目录
	 * @param dest
	 */
	public static void makeDirs(File dest) {
		String path = dest.getPath();
		makeDirs(path);
	}
	
	/**
	 * 创建目录
	 * @param path dest
	 */
	public static void makeDirs(String path) {
		try {
			path = path.replace("\\", "/");
			int i = path.lastIndexOf("/");
			path = path.substring(0, i);
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getSuffix(File file){
		
		if(file == null || !file.isFile() || !file.exists()){
			return null;
		}
		String path = file.getPath();
		path = path.replace("\\", "/");
		int i = path.lastIndexOf(".");
		return path.substring(i + 1, path.length());
	}
	
	/**
	 * 产生当前时刻的"yyyyMMddHHmmssSSS"格式字符串
	 * @return
	 */
	public static String generateFileName() {
		int random = (int)(Math.random() * 1000000);
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(new Date()) + "-" + random;
	}
	
	/**
	 * 文件拷贝功能
	 * @param src 源文件
	 * @param dest 目标文件
	 */
	public static void copy(File src, File dest) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte[] buff = new byte[8192];
		makeDirs(dest);
		try {
			bis = new BufferedInputStream(new FileInputStream(src));
			bos = new BufferedOutputStream(new FileOutputStream(dest));

			for (int count = 0; (count = bis.read(buff)) != -1;) {
				bos.write(buff, 0, count);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 以行为单位读取本地文件，常用于读面向行的格式化文件
	 * @param file 所要读取的文件
	 * @param encoding 文件编码
	 */
	@SuppressWarnings("unchecked")
	public static String readFileByLines(File file, String encoding, boolean newline) {
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		try {
			//获取编码
			is = new FileInputStream(file);
			byte[] head = new byte[3];
			is.read(head);
			if(encoding == null) {
				encoding = "GBK";
				if (head[0] == -1 && head[1] == -2){
					encoding = "UTF-16";
				}
				if (head[0] == -2 && head[1] == -1){
					encoding = "Unicode";
				}
				if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
					encoding = "UTF-8";
				}
			}
			List<String> lines = FileUtils.readLines(file, encoding);
			for (String str : lines) {
				sb.append(str);
				if(newline) {
					sb.append("\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 向文本文件中追加内容
	 * @param file
	 * @param text
	 */
	public static void appendTextToFile(File file, String text) {
		FileWriter writer = null;
		try {
			if(!file.exists()){
				file = createFile(file.getAbsolutePath(), text, "UTF-8");
			} else {
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				writer = new FileWriter(file, true);
				writer.write(text);
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception { 
		File targetFile = new File(filePath);  
        if(!targetFile.exists()){    
            targetFile.mkdirs();    
        }       
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
	
	/**
	 * 创建一个文件
	 * @param path 路径
	 * @param content 内容
	 * @param encoding 编码
	 * @return
	 */
	public static File createFile(String path,String content, String encoding) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos, true, encoding);
			if(content != null && !"".equals(content)){
				ps.print(content);
			}
			ps.flush();
			ps.close();
			fos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 根据数据库二进制文件信息生成本地文件并保存
	 * @param blob
	 * @param file
	 */
	public static void saveFileFromBlob(Blob blob, File file) {
		try {
			InputStream is = blob.getBinaryStream();
			makeDirs(file);
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除文件
	 * @param fileName
	 * @return
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName); 
		if(!file.exists()){
			logger.info("删除文件失败：" + fileName + "文件不存在"); 
			return false; 
		}else{ 
			if(file.isFile()){ 
				return deleteFile(fileName); 
			}else{ 
				return deleteDirectory(fileName); 
			} 
		} 
	}

	/**
	 * 删除单个文件
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			logger.info("删除单个文件" + fileName + "成功！");
			return true;
		} else {
			logger.info("删除单个文件" + fileName + "失败！");
			return false;
		}
	}
	
	/**
	 * 删除目录
	 * @param dir
	 * @return
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			logger.info("删除目录失败" + dir + "目录不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			} else { // 删除子目录
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		if (!flag) {
			logger.info("删除目录失败");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			logger.info("删除目录" + dir + "成功！");
			return true;
		} else {
			logger.info("删除目录" + dir + "失败！");
			return false;
		}
	}
	
	/**
	 * 获取文件的MD5值
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	/**
	 * 获取byte的MD5值
	 * @param file
	 * @return
	 */
	public static String getByteMD5(byte[] buffer) {
		if (buffer == null) {
			return null;
		}
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	
}
