import { For, createSignal, Show } from 'solid-js';
import { useNavigate } from "@solidjs/router";
import { postRepository } from "../utils";

function Landing() {
    const navigate = useNavigate();

    const [repoURL, setRepoURL] = createSignal('');
    const [showModal, setShowModal] = createSignal(false);

    const pushToRepo = async (e: Event) => {
        e.preventDefault();
        e.stopPropagation();
        setShowModal(true);
        postRepository(repoURL()).then((res: any) => {
            setShowModal(false);
            const id = res.data.repo.id;
            navigate(`/repo/${id}`);
        }).catch((err: any) => {
            setShowModal(false);
            console.log(err);
        });
    }

    return (
        <>
            <main class="container min-vh-100 d-flex flex-column justify-content-center align-items-center">
                <h1 class="mb-3">GitClout</h1>
                <form class="d-flex justify-content-center align-items-center gap-2 w-50" onSubmit={(e) => pushToRepo(e)}>
                    <input type="url" class="form-control flex-fill" placeholder="URL of a git repository" value={repoURL()} onInput={(e: any) => setRepoURL(e.target.value)} />
                    <button type="submit" class="btn btn-primary">Analyze</button>
                </form>

                <div class="mt-5 w-50">
                    <h2 class="h3">History</h2>
                    <ul>
                        <For each={[]}>
                            {(repo: any) => (
                                <li>
                                    <a href={repo.url} target="_blank" rel="noreferrer">{repo.name}</a>
                                </li>
                            )}
                        </For>
                    </ul>
                </div>
            </main>

            <Show when={showModal()}>

                <div id="modal" class="position-absolute top-0 min-vh-100 min-vw-100 d-flex justify-content-center align-items-center">
                    <div class='min-vh-100 min-vw-100 bg-dark bg-opacity-50'></div>
                    <div class='position-absolute bg-white p-4 rounded d-flex flex-column gap-3'>
                        <p class='h5'>Resolving git repository</p>
                        <div class="d-flex justify-content-center">
                            <div class="spinner-border" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                        </div>
                    </div>
                </div>

            </Show>
        </>
    )
}

export default Landing