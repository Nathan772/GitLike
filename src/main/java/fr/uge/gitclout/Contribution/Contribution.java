package fr.uge.gitclout.Contribution;

import Language.Language;
import fr.uge.gitclout.Documentation.Documentation;
import fr.uge.gitclout.model.Contributor;
import fr.uge.gitclout.model.Tag;

import java.util.Objects;
/* à supprimer , probablement...
public record Contribution(ContributionType type, int linesCount, Language language, Contributor contributor, Tag tag) {
  public Contribution {
    Objects.requireNonNull(type, "contribution's type cannot be null");
    Objects.requireNonNull(contributor, "contributor for a contribution cannot be null");
    Objects.requireNonNull(tag, "the tag used for a contribution cannot be null");
    if (linesCount < 0) {
      throw new IllegalArgumentException("lines count cannot be negative");
    }
    // Language can be null
  }
}
*/

public class Contribution {
    private final Contributor contributor;
    private final Tag tag;
    private final Documentation documentation;
    private int commentLines;
    private int lines;
    public Contribution(Contributor contributor, Tag tag, Documentation documentation, int commentLines, int lines){
        Objects.requireNonNull(contributor, "the contribution associated to the tag cannot be null");
        Objects.requireNonNull(tag, "the tag associated to the contribution cannot be null");
        Objects.requireNonNull(documentation, "the documentation associated to the contribution cannot be null");
        if(commentLines < 0){
            throw new IllegalArgumentException("the number of comment line cannot be lower than 0 ");
        }
        if(lines < 0){
            throw new IllegalArgumentException("the number of line cannot be lower than 0 ");
        }

        this.lines = lines;
        this.commentLines = commentLines;
        this.documentation = documentation;
        this.tag = tag;
        this.contributor = contributor;
    }
    @Override
    public boolean equals(Object obj){
        return obj instanceof Contribution contrib && contrib.tag.equals(tag) && contrib.contributor.equals(contributor)
           && contrib.documentation.equals(documentation);
    }

    /**
     *
     * This method is an accessor to the tag associated to a contribution
     *
     * @return
     * the tag from the contribution
     */

    public Tag tag(){
        return tag;
    }

    /**
     *
     * This method is an accessor to the contributor associated to a contribution
     *
     * @return
     * the contributor associated to the contribution.
     */

    public Contributor contributor(){
        return contributor;
    }

    /**
     * this method increases the number of lines of commentLine and code line.
     * @param commentLine
     * the number of commentLine to increase
     * @param codeLine
     * the of code line to increase
     */
    public void increaseLine(int commentLine, int codeLine){

        commentLines+= commentLine;
        lines += codeLine;

    }

    /**
     * an acccessor to the field "documentation"
     * @return
     * the documentation associated to the contribution.
     */
    public Documentation documentation(){
        return documentation;
    }
    @Override
    public String toString(){
        var bd = new StringBuilder();
        bd.append("Contribution's data : \n");
        bd.append(contributor.toString());bd.append("\n");
        bd.append(tag.toString());
        bd.append(documentation.toString());
        bd.append("the number of non-comment lines : ");
        bd.append(lines);bd.append("\n the number of comment lines : ");
        bd.append(commentLines);
        bd.append("\n");
        return bd.toString();
    }

    @Override
    public int hashCode() {
        return contributor.hashCode()^tag.hashCode()^documentation.hashCode();
    }
}

