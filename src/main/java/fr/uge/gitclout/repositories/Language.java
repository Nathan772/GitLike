package fr.uge.gitclout.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public record Language(String name, String extension, FileType fileType, String commentsRegex// "" if no comments in this language
) {
    public Language{
        Objects.requireNonNull(name, "language's name cannot be null");
        Objects.requireNonNull(extension, "language's extension cannot be null");
        Objects.requireNonNull(commentsRegex, "language's commentRegex cannot be null");
        Objects.requireNonNull(fileType, "language's file type cannot be null");
    }

    /**
     * this method enables to initiate a list with all the languages that might exist.
     * @return
     * a list with all the existing languages.
     */
    public static List<Language> initLanguages(){
        var list = new ArrayList<Language>();
        var python = new Language("Python", ".py", FileType.CODE,"#(.*)|\"\"\"(.|\n)*\"\"\"");
        var java =  new Language("Java", ".java", FileType.CODE,"\\/\\/(.*)|\\/\\*(.|\n)*\\*\\/");
        var c =  new Language("C", ".c", FileType.CODE,"\\/\\/(.*)|\\/\\*(.|\n)*\\*\\/");
        var javasS =  new Language("Javascript", ".js", FileType.CODE,"\\/\\/(.*)|\\/\\*(.|\n)*\\*\\/");
        var other = new Language("Unknown", "",FileType.Unknown, "");
        list.add(java);
        list.add(javasS);
        list.add(c);
        list.add(python);
        list.add(other);
        return list;
    }

    /**
     * this method enable to know what is the language type of a file
     * @param languages
     * a list of possible language
     * @param filePath
     * the file you want to parse.
     * @return
     * the language associated to the file.
     */
    public static Language fromFileToLanguage(List<Language> languages, String filePath){
        Objects.requireNonNull(languages);
        Objects.requireNonNull(filePath);
        for(var language:languages){
            //create a pattern to match the extension
            var pattern = Pattern.compile(".*"+language.extension+"$");
            var matcher = pattern.matcher(filePath);
            //case when you find the language
            if(matcher.find())
                return language;
        }
        //case with default language
        return new Language("Unknown", "",FileType.Unknown, "");
    }
}