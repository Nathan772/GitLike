import { render } from 'solid-js/web';
import { HashRouter, Route } from "@solidjs/router";

import Landing from "./pages/Home.tsx";
import Repo from "./pages/Repository.tsx";
/*import UserFromTagList from "./pages/Users.tsx";*/
import NotFound from './pages/NotFound.tsx';
import './scss/styles.scss';

const root = document.getElementById('root');

const html = document.querySelector('html');

const theme = localStorage.getItem('theme') || 'light';
html?.setAttribute('data-bs-theme', theme);

/* <Route path="/repository" component={TagList} />  <Route path="/repository" component={Repo} /> */
render(
    () => (
        <HashRouter>
            <Route path="/" component={Landing} />
            /* the path refers to the page from which the redirection will be done
            and the component refers to the method which will be taken from "tsx" file */
            <Route path="/repository" component={Repo} />
            <Route path="*" component={NotFound} />
        </HashRouter>
    ),
    root!
);
