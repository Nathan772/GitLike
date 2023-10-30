package fr.uge.gitclout.database;


import fr.uge.gitclout.repositories.Contributor;
import fr.uge.gitclout.repositories.Tag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class DataBaseHandler {
    private final Git git;
    private final String url;

    /**
     * Constructor that enables to open an already existing project
     * @param url
     * The path for a project either it's existing or not.
     * precise if the project already exists or not
     * @throws GitAPIException
     * @throws IOException
     */
    public DataBaseHandler(String url) throws GitAPIException, IOException {
        Objects.requireNonNull(url);
        //retrieve the regex for the url path
        var pattern = Pattern.compile(".*\\.com\\/(.*)");
        this.url = url;

        //capture the expression
        var matcher = pattern.matcher(url);
        //if the url is inconsistent with what was expected
        if(!matcher.find())
            throw new IllegalArgumentException("url must match with the regex expected for an url");
        //if the url is consistent with what was expected
        var file = new File("repo/"+matcher.group(1));

        //if the project had already been imported
        if(file.exists() && file.isDirectory())
            //case when the project has already been import
            git = Git.open(file);
        //if the project never had been imported
        else
            // clone the repo in a directory which has the same name as the repo
            // gives the path for the location of the directory
            git = Git.cloneRepository().setURI(url).setDirectory(file).call();
    }

    /**
     *
     * This method performs a blame on the file given as an argument.
     *
     * @param filePath
     * The file you want to parse.
     * You have to give the path from the project repository until the file itself.
     *
     */
    public BlameResult retrieveBlameFileResult(String filePath) throws GitAPIException {
        Objects.requireNonNull(filePath, "the file analyzed by blame cannot be null");
        return git.blame().setFilePath(filePath).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
    }

    /**
     * give the list of tags that are with the repo git.
     *
     * @return
     * the list of tags associated to the git repo.
     *
     */
    public List<Ref> retrieveTags() throws GitAPIException {
        return git.tagList().call();
    }

    /**
     *
     * This method blame every version of a given file and display the number of line by user.
     * @param filePath
     * the path for the path you want to blame
     * @throws GitAPIException
     */
    public void analyseOneFileForEachTag(String filePath) throws GitAPIException {
        var tags = retrieveTags();
        for(var tag:tags) {
            var blameResult = git.blame().setFilePath(filePath).setStartCommit(tag.getObjectId()).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
            HashMap<String, Integer> hashUserLine = new HashMap<String, Integer>();
            //retrieve the total of rows of the blame
            for (int i = 0; i < blameResult.getResultContents().size();i++) {
                //create a tuple for this user
                hashUserLine.computeIfAbsent(blameResult.getSourceAuthor(i).getName(), (k)-> 1);
                //increase by one the number of line for this user
                hashUserLine.computeIfPresent(blameResult.getSourceAuthor(i).getName(), (k,v)->v+1);

                if(i==blameResult.getResultContents().size()-1) {
                    System.out.println("Pour le tag : "+tag.getName()+", le nombre de ligne est : "+hashUserLine);
                }
            }

        }




    }

    public static String parseTag(Ref tag){
        return tag.getName();
    }

    /**
     *
     * This method performs a blame on the file given as an argument and
     * create a tuple with the number of lines for each user associated to the file.
     *
     * @param filePath
     * The file you want to parse.
     * You have to give the path from the project repository until the file itself.
     *
     */
    public HashMap<String, Integer> givesUsersContributionLines(String filePath) throws GitAPIException {
        var blameResult = retrieveBlameFileResult(filePath);
        HashMap<String, Integer> hashUserLine = new HashMap<String, Integer>();
        //retrieve the total of rows of the blame
        for (int i = 0; i < blameResult.getResultContents().size();i++) {
            //create a tuple for this user
            hashUserLine.computeIfAbsent(blameResult.getSourceAuthor(i).getName(), (k)-> 1);
            //increase by one the number of line for this user
            hashUserLine.computeIfPresent(blameResult.getSourceAuthor(i).getName(), (k,v)->v+1);
        }

        return hashUserLine;


    }

    /**
     * Close the git repository.
     */
    public void close() {
        git.close();
    }


    public List<Contributor> getContributors(Tag tag) {
        return null;
    }

    public static void main(String args[]) {

        HashMap<String,Integer> hmUsers = new HashMap<String, Integer>();
        var res2 = new StringBuilder();
        try {
            //"https://github.com/vuejs/core")
            //https://gitlab.com/seilliebert-bilingi/SEILLIEBERT-BILINGI
            //https://github.com/NathanBil/Splendor_for_visitors
            //package.json
            var repositoryPath = "https://github.com/vuejs/core";
            var handler2 = new DataBaseHandler(repositoryPath);
            // hmUsers = handler2.givesUsersContributionLines("package.json");
            //nécessite un deuxième appel avec les fichiers déjà importés
            //var listTags = handler2.retrieveTags();
            handler2.analyseOneFileForEachTag("package.json");
            //for(var tag:listTags) {
            //git = Git.cloneRepository().setURI(url).setDirectory(new File("repo/" + matcher.group(1))).call();}
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
       // System.out.println("On affiche les tags "+res2);
       // System.out.println("On affiche la hashMap "+hmUsers);
    }

}
