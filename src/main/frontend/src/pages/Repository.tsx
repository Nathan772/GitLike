import { For, Show, createSignal, onMount } from 'solid-js';
import { getRepoInfos } from "../utils";
import { useNavigate, useSearchParams } from '@solidjs/router';
import { Modal } from 'bootstrap';
import { JSONResponse, RepositoryInfos, Contributions } from '../types/JSONResponse';
import { setStoreError } from '../store';
import { Toasts, addToast } from '../components/toasts/Toasts';
import { ToastInfo } from '../types/ToastInfo';

import Stats from '../components/Stats';
import BackToHomeButton from '../components/BackToHomeButton';
import ResolvingTagsModal from '../components/ResolvingTagsModal';

function Repo() {
    const navigate = useNavigate();
    const [name, setName] = createSignal('');
    const [url, setUrl] = createSignal('');
    const [tags, setTags] = createSignal([] as string[]);

    const [progress, setProgress] = createSignal(0);

    const [selectedTag, setSelectedTag] = createSignal('' as string);
    const [dataTags, setDataTags] = createSignal({} as Record<string, Contributions>);

    const [showComments, setShowComments] = createSignal(false);

    /**
     * Set repository infos and show success toast
     * 
     * @param data 
     * @param res 
     */
    const setRepositoryInfos = (data: RepositoryInfos, res: JSONResponse) => {
        setName(data.name);
        setUrl(data.URL);
        setTags(data.tags);
        setSelectedTag(data.tags[0]);

        const info: ToastInfo = {
            type: "success",
            message: res.message
        };

        addToast(info);
    }

    /**
     * Start event source and show info toast
     * 
     * @param url 
     * @returns 
     */
    const startEventSource = (url: string) => {
        const evtSource = new EventSource(`http://localhost:8080/api/analyze-tags?url=${url}`);

        const infoCloning: ToastInfo = {
            type: "info",
            message: "Cloning repository... This may take a while."
        };

        addToast(infoCloning);
        return evtSource;
    }

    /**
     * Update progress bar
     * 
     * @param count 
     */
    const updateProgress = (count: number) => {
        const progressBar = document.querySelector('.progress-bar')! as HTMLElement;
        setProgress(count / tags().length * 100);
        progressBar.style.width = `${progress()}%`;
    }

    /**
     * Add data to dataTags
     * 
     * @param data 
     */
    const addToDataTags = (data: Contributions) => {
        setDataTags(prevDataTags => ({
            ...prevDataTags,
            [data.tagName]: data
        }));
    }

    const toastEnd = (start: number) => {
        const end = new Date().getTime();

        const info: ToastInfo = {
            type: "info",
            message: `Done in ${(end - start) / 1000} seconds.`
        };

        addToast(info);
    }

    onMount(async () => {
        const modal = new Modal(document.getElementById('modal')!);
        const modalElement = document.getElementById('modal')!;

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
                setRepositoryInfos(data, res);
                const start = new Date().getTime();
                const evtSource = startEventSource(url);
                let count = 0;

                evtSource.onmessage = (event) => {
                    const data = JSON.parse(event.data);
                    count++;
                    updateProgress(count);

                    if (data.status === "success") {
                        addToDataTags(data.data);
                    }
                    if (count === tags().length) {
                        toastEnd(start);
                    }
                }
                evtSource.onerror = () => {
                    evtSource.close();
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
        if (dataTags()[tag] === undefined) {
            return selectedTag() === tag ? "btn-secondary" : "btn-outline-secondary";
        }
        return selectedTag() === tag ? "btn-primary" : "btn-outline-primary";
    }

    return (
        <>
            <main class="container my-5">
                <div class="d-inline-flex gap-3 align-items-center">
                    <BackToHomeButton />
                    <h1>{name()}</h1>
                </div>
                <p>{url()}</p>

                <div class="card" style="width: 50%; min-width: 18rem;">
                    <div class="card-body">
                        <Show when={progress() === 100} fallback={<h2 class="h5">Analyzing...</h2>}>
                            <h2 class="h5">Done!</h2>
                        </Show>
                        <div class="progress" role="progressbar" aria-label="Analyzing Progress Bar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                            <div class="progress-bar"></div>
                        </div>
                    </div>
                </div>

                <div class="mt-4">
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

                <div class="mt-4">
                    <Show when={selectedTag() !== ''}>
                        <h2 class="h3 mb-3">Tag: {selectedTag()}</h2>
                        <button class="btn btn-secondary mb-2" role="button" data-bs-toggle="button" onClick={() => setShowComments(!showComments())}>
                            {showComments() ? "Hide Comments" : "Show Comments"}
                        </button>
                        <Show when={dataTags()[selectedTag()] !== undefined} fallback={
                            <div class="d-flex justify-content-center">
                                <div class="spinner-border" style="width: 3rem; height: 3rem;" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                            </div>}>
                            <Stats contributions={dataTags()[selectedTag()]} showComments={showComments()} />
                        </Show>
                    </Show>
                </div>

            </main>

            <ResolvingTagsModal />
            <Toasts />
        </>
    )
}

export default Repo