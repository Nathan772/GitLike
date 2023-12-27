package fr.uge.gitclout.Contribution;

import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;

import static fr.uge.gitclout.Documentation.Documentation.fromFileTypeToString;

/* this class enables to be easily convertible into a jsonDocumentation type through json api */
@Serdeable
public class ContributionIntermediate {
    private String extension;
    private String type;
    int lines;
    int commentLines;
    String contributorName;
    String contributorEmail;
    /* this fields enables to know if it refers to a programming language or not */
    boolean programmingLanguage;

    public ContributionIntermediate(String extension, String type, int line, int commentLine, boolean programmingLanguage, String
                                    contributorName, String contributorEmail){
        this.extension = extension;
        this.type = type;
        this.lines = line;
        this.commentLines = commentLine;
        this.programmingLanguage = programmingLanguage;
        this.contributorEmail = contributorEmail;
        this.contributorName=contributorName;
    }

    public static ContributionIntermediate fromContributionToContributionIntermediate(Contribution contribution){
        return new ContributionIntermediate(contribution.documentation().extension(),
                fromFileTypeToString(contribution.documentation().fileType()),
                contribution.lines(),contribution.commentLines(),
                contribution.documentation().language().isPresent(), contribution.contributor().name(),
        contribution.contributor().email());
    }
    /**
     * this method enable to produce a List of documentationIntermediate based on a list of contribution object.
     * @param contributions
     * the documentation that you want to convert
     * @return
     * the new documentation intermediate
     * */
    public static List<ContributionIntermediate> fromUserContributionToContributionIntermediate(List<Contribution> contributions){
        ArrayList<ContributionIntermediate> contributionList = new ArrayList<ContributionIntermediate>();
        for(var contrib:contributions){
            contributionList.add(fromContributionToContributionIntermediate(contrib));
        }
        return contributionList;
    }

    public String toString(){
        if(programmingLanguage)
            return "the contributor is : "+contributorName+" their mail is "+contributorEmail+" the type of data is "+type+" and the language " +
                    "extension is "+extension;
        else
            return "the contributor is : "+contributorName+" their mail is "+contributorEmail+" the type of data is "+type;
    }

    /**
     * a setter for the field extension
     * @param extension
     * the new extension
     */
    public void setExtension(String extension){
        this.extension = extension;
    }

    /**
     * a getter for the field extension
     * @return
     * the extension as string.
     */
    public String getExtension(){
        return this.extension;
    }

    /**
     * a getter for the field type
     * @return
     * the type of the contribution.
     */
    public String getType(){
        return this.type;
    }

    /**
     * a setter for the field type
     * @param type
     * the new type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * a setter for the field line
     * @param lines
     * the new line
     */
    public void setLines(int lines){
        this.lines = lines;
    }

    /**
     * a getter for the field lines
     * @return
     * the number of lines.
     */
    public int getLines(){
        return this.lines;
    }

    /**
     * a setter for the field lines
     * @param commentLines
     * the new number of commentLines
     */
    public void setCommentLines(int commentLines){
        this.commentLines = commentLines;
    }

    /**
     * a getter for the field comment line
     * @return
     * the number of commentLine.
     */
    public int getCommentLines(){
       return commentLines;
    }

    /**
     * a setter for the field ContributorName
     * @param name
     * the new name
     */
    public void setContributorName(String name){
        this.contributorName = name;
    }

    /**
     * a getter for the field contributorName
     * @return
     * the contributor name for this contribution
     *
     */
    public String getContributorName(){
        return this.contributorName;
    }

    /**
     * a setter for the field ContributorName
     * @param email
     * the new email
     */
    public void setContributorEmail(String email){
        this.contributorEmail = email;
    }

    /**
     *
     * a getter for the field associated to the email
     * @return
     * the contribution email
     *
     */
    public String getContributorEmail(){
        return this.contributorEmail;
    }

    /**
     * a setter for the field ContributorName
     * @param programmingLanguageIsPresent
     * the new value for the presence or not for a programming language.
     */
    public void setProgrammingLanguage(boolean programmingLanguageIsPresent){
        this.programmingLanguage = programmingLanguageIsPresent;
    }

    /**
     * a getter for the field programmingLanguage
        @return
     * either the contribution is a programming language or not.
     */
    public boolean getProgrammingLanguage(){
        return this.programmingLanguage;
    }



}
