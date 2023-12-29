package fr.uge.gitclout.contribution;

import fr.uge.gitclout.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageCountTest {

    @Test
    void getterAreFunctional(){
        var languageCount = new LanguageCount("JAVA",2,3);
        assertEquals("JAVA",languageCount.getLanguage());
        assertEquals(3,languageCount.getCommentCount());
        assertEquals(2,languageCount.getCount());
    }

    @Test
    void incrementationWorks(){
        var languageCount = new LanguageCount("JAVA",2,3);
        languageCount.incrementCount(5,7);
        assertEquals(7,languageCount.getCount());
        assertEquals(10,languageCount.getCommentCount());
    }

    @Test
    void exceptionAreThrownWhenFieldIsInconsistent(){
        assertThrows(NullPointerException.class,()->
                new LanguageCount(null,2,3)
        );
        assertThrows(IllegalArgumentException.class,()->
                new LanguageCount("blibli",-1,2)
        );
        assertThrows(IllegalArgumentException.class,()->
                new LanguageCount("bleble",0,-2)
        );

    }

}