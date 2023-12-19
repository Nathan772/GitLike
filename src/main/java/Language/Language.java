package Language;

import fr.uge.gitclout.model.FileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public record Language(String name, String extension, ArrayList<String> beginCommentRegex,
                       ArrayList<String> endCommentRegex

) {
    public Language{
        Objects.requireNonNull(name, "language's name cannot be null");
        Objects.requireNonNull(extension, "language's extension cannot be null");
        Objects.requireNonNull(beginCommentRegex, "language's commentRegex cannot be null");
        Objects.requireNonNull(endCommentRegex, "language's commentRegex cannot be null");
        /* à supprimer
        Objects.requireNonNull(fileType, "language's file type cannot be null");
         */
    }
    /**
     * This method enables to use a string tab format to transform it into a "Language" object.
     * @param tabLanguage
     * the string you want to parse
     * @return Language
     * an object of type Language that represents the languages you obtain from your string.
     */

    public static Language fromStringTabToLanguage(String[] tabLanguage){
        Objects.requireNonNull(tabLanguage);
        var beginRegex = new ArrayList<String>();
        var endRegex= new ArrayList<String>();
        //we start at index 1 to avoid the first "["
        for(var i =0;i<tabLanguage.length;i++){
                //add the beginning of comment
            if(i>=3 && i % 2 != 0)
                beginRegex.add(tabLanguage[i]);
                // add the end of comment
            else if(i>=4)
                endRegex.add(tabLanguage[i]);
        }
        //we start at index 1 to avoid the first "["
        return new Language(tabLanguage[0].substring(1),tabLanguage[1],beginRegex,endRegex);
    }
    /**
     * this method enables to initiate a list with all the languages that might exist.
     * @return
     * a list with all the existing languages.
     */
    /*
    à supprimer...

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
    }*/

    /**
     * this method enable to know what is the language type of a file
     * @param languages
     * a list of possible language
     * @param filePath
     * the file you want to parse.
     * @return
     * the language associated to the file.
     */
    /*
    à supprimer
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
        return new Language("Unknown", "", FileType.Unknown, "");
    }*/
}
