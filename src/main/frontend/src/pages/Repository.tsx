import { For, Show, createSignal, onMount } from 'solid-js';
import { getRepoInfos } from "../utils";
import { useNavigate, useSearchParams } from '@solidjs/router';
import { Modal } from 'bootstrap';
import { JSONResponse, RepositoryInfos, Contributions } from '../types/JSONResponse';
import { setStoreError } from '../store';
import { Toasts, addToast } from '../components/Toasts';
import { ToastInfo } from '../types/ToastInfo';

import Stats from '../components/Stats';

function Repo() {

    const navigate = useNavigate();
    const [name, setName] = createSignal('');
    const [url, setUrl] = createSignal('');
    const [tags, setTags] = createSignal([] as string[]);

    const [progress, setProgress] = createSignal(0);

    const [selectedTag, setSelectedTag] = createSignal('' as string);
    const [dataTags, setDataTags] = createSignal([] as Contributions[]);

    onMount(async () => {
        const modal = new Modal(document.getElementById('modal')!);
        const modalElement = document.getElementById('modal')!;
        const progressBar = document.querySelector('.progress-bar')! as HTMLElement;
        const [searchParams] = useSearchParams();
        const url = searchParams.url;
        if (!url) {
            navigate('/not-found');
            return;
        }
        modalElement.addEventListener('shown.bs.modal', async () => {
            const res = await getRepoInfos(url) as JSONResponse;
            const data = res.data as RepositoryInfos;
            modal.hide();
            if (res.status === "success") {
                setName(data.name);
                setUrl(data.URL);
                setTags(data.tags);

                setSelectedTag(data.tags[0]);

                const info: ToastInfo = {
                    type: "success",
                    message: res.message
                };

                addToast(info);

                const begin = new Date().getTime();

                const evtSource = new EventSource(`http://localhost:8080/api/analyze-tags?url=${url}`);

                const infoCloning: ToastInfo = {
                    type: "info",
                    message: "Cloning repository... Please wait."
                };

                addToast(infoCloning);

                let count = 0;

                evtSource.onmessage = (event) => {
                    const data = JSON.parse(event.data);
                    console.log(data);
                    // const info: ToastInfo = {
                    //     type: data.status,
                    //     message: data.message
                    // };
                    //addToast(info);
                    count++;

                    setProgress(count / tags().length * 100);
                    console.log(progress());
                    progressBar.style.width = `${progress()}%`;

                    if (data.status === "success") {
                        setDataTags([...dataTags(), data.data]);
                    }

                    if (count === tags().length) {
                        console.log('done');
                        evtSource.close();

                        const end = new Date().getTime();

                        const info: ToastInfo = {
                            type: "info",
                            message: `Done in ${(end - begin) / 1000} seconds.`
                        };

                        addToast(info);
                    }
                }

                evtSource.onerror = (event) => {
                    console.log(event);
                }


            } else {
                setStoreError({ message: res.message });
                navigate('/not-found');
            }
        });
        modal.show();

    });

    function scrollHorizontally(e: any) {
        e.preventDefault();
        const target = e.currentTarget as HTMLElement;
        target.scrollLeft += (e.deltaY * 2);
    }

    function getClassButton(tag: string) {
        if (dataTags()[tags().indexOf(tag)] === undefined) {
            return selectedTag() === tag ? "btn-secondary" : "btn-outline-secondary";
        }
        return selectedTag() === tag ? "btn-primary" : "btn-outline-primary";
    }


    return (
        <>

            <main class="container mt-5">
                <div class="d-inline-flex gap-3 align-items-center">
                    <button class="icon-button btn btn-outline-secondary rounded-circle p-2" onClick={() => navigate('/')}>
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                        </svg>
                    </button>
                    <h1>{name()}</h1>
                </div>
                <p>{url()}</p>

                <div class="progress" role="progressbar" aria-label="Analyzing Progress Bar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar"></div>
                </div>

                <div class="mt-5">
                    <h2 class="h3">Tags</h2>
                    <ul class="d-flex overflow-x-auto ps-0" onWheel={(e) => scrollHorizontally(e)}>
                        <For each={tags()}>
                            {(tag: any) => (
                                <li class="list-unstyled">
                                    <button type="button" class={(getClassButton(tag)) + " btn text-nowrap me-2 mb-2"}
                                        onClick={() => setSelectedTag(tag)}>
                                        {tag}
                                    </button>
                                </li>
                            )}
                        </For>
                    </ul>
                </div>

                <div class="mt-5">
                    <Show when={selectedTag() !== ''}>
                        <h2 class="h3">Tag: {selectedTag()}</h2>
                        <Show when={dataTags()[tags().indexOf(selectedTag())] !== undefined} fallback={<div class="spinner-border spinner-border-sm" role="status">
                            <span class="visually-hidden">Loading...</span></div>}>
                            <Stats contributions={dataTags()[tags().indexOf(selectedTag())]} />
                        </Show>
                    </Show>
                </div>





            </main>

            <div class="modal fade" id="modal" data-bs-backdrop="static" data-bs-keyboard="false" aria-labelledby="modalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="modalLabel">Resolving Tags</h1>
                        </div>
                        <div class="modal-body">
                            <div class="d-flex justify-content-center">
                                <div class="spinner-border" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <Toasts />
        </>
    )
}

export default Repo