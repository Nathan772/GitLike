package fr.uge.gitclout.Contribution;

import fr.uge.gitclout.Documentation.Documentation;
import fr.uge.gitclout.Contributor.Contributor;
import fr.uge.gitclout.Tag.Tag;

import java.util.*;

/**
 * this class enable to load a group of contribution
 */
public class ContributionLoader {
    private final ArrayList<Contribution> contributions;
    public ContributionLoader(){
        contributions = new ArrayList<Contribution>();
    }

    /**
     *
     * this method enables to add an element to the contribution loader.
     * @param contribution
     *  the element you want to add
     */
    public void add(Contribution contribution){
        Objects.requireNonNull(contribution);
        contributions.add(contribution);
    }
    /**
     * this method give access to a copy of the list of contributions.
     * @return
     * a copy of the list of contributions
     */
    public List<Contribution> contributions(){
        return List.copyOf(contributions);
    }
    @Override
    public String toString(){
        return contributions.toString();
    }

    /**
     * the element you want to retrieve
     * @param index
     * the index of the element you want to retriee
     * @return
     * the element you want to retrieve
     */
    public Contribution get(int index){
        Objects.checkIndex(index, contributions.size());
        return contributions.get(index);
    }

    /**
     * this method enable to remove an element from its contribution.
     * @param contribution
     * the contribution you want to remove
     */
    public void remove(Contribution contribution){
        Objects.requireNonNull(contribution);
        contributions.remove(contribution);
    }
    /**
     * this method enable to remove an element from its index.
     * @param index
     * the index of the contribution you want to remove
     */
    public void remove(int index){
        Objects.checkIndex(index, contributions.size());
        contributions.remove(index);
    }

    /**
     * this method enable to retrieve a contribution from its description.
     * @param tag1
     * the tag of the contribution.
     * @param contributor
     * the nanme of the contributor
     * @param documentation
     * the kind of documentation
     * @return
     * the contribution if it exists or an empty optional if it doesn't
     */
    public Optional<Contribution> getFromDescription(Tag tag1, Contributor contributor, Documentation documentation){
        Objects.requireNonNull(tag1); Objects.requireNonNull(contributor);Objects.requireNonNull(documentation);
        for(var contribution:contributions){
            if(contribution.tag().equals(tag1) && contribution.documentation().equalsCategory(documentation) && contribution.contributor().equals(contributor)){
                return Optional.of(contribution);
            }
        }
        //the contribution doesn't already exist
        return Optional.empty();
    }

    /**
     * this method retrieves all the contributor associated to this tag
     * @param tagName
     * the tag chosen
     * @return
     * A list that contains all the contributors for this tag
     *
     *
     * */
     public List<Contributor> getContributorsByTag(String tagName){
        Objects.requireNonNull(tagName);
        ArrayList<Contributor> result = new ArrayList<>();
        for(var contribution:contributions){
            if(contribution.tag().getName().equals(tagName) && !result.contains(contribution.contributor())){
                result.add(contribution.contributor());
            }
        }
        //sort to always keep a consistent order
        return result.stream().sorted().toList();
    }

    /**
     * this method enables to retrieve every contribution made a user for a specific tag.
     * It also merge the contribution of the same type.
     * @param tagName
     * the tag related to the contribution
     * @param userName
     * the user identifcator
     * @param userEmail
     * the user email adresss
     * @return
     * all the contribution made by this user for this tag
     */
    public List<Contribution> getContributionByUserAndTag(String tagName, String userName, String userEmail){
        var contributionsList = new ArrayList<Contribution>();
        for(var contribution:contributions){
            if(contribution.contributor().name().equals(userName) && contribution.contributor().email().equals(userEmail)
            & contribution.tag().getName().equals(tagName)){
                contributionsList.add(contribution);
            }
        }
        return contributionsList;
    }

    /**
     *
     * this method enables to merge contributions by categories.
     * The category that are alike (BUILD WITH BUILD, CONFIG WITH CONFIG, ....) will be merged.
     * @param contributions
     * the contribution you want to merge
     * @return
     * the contribution merged by category

    public List<Contribution> groupingContributionByCategory(ArrayList<Contribution> contributions){
        var contributionGrouped = new ArrayList<Contribution>();
        for(var contribution:contributions){

        }
    }*/

     /**
     * this method retrieves all the contributor associated to this tag
     * @param tagName
     * the tag chosen
     * @return
     * A map that contains all the contributors for this tag
     public Map<String, String> getContributorsByTag(String tagName){
        Objects.requireNonNull(tagName);
        HashMap<String, String> result = new HashMap<String,String>();
        for(var contribution:contributions){
            if(contribution.tag().getName().equals(tagName)){
                //feed with email-name from contributor
                result.put(contribution.contributor().email(), contribution.contributor().name());
            }
        }
        //sort to always keep a consistent order
        return result;
    }*/



    /**
     * This methods enables to know if the contributions already have been added
     * @return
     * true if it's empty (no contribution yet), false if not empty (there are contributions)
     */
    public boolean isEmpty(){
        return contributions.isEmpty();
    }
}
