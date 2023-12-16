import { For } from 'solid-js';
import DarkModeSwitch from '../components/DarkModeSwitch';
import InputRepository from '../components/InputRepository';

function Home() {
    return (
        <>
            <main class="container min-vh-100 d-flex flex-column justify-content-center align-items-center">
                <h1 class="mb-3">GitClout</h1>

                <InputRepository />

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

                <div class="position-absolute bottom-0 end-0 p-3">
                    <DarkModeSwitch />
                </div>
            </main>
        </>
    )
}

export default Home