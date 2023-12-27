import { render } from 'solid-js/web';
import { HashRouter, Route } from "@solidjs/router";

import Landing from "./pages/Home.tsx";
import Repo from "./pages/Repository.tsx";
import UsersFromTagList from "./pages/Users.tsx";
import UserDataFromTag from "./pages/UserDataFromTag.tsx";
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
            /* enable to choose a user to see their contribution for the tag */
            <Route path="/contributors" component={UsersFromTagList} />
            <Route path="/contributorInfos" component={UserDataFromTag} />
            <Route path="*" component={NotFound} />
        </HashRouter>
    ),
    root!
);
