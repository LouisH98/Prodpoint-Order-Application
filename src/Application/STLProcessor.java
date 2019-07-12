package Application;
/*
Class to batch call stl-thumb for every STL file in a folder

Made by: Louis Holdsworth
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class STLProcessor {


    //image size squared.
    static final int IMG_SIZE = 500;

    /**
     Function that returns the name of files in a folder
     @param dir - The directory to be searched
     @return ArrayList<String> - Each entry is a file name
     */
    public static ArrayList<String> getFiles(File dir){
        File[] files = dir.listFiles();

        ArrayList<String> fileNames = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            if(files[i].isFile()){
                fileNames.add(files[i].getName());
            }
        }
        return fileNames;
    }

    /**
    Function to get the extension of a file
    Used to check if files are of type STL
    @param filename -  the full name of the file
    @return the extension of the file (String)
     */
    private static String getExtension(String filename){
        String extension = "";
        int i = filename.lastIndexOf(".");
        if(i > 0){extension = filename.substring(i+1);}
        return extension;
    }


    /**
     * Function to call stl-thumb for the given file
     * @param file
     * @return an empty string if successful - if not then the error message
     */
    public static String makeThumbnail(File file){

        new File(file.getParentFile() + "/img").mkdirs();

        if(isSTL(file)){
            try{
                Runtime rt = Runtime.getRuntime();

                Process process = rt.exec(new String[]{"stl-thumb", "-s", Integer.toString(IMG_SIZE), file.getAbsolutePath(), file.getParent()+"/img/"+file.getName()+".png"});
                //wait for the process to complete
                try{
                    process.waitFor();
                }
                catch (InterruptedException e){
                    System.out.println("Process interrupted");
                    return "Process interrupted";
                }

                //get any error messages
                StringBuilder errorString = new StringBuilder();
                InputStream errorStream = process.getErrorStream();

                for (int i = 0; i < errorStream.available(); i++) {
                    errorString.append((char)errorStream.read());
                }

                //return error if there is any
                if(errorString.length() > 0){
                    System.out.println("Error: " + errorString.toString());
                    return errorString.toString();
                }

                //empty String signifies successful thumbnail creation
                return "";
            }
            catch (IOException e){
                System.out.println("Could not execute command");
            }
        }
        else{
            System.out.println("Not an STL file");
        }
        return "";
    }



    /**
    @return int - the number of STL files in a directory
     */
    public static int countSTL(File dir){
        int count = 0;
        if(dir != null) {
            for (File f : dir.listFiles()) {
                if(getExtension(f.getName()).equals("stl")){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if a file is of type STL
     */
    public static boolean isSTL(File file){
        return getExtension(file.getName()).equals("stl") && !file.isDirectory();
    }
}

