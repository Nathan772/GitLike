/*package fr.uge.gitclout.contribution;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AuthorContributionsTest {
    @Test
    void getterAreFunctional(){
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
        var authorContrib = new AuthorContributions(author, mapContribution);
        assertEquals("Jean",authorContrib.getAuthor());
        assertEquals(mapContribution,authorContrib.getContributionsByType());
    }

    @Test
    void exceptionAreThrownWhenFieldIsInconsistentForAddContribution(){
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
        var authorContrib = new AuthorContributions(author, mapContribution);
        assertThrows(NullPointerException.class,()->
                authorContrib.addContribution(null,"Java",0,0)
        );
        assertThrows(NullPointerException.class,()->
                authorContrib.addContribution(ContributionType.CODE,null,0,0)
        );
        assertThrows(IllegalArgumentException.class,()->
                authorContrib.addContribution(ContributionType.CODE,"Java",-1,0)
        );
        assertThrows(IllegalArgumentException.class,()->
                authorContrib.addContribution(ContributionType.CODE,"Java",0,-1)
        );
    }

    @Test
    void exceptionAreThrownWhenFieldIsInconsistentForConstructor(){
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
 */