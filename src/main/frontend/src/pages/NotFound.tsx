import { Show } from "solid-js";
import { useNavigate, useBeforeLeave } from "@solidjs/router";
import { storeError, setStoreError } from "../store";

function NotFound() {
    const navigate = useNavigate();

    useBeforeLeave(() => {
        setStoreError({ message: "" });
    });
    return (
        <main class="container-sm mt-5">
            <div class="card text-bg-danger mb-3">
                <div class="card-header">Page Not Found</div>
                <div class="card-body">
                    <h5 class="card-title">The page you are looking for does not exist.</h5>
                    <Show when={storeError.message != ""}>
                        <p class="card-text">{storeError.message}</p>
                    </Show>
                </div>
            </div>

            <button type="button" class="btn btn-primary" onClick={() => navigate("/")}>Back to Home</button>
        </main>
    );
}

export default NotFound;