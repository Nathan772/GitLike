import { For, createSignal } from 'solid-js'
import { Toast } from 'bootstrap';
import CustomToast from './CustomToast';
import { ToastInfo } from '../types/ToastInfo';


const [toasts, setToasts] = createSignal([]) as any[];

function addToast(info: ToastInfo) {
    const id = Date.now();
    setToasts([...toasts(), <CustomToast id={id} message={info.message} type={info.type} />]);
    const toastEl = document.getElementById(id.toString());
    const toast = new Toast(toastEl!);
    toast.show();
}

function Toasts() {

    return (
        <div class="toast-container position-fixed top-0 end-0 p-3">
            <For each={toasts()}>
                {(toast: any) => (
                    <div>
                        {toast}
                    </div>
                )}
            </For>
        </div>
    )
}

export { Toasts, addToast };