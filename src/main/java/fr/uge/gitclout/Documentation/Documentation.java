package fr.uge.gitclout.Documentation;

import fr.uge.gitclout.Contribution.FileType;
import fr.uge.gitclout.Language.Language;
import io.micronaut.serde.annotation.Serdeable;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/*
this class associated a fileType to a language if necessary
 */

public class Documentation {
    /* it represents the programming language used*/
    private Language language = null;
    /* it represents the fileType (CODE, BUILD, ETC...)*/
    private final FileType fileType;
    /* The extension associated to the file */
    private final  String extension;

    public Documentation(Language language, FileType fileType, String extension ){
        Objects.requireNonNull(fileType);
        Objects.requireNonNull(extension);
        //the language can be null ? (must be precise)
        //Objects.requireNonNull(language);
        this.language = language;
        this.fileType = fileType;
        this.extension = extension;
    }
    /**
     *
     * this method creates a document of type "unknown" for the unknown kind of files.
     *
     * @return
     * an unknown file type content.
     */
    public static Documentation unknownFile(){
        return new Documentation (null,FileType.Unknown,"unknown");
    }



    public boolean equals(Object obj){
        return obj instanceof Documentation doc && doc.fileType.equals(fileType) && extension.equals(doc.extension)
                && ((language == doc.language) && (doc.language == null) || language != null && doc.language != null &&
                language.equals(doc.language));
    }

    /**
     * this method enables to know if two documentation refers to the same category
     * (for example build with build, config with config, .java with .java) but not necessarily exactly the same
     * extension.
     * @param obj
     * the object of comparison
     * @return
     * if the category are alike.
     */
    public boolean equalsCategory(Object obj){
        return obj instanceof Documentation doc && ((doc.fileType.equals(fileType) && doc.fileType != FileType.CODE) ||
                (doc.fileType.equals(fileType) && extension.equals(doc.extension)
                && ((language == doc.language) && (doc.language == null) || language != null && doc.language != null &&
                language.equals(doc.language))));
    }


    /**
     * this method enables to know if two documents are of the same category when you will
     * compute the number of lines when you will sort them.
     * the document you want to check if they're of the same category
     * @return
     * either the elements are of the same category or not

    public boolean equalsCategory(Documentation doc){
        return (doc.fileType.equals(fileType) && doc.fileType != FileType.CODE)
                || (extension.equals(doc.extension)
                && ((language == doc.language) && (doc.language == null) || language != null && doc.language != null &&
                language.equals(doc.language)));
    }*/

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
        bd.append("Type of file : "); bd.append(fromFileTypeToString(fileType)); bd.append("\n");

        return bd.toString();
    }
    /**
        an accessor to the field "extension".
        @return
        the content of the field extension
     */
    public String extension(){
        return extension;
    }

    /**
     an accessor to the field "fileType".
     @return
     the content of the field fileType.
     */
    public FileType fileType(){
        return fileType;
    }

    /**
     * an accessor to the field "language".
     * @return
     * the language associated to the document.
     */
    public Optional<Language> language(){
        if(language == null)
            return Optional.empty();
        return Optional.of(language);
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
        Objects.requireNonNull(filePath); var documentations = new ArrayList<Documentation>();
        try(var reader = Files.newBufferedReader(filePath)){
            String line;
            //on analyse la ligne ssi ce n'est pas un commentaire.
            while((line = reader.readLine()) != null) {
                //gère le cas des commentaires dans le fichier à analyser
                if(!line.replaceAll(" ","").startsWith("#")) {
                    var doc = fromLineToDocumentation(line);
                    //prevent from adding twice the same language
                    if (!documentations.contains(doc)) documentations.add(doc);
                }
            }
        }
        catch(IOException e) {System.err.println(e.getMessage());}
        return documentations;
    }

    /**
     *
     * @param line
     * the line that represents the information regarding the documentation
     * @return
     * a new Documentation object which represents the object
     */
    private static Documentation fromLineToDocumentation(String line){
        Objects.requireNonNull(line);
        var tab1 = line.split(",");
        //programming language case
        if(fromStringToFileType(tab1[2].replace("\"","").replace("]","").replace(" ", "")) == FileType.CODE){
            return new Documentation(Language.fromStringTabToLanguage(tab1) ,FileType.CODE,
                    tab1[1].replace("\"", "").replace(" ", ""));
        }
        //no-programming language
        else
            return new Documentation(null, fromStringToFileType(tab1[2].replace("\"","").replace("]","").replace(" ", "")),
                    tab1[1].replace("\"", "").replace(" ", ""));
    }


    /**
     * this method enables to transform a string to a FileType type
     * @param typeFile
     * the file type you have as string
     * @return
     * the fileType object it represents
     */
    public static FileType fromStringToFileType(String typeFile){
        Objects.requireNonNull(typeFile);
        switch (typeFile){
            case "BUILD" -> {return FileType.BUILD;}
            case "CODE" -> {return FileType.CODE;}
            case "CONFIGURATION" -> {return FileType.CONFIG;}
            case "RESOURCE" -> {return FileType.RESOURCE;}
            default ->  {return FileType.Unknown;}
        }
    }

    /**
     * this method enables to transform a fileType to the corresponding String
     * @param typeFile
     * the file type you have as fileType
     * @return
     * the String objects it represents
     */
    public static String fromFileTypeToString(FileType typeFile){
        Objects.requireNonNull(typeFile);
        switch (typeFile){
            case FileType.BUILD -> {return "BUILD";}
            case FileType.CODE  -> {return "CODE";}
            case FileType.CONFIG -> {return "CONFIGURATION";}
            case FileType.RESOURCE -> {return "RESOURCE" ;}
            default ->  {return "OTHER" ;}
        }
    }

    /**
     * a getter to the field extension
     * @return
     * access to the field extension
    public String getExtension() {
        return extension;
    }
    /**
     * a getter to the field fileType
     * @return
     * access to the field fileType
    public FileType getFileType() {
        return fileType;
    }

    /**
     * a getter to the field fileType
     * @return
     * access to the field fileType
    public FileType getFileType() {
        return fileType;
    }

    /**
     * a setter to the field extension
     * the new name of the document
    public void setExtension(String extension) {
        this.extension = extension;
    }
    /**
     * a setter to the field fileType
     * the new extension of the document
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
    */

    public static void main(String[] args){
        System.out.println(" les types suivants sont équivalents : "+(FileType.Unknown == FileType.CONFIG));
    }





}
