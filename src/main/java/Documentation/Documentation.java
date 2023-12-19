package Documentation;

import fr.uge.gitclout.model.FileType;
import Language.Language;

import java.io.IOException;
import java.nio.charset.Charset;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

/*
this class associated a fileType to a language if necessary
 */
public class Documentation {
    private final Language language;
    private final FileType fileType;

    public Documentation(Language language, FileType fileType){
        Objects.requireNonNull(fileType);
        //the language can be null ? (must be precise)
        //Objects.requireNonNull(language);
        this.language = language;
        this.fileType = fileType;
    }

    /**
     *
     * this method return a list of elements of type "Documentation" which represents the transformation of the content of
     * the file "filePath".
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
        Charset charset = null;
        var documentations = new ArrayList<Documentation>();
        try(var reader = Files.newBufferedReader(filePath)){
            String line;
            while( (line = reader.readLine()) != null ) {
                var tab1 = line.split(",");
                //programming language case
                if(fromStringToFileType(tab1[2]) == FileType.CODE){
                    var language = Language.fromStringTabToLanguage(tab1);
                    documentations.add(new Documentation(language, FileType.CODE));
                }
                //no-programming language
                else
                    documentations.add(new Documentation(null, fromStringToFileType(tab1[2])));
            }
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
        return documentations;
    }



    public static FileType fromStringToFileType(String typeFile){
        switch (typeFile){
            case "BUILD" -> {return FileType.BUILD;}
            case "CODE" -> {return FileType.CODE;}
            case "CONFIGURATION" -> {return FileType.CONFIG;}
            case "RESOURCE" -> {return FileType.RESOURCE;}
            default ->  {return FileType.Unknown;}
        }
    }



}
