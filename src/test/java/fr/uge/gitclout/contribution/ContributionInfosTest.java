//package fr.uge.gitclout.contribution;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ContributionInfosTest {
//    @Test
//    void exceptionAreThrownWhenFieldIsInconsistent(){
//        var languageCount = new LanguageCount("Java", 2 ,3);
//        var languageCount2 = new LanguageCount("CSS", 5,3);
//        var hashLang = new HashMap<String,LanguageCount>();
//        hashLang.put("Java",languageCount);
//        hashLang.put("CSS", languageCount2);
//        assertThrows(IllegalArgumentException.class,()->
//                new ContributionInfos(hashLang,-1)
//        );
//        assertThrows(NullPointerException.class,()->
//                new ContributionInfos(null,2)
//        );
//        assertDoesNotThrow(() ->
//                        new ContributionInfos(hashLang,13)
//                );
//    }
//
//}