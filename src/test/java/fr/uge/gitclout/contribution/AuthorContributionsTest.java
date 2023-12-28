package fr.uge.gitclout.contribution;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AuthorContributionsTest {
    @Test
    void getterAreFunctional(){
        var languageCount = new LanguageCount("Java", 2 ,3);
        var hashLang = new HashMap<String,LanguageCount>();
        hashLang.put("Java",languageCount);
        var contributionTypeDetails = new ContributionTypeDetails(5,hashLang);
        assertEquals(5,contributionTypeDetails.getTotal());
        assertEquals(hashLang,contributionTypeDetails.getDetails());
    }

    @Test
    void addContributionWorks(){
        var languageCount = new LanguageCount("Java", 2 ,3);
        var hashLang = new HashMap<String,LanguageCount>();
        var languageCount2 = new LanguageCount("Python", 2 ,3);
        hashLang.put("Java",languageCount);
        hashLang.put("Python", languageCount2);
        var contributionTypeDetails = new ContributionTypeDetails(10,hashLang);
        contributionTypeDetails.addContribution("Python", 5, 6);
        assertEquals(21,contributionTypeDetails.getTotal());
        //assertEquals(hashLang,contributionTypeDetails.getDetails());
    }

    @Test
    void exceptionAreThrownWhenFieldIsInconsistent(){
        //var languageCount = new LanguageCount("Java", 2 ,3);
        var languageCount = new LanguageCount("Java", 2 ,3);
        var hashLang = new HashMap<String,LanguageCount>();
        var languageCount2 = new LanguageCount("Python", 2 ,3);
        hashLang.put("Java",languageCount);
        hashLang.put("Python", languageCount2);
        var contributionTypeDetails = new ContributionTypeDetails(10,hashLang);
        var mapContribution = new HashMap<ContributionType, ContributionTypeDetails>();
        mapContribution.put(ContributionType.CODE,contributionTypeDetails);
        var author = "Jean";
        hashLang.put("Java",languageCount);
        assertThrows(NullPointerException.class,()->
                new AuthorContributions(null,mapContribution)
        );
        assertThrows(NullPointerException.class,()->
                new AuthorContributions(author,null)
        );
    }

}