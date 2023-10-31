package fr.uge.gitclout.database;


import fr.uge.gitclout.repositories.Contributes;
import fr.uge.gitclout.repositories.Contributor;
import fr.uge.gitclout.repositories.Repository;
import fr.uge.gitclout.repositories.Tag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataBaseHandler {
    private final Git git;
    private final String url;
    private final String repoPath;

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
        repoPath = "repo/"+matcher.group(1);

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
     * Fill an hashMap "user, number of line"  with the number of line by user using the result of a blame
     * @param hashUserLine
     * the hashMap you want to fill
     * @
     */
    public static void feedHashWithBlame(HashMap<String, Integer> hashUserLine, BlameResult blameResult){
        //retrieve the total of rows of the blame
        for (int i = 0; i < blameResult.getResultContents().size(); i++) {
            //create a tuple for this user
            hashUserLine.computeIfAbsent(blameResult.getSourceAuthor(i).getName(), (k) -> 1);
            //increase by one the number of line for this user
            hashUserLine.computeIfPresent(blameResult.getSourceAuthor(i).getName(), (k, v) -> v + 1);
        }
    }

    /**
     *
     * This method blame every version (tag) of a given file and display the number of line by user.
     * @param filePath
     * the path for the path you want to blame
     * @throws GitAPIException
     * handle the use of blame
     * @return
     * a HashMap with the pair : <UserName, Line>
     */
    public HashMap<String, Integer> parseOneFileForEachTag(String filePath) throws GitAPIException {
        Objects.requireNonNull(filePath, "the file you're parsing cannot be null");
        HashMap<String, Integer> hashUserLine = new HashMap<String, Integer>();
        for(var tag:retrieveTags()) {
            var blameResult = git.blame().setFilePath(filePath).setStartCommit(tag.getObjectId()).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
            if(blameResult != null) {
                feedHashWithBlame(hashUserLine, blameResult);
            }
        }
        return hashUserLine;
    }
    /**
     * This method take all the contributions for each contributor from a specific tag
     * and a specific file and feed a list with it.
     * @param contributes
     * the list of contribution for each contributor that you want to fill
     * @param filePath
     * the path you want to retrieve contribution
     * @param tag
     * the tag you associated with these contributions
     * @param hashUserLine
     * a hash map that contains the number of line by user for the file "filePath" and the tag "tag"
     */
    public void fromMapToListContribution(ArrayList<Contributes> contributes, HashMap<String, Integer> hashUserLine, String filePath,  Ref tag){
        Objects.requireNonNull(tag);
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(hashUserLine);
        Objects.requireNonNull(contributes);
        /*var repo = new Repository(url);
        var file = new File(filePath,,repo);
        var tag =  new Tag(tag.getName(), repo, 0,line);
        hashUserLine.forEach((user,line) ->
                contributes.add(
                new Contributor(user),

                )
        );*/
    }
    /**
     *
     * This method blame every version (tag) of a given file and display the number of line by user.
     * @param filePath
     * the path for the path you want to blame
     * @throws GitAPIException
     * handle the use of blame
     * @return
     * a list with all the contribution
     */
    public List<Contributes> parseOneFileForEachTagWithContributors(String filePath) throws GitAPIException {
        Objects.requireNonNull(filePath, "the file you're parsing cannot be null");
        HashMap<String, Integer> hashUserLine = new HashMap<String, Integer>();
        var listContributes = new ArrayList<Contributes>();
        for(var tag:retrieveTags()) {
            var blameResult = git.blame().setFilePath(filePath).setStartCommit(tag.getObjectId()).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
            if(blameResult != null) {
                feedHashWithBlame(hashUserLine, blameResult);
                fromMapToListContribution(listContributes, hashUserLine, filePath, tag);
            }
        }
        return listContributes;
    }
    /**
     * @param directoryForRepoStorage
     * the directory where all the repo are stored
     * this method enables to retrieve every file path from the current repo.
     * @return
     * all the files associated to the repo.
     */
    public List<String> retrieveEveryFileFromRepo(String directoryForRepoStorage) throws IOException {
        Path startDir = Paths.get(repoPath);
        List<Path> fileList = Files.walk(startDir).filter(Files::isRegularFile).toList();
        var pattern = Pattern.compile(".*\\.com\\/(.*)");
        var matcher = pattern.matcher(url);
        //if the url is inconsistent with what was expected
        if(!matcher.find())
            //the regex doesn't match with a url format, it's not supposed to happen
            return new ArrayList<String>();

        //var pattern = Pattern.compile(".*\\.com\\/(.*)");
        var files = fileList.stream().map(Path::toString).collect(Collectors.toList());
        //retrieve the part of the file name which is relevant
        var files2 = files.stream().map(x->x.substring((directoryForRepoStorage+matcher.group(1)).length()+1)).toList();
        return files2;
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

    /**
     *
     * This method parse every file from the repo and give the user per line.
     * @param repoDirectory
     * The repo from which all the project are stored
     * @throws GitAPIException
     * exception for the use of method from retrieveEveryFileFromDepo.
     * @throws IOException
     * exception for the use of the method from parseOneFileForEachTag.
     */

    public void parseEveryFileFromCurrentRepo(String repoDirectory) throws GitAPIException, IOException {
        int compteur = 0;
        for(var file:retrieveEveryFileFromRepo(repoDirectory)) {
            var hmapUser = parseOneFileForEachTag(file);
            if(compteur == 0 || compteur == 1){
                System.out.println("les infos du fichier "+file+" sont : "+hmapUser);
            }
            compteur++;
        }
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
            //handler2.parseOneFileForEachTag("package.json");
            //System.out.println(""+handler2.retrieveEveryFileFromRepo());
            //handler2.parseEveryFileFromCurrentRepo();
            //System.out.println("Les hashMaps : "+handler2.parseOneFileForEachTag("package.json"));
            //System.out.println("Les hashMaps : "+handler2.retrieveEveryFileFromRepo("repo/"));
            handler2.parseEveryFileFromCurrentRepo("repo/");
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
       // System.out.println("On affiche les tags "+res2);
       // System.out.println("On affiche la hashMap "+hmUsers);
    }

}
