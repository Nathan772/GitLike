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


    /**
     *
     * an accessor to a copy of the field "beginCommentRegex".
     *
     * @return
     *
     * a copy of the field endCommentRegex.
     */
    public List<String> beginCommentRegex(){
        return List.copyOf(beginCommentRegex);
    }
    public static Language fromStringTabToLanguage(String[] tabLanguage){
        Objects.requireNonNull(tabLanguage);
        var beginRegex = new ArrayList<String>();
        var endRegex= new ArrayList<String>();
        for(var i =0;i<tabLanguage.length;i++){
                //add the beginning of comment
            if(i>=3 && i % 2 != 0)  beginRegex.add(tabLanguage[i].replace(" ", ""));
                // add the end of comment
            else if(i>=4) endRegex.add(tabLanguage[i].replace("]", "").replace(" ", ""));
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
        Objects.requireNonNull(line);
        int counter = 0;
        //On retire les espaces blancs au début de la ligne pour voir si elle commence par un commentaire.
        line=line.replaceAll(" ", "");

        for(var comment:beginCommentRegex){
            //System.out.println("la regex produite est : " +Pattern.compile("^"+comment));
            //System.out.println("la regex qui correspond au commentaire "+pattern.toString());
            var matcher = Pattern.compile("^"+comment).matcher(line);
            if(matcher.find()) return counter;
            counter++;
        }
        return -1;
    }

    /**
     * this method enables to know if a string contains a comment line or not somewhere.
     *
     * @param line
     * the string you want to parse
     *
     * @return
     * the index of the comment type (it's -1 if there's no comment)
     */
    public int thisStringContainsANewComment(String line){
        Objects.requireNonNull(line);
        int counter = 0;
        //On retire les espaces blancs au début de la ligne pour voir si elle commence par un commentaire.
        line=line.replaceAll(" ", "");

        for(var comment:beginCommentRegex){
            //System.out.println("la regex produite est : " +Pattern.compile("^"+comment));
            //System.out.println("la regex qui correspond au commentaire "+pattern.toString());
            var matcher = Pattern.compile(comment).matcher(line);
            if(matcher.find()) return counter;
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
            //var regexBeginComment = ;
            if(Pattern.compile(comment).matcher(line).find()) {
                // a regex to represents the end of comment in order to check if the comment end at the same line
                //System.out.println("la end comment regex ressmeble à : "+endCommentRegex);
                var pattern = Pattern.compile(comment+"(?!.*("+endCommentRegex.get(counter)+")).*");
                //System.out.println("la regex qui correspond au commentaire "+pattern.toString());
                var matcher = pattern.matcher(line);
                //we have to handle in a particular manner the case of comment that ends with \n
                if(matcher.find() && !endCommentRegex.get(counter).equals("\\n")) return counter;
            }
            counter++;
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
        Objects.checkIndex(commentIndex, endCommentRegex.size());
        //take off the white space in order to know if the line begins by a comment.
        line=line.replace("^\\s*", "");
        return line.contains(endCommentRegex.get(commentIndex));
    }

    /**
     * this method enables to know if the current comment is a "single line" comment.
     *
     * @param commentIndex
     * the index associated to the beginning of the comment.
     *
     * @return
     * true if it's a  single lien comment
     * false if it's not
     */
    public boolean thisIndexIsSingleLineComment(int commentIndex){
        Objects.checkIndex(commentIndex, endCommentRegex.size());
        return endCommentRegex.get(commentIndex).equals("\\n");
    }

    /**
     * this method enables to know if the current comment is a "multi line" comment.
     *
     * @param commentIndex
     * the index associated to the beginning of the comment.
     *
     * @return
     * true if it's a  single lien comment
     * false if it's not
     */
    public boolean thisIndexIsMultiLineComment(int commentIndex){
        Objects.checkIndex(commentIndex, endCommentRegex.size());
        return !endCommentRegex.get(commentIndex).equals(("\n"));
    }


    /**
     * this method enables to know if a string contains a one line, comment line or not at its beginning.
     *
     * @param line
     * the string you want to parse
     *
     * @return
     * the index of the comment type (it's -1 if there's no comment)
     */
    public boolean thisStringStartsWithSingleLineComment(String line){
        Objects.requireNonNull(line);
        var beginCommentIndex = thisStringStartsWithComment(line);
        return beginCommentIndex != -1 && thisIndexIsSingleLineComment(beginCommentIndex);
    }


    public static void main(String[] args){
        /* si une ligne contient un début de commentaire il va falloir le retirer
        après le premier appel pour pouvoir gérer les cas où le début de commentaire est identique à la fin */
        var beginCommentRegex = new ArrayList<String>();
        var endCommentRegex = new ArrayList<String>();
        beginCommentRegex.add("\\/\\*");
        beginCommentRegex.add("//");
        endCommentRegex.add("\\*\\/");
        endCommentRegex.add("\\n");
        /*

        /* */ int cc = 3; //
        var lang = new Language("Java", beginCommentRegex,endCommentRegex);
        String commentStyle1 = "int blabla; /* ceci est un commentaire random de type 1 ";
        System.out.println("this string contains an unfinished comment : "+lang.thisStringEndsWithUnfinishedComment(commentStyle1));
        //System.out.println("this string is a single line comment here : "+lang.thisStringStartsWithSingleLineComment(commentStyle1));
    }
}
