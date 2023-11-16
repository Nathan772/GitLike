import { For, createSignal } from 'solid-js';
// import {useNavigate} from "@solidjs/router";
import { postRepository } from "../utils";

function Landing() {
    //const navigate = useNavigate();

    const [repoURL, setRepoURL] = createSignal('');

    const pushToRepo = async () => {
        console.log(await postRepository(repoURL()));
        //navigate(`/repo/${id}`);
    }

    return (
        <>
            <main class="container min-vh-100 d-flex flex-column justify-content-center align-items-center">
                <h1 class="mb-3">GitClout</h1>
                <form class="d-flex justify-content-center align-items-center gap-2 w-50">
                    <input type="url" class="form-control flex-fill" placeholder="URL of a git repository" value={repoURL()} onInput={(e: any) => setRepoURL(e.target.value)} />
                    <button type="button" class="btn btn-primary" onClick={() => pushToRepo()}>Analyze</button>
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
        </>
    )
}

export default Landing