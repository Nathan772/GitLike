import { render } from 'solid-js/web';
import { HashRouter, Route } from "@solidjs/router";

import Landing from "./pages/Home.tsx";
import Repo from "./pages/Repository.tsx";
import NotFound from './pages/NotFound.tsx';
import './scss/styles.scss';

const root = document.getElementById('root');

const html = document.querySelector('html');

const theme = localStorage.getItem('theme') || 'light';
html?.setAttribute('data-bs-theme', theme);

render(
    () => (
        <HashRouter>
            <Route path="/" component={Landing} />
            <Route path="/repository" component={Repo} />
            <Route path="*" component={NotFound} />
        </HashRouter>
    ),
    root!
);
