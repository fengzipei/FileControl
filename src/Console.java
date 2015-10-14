import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by fengzipei on 15/10/9.
 */
public class Console {
    public static void main(String[] args) throws IOException {
        FileHandler fileHandler = new FileHandler();
        DirectoryHandler directoryHandler = new DirectoryHandler();
        System.out.println("***File Controler(console edtion)***");
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.print(">>>");
            ArrayList<String> parameter = new ArrayList<>();
            parameter.add(in.next());
            parameter.add(in.next());
            parameter.add(in.next());
            if(parameter.get(0).equals("cp")){
                File source = new File(parameter.get(1));
                if(source.isDirectory()){
                    directoryHandler.copyFolder(parameter.get(1), parameter.get(2));
                }
                else if(source.isFile()){
                    fileHandler.copy(parameter.get(1), parameter.get(2));
                }
            }
            else if(parameter.get(0).equals("mkdir")){
                directoryHandler.makeDirectory(parameter.get(1));
            }
            else if(parameter.get(0).equals("rmdir")){
                directoryHandler.deleteDirectory(parameter.get(1));
            }
            else if(parameter.get(0).equals("cd")){
                File source = new File(parameter.get(1));
                if(source.isDirectory()){
                    directoryHandler.enterDirectory(parameter.get(1));
                }
                else if(source.isFile()){
                    System.out.println("Wrong parameter");
                }
            }
            else if(parameter.get(0).equals("ls")){
                directoryHandler.listDirectory();
            }
            else if(parameter.get(0).equals("zip")){
                File source = new File(parameter.get(1));
                if(source.isFile()){
                    fileHandler.zip(parameter.get(1), parameter.get(2));
                }
                else if(source.isDirectory()){
                    directoryHandler.zip(parameter.get(1), parameter.get(2));
                }
            }
            else if(parameter.get(0).equals("unzip")){
                File source = new File(parameter.get(1));
                if(source.isFile()){
                    fileHandler.zip(parameter.get(1), parameter.get(2));
                }
                else
                    System.out.println("Wrong parameter!");
            }
            else{
                System.out.println("Wrong parameter!");
            }
        }
    }
}
