package ouc.cs.course.java.other;

import java.io.File;  
import java.io.IOException;  
import java.io.RandomAccessFile;  
import java.io.UnsupportedEncodingException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

public class Mp3Info {
    public static void main(String[] args) {  
        // TODO 演示  
 
    }  
  
    private String charset = "utf-8";// 解析MP3信息时用的字符编码  
  
    private byte[] buf;// MP3的标签信息的byte数组  
    
    private File mMp3File ;
  
    /** 
     * 实例化一个获得MP3文件的信息的类 
     *  
     * @param mp3 
     *            MP3文件 
     * @throws IOException 
     *             读取MP3出错或则MP3文件不存在 
     */  
    public Mp3Info(File mp3) throws IOException {  
    	mMp3File = mp3;
  
        buf = new byte[128];// 初始化标签信息的byte数组  
  
        RandomAccessFile raf = new RandomAccessFile(mp3, "r");// 随机读写方式打开MP3文件  
        raf.seek(raf.length() - 128);// 移动到文件MP3末尾  
        raf.read(buf);// 读取标签信息  
  
        raf.close();// 关闭文件  
  
        if (buf.length != 128) {// 数据是否合法  
            throw new IOException("MP3标签信息数据长度不合法!");  
        }  
  
        if (!"TAG".equalsIgnoreCase(new String(buf, 0, 3))) {// 信息格式是否正确  
            throw new IOException("MP3标签信息数据格式不正确!");  
        }  
  
    }  
  
    /** 
     * 获得目前解析时用的字符编码 
     *  
     * @return 目前解析时用的字符编码 
     */  
    public String getCharset() {  
        return charset;  
    }  
  
    /** 
     * 设置解析时用的字符编码 
     *  
     * @param charset 
     *            解析时用的字符编码 
     */  
    public void setCharset(String charset) {  
        this.charset = charset;  
    }  
  
    public String getSongName() {  
        try {  
            return new String(buf, 3, 30, charset).trim();  
        } catch (UnsupportedEncodingException e) {  
            return new String(buf, 3, 30).trim();  
        }  
    }  
  
    public String getArtist() {  
        try {  
            return new String(buf, 33, 30, charset).trim();  
        } catch (UnsupportedEncodingException e) {  
            return new String(buf, 33, 30).trim();  
        }  
    }  
  
    public String getAlbum() {  
        try {  
            return new String(buf, 63, 30, charset).trim();  
        } catch (UnsupportedEncodingException e) {  
            return new String(buf, 63, 30).trim();  
        }  
    }  
  
    public String getYear() {  
        try {  
            return new String(buf, 93, 4, charset).trim();  
        } catch (UnsupportedEncodingException e) {  
            return new String(buf, 93, 4).trim();  
        }  
    }  
  
    public String getComment() {  
        try {  
            return new String(buf, 97, 28, charset).trim();  
        } catch (UnsupportedEncodingException e) {  
            return new String(buf, 97, 28).trim();  
        }  
    }  
    
    public int getDuration() {

        int length = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(mMp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();

            // 单位为秒
            length = audioHeader.getTrackLength();

            return length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }
}
