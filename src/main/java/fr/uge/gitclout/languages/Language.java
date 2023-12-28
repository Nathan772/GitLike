package fr.uge.gitclout.languages;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.uge.gitclout.contribution.ContributionType;
import fr.uge.gitclout.contribution.Contributions;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.*;
import java.util.regex.Pattern;

public final class Language implements SupportedFiles {

    private String name;

    private String extension;

    private String beginSingleLineComment;

    private String endSingleLineComment;

    private String endMultiLineComment;

    private String beginMultiLineComment;

    public Language(@JsonProperty("name") String name,  @JsonProperty("beginSingleLineComment") String beginSingleLineComment,
                    @JsonProperty("endSingleLineComment") String endSingleLineComment,  @JsonProperty("beginMultiLineComment") String beginMultiLineComment, @JsonProperty("endMultiLineComment") String endMultiLineComment,@JsonProperty("extension") String extension ) {
        Objects.requireNonNull(name, "language's name cannot be null");
        Objects.requireNonNull(beginSingleLineComment);Objects.requireNonNull(endSingleLineComment);
        Objects.requireNonNull(beginSingleLineComment);Objects.requireNonNull(endSingleLineComment);
        this.name=name;this.beginSingleLineComment = beginSingleLineComment;
        this.endSingleLineComment = endSingleLineComment; this.beginMultiLineComment = beginMultiLineComment;
        this.endMultiLineComment = endMultiLineComment; this.extension = extension;
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof Language lang && lang.name.equals(name) && lang.beginSingleLineComment.equals(this.beginSingleLineComment)
                && lang.endSingleLineComment.equals(endSingleLineComment) &&  lang.beginMultiLineComment.equals(beginMultiLineComment)
                && lang.endMultiLineComment.equals(endMultiLineComment);
    }
    @Override
    public String toString(){
        return "the language is "+name+" its single line comments starts by "+beginSingleLineComment+" and end with "+endMultiLineComment+
                "and its multiline comment starts with "+beginMultiLineComment+" and end with "+endMultiLineComment;
    }

    /**
     * this method tells either a supportedFile contain or not comments
     * @return
     * true if yes, if no, false
     */
    @Override
    public boolean containsComments(){
        return true;
    }

    /**
     * this methods enables to know the type of contribution it represents.
     * @return
     * CODE because it's necessarily a programming language with some code.
     */
    public ContributionType getType(){
        return ContributionType.CODE;
    }

    /**
     * this methods enables to set a new name for a language
     * @param name
     * the new name for the language
     */
    public void setName(String name){
        this.name=name;
    }

    /**
     * this methods is an accessor on the field name.
     * @return
     * the name for the language
     */
    public String getName(){
      return name;
    }

    /**
     * This methods is an accessor on the field extension.
     * @return
     * the extension for the language
     */
    public String getExtension(){
        return extension;
    }


    /**
     * this methods enables to set the new extension for the language.
     * @param extension
     * the extension you want for your field extension.
     *
     */
    public void setExtension(String extension){
        this.extension=extension;
    }

    /**
     * this methods enables to set the new beginSingleLine for the language.
     * @param beginSingleLineComment
     * the beginSingleLine you want for your field beginSingleLine.
     *
     */
    public void setBeginSingleLineComment(String beginSingleLineComment){
        this.beginSingleLineComment=beginSingleLineComment;
    }

    /**
     * this methods enables to set the new beginSingleLine for the language.
     * @param endSingleLineComment
     * the beginSingleLine you want for your field beginSingleLine.
     *
     */
    public void setEndSingleLineComment(String endSingleLineComment){
        this.endSingleLineComment=endSingleLineComment;
    }

    /**
     * this methods enables to set the new beginSingleLine for the language.
     * @param beginMultiLineComment
     * the beginMultiLine you want for your field beginMultiLine.
     *
     */
    public void setBeginMultiLineComment(String beginMultiLineComment){
        this.beginMultiLineComment=beginMultiLineComment;
    }

    /**
     * this methods enables to set the new beginSingleLine for the language.
     * @param endMultiLineComment
     * the endMultiLine you want for your field endMultiLine.
     *
     */
    public void setEndMultiLineComment(String endMultiLineComment){
        this.endMultiLineComment=endMultiLineComment;
    }

