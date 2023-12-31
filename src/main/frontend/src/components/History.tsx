import { For, Show, createSignal, onMount } from 'solid-js';
import { getHistory } from '../utils';
import { HistoryItem } from '../types/HistoryItem';
import { A } from "@solidjs/router"
import { deleteHistoryItem } from "../utils"

function History() {
    const [history, setHistory] = createSignal([] as HistoryItem[]);

    function deleteItem(url: string) {
        deleteHistoryItem(url);
        setHistory(history().filter((item: HistoryItem) => item.URL !== url));
    }

    onMount(async () => {
        const history = await getHistory();
        for (const repo of history.data.repositories) {
            repo.gitCloutUrl = `http://localhost:8080/#/repository?url=${repo.URL}`;
        }
        setHistory(history.data.repositories);
    });
    return (
        <>
            <Show when={history().length > 0}>
                <h2 class="h3">History</h2>
                <For each={history()}>
                    {(repo: any) => (
                        <div class="card mb-2">
                            <div class="card-body">
                                <h5 class="card-title">{repo.name}</h5>
                                <p class="card-text">{repo.URL}</p>
                                <A href={repo.gitCloutUrl} class="btn btn-primary">See analyzed repository</A>
                                <button type="button" class="btn btn-danger ms-2" onClick={() => deleteItem(repo.URL)}>Delete</button>
                            </div>
                        </div>
                    )}
                </For>
            </Show>
        </>
    )
}

export default History