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
            if(contribution.tag().equals(tag1) && contribution.documentation().equals(documentation) && contribution.contributor().equals(contributor)){
                return Optional.of(contribution);
            }
        }
        //the contribution doesn't already exist
        return Optional.empty();
    }

    /**
     * this method retrieves all the contributor associated to this tag
     * @param tag
     * the tag chosen
     * @return
     * A list that contains all the contributors for this tag
     */
    public List<Contributor> findContributorsByTag(Tag tag){
        Objects.requireNonNull(tag);
        ArrayList<Contributor> result = new ArrayList<>();
        for(var contribution:contributions){
            if(contribution.tag().equals(tag) && !result.contains(contribution.contributor())){
                result.add(contribution.contributor());
            }
        }
        //sort to always keep a consistent order
        return result.stream().sorted().toList();
    }
}
