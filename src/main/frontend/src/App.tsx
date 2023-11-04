import './scss/styles.scss'
import {Routes, Route} from "@solidjs/router";
import Landing from "./components/Landing.tsx";
import Repo from "./components/Repo.tsx";

function App() {

    return (
        <>
            <Routes>
                <Route path="/" component={Landing} />
                <Route path="/repo/:id" component={Repo} />
            </Routes>
        </>
    )
}

export default App
