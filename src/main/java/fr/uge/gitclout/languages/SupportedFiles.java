package fr.uge.gitclout.languages;

import fr.uge.gitclout.contribution.ContributionType;

public sealed interface SupportedFiles permits Language, Documentation {
    /**
     * this method tells either a supportedFile contain or not comments
     * @return
     * true if yes, if no, false
     */
    public boolean containsComments();

    /**
     * this field refers to the file extension
     @return
     the extension for this language
     */
    public String extension();

    /**
     * this method is a getter to the field name
     * @return
     * the name of the file
     */
    public String name();

    /**
     * this methods enables to know the type of contribution it represents.
     * @return
     * the type of contribution it represents;
     */
    public ContributionType getType();
}