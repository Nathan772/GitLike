package fr.uge.gitclout.Documentation;

import fr.uge.gitclout.model.FileType;
import Language.Language;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

/*
this class associated a fileType to a language if necessary
 */
public class Documentation {
    /* it represents the programming language used*/
    private final Language language;
    /* it represents the fileType (CODE, BUILD, ETC...)*/
    private final FileType fileType;
    /* The extension associated to the file */
    private final String extension;

    public Documentation(Language language, FileType fileType, String extension ){
        Objects.requireNonNull(fileType);
        Objects.requireNonNull(extension);
        //the language can be null ? (must be precise)
        //Objects.requireNonNull(language);
        this.language = language;
        this.fileType = fileType;
        this.extension = extension;
    }

    public boolean equals(Object obj){
        return obj instanceof Documentation doc && doc.fileType.equals(fileType) && extension.equals(doc.extension)
                && language.equals(doc.language);
    }

    public int hashCode(){
        return extension.hashCode()^fileType.hashCode()^ language.hashCode();
    }
    public String toString(){
        var bd = new StringBuilder();
        if(language == null){
            bd.append("Programming language : no ");
        }
        else {
            bd.append("Programming language : yes \n Language informations : \n"); bd.append(language.toString());
        }
        bd.append("\n extension : "); bd.append(extension); bd.append("\n");
        bd.append("Type of file : "); bd.append(fileType); bd.append("\n");

        return bd.toString();
    }

    /**
     *
     * this method return a list of elements of type "Documentation" which represents the transformation of the content of
     * the file "filePath" into Documentation contents.
     *
     * @param filePath
     * the path to the file you want to parse to obtain the type associated.
     *
     @return ArrayList<Documentation>
     this method return a list of elements of type "Documentation" which represents all the files given
     in the entry.
     */

    //trop de lignes, à modifier...
    public static ArrayList<Documentation> fromPathToListDocumentation(Path filePath){
        Objects.requireNonNull(filePath);
        var documentations = new ArrayList<Documentation>();
        try(var reader = Files.newBufferedReader(filePath)){
            String line;
            while((line = reader.readLine()) != null) {
               var doc= fromLineToDocumentation(line);
               //prevent from adding twice the same language
               if(!documentations.contains(doc))
                   documentations.add(doc);
            }
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }
        return documentations;
    }

    /**
     *
     * @param line
     * the line that represents the information regarding the documentation
     * @return
     * a new Documentation object which represents the object
     */
    public static Documentation fromLineToDocumentation(String line){
        Objects.requireNonNull(line);
        var tab1 = line.split(",");
        //programming language case
        if(fromStringToFileType(tab1[2].replace("\"","").replace("]","").replace(" ", "")) == FileType.CODE){
            return new Documentation(Language.fromStringTabToLanguage(tab1) ,FileType.CODE, tab1[1]);
        }
        //no-programming language
        else
            return new Documentation(null, fromStringToFileType(tab1[2].replace("\"","").replace("]","").replace(" ", "")), tab1[1]);
    }



    public static FileType fromStringToFileType(String typeFile){
        System.out.println("le fichier est de type : "+typeFile);
        System.out.println("typeFile est égal à build:" +typeFile.equals("BUILD"));
        switch (typeFile){
            case "BUILD" -> {return FileType.BUILD;}
            case "CODE" -> {return FileType.CODE;}
            case "CONFIGURATION" -> {return FileType.CONFIG;}
            case "RESOURCE" -> {return FileType.RESOURCE;}
            default ->  {return FileType.Unknown;}
        }
    }

    public static void main(String[] args){
           var listDocumentation = fromPathToListDocumentation(Path.of("FILES/langages_reconnus/langagesListe.txt"));
           System.out.println(listDocumentation);
    }



}
