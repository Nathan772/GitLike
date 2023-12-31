import { createSignal, Show } from 'solid-js';
import { useNavigate } from "@solidjs/router";
import { checkRepository } from "../utils";
import { JSONResponse, URL } from '../types/JSONResponse';
import { Toasts, addToast } from './toasts/Toasts';
import { ToastInfo } from '../types/ToastInfo';


function InputRepository() {
    const navigate = useNavigate();

    const [repoURL, setRepoURL] = createSignal("");
    const [waiting, setWaiting] = createSignal(false);

    const pushToRepo = async (e: Event) => {
        e.preventDefault();
        e.stopPropagation();

        setWaiting(true);
        const res = await checkRepository(repoURL()) as JSONResponse;
        setWaiting(false);

        if (res.status === "success") {
            const url = (res.data as URL).URL;
            navigate(`/repository?url=${url}`);
        } else {
            const info: ToastInfo = {
                type: "error",
                message: res.message
            };
            addToast(info);
        }
    }

    return (
        <>
            <form class="d-flex justify-content-center align-items-center gap-2 w-50" onSubmit={(e) => pushToRepo(e)}>
                <input type="url" class="form-control flex-fill" placeholder="URL of a git repository"
                    value={repoURL()} onInput={(e: any) => setRepoURL(e.target.value)}
                    required
                    disabled={waiting()}
                />
                <button type="submit" class="btn btn-primary" disabled={waiting()}>
                    <Show when={waiting()} fallback={<>Analyze</>}>
                        <div class="spinner-border spinner-border-sm" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </Show>
                </button>
            </form>

            <Toasts />
        </>
    );
}

export default InputRepository;