import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;
import java.util.Scanner;
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

    public void encrypt(String sourceFile, String toPath) throws FileNotFoundException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        try {
            System.out.print("Please input the key:");
            Scanner input = new Scanner(System.in);
            String key = input.next();
            if(key.length() < 8){
                System.out.println("Key's length must longer than 8");

            }
            File file = new File(sourceFile);
            FileInputStream in = new FileInputStream(sourceFile);
            FileOutputStream out = new FileOutputStream(toPath + File.separator + file.getName() + ".encrypted");
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey desKey = skf.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cin = new CipherInputStream(in, cipher);
            byte[] bytes = new byte[1024];
            int length;
            while((length = cin.read(bytes)) >= 0){
                out.write(bytes, 0, length);
            }
            out.flush();
            out.close();
            cin.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decrypt(String sourceFile, String toPath){
        try {
            System.out.print("Please input the key:");
            Scanner input = new Scanner(System.in);
            String key = input.next();
            if(key.length() < 8){
                System.out.println("Key's length must longer than 8");

            }
            File file = new File(sourceFile);
            FileInputStream in = new FileInputStream(sourceFile);
            FileOutputStream out = new FileOutputStream(toPath + File.separator + "decrypted_" + file.getName().substring(0, file.getName().length() - 10));
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey desKey = skf.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cout = new CipherOutputStream(out, cipher);
            byte[] bytes = new byte[1024];
            int length;
            while((length = in.read(bytes)) >= 0){
                cout.write(bytes, 0, length);
            }
            cout.flush();
            cout.close();
            in.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        FileHandler fileHandler = new FileHandler();
        //fileHandler.copy("./Java.iml", "./directory/");
        //System.out.println(File.separator);
        //fileHandler.zip("/Users/fengzipei/Desktop/作息时间.pdf", "/Users/fengzipei/Desktop/");
        //fileHandler.unzip("/Users/fengzipei/Desktop/作息时间.pdf.zip", "/Users/fengzipei/Desktop/test/");
    }
}
