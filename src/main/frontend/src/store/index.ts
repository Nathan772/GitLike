import { createStore } from "solid-js/store";

const [storeError, setStoreError] = createStore({ message: "" });

export { storeError, setStoreError };