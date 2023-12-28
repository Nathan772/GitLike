package fr.uge.gitclout.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {
    @Test
    void getterAreFunctional(){
        var repo = new Repository("testURL","testRepoName","testRepositoryLocalPath");
        assertEquals("testURL",repo.getRepositoryURL());
        assertEquals("testRepoName",repo.getRepositoryName());
        assertEquals("testRepositoryLocalPath",repo.getRepositoryLocalPath());
    }

    @Test
    void setterAreFunctional(){
        var repo = new Repository("testURL","testRepoName","testRepositoryLocalPath");
        repo.setRepositoryName("blibli");
        repo.setRepositoryLocalPath("blublu");
        repo.setRepositoryURL("bleble");
        assertEquals("bleble",repo.getRepositoryURL());
        assertEquals("blibli",repo.getRepositoryName());
        assertEquals("blublu",repo.getRepositoryLocalPath());
    }

    @Test
    void exceptionAreThrownWhenFieldIsNull(){
        assertThrows(NullPointerException.class,()->
                new Repository(null,"testRepoName","testRepositoryLocalPath")
        );
        assertThrows(NullPointerException.class,()->
                new Repository("blibli",null,"testRepositoryLocalPath")
        );
        assertThrows(NullPointerException.class,()->
                new Repository("bleble","testRepoName",null)
        );

    }


}
