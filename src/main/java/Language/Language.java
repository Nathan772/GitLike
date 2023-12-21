package Language;

import fr.uge.gitclout.model.FileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Language {
    private final String name;
    private final ArrayList<String> beginCommentRegex;
    private final ArrayList<String> endCommentRegex;

    public Language(String name, ArrayList<String> beginCommentRegex,
                    ArrayList<String> endCommentRegex){
        Objects.requireNonNull(name, "language's name cannot be null");
        Objects.requireNonNull(beginCommentRegex, "language's commentRegex cannot be null");
        Objects.requireNonNull(endCommentRegex, "language's commentRegex cannot be null");
       this.name=name;
       this.beginCommentRegex = beginCommentRegex;
       this.endCommentRegex = endCommentRegex;
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof Language lang && lang.name.equals(name) && lang.beginCommentRegex.equals(this.beginCommentRegex)
                && lang.endCommentRegex.equals(endCommentRegex);
    }
    @Override
    public String toString(){
        var bd = new StringBuilder();
        bd.append(" the language is : ");bd.append(name);

        for(var i=0;i<beginCommentRegex.size();i++){
            bd.append("\n its comments can start by : "); bd.append(beginCommentRegex.get(i));
            bd.append("\n"); bd.append(" and end with : "); bd.append(endCommentRegex.get(i));
            bd.append("\n");
        }
        return bd.toString();
    }

    @Override
    public int hashCode(){
       return name.hashCode()^beginCommentRegex.hashCode()^endCommentRegex.hashCode();
    }
    /**
     * This method enables to use a string tab format to transform it into a "Language" object.
     * @param tabLanguage
     * the string you want to parse
     * @return Language
     * an object of type Language that represents the languages you obtain from your string.
     */

    /**
     *
     * an accessor to a copy of the field "endCommentRegex".
     *
     * @return
     *
     * a copy of the field endCommentRegex.
     */
    public List<String> endCommentRegex(){
        return List.copyOf(endCommentRegex);
    }


    public static Language fromStringTabToLanguage(String[] tabLanguage){
        Objects.requireNonNull(tabLanguage);
        var beginRegex = new ArrayList<String>();
        var endRegex= new ArrayList<String>();
        for(var i =0;i<tabLanguage.length;i++){
                //add the beginning of comment
            if(i>=3 && i % 2 != 0)
                beginRegex.add(tabLanguage[i]);
                // add the end of comment
            else if(i>=4)
                endRegex.add(tabLanguage[i].replace("]", ""));
        }
        //we start at index 1 to avoid the first "["
        return new Language(tabLanguage[0].substring(1),beginRegex,endRegex);
    }

    /**
     * this method enables to know if a string contains a comment line or not at its beginning.
     *
     * @param line
     * the string you want to parse
     *
     * @return
     * the index of the comment type (it's -1 if there's no comment)
     */
    public int thisStringStartsWithComment(String line){
        int counter = 0;
        //On retire les espaces blancs au début de la ligne pour voir si elle commence par un commentaire.
        line=line.replaceFirst("^\\s*", "");
        for(var comment:beginCommentRegex){
            if(line.startsWith(comment))
                return counter;
            counter++;
        }
        return -1;
    }

    /**
     * this method enables to know if a string contains a comment line or not at its end that doesn't
     * finish at the end of the line.
     *
     * @param line
     * the string you want to parse
     *
     * @return
     * the index of the comment type (it's -1 if there's no comment)
     */
    public int thisStringEndsWithUnfinishedComment(String line){

        //take off the white space in order to know if the line begins by a comment.
        int counter = 0; line=line.replace("^\\s*", "");
        for(var comment:beginCommentRegex){
            if(line.contains(comment)) {
                // a regex to represents the end of comment in order to check if the comment end at the same line
                var pattern = Pattern.compile(comment+".*(?!"+endCommentRegex.get(counter)+")$"); var matcher = pattern.matcher(line);
                if(matcher.find())
                    return counter;
            } counter++;
        }
        return -1;
    }

    /**
     * this method enables to know if a string contains the end of a comment that had started.
     *
     * @param line
     * the string you want to parse
     *
     * @param commentIndex
     * the index associated to the beginning of the comment.
     *
     * @return
     * the index of the comment type (it's -1 if there's no comment)
     */
    public boolean thisStringEndsComment(String line, int commentIndex){
        //take off the white space in order to know if the line begins by a comment.
        line=line.replace("^\\s*", "");

        return line.contains(endCommentRegex.get(commentIndex));
    }

    public static void main(String[] args){
        /* si une ligne contient un début de commentaire il va falloir le retirer
        après le premier appel pour pouvoir gérer les cas où le début de commentaire est identique à la fin */
        var beginCommentRegex = new ArrayList<String>();
        var endCommentRegex = new ArrayList<String>();
        beginCommentRegex.add("\"\"\"");
        beginCommentRegex.add("#");
        endCommentRegex.add("\"\"\"");
        endCommentRegex.add("\n");
        var lang = new Language("Python", beginCommentRegex,endCommentRegex);
        String commentStyle1 = "# ceci est un commentaire random de type 1";
        String commentStyle3 = "x=3 #ceci est un commentaire random de type 3\n";
        String commentStyle4 = "     # ceci est un commentaire random de type 4";
        String commentStyle2 = "\"\"\" est un commentaire random de type 2 \"\"\" ";
        String commentStyle5 = "\"\"\" est un début de commentaire random de type 5 ";
        /*String commentStyle6 = " \"\"\" " +
                Perform text completion for a list of prompts using the language generation model.
        \"\"\" "*/
        /*System.out.println("this string starts comment : "+lang.thisStringStartsWithComment(commentStyle4));
        System.out.println("this string ends with a comment that doesn't finish: "+lang.thisStringEndsWithUnfinishedComment(commentStyle3));
        System.out.println("this string ends with a comment : "+lang.thisStringEndsComment(commentStyle3,1));*/
        System.out.println("this string starts with a comment "+lang.thisStringStartsWithComment(commentStyle5));
        commentStyle5 = commentStyle5.replaceFirst("\"\"\"", "");
        System.out.println("this string ends with a comment that actually finish: "+lang.thisStringEndsComment(commentStyle5, 0));
    }
}
