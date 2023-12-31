/*package fr.uge.gitclout.contribution;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ContributionTypeDetailsTest {
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
        var languageCount = new LanguageCount("Java", 2 ,3);
        var hashLang = new HashMap<String,LanguageCount>();
        hashLang.put("Java",languageCount);
        assertThrows(NullPointerException.class,()->
                new ContributionTypeDetails(1,null)
        );
        assertThrows(IllegalArgumentException.class,()->
                new ContributionTypeDetails(-1,hashLang)
        );
    }

}
 */