import { useParams } from "@solidjs/router";
import { For, createSignal, Show, onMount } from 'solid-js';
import { getTags } from "../utils";

function Repo() {
    const [showModal, setShowModal] = createSignal(false);
    const [name, setName] = createSignal('');
    const [url, setUrl] = createSignal('');
    const [tags, setTags] = createSignal([]);

    onMount(async () => {
        const id = useParams().id;
        setShowModal(true)
        await getTags(id).then((res: any) => {
            setShowModal(false)
            setName(res.data.repo.name)
            setUrl(res.data.repo.URL)
            console.log(url())
            setTags(res.data.tags)
        }).catch((err: any) => {
            setShowModal(false)
            console.log(err)
        });

    });

    return (
        <>
            <main class="container mt-5">
                <h1>{name()}</h1>
                <p>{url()}</p>

                <div class="mt-5">
                    <h2 class="h3">Tags</h2>
                    <ul>
                        <For each={tags()}>
                            {(tag: any) => (
                                <li>
                                    {tag.name}
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
                        <p class='h5'>Resolving tags</p>
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

export default Repo