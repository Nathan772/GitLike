package fr.uge.gitclout.app;

import fr.uge.gitclout.db.RepositoryRepository;
import fr.uge.gitclout.db.TagRepository;
import fr.uge.gitclout.git.GitManager;
import fr.uge.gitclout.model.Repository;
import fr.uge.gitclout.model.Tag;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.eclipse.jgit.api.errors.GitAPIException;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Map;

@Singleton
public class ApplicationUtils {

    private final RepositoryRepository repositoryRepository;
    private final TagRepository tagRepository;

    @Inject
    public ApplicationUtils(RepositoryRepository repositoryRepository, TagRepository tagRepository) {
        this.repositoryRepository = repositoryRepository;
        this.tagRepository = tagRepository;
    }

    static Map<String, String> createJsonResponse(String status, String message) {
        return Map.of("status", status, "message", message);
    }

    Repository processRepository(String repositoryURL) throws GitAPIException, IOException {
        var git = new GitManager(repositoryURL);
        if (!git.isCloned()) {
            var repo = git.cloneRepository();
            repositoryRepository.save(repo);
            return repo;
        } else if (repositoryRepository.findByURL(repositoryURL).isPresent()) {
            return repositoryRepository.findByURL(repositoryURL).get();
        } else {
            throw new RuntimeException("Repository not found");
        }
    }

    Repository getRepositoryById(long id) {
        if (repositoryRepository.findById(id).isPresent()) {
            return repositoryRepository.findById(id).get();
        }
        throw new RuntimeException("Repository not found");
    }

    @Transactional
    Flux<Tag> processTags(Repository repository) {
        return tagRepository.findAllByRepositoryId(repository.getId())
                .switchIfEmpty(Flux.defer(() -> {
                    try {
                        var git = GitManager.fromRepository(repository);
                        var tags = git.getTags();
                        tags.forEach(tag -> tag.setRepository(repository));
                        tagRepository.saveAll(tags);
                        return Flux.fromIterable(tags);
                    } catch (IOException | GitAPIException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

//    Flux<Tag> processTags(Repository repository) throws IOException, GitAPIException {
//        if (tagRepository.findAllByRepositoryId(repository.getId()) != null) {
//            return tagRepository.findAllByRepositoryId(repository.getId());
//        }
//        var git = GitManager.fromRepository(repository);
//        var tags = git.getTags();
//        tags.forEach(tag -> tag.setRepository(repository));
//        tagRepository.saveAll(tags);
//        return tags;
//    }


}
