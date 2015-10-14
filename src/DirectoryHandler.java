import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by fengzipei on 15/10/9.
 */
public class DirectoryHandler {
    private File currentDirectory;

    public DirectoryHandler(){
        this.currentDirectory = new File(System.getProperty("user.dir"));
    }

    public String getCurrentDirectory(){
        return currentDirectory.getAbsolutePath();
    }

    public boolean copyFolder(String directoryName, String toPath) throws IOException{
        File fromDirectory = new File(directoryName);
        File toDirectory = new File(toPath);

        if(!fromDirectory.exists()){
            System.out.println("Source directory is not exists! " + fromDirectory.getAbsolutePath());
            return false;
        }
        if(!toDirectory.exists()){
            System.out.println("Destination directory is not exists! " + toDirectory.getAbsolutePath());
            return false;
        }
        if(fromDirectory.isFile()){
            System.out.println("Source directory is not a valid path! " + fromDirectory.getAbsolutePath());
            return false;
        }
        if(toDirectory.isFile()){
            System.out.println("Destination directory is not a valid path! " + toDirectory.getAbsolutePath());
            return false;
        }
        if(fromDirectory.isDirectory() && toDirectory.isDirectory()){
            makeDirectory(toPath + File.separator + fromDirectory.getName());
            File[] fileList = fromDirectory.listFiles();
            for(File file : fileList){
                if(file.isFile()){
                    FileHandler fileHandler = new FileHandler();
                    fileHandler.copy(file.getAbsolutePath(), toPath + File.separator + fromDirectory.getName() + File.separator);
                }
                if(file.isDirectory()){
                    copyFolder(file.getAbsolutePath(), toPath + File.separator + fromDirectory.getName());
                }
            }
        }
        return true;
    }

    public boolean makeDirectory(String directoryName) throws IOException{
        File newDirectory = new File(directoryName);
        if(!newDirectory.exists()) {
            if (newDirectory.mkdirs()) {
                System.out.println("Create directory " + newDirectory.getAbsolutePath() + " successfully!");
                return true;
            }
            else {
                System.out.println("Failed to create directory!");
                return false;
            }
        }
        else{
            System.out.println("Failed to create directory! (The directory has already exists or No root authority)");
            return false;
        }
    }

    public void deleteDirectory(String directoryName) throws IOException{
        File directory = new File(directoryName);
        if(!directory.exists()){
            System.out.println("Directory doesn't exist!");
            return;
        }
        if(directory.isFile()){
            directory.delete();
        }
        else if(directory.isDirectory()){
            File[] fileList = directory.listFiles();
            for(File file : fileList) {
                deleteDirectory(file.getAbsolutePath());
            }
            directory.delete();
        }
        else{
            System.out.println("Failed to delete " + directoryName);
            return;
        }
    }

    public void enterDirectory(String directoryName){
        File diretory = new File(directoryName);
        if(diretory.isFile()){
            System.out.println(diretory.getAbsolutePath() + " is a file!");
            return;
        }
        if(diretory.isDirectory()){
            currentDirectory = new File(directoryName);
            System.setProperty("user.dir", currentDirectory.getAbsolutePath());
        }
    }

    public void listDirectory(){
        File[] fileList = currentDirectory.listFiles();
        System.out.println(currentDirectory.getName());
        for(File file : fileList){
            System.out.println("    " + file.getName());
        }
    }

    public void zip(String directoryName, String toPath) throws IOException {
        File directory = new File(directoryName);
        List<File> fileList = new ArrayList<File>();
        getFiles(directory, fileList);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(toPath + File.separator +directory.getName() + ".zip"));
        for(File file: fileList){
            ZipEntry cache = new ZipEntry(file.getAbsolutePath().substring(directory.getAbsolutePath().length() + 1, file.getAbsolutePath().length()));
            try {
                FileInputStream in = new FileInputStream(file);
                out.putNextEntry(cache);
                byte[] bytes = new byte[1024];
                int length;
                while((length = in.read(bytes)) >= 0){
                    out.write(bytes, 0, length);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out.close();
    }

    private void getFiles(File directory, List<File> fileList){
        File[] list = directory.listFiles();
        for(File file : list){
            if(file.isFile()){
                fileList.add(file);
            }
            else{
                getFiles(file, fileList);
            }
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

    public static void main(String[] args) throws IOException {
        DirectoryHandler handler = new DirectoryHandler();
        //System.out.println(handler.makeDirectory("./directory/"));
        //handler.makeDirectory("/Users/fengzipei/Documents/2/2/2");
        //handler.deleteDirectory("./directory");
        //handler.listDirectory();
        //handler.enterDirectory("./directory");
        //handler.listDirectory();
        //System.out.println(System.getProperty("user.dir"));
        //handler.copyFolder("/Users/fengzipei/Documents/selenium-python", "/Users/fengzipei/Desktop");
        //handler.zip("/Users/fengzipei/Documents/作息时间.pdf", "/Users/fengzipei/Desktop/");
        //handler.unzip("/Users/fengzipei/Desktop/作息时间.pdf.zip", "/Users/fengzipei/Desktop/test/");

    }
}
