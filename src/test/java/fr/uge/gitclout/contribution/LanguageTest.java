package fr.uge.gitclout.contribution;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {
    @Test
    void exceptionAreThrownWhenFieldIsInconsistent(){
        assertThrows(NullPointerException.class,()->
                new Language(null)
        );
    }

}