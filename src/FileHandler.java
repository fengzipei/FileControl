import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by fengzipei on 15/10/9.
 */
public class FileHandler {
    public void copy(String sourceFile, String toPath){
        File fromFile = new File(sourceFile);
        File toFile = new File(toPath + fromFile.getName());

        if(!fromFile.exists()){
            System.out.println("Source file doesn't exist!");
        }

        //此处应添加对目的路径的错误判断

        try {
            FileInputStream in = new FileInputStream(fromFile);
            FileOutputStream out = new FileOutputStream(toFile);
            int read = in.read();
            while(read != -1){
                out.write(read);
                read = in.read();
            }
            in.close();
            out.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public void zip(String sourceFile, String toPath){
        File fromFile = new File(sourceFile);
        File zipFile = new File(toPath + fromFile.getName() + ".zip");
        try {
            FileInputStream in = new FileInputStream(fromFile);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            ZipEntry entry = new ZipEntry(fromFile.getName());
            out.putNextEntry(entry);
            int read = 0;
            while(read != -1){
                out.write(read);
                read = in.read();
            }
            in.close();
            out.close();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public void unzip(String zipFile, String toPath) throws IOException {
        ZipFile zipfile = new ZipFile(zipFile);
        Enumeration<?> enu = zipfile.entries();
        while(enu.hasMoreElements()){
            ZipEntry entry = (ZipEntry)enu.nextElement();
            System.out.println(entry.getName());
            File file = new File(toPath + File.separator + entry.getName());
            if(entry.isDirectory()){
                file.mkdirs();
            }
            File parentDirectory = file.getParentFile();
            if(parentDirectory != null){
                parentDirectory.mkdirs();
            }
            InputStream in = zipfile.getInputStream(entry);
            FileOutputStream out = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length;
            while((length = in.read(bytes)) >= 0){
                out.write(bytes, 0, length);
            }
            in.close();
            out.close();
        }
        zipfile.close();
    }


    public void encrypt(String sourceFile, String toPath){

    }

    public void uncrypt(String sourceFile, String toPath){

    }
    //此处应添加将多个文件压缩的函数
    public static void main(String[] args) throws IOException{
        FileHandler fileHandler = new FileHandler();
        //fileHandler.copy("./Java.iml", "./directory/");
        //System.out.println(File.separator);
        //fileHandler.zip("/Users/fengzipei/Desktop/作息时间.pdf", "/Users/fengzipei/Desktop/");
        //fileHandler.unzip("/Users/fengzipei/Desktop/作息时间.pdf.zip", "/Users/fengzipei/Desktop/test/");
    }
}
