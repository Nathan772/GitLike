package fr.uge.gitclout.tag;

import fr.uge.gitclout.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void getterAreFunctional(){
        var repo = new Repository("testURL","testRepoName","testRepositoryLocalPath");
        var tag = new Tag("tagName","gitName",repo);
        assertEquals(repo,tag.getRepository());
        assertEquals("gitName",tag.getRefTagName());
        assertEquals("tagName",tag.getTagName());
    }

    @Test
    void settersAreFunctional(){
        var repo = new Repository("testURL","testRepoName","testRepositoryLocalPath");
        var repo2 = new Repository("testURL2","testRepoName","testRepositoryLocalPath");
        var tag = new Tag("tagName","gitName",repo);
        tag.setId(25L);
        tag.setTagName("bloblo");
        tag.setRepository(repo2);
        tag.setRefTagName("blibli");
        assertEquals(repo2,tag.getRepository());
        assertEquals("blibli",tag.getRefTagName());
        assertEquals("bloblo",tag.getTagName());
        assertEquals(25L,tag.getId());
    }

    @Test
    void exceptionAreThrownWhenFieldIsNull(){
        var repo = new Repository("testURL","testRepoName","testRepositoryLocalPath");
        assertThrows(NullPointerException.class,()->
                new Tag("tagName","gitName",null)
        );
        assertThrows(NullPointerException.class,()->
                new Tag(null,"gitName",repo)

        );
        assertThrows(NullPointerException.class,()->
                new Tag("blibli",null,repo)
        );
        assertDoesNotThrow(()->
                new Tag("tagName","gitName",repo)
        );

    }

}