    @Override
    public int hashCode(){
        return name.hashCode()^beginSingleLineComment.hashCode()^endSingleLineComment.hashCode()^
                beginMultiLineComment.hashCode()^endMultiLineComment.hashCode()
                ;
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
     * an accessor to a copy of the field "endSingleLineComment".
     *
     * @return
     *
     * a copy of the field endCommentRegex.
     */
    public String getEndSingleLineComment(){
        return endSingleLineComment;
    }

    /**
     * this field refers to the file extension
     @return
     the extension for this language
     */
    public String extension(){
        return extension;
    }


    /**
     *
     * an accessor to a copy of the field "beginSingleLineComment".
     *
     * @return
     *
     * a copy of the field endCommentRegex.
     */
    public String getBeginSingleLineComment(){
        return beginSingleLineComment;
    }

    /**
     * this method is a getter to the field name
     * @return
     * the name of the file
     */
    public String name(){
        return name;
    }

    /**
     *
     * an accessor to a copy of the field "beginSingleLineComment".
     *
     * @return
     *
     * a copy of the field endCommentRegex.
     */
    public String getEndMultiLineComment(){
        return endMultiLineComment;
    }


    /**
     *
     * an accessor to a copy of the field "beginSingleLineComment".
     *
     * @return
     *
     * a copy of the field endCommentRegex.
     */
    public String getBeginMultiLineComment(){
        return beginMultiLineComment;
    }

    /**
     * this method enables to know if a string contains a comment line or not at its beginning.
     *
     * @param line
     * the string you want to parse
     *
     * @return
     * the String that represents the comment and empty optional if
     * it doesn't start any comment.
     *
     */
    public Optional<String> thisStringStartsWithComment(String line){
        Objects.requireNonNull(line);
        //take off the space in order to know if it starts with a comment
        line=line.replaceAll(" ", "");
        var beginCommentForm= new ArrayList<String>();
        beginCommentForm.add(beginSingleLineComment); beginCommentForm.add(beginMultiLineComment);
        for(var comment:beginCommentForm){
            var matcher = Pattern.compile("^"+comment).matcher(line);
            if(matcher.find()) return Optional.of(comment);
        }
        return Optional.empty();
    }

    /**
     * this method enables to know if a string contains a comment line or not somewhere.
     *
     * @param line
     * the string you want to parse
     *
     * @return
     * an optional that contains the comment if it starts a new comment.
     */
    public Optional<String> thisStringContainsANewComment(String line){
        Objects.requireNonNull(line);
        //On retire les espaces blancs au début de la ligne pour voir si elle commence par un commentaire.
        line=line.replaceAll(" ", "");
        var beginCommentRegex = new ArrayList<String>();
        beginCommentRegex.add(beginMultiLineComment); beginCommentRegex.add(beginSingleLineComment);
        for(var comment:beginCommentRegex){
            var matcher = Pattern.compile(comment).matcher(line);
            if(matcher.find()) return Optional.of(comment);
        }
        return Optional.empty();
    }

    /**
     * this method enables to know if a string contains a comment line or not at its end that doesn't
     * finish at the end of the line.
     *
     * @param line
     * the string you want to parse
     *
     * @return
     * the optional that contains the comment type that has started.
     * (optional empty if there's none).
     */
    public Optional<String> thisStringEndsWithUnfinishedComment(String line){
        Objects.requireNonNull(line);
        //take off the white space in order to know if the line begins by a comment.
        int counter = 0; line=line.replace("^\\s*", "");
        var beginCommentRegex = new ArrayList<String>(); var endCommentRegex = new  ArrayList<String>();
        beginCommentRegex.add(beginSingleLineComment); beginCommentRegex.add(endMultiLineComment);
        endCommentRegex.add(endSingleLineComment);endCommentRegex.add(endMultiLineComment);
        for(var comment:beginCommentRegex){
            //var regexBeginComment = ;
            if(Pattern.compile(comment).matcher(line).find()) {
                // a regex to represents the end of comment in order to check if the comment end at the same line
                //System.out.println("la end comment regex ressmeble à : "+endCommentRegex);
                var pattern = Pattern.compile(comment+"(?!.*("+endCommentRegex.get(counter)+")).*");
                //System.out.println("la regex qui correspond au commentaire "+pattern.toString());
                var matcher = pattern.matcher(line);
                //we have to handle in a particular manner the case of comment that ends with \n
                if(matcher.find() && !endCommentRegex.get(counter).equals("\\n")) return Optional.of(comment);
            }
            counter++;
        }
        return Optional.empty();
    }

    /**
     * this method enables to know if a string contains the end of a comment that had started.
     *
     * @param line
     * the string you want to parse
     *
     * @param commentEndingType
     * the form of the comment that has started
     *
     * @return
     * a boolean that tell either or not this string (line) end the comment (commentBeginning)
     */
    public boolean thisStringEndsComment(String line, String commentEndingType){
        Objects.requireNonNull(commentEndingType);
        //take off the white space in order to know if the line begins by a comment.
        line=line.replace("^\\s*", "");
        return line.contains(commentEndingType);
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
        return thisStringStartsWithComment(line).isPresent() && thisStringStartsWithComment(line).orElseThrow().equals(beginSingleLineComment);
    }

    /**
     * @param blameResult
     * the blame result you want to parse.
     * @param contributions
     * the object that represents the kind of file
     * @param tagName
     * the object that represents the tag associated to the contribution
     *
     */
    @Override
    public void feedWithContributions(BlameResult blameResult, Map<String, Contributions> contributions, String tagName) {
        Objects.requireNonNull(tagName);Objects.requireNonNull(contributions); Objects.requireNonNull(blameResult);
        //precise if a multiline comment has started.
        var isInMultiLineCommentMode = false;

        for (int i = 0; i < blameResult.getResultContents().size(); i++) {
            RevCommit lineCommit = blameResult.getSourceCommit(i);
            String author = lineCommit.getAuthorIdent().getName();
            String email = lineCommit.getAuthorIdent().getEmailAddress();
            author = author + " <" + email + ">";
            //      //retrieve the current line content
            var lineToParse = blameResult.getResultContents().getString(i);
//      //a multiline continue
            if (isInMultiLineCommentMode && !this.thisStringEndsComment(lineToParse, this.endMultiLineComment)) {
//        //the comments ends here
                isInMultiLineCommentMode= false;
//              //one last comment line
                Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
                //hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
                tagContributions.addAuthorContribution(author, this.getType(), this.name(), 0, 1);
            }
//      //we start a single line comment
            else if (this.thisStringStartsWithSingleLineComment(lineToParse)) {
//        //increase the number of comments
//              //one last comment line
                Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
                //hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
                tagContributions.addAuthorContribution(author, this.getType(), this.name(), 0, 1);
            }

            else{
//        //check if a new multiline comment starts and doesn't finish on the same line
                if(this.thisStringEndsWithUnfinishedComment(lineToParse).isPresent()){
//         //System.out.println(" un multi line comment commence "+lineToParse);
//         //comment mode start
                    isInMultiLineCommentMode = true;
//                  //the line didn't start by a comment, then it starts by code
                    if(this.thisStringStartsWithComment(lineToParse).isEmpty()) {
                        //hashUserLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
                        Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
                        tagContributions.addAuthorContribution(author, this.getType(), this.name(), 1, 0);
                    }
                    //the line start with comments
                    else {
                        Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
                        tagContributions.addAuthorContribution(author, this.getType(), this.name(), 0, 1);
                    }
                }
//
//              //a multiline comment starts and ends on the same line at the start of the line
                else if(this.thisStringStartsWithComment(lineToParse).isPresent()){
                    Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
                    tagContributions.addAuthorContribution(author, this.getType(), this.name(), 0, 1);
                }
//              //code line (but not exhaustive should check if there's code between two comments
                else{
                    Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
                    tagContributions.addAuthorContribution(author, this.getType(), this.name(), 1, 0);
                    isInMultiLineCommentMode = false;
                }
                //

            }
        }
    }


//    public static void main(String[] args){
//        si une ligne contient un début de commentaire il va falloir le retirer
//        après le premier appel pour pouvoir gérer les cas où le début de commentaire est identique à la fin */
//        var beginCommentRegex = new ArrayList<String>();
//        var endCommentRegex = new ArrayList<String>();
//        beginCommentRegex.add("\\/\\*");
//        beginCommentRegex.add("//");
//        endCommentRegex.add("\\*\\/");
//        endCommentRegex.add("\\n");
//
//        int cc = 3; //
//        var lang = new Language("Java", beginCommentRegex,endCommentRegex);
//        String commentStyle1 = "int blabla; /* ceci est un commentaire random de type 1 ";
//        System.out.println("this string contains an unfinished comment : "+lang.thisStringEndsWithUnfinishedComment(commentStyle1));
//        //System.out.println("this string is a single line comment here : "+lang.thisStringStartsWithSingleLineComment(commentStyle1));
//    }
}

