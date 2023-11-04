import {For} from 'solid-js';
import {useNavigate} from "@solidjs/router";

const r = {
    "status": "success",
    "data": [
        {
            "repo_id": 12345,
            "name": "link1",
            "url": "https://github.com/link1"
        },
        {
            "repo_id": 67890,
            "name": "link2",
            "url": "https://github.com/link2"
        }
    ]
}

function Landing() {
    const navigate = useNavigate();

    const pushToRepo = (id: number) => {
        navigate(`/repo/${id}`);
    }
    
    return (
        <main class="container min-vh-100 d-flex flex-column justify-content-center align-items-center">
            <h1 class="mb-3">GitClout</h1>
            <form class="d-flex justify-content-center align-items-center gap-2 w-50">
                <input type="url" class="form-control flex-fill" placeholder="URL of a git repository"/>
                <button type="button" class="btn btn-primary" onClick={() => pushToRepo(1)}>Analyze</button>
            </form>

            <div class="mt-5 w-50">
                <h2 class="h3">Hisorique</h2>
                <ul>
                    <For each={r.data}>
                        {(repo: any) => (
                            <li>
                                <a href={repo.url} target="_blank" rel="noreferrer">{repo.name}</a>
                            </li>
                        )}
                    </For>
                </ul>
            </div>
        </main>
    )
}

export default Landing